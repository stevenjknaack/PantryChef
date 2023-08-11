import userLoggedIn from "../context/userLoggedIn";
import { useContext, useState, useEffect } from 'react';
import Review from "./Review";

// ingredients will become ingredients, recipe
function Recipe({ recipesData }) {

  // Get the user's logged in status from context
  const [loggedIn, setLoggedIn] = useContext(userLoggedIn);
  // State to manage the loading screen display
  const [isLoading, setIsLoading] = useState(true);  // Add this line to initialize loading state

  // Hook that runs after the component is mounted
  useEffect(() => {
    // This will run when the component mounts
    const timer = setTimeout(() => {
      setIsLoading(false); // After 10 seconds, set isLoading to false
    }, 8000);

    return () => clearTimeout(timer); // Cleanup timer if the component is unmounted
  }, []);


  // Show a loading message while the content is being prepared
  if (isLoading) {
    return <div className="page-content">Loading Recipe Page...</div>;
  }

  // Function to add a recipe to the user's favorites
  const addFavorite = (recipeID) => {

    // Send a POST request to add the selected recipe to favorites
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

  // Render the list of recipes and their details
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
