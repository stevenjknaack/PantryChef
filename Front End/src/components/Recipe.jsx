import userLoggedIn from "../context/userLoggedIn";
import { useContext, useState, useEffect } from 'react';

// ingredients will become ingredients, recipe
function Recipe({ recipesData }) {

  const [loggedIn, setLoggedIn] = useContext(userLoggedIn);
  const [isLoading, setIsLoading] = useState(true);  // Add this line to initialize loading state
  const [favorites, setFavorites] = useState([]);

  console.log(loggedIn)
  useEffect(() => {
    // This will run when the component mounts
    const timer = setTimeout(() => {
      setIsLoading(false); // After 5 seconds, set isLoading to false
    }, 4000);

    return () => clearTimeout(timer); // Cleanup timer if the component is unmounted
  }, []);


  if (isLoading) {
    return <div className="page-content">Loading Recipe Page...</div>;
  }


  // // Function to load favorites from a server but to be held in a list to show button or not
  // const loadFavorites = () => {

  //   // use this query in the backend, then return array to recipe page: 
  //   /**
  //    * SELECT R.RecipeID 
  // FROM Recipe AS R JOIN Favorites AS F ON R.RecipeID = F.RecipeID 
  // WHERE Username = ?;
  //    */
  //   // this is so we have a list of the users favorites so we can use the button
  //   fetch("http://localhost:8000/getFavoritesRecipe", {
  //     method: "POST",
  //     body: JSON.stringify({
  //       username: loggedIn,
  //     }),

  //   }).then(res => {

  //     if (res.status === 401) {
  //       alert('failed to find user')
  //       return false;
  //     } else if (res.status === 404) {
  //       alert('no favorited recipes')
  //       return false;
  //     } else if (res.status === 200) {
  //       return res.json()
  //     } else if (res.status === 405) {
  //       alert('unknown error')
  //       return false;
  //     }
  //   }).then(json => {
  //     if (json == false) {
  //       return;
  //     }
  //     else {
  //       setFavorites(json.recipes);
  //       console.log(json.recipes)
  //     }
  //   })

  // };
  // useEffect(() => {
  //   loadFavorites();
  // }, [loggedIn]);


  const addFavorite = (recipeID) => {

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
      <h1>Recipes:</h1>
      {recipesData && recipesData.length > 0 ? (
        <ul>
          {recipesData.map((recipe) => (
            <li key={recipe.RecipeID}>
              <h2>{recipe.Name}</h2>
              {/* Uncomment the following line after setting up the CSS */}
              {/* <img src={recipe.Link.replace(/"/g, '')} alt={recipe.Name} /> */}
              <p><strong>Description:</strong> {recipe.Description}</p>
              <p><strong>Ingredients:</strong> {recipe.Ingredients}</p>
              <p><strong>Servings:</strong> {recipe.Servings}</p>
              <p><strong>Instructions:</strong> {recipe.Instructions}</p>
              <p><strong>Time:</strong> {recipe.TimeDescription}</p>
              <p><strong>Nutritional Content:</strong> {recipe.NutritionalContent}</p>
              <p><strong>Author:</strong> {recipe.AuthorUsername}</p>
              <p><strong>Date Modified:</strong> {recipe.DateModified}</p>
              <p><strong>Date Published:</strong> {recipe.DatePublished}</p>
              {/* Uncomment the following lines if you want the "Add to Favorites" button */}
              {/* {!addedFavorites.includes(recipe.RecipeID) && (
                <button onClick={() => addFavorite(recipe.RecipeID)}>Add to Favorites</button>
              )} */}
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
