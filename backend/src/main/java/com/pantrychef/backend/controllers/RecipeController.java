package com.pantrychef.backend.controllers;

import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/recipes")
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;

    @PostMapping
    public Recipe addRecipe(@RequestBody Recipe recipe) throws Exception {
        recipe.setId(null);
        return recipeRepository.save(recipe);
    }

    @GetMapping
    public Page<Recipe> getPagesOfRecipes(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "100") Integer size
    ) throws Exception {
        if (page < 0 || size < 1) throw new Exception();

        Pageable pageRequest = PageRequest.of(page, size);
        return recipeRepository.findAll(pageRequest);
    }

    @GetMapping(path="/{id}")
    public Recipe getRecipe(@PathVariable Integer id) throws Exception {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isEmpty()) throw new Exception();
        return recipe.get();
    }

    @PutMapping(path = "/{id}")
    public Recipe updateRecipe(@RequestBody Recipe recipe) throws Exception {
        if (recipe.getId() == null || !recipeRepository.existsById(recipe.getId())) {
            throw new Exception();
        }
        return recipeRepository.save(recipe);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteRecipe(@PathVariable Integer id) throws Exception {
        if (!recipeRepository.existsById(id)) {
            throw new Exception();
        }
        recipeRepository.deleteById(id);
    }
}
