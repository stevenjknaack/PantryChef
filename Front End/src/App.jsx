import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
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

const App = () => {
  const [user, setUser] = useState(false); // true if user is logged in
  const [selectedIngredients, setSelectedIngredients] = useState([]);
 
  // for testing
  useEffect(() => {
    console.log(user);
  }, [user]);

  const login = async (credentials) => { 

    // when a user logs in, we will want to get the userID back from the API 
    // and store it in localStorage so then when we do the favorites serach, 
    // all we need is the userID to be used to search

    // logic to login user with axios, using the API endpoint for login
    // setUser with the logged in user data
    if (credentials.username === '' || credentials.password === '') {
      alert('Please input a valid username and password.');
      return;
    }
    setUser(true)

    // set to sessStorage
  };

  const register = async (credentials) => {
    // logic to register user with axios, using the API endpoint for register
    // setUser with the registered user data

    if (credentials.password === '' || credentials.username === '') {
      alert('You must provide both a username and password!')
      return
    }
    else if (credentials.password !== credentials.passwordConfirm) {
      alert("Your passwords do not match!");
      return
    }
    setUser(true)
    // set to sessStorage
  };

  const logout = async ()=> {
    // logic to database to logout
    setUser(false)
    // remove from sessStorage
  }

  function submitIngredients(ingredients) {
    setSelectedIngredients(ingredients);
  };
  
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<NavBar user={user} />}>
          <Route index element={<Home />} />
          <Route path="/login" element={<Login onLogin={login} />} />
          <Route path="/register" element={<Register onRegister={register} />} />
          <Route path="/search" element={<Search onSubmit={submitIngredients} />} />
          <Route path="/favorites" element={<Favorites user={user} />} />
          <Route path="/logout" element={<Logout onLogout={logout} />} />
          <Route path="/recipe" element={<Recipe ingredients={selectedIngredients} />} />
          <Route path="/add-recipe" element={<AddRecipe user={user} />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default App;


/*
import { Container, Row, Col } from 'react-bootstrap';

// ... Your code here ...

return (
  <BrowserRouter>
    <Container fluid>
      <Row className="justify-content-md-center">
        <Col md="auto">
          <Routes>
            <Route path="/" element={<NavBar user={user}/>}>
              <Route index element={<Home />} />
              <Route path="/login" element={<Login onLogin={login} />} />
              <Route path="/register" element={<Register onRegister={register} />} />
              <Route path="/search" element={<Search />} />
              <Route path="/favorites" element={<Favorites user={user} />} />
            </Route>
          </Routes>
        </Col>
      </Row>
    </Container>
  </BrowserRouter>
);
*/