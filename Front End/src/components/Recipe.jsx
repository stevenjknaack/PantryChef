import userLoggedIn from "../context/userLoggedIn";
import { useContext, useState, useEffect } from 'react';
import Review from "./Review";

// ingredients will become ingredients, recipe
function Recipe({ recipesData }) {

  const [loggedIn, setLoggedIn] = useContext(userLoggedIn);
  const [isLoading, setIsLoading] = useState(true);  // Add this line to initialize loading state

  console.log(loggedIn)
  useEffect(() => {
    // This will run when the component mounts
    const timer = setTimeout(() => {
      setIsLoading(false); // After 10 seconds, set isLoading to false
    }, 15000);

    return () => clearTimeout(timer); // Cleanup timer if the component is unmounted
  }, []);


  if (isLoading) {
    return <div className="page-content">Loading Recipe Page...</div>;
  }

  const addFavorite = (recipeID) => {

    console.log(recipeID)

    fetch("http://localhost:8000/addfavorites", {
      method: "POST",
      body: JSON.stringify({
        username: loggedIn,
        recipeID: recipeID,
      }),
    }).then(res => {
      if (res.status === 401) {
        alert('already in favorites')
      } else if (res.status === 404) {
        alert('failed to add recipe')
      } else if (res.status === 200) {
        alert('recipe successfully added to favorites!')
      } else if (res.status === 500) {
        alert('unknown error')
      }
    })
  };

  return (
    <div className="page-content">
      <h1>Recipes:</h1>
      {recipesData && recipesData.length > 0 ? (
        <ul>
          {recipesData.map((recipe) => (
            <li key={recipe.RecipeID}>
              <h2>{recipe.Name}</h2>
              <img className="small-image" src={recipe.Link.replace(/"/g, '')} />
              <p><strong>Description:</strong> {recipe.Description}</p>
              <p><strong>Ingredients:</strong> {recipe.Ingredients}</p>
              <p><strong>Servings:</strong> {recipe.Servings}</p>
              <p><strong>Instructions:</strong> {recipe.Instructions}</p>
              <p><strong>Time:</strong> {recipe.TimeDescription}</p>
              <p><strong>Nutritional Content:</strong> {recipe.NutritionalContent}</p>
              <p><strong>Author:</strong> {recipe.AuthorUsername}</p>
              <p><strong>Date Modified:</strong> {recipe.DateModified}</p>
              <p><strong>Date Published:</strong> {recipe.DatePublished}</p>
              <Review RecipeID={recipe.RecipeID}></Review>
              <button onClick={() => addFavorite(recipe.RecipeID)}>Add To Favorites</button>
            </li>
          ))}
        </ul>
      ) : (
        <p>No results found.</p>
      )}
    </div>
  );


}

export default Recipe;
