package com.pantrychef.backend.entities.instructions;

import com.pantrychef.backend.entities.recipes.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructionId implements Serializable {
    private Integer stepNumber;
    private Recipe recipe;
}
