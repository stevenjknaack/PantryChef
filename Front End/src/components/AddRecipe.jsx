import React, { useState } from 'react';

function AddRecipe() {
    const [recipe, setRecipe] = useState({
        title: '',
        ingredients: '',
        instructions: '',
        // Add other fields, need to check over what we are keeping and not
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setRecipe({
            ...recipe,
            [name]: value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // Logic to save the recipe to your server/database
        // For example, make a POST request to our API here

        alert('Recipe added successfully!'); // You can replace this with a more sophisticated notification

    };

    return (
        <div className="add-recipe">
            <h1>Add a New Recipe</h1>
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
                    Instructions:
                    <textarea name="instructions" value={recipe.instructions} onChange={handleChange} required />
                </label>
                <br></br>
                {/* Add other fields as needed */}
                <button type="submit">Add Recipe</button>
            </form>
        </div>
    );
}

export default AddRecipe;
