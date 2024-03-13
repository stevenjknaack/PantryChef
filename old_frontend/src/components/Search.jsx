import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function Search({ onSubmit }) {
  // state to hold the current search term
  const [searchTerm, setSearchTerm] = useState('');
  // state to hold a list of selected ingredients
  const [selectedIngredients, setSelectedIngredients] = useState([]);
  // hook for programmatic navigation
  const navigate = useNavigate();

  // handle change of the search input
  function handleChange(e) {
    setSearchTerm(e.target.value);
  }

  // handle removal of a selected ingredient
  function handleRemoveIngredient(ingredient) {
    setSelectedIngredients(selectedIngredients.filter((item) => item !== ingredient));
  }

  // handle adding a new ingredient to the list
  function handleAddIngredient() {
    // if input is empty, show an alert and return
    if (!searchTerm) {
      alert('Ingredient cannot be empty.');
      return;
    }
    // add current search term to the list of selected ingredients
    setSelectedIngredients([...selectedIngredients, searchTerm]);
    // reset the search term
    setSearchTerm('');
  }

  // handle form submission
  function handleSubmit(e) {
    e.preventDefault();
    if (selectedIngredients.length === 0) { // if no ingredients are selected, show an alert and return
      alert('No ingredients selected.');
      return;
    }
    // call the onSubmit prop function with the selected ingredients
    onSubmit(selectedIngredients);
    // navigate to the /recipe route
    navigate('/recipe');
    // clear the list of selected ingredients
    setSelectedIngredients([]);
  }

  // render the component
  return (
    <div className="page-content">
      <h1>Search Ingredients</h1>
      <form onSubmit={handleSubmit}>
        <input name="search" type="text" placeholder="Search recipes..." value={searchTerm} onChange={handleChange} />
        <button type="button" onClick={handleAddIngredient}>Add Ingredient</button>
        <ul>
          {selectedIngredients.map(ingredient => (
            <li key={ingredient}>
              {ingredient}
              <button
                style={{ color: 'red', marginLeft: '10px' }}
                onClick={() => handleRemoveIngredient(ingredient)}
              >
                X
              </button>
            </li>
          ))}
        </ul>
        <button type="submit">Submit</button>
      </form>
    </div>
  );
};

export default Search;
