package com.pantrychef.backend.repositories;

import com.pantrychef.backend.entities.recipes.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * For database interactions related to Recipes
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}
