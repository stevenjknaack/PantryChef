import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function Search({ onSubmit }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedIngredients, setSelectedIngredients] = useState([]);
  const navigate = useNavigate();

  function handleChange(e) {
    setSearchTerm(e.target.value);
  }

  function handleRemoveIngredient(ingredient) {
    setSelectedIngredients(selectedIngredients.filter((item) => item !== ingredient));
  }

  function handleAddIngredient() {
    if (!searchTerm) {
      alert('Ingredient cannot be empty.');
      return;
    }
    setSelectedIngredients([...selectedIngredients, searchTerm]);
    setSearchTerm('');
  }

  function handleSubmit(e) {
    e.preventDefault();
    if (selectedIngredients.length === 0) {
      alert('No ingredients selected.');
      return;
    }
    // call onSubmit prop with the selected ingredients
    onSubmit(selectedIngredients);
    navigate('/recipe');
    setSelectedIngredients([]);
  }

  // for testing
  useEffect(() => {
    console.log(selectedIngredients);
  }, [selectedIngredients]);

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
