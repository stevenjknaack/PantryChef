package com.pantrychef.backend.entities.ingredients;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @Column(name = "id")
    @SequenceGenerator(
            name = "ingredient_sequence",
            sequenceName = "ingredient_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ingredient_sequence"
    )
    private Integer id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(name = "quantity")
    private String quantity;
}
