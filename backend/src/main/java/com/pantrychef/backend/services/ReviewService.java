package com.pantrychef.backend.services;

import com.pantrychef.backend.entities.Review;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.errors.InvalidRequestException;
import com.pantrychef.backend.errors.ResourceNotFoundException;
import com.pantrychef.backend.errors.UnauthorizedException;
import com.pantrychef.backend.repositories.RecipeRepository;
import com.pantrychef.backend.repositories.ReviewRepository;
import com.pantrychef.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Provides business logic for dealing with reviews
 */
@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    /**
     * Either creates or updates a review
     * @param id The id of the review to update. If null, recipe will be created by the provided author
     * @param review The review to create or update an existing review with the given id with. If provided,
     *               the id and author attributes will be ignored
     * @param author The author of the review to create or update
     * @return The created or updated review
     */
    public Review saveReview(Integer id, Review review, User author) {
        if (id != null) {
            Review extantReview = reviewRepository.findById(id)
                    .orElseThrow(ResourceNotFoundException::new);

            if (!extantReview.getAuthor().equals(author)) {
                throw new InvalidRequestException();
            }
        }

        review.setId(id);
        review.setAuthor(author);
        return reviewRepository.save(review);
    }

    /**
     * Deletes a given review
     * @param id The id of the review to delete
     * @param author The author of the review to delete
     * @return A ResponseEntity with the deleted review
     */
    public Review deleteReview(Integer id, User author) {
        if (id == null) throw new InvalidRequestException();

        Review extantReview = reviewRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        if (!extantReview.getAuthor().equals(author))
            throw new UnauthorizedException();

        reviewRepository.delete(extantReview);
        return extantReview;
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
    public Page<Review> queryReviews(
            Integer page, Integer size, Integer rating, Integer recipeId, String authorUsername
    ) {
        if (page < 0)
            throw new InvalidRequestException("page must be greater than or equal to 0. Provided: " + page);
        if (size < 1)
            throw new InvalidRequestException("size must be greater than or equal to 1. Provided: " + size);
        if (rating < 1 || rating > 5)
            throw new InvalidRequestException("rating must be between 1 and 5 inclusive. Provided: " + rating);

        User author = null;
        Recipe recipe = null;

        if (authorUsername != null) {
            author = userRepository.findById(authorUsername)
                    .orElseThrow(() -> new InvalidRequestException("authorUsername must refer to an existing author"));
        }
        if (recipeId != null) {
            recipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> new InvalidRequestException("recipeId must refer to an existing recipe"));
        }

        Review reviewExampleProbe = Review.builder()
                .rating(rating)
                .recipe(recipe)
                .author(author)
                .build();

        Example<Review> reviewExample = Example.of(reviewExampleProbe);
        Pageable pageRequest = PageRequest.of(page, size);

        return reviewRepository.findAll(reviewExample, pageRequest);
    }
}
