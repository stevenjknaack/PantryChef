package com.pantrychef.backend.reviews;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Integer> {
    public Optional<Review> findById(Integer id);

    public List<Review> findByRecipeId(Integer recipeId);
    
    public List<Review> findByAuthorUsername(String authorUsername);
}
