package com.pantrychef.backend.reviews;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface ReviewRepository extends CrudRepository<Review, Integer> {
    public Optional<Review> findById(Integer id);

    public List<Review> findByRecipeId(Integer recipeId);
    
    public List<Review> findByAuthorUsername(String authorUsername);
}
