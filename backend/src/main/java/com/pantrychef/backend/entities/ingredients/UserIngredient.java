package com.pantrychef.backend.entities.ingredients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pantrychef.backend.entities.users.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * A food resource that a user has access to in their pantry
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_ingredient")
public class UserIngredient extends Ingredient {
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "username",
            referencedColumnName = "username",
            nullable = false
    )
    private User user;
}
