import React, { useState, useContext, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

function ModifyRecipe() {

    // Get the current location object which might contain state from a previous navigation
    const location = useLocation();
    // Extracting the recipe that we wish to modify from the location's state
    const recipeToModify = location.state?.selectedRecipe;

    // Parsing various attributes of the recipe into a more digestible format
    const ingredientsToBeModified = parseIngredients(recipeToModify.Ingredients);
    const nutritionValues = parseNutritionValues(recipeToModify.NutritionalContent);
    const servingValues = parseServingsAndYield(recipeToModify.Servings);
    const times = parseTimes(recipeToModify.TimeDescription);
    // Hook for programmatic navigation
    const navigate = useNavigate();

    // Get the current date
    const d = new Date();

    // Initial state for the recipe that we're modifying
    const [recipe, setRecipe] = useState({
        title: recipeToModify.Name,
        cookTime: times.cookTime,
        prepTime: times.prepTime,
        totalTime: times.totalTime,
        description: recipeToModify.Description,
        ingredients: ingredientsToBeModified,
        instructions: recipeToModify.Instructions,
        calories: nutritionValues.calories,
        fatContent: nutritionValues.fatContent,
        saturatedFatContent: nutritionValues.saturatedFatContent,
        cholesterolContent: nutritionValues.cholesterolContent,
        sodiumContent: nutritionValues.sodiumContent,
        carbohydrateContent: nutritionValues.carbohydrateContent,
        fiberContent: nutritionValues.fiberContent,
        sugarContent: nutritionValues.sugarContent,
        proteinContent: nutritionValues.proteinContent,
        recipeServings: servingValues.recipeServings,
        recipeYield: servingValues.recipeYield,
        image: recipeToModify.Images,
        username: recipeToModify.AuthorUsername,
        datePublished: recipeToModify.DatePublished,
        dateModified: recipeToModify.DateModified,
        recipeID: recipeToModify.RecipeID,
    });

    // Handler for updating the recipe state when input values change
    const handleChange = (e) => {
        const { name, value } = e.target;
        setRecipe({
            ...recipe,
            [name]: value,
        });
    };

    // Handler for updating specific ingredient at a particular index and position
    const handleIngredientChange = (e, index, position) => {
        const list = [...recipe.ingredients];
        list[index][position] = e.target.value;
        setRecipe(prevState => ({ ...prevState, ingredients: list }));
    };

    // Add a new ingredient tuple to the ingredients list
    const handleAddClick = () => {
        const list = [...recipe.ingredients];
        list.push(['', '']); // push a new tuple
        setRecipe(prevState => ({ ...prevState, ingredients: list }));
        console.log(recipe.ingredients)
    };

    // Remove a specific ingredient tuple from the ingredients list
    const handleRemoveClick = index => {
        const list = [...recipe.ingredients];
        list.splice(index, 1);
        setRecipe(prevState => ({ ...prevState, ingredients: list }));
    };

    // Parses a string to extract ingredient names and quantities using regex
    function parseIngredients(str) {
        const regex = /([a-zA-Z\s]+)\s\((\d+)\)/g;
        let match;
        const result = [];
        while ((match = regex.exec(str)) !== null) {
            result.push([match[1].trim(), match[2]]);
        }
        return result;
    }

     // Extracts nutrition values from the input string
    function parseNutritionValues(input) {
        const parseValue = (name) => {
            const regex = new RegExp(name + ':\\s([^|]+)');
            const match = input.match(regex);
            return match ? match[1].trim() : '';
        };

        return {
            calories: parseValue('Calories'),
            fatContent: parseValue('FatContent'),
            saturatedFatContent: parseValue('SaturatedFatContent'),
            cholesterolContent: parseValue('CholesterolContent'),
            sodiumContent: parseValue('SodiumContent'),
            carbohydrateContent: parseValue('CarbohydrateContent'),
            fiberContent: parseValue('FiberContent'),
            sugarContent: parseValue('SugarContent'),
            proteinContent: parseValue('ProteinContent')
        };
    }

    // Extracts servings and yield information from the input string
    function parseServingsAndYield(input) {
        const parseValue = (name) => {
            const regex = new RegExp(name + ':\\s([^|]+)');
            const match = input.match(regex);
            return match ? match[1].trim() : '';
        };

        return {
            recipeServings: parseValue('Servings'),
            recipeYield: parseValue('Yield')
        };
    }

    // Extracts different time-related values from the input string
    function parseTimes(input) {
        const parseValue = (name) => {
            const regex = new RegExp(name + ':\\s([^|]+)');
            const match = input.match(regex);
            return match ? match[1].trim() : '';
        };
        return {
            cookTime: parseValue('Cook Time'),
            prepTime: parseValue("Prep Time"),
            totalTime: parseValue("Total Time"),
        }
    }

    // Handler for the modification logic
    const handleMod = (e) => {
        e.preventDefault();

        console.log(recipe)

        const defaultNAFields = [
            'calories', 'fatContent', 'saturatedFatContent', 'cholesterolContent',
            'sodiumContent', 'carbohydrateContent', 'fiberContent', 'sugarContent',
            'proteinContent', 'recipeServings', 'recipeYield', 'image'
        ];

        // Setting default values to "N/A" for blank fields as there are not required
        defaultNAFields.forEach(field => {
            if (!recipe[field] || recipe[field] === "") {
                recipe[field] = "N/A";
            }
        });

        let ingreds = recipe.ingredients.map(ingredientTuple => ({
            IngredientName: ingredientTuple[0],
            Quantity: ingredientTuple[1]
        }))

        console.log(recipe.ingredients)
        console.log(ingreds)

        fetch("http://localhost:8000/modRecipe", {
            method: "PUT",
            body: JSON.stringify({
                // every recipe attribute

                Title: `${recipe.title}`,

                Description: `${recipe.description}`,

                Ingredients: ingreds,

                Servings: `Servings: ${recipe.recipeServings} | Yield: ${recipe.recipeYield}`,

                Instructions: recipe.instructions,

                Time: `Cook Time: ${recipe.cookTime} | Prep Time: ${recipe.prepTime} | Total Time: ${recipe.totalTime}`,

                NutritionalContent: `Calories: ${recipe.calories} | FatContent: ${recipe.fatContent} | SaturatedFatContent: ${recipe.saturatedFatContent} | CholesterolContent: ${recipe.cholesterolContent} | SodiumContent: ${recipe.sodiumContent} | CarbohydrateContent: ${recipe.carbohydrateContent} | FiberContent: ${recipe.fiberContent} | SugarContent: ${recipe.sugarContent} | ProteinContent: ${recipe.proteinContent}`,

                Author: recipe.username,

                DateModified: d.toISOString(),

                DatePublished: recipe.datePublished,

                Image: recipe.image,
                
                RecipeID: recipe.recipeID,
            }),
        }).then(res => {
            if (res.status === 200) {
                alert('recipe modified!')
                return true;
            } else if (res.status === 500) {
                alert('unknown error')
                return false;
            } else if (res.status === 404) {
                alert('recipe was NOT modified!')
                return false;
            }
        }).then(success => {
            if (success == false) {
                return;
            }
            else {
                navigate("/your-recipes")
            }
        })
    }

    // Render the form to modify the recipe
    return (
        <div className="page-content">
            <h1>Modify Recipe</h1>

            <h6>Write 'N/A' for anything that is not applicable for a required field (*)</h6>
            <form onSubmit={handleMod}>
                {/* The form fields similar to AddRecipe with placeholders and pre-filled values from recipeToModify */}
                <label>
                    Title*:
                    <input type="text" name="title" value={recipe.title} onChange={handleChange} />
                </label>
                <br>
                </br>
                <label>
                    Description*:
                    <input type="text" name="description" value={recipe.description} onChange={handleChange} />
                </label>
                <br></br>
                {
                    recipe.ingredients.map((ingredientTuple, index) => (
                        <div key={index}>
                            <label>
                                Ingredient*:
                                <input type="text" value={ingredientTuple[0]} onChange={(e) => handleIngredientChange(e, index, 0)} required />
                            </label>
                            <label>
                                Quantity*:
                                <input type="number" step="any" min="0" value={ingredientTuple[1]} onChange={(e) => handleIngredientChange(e, index, 1)} required />
                            </label>
                            {recipe.ingredients.length !== 1 && <button onClick={() => handleRemoveClick(index)}>Remove</button>}
                            {recipe.ingredients.length - 1 === index && <button onClick={handleAddClick}>Add More Ingredients</button>}
                        </div>
                    ))
                }

                <label>
                    Instructions*:
                    <textarea name="instructions" value={recipe.instructions} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                    Cook Time*:
                    <textarea name="cookTime" value={recipe.cookTime} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                    Prep Time*:
                    <textarea name="prepTime" value={recipe.prepTime} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                    Total Time*:
                    <textarea name="totalTime" value={recipe.totalTime} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                    calories:
                    <textarea name="calories" value={recipe.calories} onChange={handleChange} />
                </label>
                <br></br>
                <label>
                    Fat Content:
                    <textarea name="fatContent" value={recipe.fatContent} onChange={handleChange} />
                </label>
                <br></br>
                <label>
                    Saturated Fat Content:
                    <textarea name="saturatedFatContent" value={recipe.saturatedFatContent} onChange={handleChange} />
                </label>
                <br></br>
                <label>
                    Cholesterol Content:
                    <textarea name="cholesterolContent" value={recipe.cholesterolContent} onChange={handleChange} />
                </label>
                <br></br>
                <label>
                    Sodium Content:
                    <textarea name="sodiumContent" value={recipe.sodiumContent} onChange={handleChange} />
                </label>
                <br></br>
                <label>
                    Carbohydrate Content:
                    <textarea name="carbohydrateContent" value={recipe.carbohydrateContent} onChange={handleChange} />
                </label>
                <br></br>
                <label>
                    Fiber Content:
                    <textarea name="fiberContent" value={recipe.fiberContent} onChange={handleChange} />
                </label>
                <br></br>
                <label>
                    Sugar Content:
                    <textarea name="sugarContent" value={recipe.sugarContent} onChange={handleChange} />
                </label>
                <br></br>
                <label>
                    Protein Content:
                    <textarea name="proteinContent" value={recipe.proteinContent} onChange={handleChange} />
                </label>
                <br></br>
                <label>
                    Recipe Servings:
                    <textarea name="recipeServings" value={recipe.recipeServings} onChange={handleChange} />
                </label>
                <br></br>
                <label>
                    Recipe Yield:
                    <textarea name="recipeYield" value={recipe.recipeYield} onChange={handleChange} />
                </label>
                <br></br>
                <br></br>
                <label>
                    Image:
                    <textarea name="image" value={recipe.image} onChange={handleChange} maxLength="2000" />
                </label>
                <br></br>

                <button type="submit">Modify Recipe</button>
            </form>
        </div>
    );
};

export default ModifyRecipe;
