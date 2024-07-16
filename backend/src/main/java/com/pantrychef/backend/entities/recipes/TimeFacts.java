package com.pantrychef.backend.entities.recipes;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Details of a recipe's time requirements
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AttributeOverrides({
        @AttributeOverride(
                name = "prepTime",
                column = @Column(name = "prep_time")
        ),
        @AttributeOverride(
                name = "cookTime",
                column = @Column(name = "cook_time")
        ),
        @AttributeOverride(
                name = "totalTime",
                column = @Column(name = "total_time")
        )
})
public class TimeFacts {
    private String prepTime;
    private String cookTime;
    private String totalTime;
}
