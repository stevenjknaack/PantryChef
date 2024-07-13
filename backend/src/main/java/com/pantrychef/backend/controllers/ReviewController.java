package com.pantrychef.backend.controllers;

import com.pantrychef.backend.entities.Review;
import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.services.JWTService;
import com.pantrychef.backend.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

/**
 * Contains Web API endpoints for managing recipe reviews
 */
@RestController
@RequestMapping(path = "/api")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private JWTService jWTService;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Creates a new recipe
     * @param jWTToken Token used to identify and authenticate author
     * @param review The review to create. If an authorUsername is provided,
     *               it will be ignored
     * @return A ResponseEntity containing the created review
     */
    @PostMapping(path = "/recipes/{recipeId}/reviews")
    public ResponseEntity<Review> addReview(
            @CookieValue(name = "jwtToken") String jWTToken,
            @RequestBody Review review
    ) {
        String authorUsername = jWTService.extractUsername(jWTToken);
        User author = (User) userDetailsService.loadUserByUsername(authorUsername);
        return ResponseEntity.ok(reviewService.saveReview(null, review, author));
    }

    /**
     * Completes a query for reviews
     * @param page The page number. 0 <= page. Defaults 0
     * @param size The number of results that should be included in the page. size >= 1. Defaults 100
     * @param rating A rating to query by. 1 <= rating <= 5. If not provided, all ratings are considered
     * @param recipeId Only get reviews on this recipe. If not provided, all recipes are considered
     * @param authorUsername Only get reviews written by this author. If not provided, all authors are considered
     * @return A page of reviews satisfying the query parameters
     */
    @GetMapping(path = "/reviews")
    public ResponseEntity<Page<Review>> getPageOfReviews(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "100") Integer size,
            @RequestParam(name = "rating") Integer rating,
            @RequestParam(name = "recipeId") Integer recipeId,
            @RequestParam(name = "authorUsername") String authorUsername
    ) {
        return ResponseEntity.ok(
                reviewService.queryReviews(page, size, rating, recipeId, authorUsername)
        );
    }

    /**
     * Updates a given review
     * @param jWTToken Identifies and authenticates the author, who must match the review's author
     * @param id The id of the review to update
     * @param review The updated review. Any authorUsername attributes will be ignored
     * @return A ResponseEntity with the updated review
     */
    @PutMapping(path = "/reviews/{id}")
    public ResponseEntity<Review> updateReview(
            @CookieValue(name = "jwtToken") String jWTToken,
            @PathVariable Integer id,
            @RequestBody Review review
    ) {
        String authorUsername = jWTService.extractUsername(jWTToken);
        User author = (User) userDetailsService.loadUserByUsername(authorUsername);
        return ResponseEntity.ok(reviewService.saveReview(id, review, author));
    }

    /**
     * Deletes a given review
     * @param jWTToken Identifies and authenticates the author, who must match the review's author
     * @param id The id of the review to delete
     * @return A ResponseEntity with the deleted review
     */
    @DeleteMapping(path = "/reviews/{id}")
    public ResponseEntity<Review> deleteReview(
            @CookieValue(name = "jwtToken") String jWTToken,
            @PathVariable Integer id
    ) {
        String authorUsername = jWTService.extractUsername(jWTToken);
        User author = (User) userDetailsService.loadUserByUsername(authorUsername);
        return ResponseEntity.ok(reviewService.deleteReview(id, author));
    }
}
