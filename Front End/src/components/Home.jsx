// Import necessary React library
import React from 'react';

// Define the Home component
function Home () {
  return (
    <div className="page-content">
      <h1>Welcome to Pantry Chef!</h1>
      
      {/* Introductory content about Pantry Chef */}
      <p>
        Pantry Chef is your ultimate kitchen companion. We aim to help individuals discover 
        recipes and explore your pantry inventory
        tailored to what they have on hand. With Pantry Chef, say goodbye to the 
        age-old question, "What should I cook today?".
      </p>

      {/* Additional details or features of Pantry Chef */}
      <p>

        Simply let us suggest dishes based on the ingredients you already have, favorite recipes for the future, or dare to contribute
        your own recipe.
        Our mission is to make cooking hassle-free and enjoyable for everyone.
      </p>

    </div>
  );
};

// Export the Home component for use in other parts of the application
export default Home;
