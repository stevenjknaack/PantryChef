package com.pantrychef.backend.entities.instructions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pantrychef.backend.entities.recipes.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO make composite key

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Instruction")
@IdClass(Instruction.class)
public class Instruction {
    @Id
    @Column(
            name = "step_number",
            nullable = false
    )
    private Integer stepNumber;

    @Id
    @JsonIgnore
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id"
    )
    private Recipe recipe;

    @Column(
            name = "text",
            nullable = false
    )
    private String text;
}
