package com.pantrychef.backend.images;

import com.pantrychef.backend.recipes.Recipe;
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
    private Integer id;

    @Column(
            name = "url",
            nullable = false
    )
    private String url;

    @Column(name = "alt_text")
    private String altText;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id"
    )
    private Recipe recipe;
}
