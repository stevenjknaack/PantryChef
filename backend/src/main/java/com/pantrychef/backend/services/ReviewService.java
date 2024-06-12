package com.pantrychef.backend.services;

import com.pantrychef.backend.entities.Review;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Review createReview(Integer recipeId, Review review, String jWTToken) {
        return null;
    }
}
