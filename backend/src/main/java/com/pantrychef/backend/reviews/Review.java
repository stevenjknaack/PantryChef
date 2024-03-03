package com.pantrychef.backend.reviews;

import com.pantrychef.backend.recipes.Recipe;
import com.pantrychef.backend.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Reference;

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

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Recipe recipe;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "author_username",
            referencedColumnName = "username"
    )
    private User author;

    @ManyToMany(
            mappedBy = "likedReviews",
            cascade = CascadeType.ALL
    )
    private List<User> likes;
}
