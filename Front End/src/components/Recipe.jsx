import userLoggedIn from "../context/userLoggedIn";
import {useContext} from 'react';

// ingredients will become ingredients, recipe
function Recipe({ ingredients, recipesData }) {

  const [loggedIn, setLoggedIn] = useContext(userLoggedIn);
  console.log(loggedIn)

  const addFavorite = (recipeID) => {

    // what will happen is when the + button gets clicked, 
    // it will be added to the favorites in the database (recipeID, username)
    // button will also disapper on the recipe card
    fetch("http://localhost:8000/addfavorites", {
      method: "POST",
      body: JSON.stringify({
        username: loggedIn,
        recipeID: recipeID, 
      }),

    }).then(res => {

      if (res.status === 401) {
        alert('failed to find user')
      } else if (res.status === 404) {
        alert('failed to find recipe')
      } else if (res.status === 200) {
        alert('recipe successfully added to favorites!')
      } else if (res.status === 405) {
        alert('unknown error')
      }
    })
  };

  return (
    <div className="page-content">
      <h1>Recipes</h1>
      
      {/* Display the given list of ingredients */}
      <div className="ingredients-list">
        <h2>Your Ingredients</h2>
        <ul>
          {ingredients.map((ingredient, index) => (
            <li key={index}>{ingredient}</li>
          ))}
        </ul>
      </div>
      
      {/* Display the recipes */}
      <ul>
        {recipesData.recipes && recipesData.recipes.map((recipe) => (
          <li key={recipe.recipeID}>
            <h2>{recipe.name}</h2>
            <h2>{recipe.ingredient}</h2>
            <button onClick={() => addFavorite(recipe.recipeID)}>Add to Favorites</button>
          </li>
        ))}
      </ul>
    </div>
  );
     
}

export default Recipe;
