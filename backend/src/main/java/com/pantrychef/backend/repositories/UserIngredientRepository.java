package com.pantrychef.backend.repositories;

import com.pantrychef.backend.entities.ingredients.UserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * For database interactions related to user ingredients
 */
@Repository
public interface UserIngredientRepository extends JpaRepository<UserIngredient, Integer> {
}