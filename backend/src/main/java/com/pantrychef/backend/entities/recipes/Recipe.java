package com.pantrychef.backend.entities.recipes;

import com.fasterxml.jackson.annotation.*;
import com.pantrychef.backend.entities.Image;
import com.pantrychef.backend.entities.ingredients.RecipeIngredient;
import com.pantrychef.backend.entities.Instruction;
import com.pantrychef.backend.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Instructions and information for a food item or meal that can be created
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "recipe")
public class Recipe {
    @Id
    @Column(
            name = "id",
            nullable = false
    )
    @SequenceGenerator(
            name = "recipe_sequence",
            sequenceName = "recipe_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recipe_sequence"
    )
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

    @JsonIgnoreProperties({"email"})
    @ManyToOne(fetch = FetchType.EAGER) //TODO
    @JoinColumn(
            name = "author_username",
            referencedColumnName = "username",
            nullable = false
    )
    private User author;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY
    )
    private List<Image> images;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY
    )
    private List<RecipeIngredient> ingredients;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY
    )
    private List<Instruction> instructions;

    @JsonIgnore // TODO maybe add likes count?
    @ManyToMany(
            mappedBy = "likedRecipes",
            cascade = CascadeType.REMOVE, //TODO does this cascade to users or likedRecipes table
            fetch = FetchType.LAZY
    )
    private List<User> likes;

    /**
     * Adds an image to the recipe
     * @param image The image
     */
    public void addImage(Image image) {
        if (this.images == null) this.images = new ArrayList<>();
        this.images.add(image);
    }

    /**
     * Adds an ingredient to the recipe
     * @param ingredient Then ingredient
     */
    public void addIngredient(RecipeIngredient ingredient) {
        if (this.ingredients == null) this.ingredients = new ArrayList<>();
        this.ingredients.add(ingredient);
    }

    /**
     * Adds an instruction to the recipe
     * @param instruction The instruction
     */
    public void addInstruction(Instruction instruction) {
        if (this.instructions == null) this.instructions = new ArrayList<>();
        this.instructions.add(instruction);
    }

    /**
     * Adds a like to the recipe
     * @param like The user who is liking the recipe
     */
    public void addLike(User like) {
        if (this.likes == null) this.likes = new ArrayList<>();
        this.likes.add(like);
    }
}
