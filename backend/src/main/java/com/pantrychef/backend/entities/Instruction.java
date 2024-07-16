package com.pantrychef.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pantrychef.backend.entities.recipes.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An instruction needed to make a food item specified in a recipe
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Instruction")
public class Instruction {
    @Id
    @Column(name="id")
    @SequenceGenerator(
            name = "instruction_sequence",
            sequenceName = "instruction_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "instruction_sequence"
    )
    private Integer id;

    @Column(
            name = "step_number",
            nullable = false
    )
    private Integer stepNumber;

    @Column(
            name = "text",
            nullable = false
    )
    private String text;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id"
    )
    private Recipe recipe;
}
