package com.pantrychef.backend.controllers;

import com.pantrychef.backend.entities.Review;
import com.pantrychef.backend.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

//TODO figure out naming conventions for paths
//TODO add comments
//TODO Figure out good return and Exception types
//TODO add bulk method for each one
//TODO Implement searching by recipeId and authorUsername
@RestController
@RequestMapping(path="/api/recipes/{recipeId}/reviews")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;

}
