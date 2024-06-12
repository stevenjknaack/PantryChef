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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public Recipe createRecipe(Recipe recipe, User user) {
        return saveRecipeRespectSublists(null, recipe, user);
    }

    public Recipe getRecipe(Integer id) {
        return recipeRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public Page<RecipeResultDTO> queryRecipes(Integer page, Integer size) {
        if (page < 0 || size < 1)
            throw new IllegalArgumentException(
                    "Page must be greater than 0 and size greater than 1."
            );

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Recipe> recipePage = recipeRepository.findAll(pageRequest);

        return recipePage.map(RecipeResultMapper::toDTO);
    }

    public Recipe updateRecipe(Integer id, Recipe recipe, User user) { //TODO fix check author
        Recipe extantRecipe = recipeRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        if (!extantRecipe.getAuthor().getUsername().equals(user.getUsername()))
            throw new UnauthorizedException();

        return saveRecipeRespectSublists(id, recipe, extantRecipe.getAuthor());
    }

    public Recipe deleteRecipe(Integer id, User user) {
        if (id == null) throw new InvalidRequestException();

        Recipe extantRecipe = recipeRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        if (!extantRecipe.getAuthor().equals(user))
            throw new UnauthorizedException();

        recipeRepository.delete(extantRecipe);

        return extantRecipe;
    }

    @Transactional
    public Recipe saveRecipeRespectSublists(Integer id, Recipe recipe, User author) {
        recipe.setId(id);
        recipe.setAuthor(author);
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
}
