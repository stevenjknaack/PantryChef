package com.pantrychef.backend.services;

import com.pantrychef.backend.entities.Image;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.errors.ResourceNotFoundException;
import com.pantrychef.backend.errors.UnauthorizedException;
import com.pantrychef.backend.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides business logic for dealing with images
 */
@Service
@RequiredArgsConstructor
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    /**
     * Creates an image or updates an existing one
     * @param image The image
     * @param recipe The recipe this image belongs to
     * @return The saved image
     */
    public Image saveImage(Image image, Recipe recipe) {
        if (image.getId() != null) {
            Image extantImage = imageRepository.findById(image.getId())
                    .orElseThrow(ResourceNotFoundException::new);

            if (!extantImage.getRecipe().getId().equals(recipe.getId())) {
                throw new UnauthorizedException();
            }
        }

        image.setRecipe(recipe);
        return imageRepository.save(image);
    }
}
