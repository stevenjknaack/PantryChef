import userLoggedIn from "../context/userLoggedIn";
import React, { useState, useContext } from 'react';

function AddRecipe() {

    // Get the current logged-in user status using the 'userLoggedIn' context
    const [loggedIn, setLoggedIn] = useContext(userLoggedIn);

    // Date object to capture the current date/time
    const d = new Date();

    // useState hook to manage the recipe details
    const [recipe, setRecipe] = useState({
        title: '',
        cookTime: '',
        prepTime: '',
        totalTime: '',
        description: '',
        ingredients: [['', '']],
        instructions: '',
        calories: '',
        fatContent: '',
        saturatedFatContent: '',
        cholesterolContent: '',
        sodiumContent: '',
        carbohydrateContent: '',
        fiberContent: '',
        sugarContent: '',
        proteinContent: '',
        recipeServings: '',
        recipeYield: '',
        image: '',
        username: loggedIn,
        datePublished: '',
        dateModified: 'N/A',
        // recipeID is sent using the backend
    });

    // Handler to update a specific attribute of the recipe object
    const handleChange = (e) => {
        const { name, value } = e.target;
        setRecipe({
            ...recipe,
            [name]: value,
        });
    };

    // Handler to update specific ingredient within the ingredients list
    const handleIngredientChange = (e, index, position) => {
        const list = [...recipe.ingredients];
        list[index][position] = e.target.value;
        setRecipe(prevState => ({ ...prevState, ingredients: list }));
    };

    // Handler to add a new ingredient field to the form
    const handleAddClick = () => {
        const list = [...recipe.ingredients];
        list.push(['', '']); // push a new tuple
        setRecipe(prevState => ({ ...prevState, ingredients: list }));
        console.log(recipe.ingredients)
    };

    // Handler to remove an ingredient field from the form
    const handleRemoveClick = index => {
        const list = [...recipe.ingredients];
        list.splice(index, 1);
        setRecipe(prevState => ({ ...prevState, ingredients: list }));
    };

    // Handler to submit the form and save the recipe details
    const handleSubmit = (e) => {
        e.preventDefault();

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

        // HTTP request to save the recipe details to the server
        fetch("http://localhost:8000/addrecipe", {
            method: "POST",
            body: JSON.stringify({

                Title: `${recipe.title}`,

                Description: `${recipe.description}`,

                Ingredients: ingreds,

                Servings: `Servings: ${recipe.recipeServings} | Yield: ${recipe.recipeYield}`,

                Instructions: recipe.instructions,

                Time: `Cook Time: ${recipe.cookTime} | Prep Time: ${recipe.prepTime} | Total Time: ${recipe.totalTime}`,

                NutritionalContent: `Calories: ${recipe.calories} | FatContent: ${recipe.fatContent} | SaturatedFatContent: ${recipe.saturatedFatContent} | CholesterolContent: ${recipe.cholesterolContent} | SodiumContent: ${recipe.sodiumContent} | CarbohydrateContent: ${recipe.carbohydrateContent} | FiberContent: ${recipe.fiberContent} | SugarContent: ${recipe.sugarContent} | ProteinContent: ${recipe.proteinContent}`,

                Author: recipe.username,

                DateModified: recipe.dateModified,

                DatePublished: d.toISOString(),

                Image: recipe.image,

            }),

        }).then(res => {

            if (res.status === 401) {
                alert('recipe failed to add!')
                return false;
            }
            else if (res.status === 200) {
                alert('recipe successfully added!')
                return true;

            } else if (res.status === 500) {
                alert('unknown error')
                return false;
            }

        }).then(success => {
            if (success == false) {
                return;
            }
            else {
                setRecipe({
                    title: '',
                    cookTime: '',
                    prepTime: '',
                    totalTime: '',
                    description: '',
                    ingredients: [['', '']],
                    instructions: '',
                    recipeIngredientQuantities: '',
                    calories: '',
                    fatContent: '',
                    saturatedFatContent: '',
                    cholesterolContent: '',
                    sodiumContent: '',
                    carbohydrateContent: '',
                    fiberContent: '',
                    sugarContent: '',
                    proteinContent: '',
                    recipeServings: '',
                    recipeYield: '',
                    username: loggedIn,
                    datePublished: '', 
                    dateModified: 'N/A'
                });
            }
        })
    };

    // Return the form's JSX to be rendered
    return (
        <div className="page-content">
            <h1>Add a New Recipe</h1>
            <h6>Write 'N/A' for anything that is not applicable for a required field (*)</h6>
            <form onSubmit={handleSubmit}>
                <label>
                    Title*:
                    <input type="text" name="title" value={recipe.title} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                    Description*:
                    <textarea name="description" value={recipe.description} onChange={handleChange} required />
                </label>
                <br></br>
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
                <br></br>
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
                    <textarea name="image" value={recipe.image} onChange={handleChange} maxLength="2000"  />
                </label>
                <br></br>

                <button type="submit">Add Recipe</button>
            </form>
        </div>
    );
}

export default AddRecipe;
