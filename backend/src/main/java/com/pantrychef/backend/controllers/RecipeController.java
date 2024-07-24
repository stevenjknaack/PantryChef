package com.pantrychef.backend.controllers;

import com.pantrychef.backend.dtos.RecipeResultDTO;
import com.pantrychef.backend.entities.ingredients.RecipeIngredient;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.services.JWTService;
import com.pantrychef.backend.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Web API endpoints related to recipes
 */
@RestController
@RequestMapping(path = "/api/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private JWTService jWTService;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Creates a new recipe
     * @param jWTToken Token used to identify and authenticate author
     * @param recipe The recipe to create. If an authorUsername is provided,
     *               it will be ignored
     * @return A ResponseEntity containing the created recipe
     */
    @PostMapping
    public ResponseEntity<Recipe> addRecipe(
            @CookieValue(name = "jwtToken") String jWTToken,
            @RequestBody Recipe recipe
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        return new ResponseEntity<Recipe>(
                recipeService.saveRecipeRespectSublists(null, recipe, user),
                HttpStatus.CREATED
        );
    }

    /**
     * Completes a query for recipes
     * @param page The page number. 0 <= page. Defaults 0
     * @param size The number of results that should be included in the page. size >= 1. Defaults 100
     * @param authorUsername An optional authorUsername to query by
     * @param ingredients An optional ingredient list to query by
     * @return A page of recipes satisfying the query parameters
     */
    @GetMapping
    public ResponseEntity<Page<RecipeResultDTO>> getPageOfRecipes(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "100") Integer size,
            @RequestParam(required = false) String authorUsername,
            @RequestParam(required = false) List<String> ingredients
            ) {
        return ResponseEntity.ok(
                recipeService.queryRecipes(page, size, authorUsername, ingredients)
        );
    }

    /**
     * Retrieves a recipe, if it exists
     * @param id The id of the recipe to get
     * @return The recipe
     */
    @GetMapping(path="/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Integer id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    /**
     * Updates a given recipe
     * @param jWTToken Identifies and authenticates the author, who must match the recipe's author
     * @param id The id of the recipe to update
     * @param recipe The updated recipe. Any author.username attributes will be ignored
     * @return A ResponseEntity with the updated recipe
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<Recipe> updateRecipe(
            @CookieValue(name = "jwtToken") String jWTToken,
            @PathVariable Integer id,
            @RequestBody Recipe recipe
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(recipeService.saveRecipeRespectSublists(id, recipe, user));
    }

    /**
     * Deletes a given recipe
     * @param jWTToken Identifies and authenticates the author, who must match the recipe's author
     * @param id The id of the recipe to delete
     * @return A ResponseEntity with the deleted recipe
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Recipe> deleteRecipe(
            @CookieValue(name = "jwtToken") String jWTToken,
            @PathVariable Integer id
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(recipeService.deleteRecipe(id, user));
    }
}

