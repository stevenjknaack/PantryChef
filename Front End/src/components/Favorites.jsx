import React, { useState, useEffect, useContext } from 'react';
import userLoggedIn from '../context/userLoggedIn';

function Favorites() {

  const [loggedIn, setLoggedIn] = useContext(userLoggedIn);
  const [favorites, setFavorites] = useState([]);

  // Function to load favorites from a server
  const loadFavorites = () => {
    // fetch favorites from the server when changed to favs
    
    // a static list for now
    setFavorites(["Recipe 1", "Recipe 2", "Recipe 3"]);
  };

  useEffect(() => {
    loadFavorites();
  }, [loggedIn]); // reload favorites when the user changes


  const removeFavorite = (recipe) => {
    // remove the favorite from the list for now
    setFavorites(favorites.filter(favorite => favorite !== recipe));

    // what will happen is when the - button gets clicked
    // it will be removed from the favorites in the database (recipeID, username)
    // and site will reload the favorites when action occurs
    console.log(loggedIn)
  };

  return (
    <div className="page-content">
      <h1>Your Favorites</h1>
      <ul>
        {favorites.map((favorite, index) => (
          <li key={index}>
            {favorite} <button onClick={() => removeFavorite(favorite)}>Remove</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Favorites;
