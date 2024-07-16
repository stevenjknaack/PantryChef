package com.pantrychef.backend.repositories;

import com.pantrychef.backend.entities.ingredients.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * For database interactions related to recipe ingredients
 */
@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {
}
