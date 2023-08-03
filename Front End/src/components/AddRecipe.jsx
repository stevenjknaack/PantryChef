import React, { useState } from 'react';

function AddRecipe() {
    const [recipe, setRecipe] = useState({
        title: '',
        ingredients: '',
        instructions: '',
        // add other fields, need to check over what we are keeping and not
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

        // logic to save the recipe to your server/database

        alert('Recipe added successfully!'); // You can replace this with a more sophisticated notification

    };

    return (
        <div className="page-content">
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
                {/* add other fields */}
                <button type="submit">Add Recipe</button>
            </form>
        </div>
    );
}

export default AddRecipe;
