import React, { useState, useEffect } from 'react';

function Favorites({ user }) {
  const [favorites, setFavorites] = useState([]);

  // Function to load favorites from a server, or you might load them from local storage
  const loadFavorites = () => {
    // fetch favorites from the server
    // a static list for now
    setFavorites(["Recipe 1", "Recipe 2", "Recipe 3"]);
  };

  useEffect(() => {
    loadFavorites();
  }, [user]); // reload favorites when the user changes

  const addFavorite = (recipe) => {
    // add the new favorite to the list
    setFavorites([...favorites, recipe]);

    // send this change to the server
    // this will need to be moved the recipe side of stuff
  };

  const removeFavorite = (recipe) => {
    // remove the favorite from the list
    setFavorites(favorites.filter(favorite => favorite !== recipe));

    // send this change to the server
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
