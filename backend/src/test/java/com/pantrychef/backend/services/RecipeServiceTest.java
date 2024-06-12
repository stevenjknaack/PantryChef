package com.pantrychef.backend.services;

import com.pantrychef.backend.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private InstructionService instructionService;
    @Mock
    private RecipeIngredientService ingredientService;
    @InjectMocks
    private RecipeService recipeService;

    @Test
    public void createRecipe_ReturnsSavedRecipe() {

    }
}
