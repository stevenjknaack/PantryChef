package com.pantrychef.backend.instructions;

import com.pantrychef.backend.recipes.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Instruction")
public class Instruction {
    @Id
    @Column(
            name = "step_number",
            nullable = false
    )
    private Integer stepNumber;

    @Id
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
