package com.pantrychef.backend.repositories;

import com.pantrychef.backend.dtos.RecipeResultDTO;
import com.pantrychef.backend.entities.recipes.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * For database interactions related to Recipes
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    // TODO
//    @Query(value = "SELECT new RecipeResultDTO(r.id)"
//            + " FROM recipe r, recipe_ingredient i"
//            + " WHERE r.id = i.recipe_id")
//    public Page<RecipeResultDTO> findAllByIngredientNames(Pageable pageable);
}
