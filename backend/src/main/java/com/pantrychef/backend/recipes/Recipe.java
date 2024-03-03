package com.pantrychef.backend.recipes;

import jakarta.persistence.Embedded;
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

    @Embedded
    private NutritionalContent nutritionalContent;

    @Embedded
    private PortionFacts portionFacts;

    @Embedded
    private TimeFacts timeFacts;

    /*
    private User author;
    private List<Image> images;
    private List<String> keywords;
    private List<Ingredient>
    private List<String> instructions;
     */

}
