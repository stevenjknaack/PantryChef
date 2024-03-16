package com.pantrychef.backend.dtos;

import com.pantrychef.backend.entities.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResultDTO {
    private Integer id;
    private String name;
    private String description;
    private String authorUsername;
    private Image mainImage;
    private Integer aggregateRating;
}
