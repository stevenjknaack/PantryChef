package com.pantrychef.backend.recipes;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NutritionalContent {
    private Integer calories;
    private String fat; // TODO how to handle units
    private String saturatedFat;
    private String cholesterol;
    private String sodium;
    private String carbohydrate;
    private String fiber;
    private String sugar;
    private String protein;
}
