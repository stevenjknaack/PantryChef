package com.pantrychef.backend.recipes;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeFacts {
    private String prepTime;
    private String cookTime;
    private String totalTime; // Might not include -Time suffix
}
