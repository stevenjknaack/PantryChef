import React, { useState, useEffect, useContext } from 'react';
import userLoggedIn from '../context/userLoggedIn';

function Favorites() {

  const [loggedIn, setLoggedIn] = useContext(userLoggedIn);
  const [favorites, setFavorites] = useState([]);

  // Function to load favorites from a server
  const loadFavorites = () => {
    // fetch favorites from the server when changed to favs
    fetch("http://localhost:8000/getfavorites", {
      method: "GET",
      body: JSON.stringify({
        username: loggedIn,
      }),

    }).then(res => {

      if (res.status === 401) {
        alert('failed to find user')
        return false;
      } else if (res.status === 404) {
        alert('no favorited recipes')
        return false;
      } else if (res.status === 200) {
        return res.json()
      } else if (res.status === 405) {
        alert('unknown error')
        return false;
      }
    }).then (json => {
      if (json == false) {
        return;
      }
      else {
        setFavorites(json);
      }

      // expected json back
        // {
        //   "recipes": [
        //     {
        //       "name": "Recipe 1",
        //       "recipeID": 1
        //       "ingredients": ["Ingredient 1", "Ingredient 2"]
        //        ...
        //     },
        //     {
        //       "name": "Recipe 2",
        //       "recipeID": 2
        //       "ingredients": ["Ingredient 3", "Ingredient 4"]
        //        ...
        //     },
        //    ...
        //   ]
        // }
    })
    
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
    fetch("http://localhost:8000/deletefavorites", {
      method: "DELETE",
      body: JSON.stringify({
        username: loggedIn,
        recipeID: "recipeID", // this right now isn't a thing since I don't have the returned json
      }),

    }).then(res => {

      if (res.status === 401) {
        alert('failed to find user')
        return false;
      } else if (res.status === 404) {
        alert('failed to remove')
        return false;
      } else if (res.status === 200) {
        alert('recipe removed!')
        return true;
      } else if (res.status === 405) {
        alert('unknown error')
        return false;
      }
    }).then (success => {
      if (success == false) {
        return;
      }
      else {
        // reload displayed favorites if successful
        loadFavorites();
      }
    })

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
