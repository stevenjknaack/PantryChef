package com.pantrychef.backend.services;

import com.pantrychef.backend.entities.ingredients.UserIngredient;
import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.errors.InvalidRequestException;
import com.pantrychef.backend.errors.ResourceNotFoundException;
import com.pantrychef.backend.repositories.UserIngredientRepository;
import com.pantrychef.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Business logic related to user ingredients
 */
@Service
public class UserIngredientService {
    @Autowired
    private UserIngredientRepository userIngrRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Creates or updates a user ingredient
     * @param id The id of the ingredient. Should be null if creating
     * @param userIngr The user ingredient to save
     * @param user The user trying to save the ingredient
     * @return The saved ingredient
     */
    public UserIngredient saveUserIngredient(
            Integer id, UserIngredient userIngr, User user
    ) {
        if (id != null) {
            UserIngredient extantIngr = userIngrRepository.findById(id)
                    .orElseThrow(ResourceNotFoundException::new);

            if (!extantIngr.getUser().equals(user)) {
                throw new InvalidRequestException();
            }

            userIngr.setUser(extantIngr.getUser());
        } else {
            userIngr.setUser(user);
        }

        userIngr.setId(id);
        return userIngrRepository.save(userIngr);
    }

    /**
     * Deletes a user's ingredient
     * @param user The user trying to delete the ingredient
     * @param id The ingredient's id
     * @return The deleted ingredient
     */
    public UserIngredient deleteUserIngredient(Integer id, User user) {
        if (id == null) throw new InvalidRequestException();

        UserIngredient extantIngr = userIngrRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        if (!extantIngr.getUser().equals(user)) {
            throw new InvalidRequestException();
        }

        userIngrRepository.delete(extantIngr);

        return extantIngr;
    }
}
