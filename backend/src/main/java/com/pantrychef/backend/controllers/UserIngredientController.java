package com.pantrychef.backend.controllers;

import com.pantrychef.backend.entities.ingredients.UserIngredient;
import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.services.JWTService;
import com.pantrychef.backend.services.UserIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web API endpoints for interacting with a user's pantry
 */
@RestController
@RequestMapping(path = "/api/users/ingredients")
public class UserIngredientController {
    @Autowired
    private JWTService jWTService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserIngredientService userIngrService;

    /**
     * Adds the given ingredient to a user's pantry
     * @param jWTToken The jwt token identifying the user
     * @param userIngr The ingredient
     * @return The added ingredient
     */
    @PostMapping
    public ResponseEntity<UserIngredient> createUserIngredient(
            @CookieValue(name = "jwtToken") String jWTToken,
            @RequestBody UserIngredient userIngr
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        return new ResponseEntity<UserIngredient>(
                userIngrService.saveUserIngredient(null, userIngr, user),
                HttpStatus.CREATED
        );
    }

    /**
     * Gets the ingredients a user has in their pantry
     * @param jWTToken Token used to identify and authenticate the user
     * @return The ingredients
     */
    @GetMapping
    public ResponseEntity<List<UserIngredient>> getUserIngredients(
            @CookieValue(name = "jwtToken") String jWTToken
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(user.getPantry());
    }

    /**
     * Updates an ingredient in a users pantry
     * @param jWTToken Token used to identify and authenticate the user
     * @param id The ingredient's id
     * @param userIngr The user ingredient to save
     * @return The updated user ingredient
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<UserIngredient> updateUserIngredient(
            @CookieValue(name = "jwtToken") String jWTToken,
            @PathVariable(name = "id") Integer id,
            @RequestBody UserIngredient userIngr
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(userIngrService.saveUserIngredient(id, userIngr, user));
    }

    /**
     * Deletes an ingredient from a user's pantry
     * @param jWTToken Token used to identify and authenticate the user
     * @param id The ingredient's id
     * @return The deleted ingredient
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<UserIngredient> deleteUserIngredient(
            @CookieValue(name = "jwtToken") String jWTToken,
            @PathVariable(name = "id") Integer id
    ) {
        String username = jWTService.extractUsername(jWTToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(userIngrService.deleteUserIngredient(id, user));
    }
}
