package com.pantrychef.backend.mappers;

import com.pantrychef.backend.dtos.RecipeResultDTO;
import com.pantrychef.backend.entities.Image;
import com.pantrychef.backend.entities.recipes.Recipe;

public class RecipeResultMapper {
    public static RecipeResultDTO toDTO(Recipe recipe) {
        Image mainImage;
        if (recipe.getImages().isEmpty()) mainImage = null;
        else mainImage = recipe.getImages().get(0);

        return RecipeResultDTO.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .authorUsername(recipe.getAuthor().getUsername())
                .mainImage(mainImage)
                .build();
    }
}
