package com.pantrychef.backend.services;

import com.pantrychef.backend.dtos.RecipeResultDTO;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.errors.InvalidRequestException;
import com.pantrychef.backend.errors.ResourceNotFoundException;
import com.pantrychef.backend.mappers.RecipeResultMapper;
import com.pantrychef.backend.errors.UnauthorizedException;
import com.pantrychef.backend.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides business logic for dealing with recipes
 */
@Service
@RequiredArgsConstructor
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private InstructionService instructionService;
    @Autowired
    private RecipeIngredientService ingredientService;

    /**
     * Creates or updates a recipe. All sublists included will also be saved
     * @param id The id of the recipe. Should be null if creating, non-null if updating
     * @param recipe The recipe
     * @param user The user trying to save this recipe
     * @return The saved recipe
     */
    @Transactional // TODO do i need to check if this is the right author
    public Recipe saveRecipeRespectSublists(Integer id, Recipe recipe, User user) {
        if (id != null) {
            Recipe extantRecipe = recipeRepository.findById(id)
                    .orElseThrow(ResourceNotFoundException::new);

            if (!extantRecipe.getAuthor().getUsername().equals(user.getUsername()))
                throw new UnauthorizedException();

            recipe.setAuthor(extantRecipe.getAuthor());
        } else {
            recipe.setAuthor(user);
        }

        recipe.setId(id);
        Recipe savedRecipe = recipeRepository.save(recipe);

        if (recipe.getImages() != null) {
            recipe.getImages()
                    .replaceAll(image -> imageService.saveImage(image, savedRecipe));
        }

        if (recipe.getInstructions() != null) {
            recipe.getInstructions()
                    .replaceAll(instr -> instructionService.saveInstruction(instr, savedRecipe));
        }

        if (recipe.getIngredients() != null) {
            recipe.getIngredients()
                    .replaceAll(ingr -> ingredientService.saveIngredient(ingr, savedRecipe));
        }

        return savedRecipe;
    }

    /**
     * Gets the recipe with the specified id, if it exists
     * @param id The id
     * @return The recipe with the id
     */
    public Recipe getRecipe(Integer id) {
        return recipeRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Completes a query for recipes
     * @param page The page number. 0 <= page. Defaults 0
     * @param size The number of results that should be included in the page. size >= 1. Defaults 100
     * @return A page of recipes satisfying the query parameters
     */
    public Page<RecipeResultDTO> queryRecipes(Integer page, Integer size) {
        if (page < 0 || size < 1)
            throw new IllegalArgumentException(
                    "Page must be greater than or equal to 0 and size greater than or equal to 1."
            );

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Recipe> recipePage = recipeRepository.findAll(pageRequest);

        return recipePage.map(RecipeResultMapper::toDTO);
    }

    /**
     * Deletes a given recipe
     * @param user The user attempting to delete the recipe
     * @param id The id of the recipe to delete
     * @return A ResponseEntity with the deleted recipe
     */
    public Recipe deleteRecipe(Integer id, User user) {
        if (id == null) throw new InvalidRequestException();

        Recipe extantRecipe = recipeRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        if (!extantRecipe.getAuthor().equals(user))
            throw new UnauthorizedException();

        recipeRepository.delete(extantRecipe);

        return extantRecipe;
    }
}
