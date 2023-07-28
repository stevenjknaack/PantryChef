import React from 'react';

function Favorites({ user }) {
  // Retrieve user's favorite recipes here.
  // For now, let's pretend the user has favorites
  const favorites = ["Recipe 1", "Recipe 2", "Recipe 3"];

  return (
    <div className="page-content">
      <h1>Your Favorites</h1>
      <ul>
        {favorites.map((favorite, index) => (
          <li key={index}>{favorite}</li>
        ))}
      </ul>
    </div>
  );
};

export default Favorites;
