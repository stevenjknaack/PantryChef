package com.pantrychef.backend.repositories;

import com.pantrychef.backend.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * For database interactions related to Images
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
