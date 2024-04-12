package com.pantrychef.backend.entities.ingredients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pantrychef.backend.entities.recipes.Recipe;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "recipe_ingredient")
public class RecipeIngredient {
    @Id
    @Column(name = "id")
    @SequenceGenerator(
            name = "recipe_ingredient_sequence",
            sequenceName = "recipe_ingredient_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recipe_ingredient_sequence"
    )
    private Integer id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(name = "quantity")
    private String quantity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id"
    )
    private Recipe recipe;
}
