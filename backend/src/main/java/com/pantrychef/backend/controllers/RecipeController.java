package com.pantrychef.backend.controllers;

import com.pantrychef.backend.dtos.RecipeResultDTO;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.services.JWTService;
import com.pantrychef.backend.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @PostMapping
    public ResponseEntity<Recipe> addRecipe(
            @RequestHeader(name = "Authorization") String authHeader,
            @RequestBody Recipe recipe
    ) {
        return ResponseEntity.ok(recipeService.createRecipe(recipe, authHeader));
    }

    @GetMapping
    public ResponseEntity<Page<RecipeResultDTO>> getPageOfRecipes(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "100") Integer size
    ) {
        return ResponseEntity.ok(recipeService.queryRecipes(page, size));
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Integer id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable Integer id,
            @RequestHeader(name = "Authorization") String authHeader,
            @RequestBody Recipe recipe
    ) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, recipe, authHeader));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Recipe> deleteRecipe(
            @RequestHeader(name = "Authorization") String authHeader,
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(recipeService.deleteRecipe(id, authHeader));
    }
}

