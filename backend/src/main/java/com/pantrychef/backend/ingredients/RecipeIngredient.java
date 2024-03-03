package com.pantrychef.backend.ingredients;

import com.pantrychef.backend.recipes.Recipe;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "recipe_ingredient")
public class RecipeIngredient extends Ingredient {
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Recipe recipe;
}
