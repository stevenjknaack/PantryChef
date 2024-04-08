package com.pantrychef.backend.controllers;

import com.pantrychef.backend.entities.Image;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recipes/{recipeId}/images")
public class RecipeImageController {
    @PostMapping()
    public Image addImage(@PathVariable("recipeId") Integer recipeId,
                          @RequestBody Image img) {
        return img;
    }

    @GetMapping
    public List<Image> getAllImagesForRecipe(@PathVariable("recipeId") Integer recipeId) {
        return new ArrayList<Image>();
    }

    @PutMapping("/{id}")
    public Image updateImageFromRecipe(@PathVariable("recipeId") Integer recipeId,
                                       @PathVariable("id") Integer id,
                                       @RequestBody Image img) {
        return img;
    }

    @DeleteMapping("/{id}")
    public String deleteImageFromRecipe(@PathVariable("recipeId") Integer recipeId) {
        return "not deleted";
    }
}
