package com.pantrychef.backend.repositories;

import com.pantrychef.backend.entities.Instruction;
import com.pantrychef.backend.entities.recipes.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * For database interactions related to Instructions
 */
@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Integer> {
}
