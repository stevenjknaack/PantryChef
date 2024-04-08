package com.pantrychef.backend.services;

import com.pantrychef.backend.dtos.RecipeResultDTO;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.errors.ResourceNotFoundException;
import com.pantrychef.backend.mappers.RecipeResultMapper;
import com.pantrychef.backend.repositories.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe createRecipe(Recipe recipe) {
        recipe.setId(null);
        return recipeRepository.save(recipe);
    }

    public Recipe getRecipe(Integer id) {
        Optional<Recipe> recipeResult = recipeRepository.findById(id);
        if (recipeResult.isEmpty())
            throw new ResourceNotFoundException();
        return recipeResult.get();
    }

    public Page<RecipeResultDTO> queryRecipes(Integer page, Integer size) {
        if (page < 0 || size < 1)
            throw new IllegalArgumentException("Page must be greater than 0 and size greater than 1.");

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Recipe> recipePage = recipeRepository.findAll(pageRequest);

        return recipePage.map(RecipeResultMapper::toDTO);
    }

    public Recipe updateRecipe(Recipe recipe) {
        if (recipe.getId() == null || !recipeRepository.existsById(recipe.getId())) {
            throw new RuntimeException();
        }

        return recipeRepository.save(recipe);
    }

    public Recipe deleteRecipe(Integer id) {
        Optional<Recipe> recipeResult = recipeRepository.findById(id);
        if (recipeResult.isEmpty())
            throw new ResourceNotFoundException();

        Recipe recipe = recipeResult.get();
        recipeRepository.delete(recipe);

        return recipe;
    }

}
