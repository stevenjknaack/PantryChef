package com.pantrychef.backend.services;

import com.pantrychef.backend.entities.Instruction;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.errors.ResourceNotFoundException;
import com.pantrychef.backend.errors.UnauthorizedException;
import com.pantrychef.backend.repositories.InstructionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructionService {
    @Autowired
    private InstructionRepository instructionRepository;

    /**
     * assume recipe is fully saved
     * @param instr
     * @param recipe
     * @return
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
