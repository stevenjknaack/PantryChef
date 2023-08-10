import React, { useState, useEffect, useContext } from 'react';
import userLoggedIn from '../context/userLoggedIn';


function YourRecipes() {

    const [loggedIn, setLoggedIn] = useContext(userLoggedIn);
    const [yourRecipes, setYourRecipes] = useState([]);
    const [isLoading, setIsLoading] = useState(true);  // Add this line to initialize loading state

    const loadYourRecipes = () => {
        // fetch favorites from the server when changed to favs
        fetch("http://localhost:8000/loadYourRecipes", {
            method: "POST",
            body: JSON.stringify({
                username: loggedIn,
            }),

        }).then(res => {
            if (res.status === 401) { // no recipes found
                return false;
            } else if (res.status === 200) { // recipes found
                return res.json()
            } else if (res.status === 500) {
                alert('unknown error')
                return false;
            }
        }).then(json => {
            if (json == false) {
                return;
            }
            else {
                setIsLoading(false);
                setYourRecipes(json.recipes);
            }
        })
    };

    useEffect(() => {
        loadYourRecipes();
    }, [loggedIn]);

    if (isLoading) {
        return <div className="page-content">Loading Your Recipes ...</div>;
    }

    const removeRecipe = (recipeID) => {
        console.log(recipeID)

        fetch("http://localhost:8000/deleteRecipe", {
            method: "DELETE",
            body: JSON.stringify({
                username: loggedIn,
                recipeID: recipeID,
            }),
        }).then(res => {
            if (res.status === 200) {
                alert('recipe removed!')
                return true;
            } else if (res.status === 500) {
                alert('unknown error')
                return false;
            } else if (res.status === 404) {
                alert('recipe was NOT removed!')
                return false;
            }
        }).then(success => {
            if (success == false) {
                return;
            }
            else {
                // reload displayed favorites if successful
                loadYourRecipes();
            }
        })

    }

    return (
        <div className="page-content">
            <h1>YOUR RECIPES</h1>
            {yourRecipes && yourRecipes.length > 0 ? (
                <ul>
                    {yourRecipes.map((recipe) => (
                        <li key={recipe.RecipeID}>
                            <h2>{recipe.Name}</h2>
                            {recipe.Images.split(" | ").map((imgLink, imgIndex) => (
                                <img key={imgIndex} className="small-image" src={imgLink.replace(/"/g, '')} />
                            ))}
                            <p><strong>Description:</strong> {recipe.Description}</p>
                            <p><strong>Ingredients:</strong> {recipe.Ingredients}</p>
                            <p><strong>Servings:</strong> {recipe.Servings}</p>
                            <p><strong>Instructions:</strong> {recipe.Instructions}</p>
                            <p><strong>Time:</strong> {recipe.TimeDescription}</p>
                            <p><strong>Nutritional Content:</strong> {recipe.NutritionalContent}</p>
                            <p><strong>Author:</strong> {recipe.AuthorUsername}</p>
                            <p><strong>Date Modified:</strong> {recipe.DateModified}</p>
                            <p><strong>Date Published:</strong> {recipe.DatePublished}</p>
                            <button onClick={() => removeRecipe(recipe.RecipeID)}>Remove Recipe</button>
                        </li>
                    ))}
                </ul>
            ) : (
                <><p>No Recipes found.</p><p>Click AddRecipes to add a recipe!</p></>
            )}
        </div>
    );
};

export default YourRecipes;
