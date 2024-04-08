package com.pantrychef.backend.repositories;

import com.pantrychef.backend.entities.instructions.Instruction;
import com.pantrychef.backend.entities.instructions.InstructionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, InstructionId> {
}
