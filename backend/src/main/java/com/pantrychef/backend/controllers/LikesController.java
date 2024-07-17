package com.pantrychef.backend.controllers;

import com.pantrychef.backend.dtos.MessageResponse;
import com.pantrychef.backend.dtos.RecipeResultDTO;
import com.pantrychef.backend.entities.Review;
import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.mappers.RecipeResultMapper;
import com.pantrychef.backend.services.JWTService;
import com.pantrychef.backend.services.LikesService;
import com.pantrychef.backend.services.RecipeService;
import com.pantrychef.backend.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web API endpoints for liking reviews and recipes
 */
@RestController
@RequestMapping(path = "/api")
public class LikesController {
    @Autowired
    private JWTService jWTService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private LikesService likesService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private ReviewService reviewService;

    /**
     * Adds a recipe to the user's liked recipes
     * @param jWTToken The jwt token used to identify and authenticate the user
     * @param id The id of the recipe to like
     * @return A message indicating success
     */
    @PostMapping(path = "/users/likes/recipes/{id}")
    public ResponseEntity<MessageResponse> likeRecipe(
            @CookieValue(name = "jwtToken") String jWTToken,
            @PathVariable(name = "id") Integer id
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        likesService.likeRecipe(user, id);
        return new ResponseEntity<>(
                new MessageResponse(username + " successfully liked recipe " + id),
                HttpStatus.CREATED
        );
    }

    /**
     * Deletes a recipe from the user's liked recipes
     * @param jWTToken The jwt token used to identify and authenticate the user
     * @param id The id of the recipe to delete
     * @return A message indicating success
     */
    @DeleteMapping(path = "/users/likes/recipes/{id}")
    public ResponseEntity<MessageResponse> unlikeRecipe(
            @CookieValue(name = "jwtToken") String jWTToken,
            @PathVariable(name = "id") Integer id
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        likesService.unlikeRecipe(user, id);
        return ResponseEntity.ok(
                new MessageResponse(username + " successfully unliked recipe " + id)
        );
    }

    /**
     * Gets the recipes a user has liked
     * @param jWTToken The jwt token used to identify and authenticate the user
     * @return A list of recipes
     */
    @GetMapping(path = "/users/likes/recipes")
    public ResponseEntity<List<RecipeResultDTO>> getUserLikedRecipes(
            @CookieValue(name = "jwtToken") String jWTToken
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(
                user.getLikedRecipes().stream().map(RecipeResultMapper::toDTO).toList()
        );
    }

    /**
     * Get all the users that have liked a recipe
     * @param id The id of the recipe
     * @return A list of usernames
     */
    @GetMapping(path = "/recipes/{id}/likes")
    public ResponseEntity<List<String>> getRecipeLikes(
            @PathVariable(name = "id") Integer id
    ) {
        List<User> likes = recipeService.getRecipe(id).getLikes();
        return ResponseEntity.ok(
                likes.stream().map(User::getUsername).toList()
        );
    }

    /**
     * Adds a review to the user's liked reviews
     * @param jWTToken The jwt token used to identify and authenticate the user
     * @param id The id of the review to like
     * @return A message indicating success
     */
    @PostMapping(path = "/users/likes/reviews/{id}")
    public ResponseEntity<MessageResponse> likeReview(
            @CookieValue(name = "jwtToken") String jWTToken,
            @PathVariable(name = "id") Integer id
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        likesService.likeReview(user, id);
        return new ResponseEntity<>(
                new MessageResponse(username + " successfully liked review " + id),
                HttpStatus.CREATED
        );
    }

    /**
     * Deletes a review from the user's liked reviews
     * @param jWTToken The jwt token used to identify and authenticate the user
     * @param id The id of the review to delete
     * @return A message indicating success
     */
    @DeleteMapping(path = "/users/likes/reviews/{id}")
    public ResponseEntity<MessageResponse> unlikeReview(
            @CookieValue(name = "jwtToken") String jWTToken,
            @PathVariable(name = "id") Integer id
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        likesService.unlikeReview(user, id);
        return ResponseEntity.ok(
                new MessageResponse(username + " successfully unliked review " + id)
        );
    }

    /**
     * Gets the reviews a user has liked
     * @param jWTToken The jwt token used to identify and authenticate the user
     * @return A list of reviews
     */
    @GetMapping(path = "/users/likes/reviews")
    public ResponseEntity<List<Review>> getUserLikedReviews(
            @CookieValue(name = "jwtToken") String jWTToken
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(user.getLikedReviews());
    }

    /**
     * Get all the users that have liked a review
     * @param id The id of the review
     * @return A list of usernames
     */
    @GetMapping(path = "/reviews/{id}/likes")
    public ResponseEntity<List<String>> getReviewLikes(
            @PathVariable(name = "id") Integer id
    ) {
        List<User> likes = reviewService.getReview(id).getLikes();
        return ResponseEntity.ok(
                likes.stream().map(User::getUsername).toList()
        );
    }
}
