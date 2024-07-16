package com.pantrychef.backend.entities.recipes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Details many aspects of a recipe's nutritional content
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AttributeOverrides({
        @AttributeOverride(
                name = "calories",
                column = @Column(name = "calories")
        ),
        @AttributeOverride(
                name = "fat",
                column = @Column(name = "fat")
        ),
        @AttributeOverride(
                name = "saturatedFat",
                column = @Column(name = "saturated_fat")
        ),
        @AttributeOverride(
                name = "cholesterol",
                column = @Column(name = "cholesterol")
        ),
        @AttributeOverride(
                name = "sodium",
                column = @Column(name = "sodium")
        ),
        @AttributeOverride(
                name = "carbohydrate",
                column = @Column(name = "carbohydrate")
        ),
        @AttributeOverride(
                name = "fiber",
                column = @Column(name = "fiber")
        ),
        @AttributeOverride(
                name = "sugar",
                column = @Column(name = "sugar")
        ),
        @AttributeOverride(
                name = "protein",
                column = @Column(name = "protein")
        )
})
public class NutritionalContent {
    private Integer calories;
    private String fat;
    private String saturatedFat;
    private String cholesterol;
    private String sodium;
    private String carbohydrate;
    private String fiber;
    private String sugar;
    private String protein;
}
