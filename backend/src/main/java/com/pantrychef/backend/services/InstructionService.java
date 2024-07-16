package com.pantrychef.backend.services;

import com.pantrychef.backend.entities.Instruction;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.errors.ResourceNotFoundException;
import com.pantrychef.backend.errors.UnauthorizedException;
import com.pantrychef.backend.repositories.InstructionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides business logic for dealing with instructions
 */
@Service
@RequiredArgsConstructor
public class InstructionService {
    @Autowired
    private InstructionRepository instructionRepository;

    /**
     * Creates or updates an instruction. Assumes its recipe is fully saved
     * @param instr The instruction
     * @param recipe The instruction's recipe
     * @return Saved instruction
     */
    public Instruction saveInstruction(Instruction instr, Recipe recipe) {
        if (instr.getId() != null) {
            Instruction extantInstr = instructionRepository.findById(instr.getId())
                    .orElseThrow(ResourceNotFoundException::new);

            if (!extantInstr.getRecipe().getId().equals(recipe.getId())) { // TODO MIGHT BE UNNECESSARY
                throw new UnauthorizedException();
            }
        }

        instr.setRecipe(recipe);
        return instructionRepository.save(instr);
    }
}
