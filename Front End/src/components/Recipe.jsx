import userLoggedIn from "../context/userLoggedIn";
import {useContext} from 'react';

// ingredients will become ingredients, recipe
function Recipe({ ingredients }) {

  const [loggedIn, setLoggedIn] = useContext(userLoggedIn);
  console.log(loggedIn)

  const addFavorite = (recipe) => {

    // what will happen is when the + button gets clicked, 
    // it will be added to the favorites in the database (recipeID, username)
    // button will also disapper on the recipe card
  };

  return (
    <div className="page-content">
      <h1>WORK IN PROGRESS</h1>
      <h3>redo this later</h3>
      <ul>
        {ingredients.map((ingredient, index) => (
          <li key={index}>{ingredient}</li>
        ))}
      </ul>
    </div>
  );
}

export default Recipe;
