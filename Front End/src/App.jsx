import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { useState, useEffect } from 'react';
import './App.css';

// Components
import Home from './components/Home';
import Login from './components/Login';
import Register from './components/Register';
import Search from './components/Search';
import Favorites from './components/Favorites';
import NavBar from './components/NavBar';
import Logout from './components/Logout';
import Recipe from './components/Recipe';
import AddRecipe from './components/AddRecipe';

// Contexts
import userLoggedIn from './context/userLoggedIn';

const App = () => {
  
  // the array that holds the selected ingredients by the user
  const [selectedIngredients, setSelectedIngredients] = useState([]);
  //will be filled with recipes when the recipes are returned by the backend
  const [recipes, setRecipes] = useState([])

  // for setting the userName to sessionStorage
  const isLoggedIn = JSON.parse(sessionStorage.getItem("loggedIn"));
  const [loggedIn, setLoggedIn] = useState(isLoggedIn !== null ? isLoggedIn : null);


  // for setting the userName to sessionStorage
  useEffect(() => {
    sessionStorage.setItem("loggedIn", JSON.stringify(loggedIn))
  }, [loggedIn])

  const login = async (credentials) => {

    // when a user logs in, we will want to get the userID back from the API 
    // and store it in localStorage so then when we do the favorites serach, 
    // all we need is the userID to be used to search

    if (credentials.username === '' || credentials.password === '') {
      alert('Please input a valid username and password.');
      return;
    }

    // logic to backend
    
    // when it returns from backend set returned username to sessionStorage
    setLoggedIn(credentials.username)
    
  };

  const register = async (credentials) => {

    if (credentials.password === '' || credentials.username === '') {
      alert('You must provide both a username and password!')
      return
    }
    
    if (credentials.password !== credentials.passwordConfirm) {
      alert("Your passwords do not match!");
      return
    }

    // check if userName is > 50 chars
    if (credentials.username.length > 50) {
      alert ("Your username must be less than 50 characters")
      return
    }

    if (credentials.username.includes(' ') || credentials.password.includes(' ')) {
      alert ("Your username and password cannot contain whitespace")
      return
    }
    // I can add more checks as needed
    
    // logic to backend
    
    // when it returns from backend set username to sessionStorage
    setLoggedIn(credentials.username)
  };

  const logout = async () => {
    setLoggedIn(null)
  }

  function submitIngredients(ingredients) {

    // logic to get recipes from database

    // for now just set the ingredients and send to recipe.jsx
    setSelectedIngredients(ingredients);

    // will also need to then send the recipes along with ingredients to recipe.jsx
  };

  return (
    <userLoggedIn.Provider value={[loggedIn, setLoggedIn]}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<NavBar/>}>
            <Route index element={<Home />} />
            <Route path="/login" element={<Login onLogin={login} />} />
            <Route path="/register" element={<Register onRegister={register} />} />
            <Route path="/search" element={<Search onSubmit={submitIngredients} />} />
            <Route path="/favorites" element={<Favorites/>} />
            <Route path="/logout" element={<Logout onLogout={logout} />} />
            <Route path="/recipe" element={<Recipe ingredients={selectedIngredients} />} />
            <Route path="/add-recipe" element={<AddRecipe/>} />
          </Route>
        </Routes>
      </BrowserRouter>
    </userLoggedIn.Provider>
  );
};

export default App;
