package com.pantrychef.backend.repositories;

import com.pantrychef.backend.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    public List<Image> findByRecipeId(Integer recipeId);
}