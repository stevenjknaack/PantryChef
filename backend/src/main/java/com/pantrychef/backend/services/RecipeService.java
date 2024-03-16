package com.pantrychef.backend.services;

import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.repositories.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe addRecipe(Recipe recipe) {
        recipe.setId(null);
        return recipeRepository.save(recipe);
    }

}
