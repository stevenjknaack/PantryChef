package com.pantrychef.backend.services;

import com.pantrychef.backend.entities.ingredients.RecipeIngredient;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.errors.ResourceNotFoundException;
import com.pantrychef.backend.errors.UnauthorizedException;
import com.pantrychef.backend.repositories.RecipeIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides business logic for dealing with recipe ingredients
 */
@Service
@RequiredArgsConstructor
public class RecipeIngredientService {
    @Autowired
    private RecipeIngredientRepository ingredientRepository;

    /**
     * Creates or updates a recipe's ingredient
     * @param ingr The ingredient
     * @param recipe The recipe
     * @return The saved ingredient
     */
    public RecipeIngredient saveIngredient(RecipeIngredient ingr, Recipe recipe) {
        if (ingr.getId() != null) {
            RecipeIngredient extantIngr = ingredientRepository.findById(ingr.getId())
                    .orElseThrow(ResourceNotFoundException::new);

            if (!extantIngr.getRecipe().getId().equals(recipe.getId())) {
                throw new UnauthorizedException();
            }
        }

        ingr.setRecipe(recipe);
        return ingredientRepository.save(ingr);
    }
}
