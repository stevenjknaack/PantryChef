package com.pantrychef.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pantrychef.backend.entities.recipes.Recipe;
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
@Table(name = "image")
public class Image {
    @Id
    @Column(
            name = "id",
            nullable = false
    )
    @SequenceGenerator(
            name = "image_sequence",
            sequenceName = "image_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_sequence"
    )
    private Integer id;

    @Column(
            name = "url",
            nullable = false
    )
    private String url;

    @Column(name = "alt_text")
    private String altText;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id"
    )
    private Recipe recipe;
}
