import React, { useState, useEffect, useContext } from 'react';
import userLoggedIn from '../context/userLoggedIn';

function Favorites() {

  // Obtain the logged-in user context
  const [loggedIn, setLoggedIn] = useContext(userLoggedIn);
  // State for storing favorite recipes
  const [favorites, setFavorites] = useState([]);
  // State for checking if data is still being loaded
  const [isLoading, setIsLoading] = useState(true);

  // Function to load favorites from a server
  const loadFavorites = () => {
    // fetch favorites from the server when changed to favs
    fetch("http://localhost:8000/getfavorites", {
      method: "POST",
      body: JSON.stringify({
        username: loggedIn,
      }),

    }).then(res => {
      if (res.status === 401) { // no favs found
        return false;
      } else if (res.status === 200) { // favs found
        return res.json()
      } else if (res.status === 405) {
        alert('unknown error')
        return false;
      }
    }).then(json => {
      if (json == false) {
        return;
      }
      else {
        setIsLoading(false); // Set loading to false since data is fetched
        setFavorites(json.recipes); // Update the favorites state
      }
    })
  };

  // UseEffect to load favorites when the component mounts or when the logged-in user changes
  useEffect(() => {
    loadFavorites();
  }, [loggedIn]);

  // Display a loading message while data is being fetched
  if (isLoading) {
    return <div className="page-content">Loading Favorites Page...</div>;
  }

  // Function to remove a favorite recipe
  const removeFavorite = (recipeID) => {

    // Request to delete a favorite from the server
    fetch("http://localhost:8000/deletefavorites", {
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
        // Reload displayed favorites if deletion was successful
        loadFavorites();
      }
    })
  };

  // Render the favorites list
  return (
    <div className="page-content">
      <h1>Favorites:</h1>
      {favorites && favorites.length > 0 ? (
        <ul>
          {favorites.map((fav) => (
            <li key={fav.RecipeID}>
              <h2>{fav.Name}</h2>
              {fav.Link.split(" | ").map((imgLink, imgIndex) => (
                <img key={imgIndex} className="small-image" src={imgLink.replace(/"/g, '')}/>
              ))}
              <p><strong>Description:</strong> {fav.Description}</p>
              <p><strong>Ingredients:</strong> {fav.Ingredients}</p>
              <p><strong>Servings:</strong> {fav.Servings}</p>
              <p><strong>Instructions:</strong> {fav.Instructions}</p>
              <p><strong>Time:</strong> {fav.TimeDescription}</p>
              <p><strong>Nutritional Content:</strong> {fav.NutritionalContent}</p>
              <p><strong>Author:</strong> {fav.AuthorUsername}</p>
              <p><strong>Date Modified:</strong> {fav.DateModified}</p>
              <p><strong>Date Published:</strong> {fav.DatePublished}</p>
              <button onClick={() => removeFavorite(fav.RecipeID)}>Remove From Favorites</button>
            </li>
          ))}
        </ul>
      ) : (
        <p>No favorites found.</p>
      )}
    </div>
  );
};

export default Favorites;
