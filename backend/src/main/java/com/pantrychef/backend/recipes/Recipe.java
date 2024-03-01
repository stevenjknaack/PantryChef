package com.pantrychef.backend.recipes;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recipe {
    @Id
    private Integer id;

    private String name;

    private String description;

    private String category;



    /*
    private RecipeTimes times;
    private User author;
    private List<Image> images;
    keywords;
    private List<Ingredient>
    private RecipeNutritionalContent nutritionalContent;
    private List<String> instructions;
     */

}
