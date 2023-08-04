import React, { useState } from 'react';

function AddRecipe() {
    const [recipe, setRecipe] = useState({
        title: '',
        cookTime:'',
        prepTime:'',
        totalTime:'',
        description: '',
        ingredients:'',
        instructions: '',
        recipeIngredientQuantities: '',
        calories:'',
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

        // add other fields, need to check over what we are keeping and not
    });

    // console.log("RecipeId,Name,AuthorId,AuthorName,CookTime,PrepTime,TotalTime,DatePublished,Description,Images,RecipeCategory,Keywords,RecipeIngredientQuantities,RecipeIngredientParts,AggregatedRating,ReviewCount,Calories,FatContent,SaturatedFatContent,CholesterolContent,SodiumContent,CarbohydrateContent,FiberContent,SugarContent,ProteinContent,RecipeServings,RecipeYield,RecipeInstructions")
    
    const handleChange = (e) => {
        const { name, value } = e.target;
        setRecipe({
            ...recipe,
            [name]: value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // logic to save the recipe to your server/database
        // will need all the attributes from above, + username + a generated recipeID (from the backend) + a way to get the date

        alert('Recipe added successfully!');
    };

    return (
        <div className="page-content">
            <h1>Add a New Recipe</h1>
            <h6>Write 'NA' for anything that is not applicable</h6>
            <form onSubmit={handleSubmit}>
                <label>
                    Title:
                    <input type="text" name="title" value={recipe.title} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                    Ingredients:
                    <textarea name="ingredients" value={recipe.ingredients} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Recipe Ingredient Quantities:
                    <textarea name="ingredient quantities" value={recipe.recipeIngredientQuantities} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                    Instructions:
                    <textarea name="instructions" value={recipe.instructions} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                    Cook Time:
                    <textarea name="cook time" value={recipe.cookTime} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                    Prep Time:
                    <textarea name="prep time" value={recipe.prepTime} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Total Time:
                    <textarea name="total time" value={recipe.totalTime} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Description:
                    <textarea name="description" value={recipe.description} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                calories:
                    <textarea name="calorites" value={recipe.calories} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Fat Content:
                    <textarea name="fat content" value={recipe.fatContent} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Saturated Fat Content:
                    <textarea name="sat fat content" value={recipe.saturatedFatContent} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Cholesterol Content:
                    <textarea name="cholesterol content" value={recipe.cholesterolContent} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Sodium Content:
                    <textarea name="sodium content" value={recipe.sodiumContent} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Carbohydrate Content:
                    <textarea name="carbs" value={recipe.carbohydrateContent} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Fiber Content:
                    <textarea name="fiber" value={recipe.fiberContent} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Sugar Content:
                    <textarea name="sugar" value={recipe.sugarContent} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Protein Content:
                    <textarea name="protein" value={recipe.proteinContent} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Recipe Servings:
                    <textarea name="servings" value={recipe.recipeServings} onChange={handleChange} required />
                </label>
                <br></br>
                <label>
                Recipe Yield:
                    <textarea name="yield" value={recipe.recipeYield} onChange={handleChange} required />
                </label>
                <br></br>
                <br></br>

                {/* add other fields */}
                <button type="submit">Add Recipe</button>
            </form>
        </div>
    );
}

export default AddRecipe;
