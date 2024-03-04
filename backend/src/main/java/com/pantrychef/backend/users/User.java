package com.pantrychef.backend.users;

import com.pantrychef.backend.ingredients.UserIngredient;
import com.pantrychef.backend.recipes.Recipe;
import com.pantrychef.backend.reviews.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
// MAYBE ADD A PROFILE PIC OPTION (Randomly generated)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @Column(
            name = "username",
            nullable = false
    )
    private String username;

    @Column(
            name = "email",
            nullable = false,
            unique = true
    )
    private String email;

    @Column(
            name = "hashed_password",
            nullable = false
    )
    private String hashedPassword;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<UserIngredient> pantry;

    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Recipe> recipes;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_recipe_likes",
            joinColumns = @JoinColumn(
                    name = "username",
                    referencedColumnName = "username"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "recipe_id",
                    referencedColumnName = "id"
            )
    )
    private List<Recipe> likedRecipes;

    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Review> reviews;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_review_likes",
            joinColumns = @JoinColumn(
                    name = "username",
                    referencedColumnName = "username"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "review_id",
                    referencedColumnName = "id"
            )
    )
    private List<Review> likedReviews;

    public void addIngredientToPantry(UserIngredient ingredient) {
        if (this.pantry == null) this.pantry = new ArrayList<>();
        this.pantry.add(ingredient);
    }

    public void addRecipe(Recipe recipe) {
        if (this.recipes == null) this.recipes = new ArrayList<>();
        this.recipes.add(recipe);
    }

    public void addLikedRecipe(Recipe recipe) {
        if (this.likedRecipes == null) this.likedRecipes = new ArrayList<>();
        this.likedRecipes.add(recipe);
    }

    public void addReview(Review review) {
        if (this.reviews == null) this.reviews = new ArrayList<>();
        this.reviews.add(review);
    }

    public void addLikedReview(Review review) {
        if (this.likedReviews == null) this.likedReviews = new ArrayList<>();
        this.likedReviews.add(review);
    }
}
