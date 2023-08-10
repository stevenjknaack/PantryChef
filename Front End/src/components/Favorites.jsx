import React, { useState, useEffect, useContext } from 'react';
import userLoggedIn from '../context/userLoggedIn';

function Favorites() {

  const [loggedIn, setLoggedIn] = useContext(userLoggedIn);
  const [favorites, setFavorites] = useState([]);
  const [isLoading, setIsLoading] = useState(true);  // Add this line to initialize loading state

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
        setIsLoading(false);
        setFavorites(json.recipes);
      }
    })
  };

  useEffect(() => {
    loadFavorites();
  }, [loggedIn]);

  if (isLoading) {
    return <div className="page-content">Loading Favorites Page...</div>;
  }

  const removeFavorite = (recipeID) => {
    
    console.log(recipeID)

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
        // reload displayed favorites if successful
        loadFavorites();
      }
    })
  };

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
