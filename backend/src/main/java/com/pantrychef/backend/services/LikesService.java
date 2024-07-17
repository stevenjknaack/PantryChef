package com.pantrychef.backend.services;

import com.pantrychef.backend.entities.Review;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.errors.ResourceNotFoundException;
import com.pantrychef.backend.repositories.RecipeRepository;
import com.pantrychef.backend.repositories.ReviewRepository;
import com.pantrychef.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Business logic related to liking reviews and recipes
 */
@Service
public class LikesService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * Adds a recipe to the user's liked recipes
     * @param user The user
     * @param id The id of the recipe to like
     */
    public void likeRecipe(User user, Integer id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        user.addLikedRecipe(recipe);
        userRepository.save(user);
    }

    /**
     * Deletes a recipe from the user's liked recipes
     * @param user The user
     * @param id The id of the recipe to delete
     */
    public void unlikeRecipe(User user, Integer id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        user.unlikeRecipe(recipe);
        userRepository.save(user);
    }

    /**
     * Adds a recipe to the user's liked reviews
     * @param user The user
     * @param id The id of the review to like
     */
    public void likeReview(User user, Integer id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        user.addLikedReview(review);
        userRepository.save(user);
    }

    /**
     * Deletes a review from the user's liked reviews
     * @param user The user
     * @param id The id of the review to delete
     */
    public void unlikeReview(User user, Integer id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        user.unlikeReview(review);
        userRepository.save(user);
    }

}
