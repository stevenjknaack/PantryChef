package com.pantrychef.backend.recipes;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AttributeOverrides({
    @AttributeOverride(
            name = "servings",
            column = @Column(name = "servings")
    ),
    @AttributeOverride(
        name = "yield",
        column = @Column(name = "yield")
    )
})
public class PortionFacts {
    private String servings;
    private String yield;
}
