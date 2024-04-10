package com.pantrychef.backend.controllers;

import com.pantrychef.backend.dtos.RecipeResultDTO;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.services.JWTService;
import com.pantrychef.backend.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private JWTService jWTService;


    @PostMapping
    public Recipe addRecipe(
            @RequestHeader(name = "Authorization") String authHeader,
            @RequestBody Recipe recipe
    ) {
        return recipeService.createRecipe(recipe, jWTService.extractJWTToken(authHeader)); //TODO
    }

    @GetMapping
    public Page<RecipeResultDTO> getPageOfRecipes(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "100") Integer size
    ) {
        return recipeService.queryRecipes(page, size);
    }

    @GetMapping(path="/{id}")
    public Recipe getRecipe(@PathVariable Integer id) {
        return recipeService.getRecipe(id);
    }

    @PutMapping(path = "/{id}")
    public Recipe updateRecipe(@RequestBody Recipe recipe) throws Exception {
        return recipeService.updateRecipe(recipe);
    }

    @DeleteMapping(path = "/{id}")
    public Recipe deleteRecipe(@PathVariable Integer id) throws Exception {
        return recipeService.deleteRecipe(id);
    }
}

//    @CrossOrigin(origins = "http://localhost:5173")
//    @GetMapping(path = "/example")
//    public Recipe getExample() {
//        Recipe newRecipe = Recipe.builder()
//                .name("my new example recipe please change")
//                .build();
//
//        Recipe savedRecipe = recipeRepository.save(newRecipe);
//
////        String exampleName = savedRecipe.getName() + " " + savedRecipe.getId();
//
//        S.delete(savedRecipe);
//
//        return savedRecipe;
//    }

