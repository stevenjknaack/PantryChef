package com.pantrychef.backend.entities;

import com.fasterxml.jackson.annotation.*;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Details the experience and/or thoughts a user has about a recipe
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "review")
public class Review {
    @Id
    @Column(
            name = "id",
            nullable = false
    )
    @SequenceGenerator(
            name = "review_sequence",
            sequenceName = "review_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "review_sequence"
    )
    private Integer id;

    @Column(
            name = "review",
            nullable = false
    )
    private String review;

    @Column(
            name = "rating",
            nullable = false
    )
    private Integer rating;

    @Column(
            name = "date_created",
            nullable = false
    )
    private LocalDateTime dateCreated;

    @Column(
            name = "date_modified",
            nullable = false
    )
    private LocalDateTime dateModified;

    @JsonIncludeProperties({"id"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Recipe recipe;

    @JsonIncludeProperties({"username"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( // TODO make un-nullable
            name = "author_username",
            referencedColumnName = "username"
    )
    private User author;

    @JsonIgnore
    @ManyToMany(
            mappedBy = "likedReviews",
            fetch = FetchType.LAZY
    )
    private List<User> likes;

    /**
     * Adds a like to this review
     * @param like A user that is liking this review
     */
    public void addLike(User like) {
        if (this.likes == null) this.likes = new ArrayList<>();
        this.likes.add(like);
    }

    /**
     * Removes a like from the review
     * @param like The user who is unliking the review
     */
    public void removeLike(User like) {
        if (this.likes == null) return;
        this.likes.remove(like);
    }
}
