package com.pantrychef.backend.recipes;

import com.pantrychef.backend.images.Image;
import com.pantrychef.backend.ingredients.RecipeIngredient;
import com.pantrychef.backend.instructions.Instruction;
import com.pantrychef.backend.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "recipe")
public class Recipe {
    @Id
    private Integer id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Embedded
    private NutritionalContent nutritionalContent;

    @Embedded
    private PortionFacts portionFacts;

    @Embedded
    private TimeFacts timeFacts;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "author_username",
            referencedColumnName = "username"
    )
    private User author;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Image> images;

    @OneToMany( // fetch type?
            mappedBy = "recipe",
            cascade = CascadeType.ALL
    )
    private List<RecipeIngredient> ingredients;

    @OneToMany( // Fetch type?
            mappedBy = "recipe",
            cascade = CascadeType.ALL
    )
    private List<Instruction> instructions;

    @ManyToMany(
            mappedBy = "likedRecipes",
            cascade = CascadeType.ALL
    )
    private List<User> likes;

    public void addImage(Image image) {
        if (this.images == null) this.images = new ArrayList<>();
        this.images.add(image);
    }
    public void addIngredient(RecipeIngredient ingredient) {
        if (this.ingredients == null) this.ingredients = new ArrayList<>();
        this.ingredients.add(ingredient);
    }

    public void addInstruction(Instruction instruction) {
        if (this.instructions == null) this.instructions = new ArrayList<>();
        this.instructions.add(instruction);
    }
    public void addLike(User like) {
        if (this.likes == null) this.likes = new ArrayList<>();
        this.likes.add(like);
    }
}
