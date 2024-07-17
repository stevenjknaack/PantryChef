package com.pantrychef.backend.entities.users;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.pantrychef.backend.entities.Review;
import com.pantrychef.backend.entities.ingredients.UserIngredient;
import com.pantrychef.backend.entities.recipes.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
// TODO MAYBE ADD A PROFILE PIC OPTION (Randomly generated)

/**
 * A person that may interact with recipes and reviews
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@JsonIdentityInfo(property = "username", generator = ObjectIdGenerators.PropertyGenerator.class)
@JsonIgnoreProperties({"enabled", "accountNonExpired", "credentialsNonExpired",  "authorities", "accountNonLocked"})
@Table(name = "user")
public class User implements UserDetails {
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

    @JsonIgnore
    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<UserIngredient> pantry;

    @JsonIgnore
    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Recipe> recipes;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
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

    @JsonIgnore
    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Review> reviews;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
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

    /**
     * Adds an ingredient to this user's pantry
     * @param ingredient The ingredient
     */
    public void addIngredientToPantry(UserIngredient ingredient) {
        if (this.pantry == null) this.pantry = new ArrayList<>();
        this.pantry.add(ingredient);
    }

    /**
     * Attributes a recipe to this author
     * @param recipe The recipe
     */
    public void addRecipe(Recipe recipe) {
        if (this.recipes == null) this.recipes = new ArrayList<>();
        this.recipes.add(recipe);
    }

    /**
     * Add a like to the recipe by this user
     * @param recipe The recipe
     */
    public void addLikedRecipe(Recipe recipe) {
        if (this.likedRecipes == null) this.likedRecipes = new ArrayList<>();
        this.likedRecipes.add(recipe);
    }

    /**
     * Removes a recipe like from the user
     * @param recipe The recipe
     */
    public void unlikeRecipe(Recipe recipe) {
        if (this.likedRecipes == null) return;
        this.likedRecipes.remove(recipe);
    }

    /**
     * Attributes a review to this user
     * @param review The review
     */
    public void addReview(Review review) {
        if (this.reviews == null) this.reviews = new ArrayList<>();
        this.reviews.add(review);
    }

    /**
     * Add a like to the review by this user
     * @param review The review
     */
    public void addLikedReview(Review review) {
        if (this.likedReviews == null) this.likedReviews = new ArrayList<>();
        this.likedReviews.add(review);
    }

    /**
     * Removes a review like from the user
     * @param review The review
     */
    public void unlikeReview(Review review) {
        if (this.likedReviews == null) return;
        this.likedReviews.remove(review);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
