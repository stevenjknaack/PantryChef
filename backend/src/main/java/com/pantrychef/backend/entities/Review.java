package com.pantrychef.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.entities.users.User;
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
    private String dateCreated;

    @Column(
            name = "date_modified",
            nullable = false
    )
    private String dateModified;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( // TODO make un-nullable
            name = "author_username",
            referencedColumnName = "username"
    )
    private User author;

    @ManyToMany(
            mappedBy = "likedReviews",
            fetch = FetchType.LAZY
    )
    private List<User> likes;

    public void addLike(User like) {
        if (this.likes == null) this.likes = new ArrayList<>();
        this.likes.add(like);
    }
}
