package com.pantrychef.backend.repositories;

import com.pantrychef.backend.entities.ingredients.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO how does this work with inheritance
@Repository
public interface UserIngredientRepository extends JpaRepository<Ingredient, Integer> {
}
