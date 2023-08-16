import React from 'react';
import { Link } from 'react-router-dom';
import { Container, Nav, Navbar } from 'react-bootstrap';
import { Outlet } from 'react-router-dom';
import { useContext } from 'react';
import '../App.css';
import userLoggedIn from '../context/userLoggedIn';

function NavBar() {

    // Get the current login status from context
    const [loggedIn, setLoggedIn] = useContext(userLoggedIn);

    return (
        <div>
            {/* Define a Navbar component with bootstrap */}
            <Navbar bg="primary" data-bs-theme="dark" fixed="top" className="navbar-custom" >
                {/* Brand name of the navbar which redirects to homepage when clicked */}
                <div className='main-title-container'>
                    <Navbar.Brand as={Link} to="/" className="main-title"> PANTRY CHEF </Navbar.Brand>
                </div>
                <Container>
                    {/* Check if user is not logged in */}
                    {(loggedIn === null) && (
                        <Nav className="nav-links">
                            <Nav.Link as={Link} to="/login" className='nav-link'><p>Login</p></Nav.Link>
                            <Nav.Link as={Link} to="/register" className='nav-link'><p>Register</p></Nav.Link>
                        </Nav>
                    )}

                    {/* Check if user is logged in <Nav.Link as={Link} to="/recipe"></Nav.Link>*/}
                    {(loggedIn !== null) && (
                        <Nav className="nav-links column">
                            <Nav.Link as={Link} to="/search"> Search </Nav.Link>
                            <Nav.Link as={Link} to="/favorites"> Favorites </Nav.Link>
                            <Nav.Link as={Link} to="/add-recipe"> Add Recipe </Nav.Link>
                            <Nav.Link as={Link} to="/your-recipes">Your Recipes</Nav.Link>
                            <Nav.Link as={Link} to="/logout"> Logout </Nav.Link>
                        </Nav>
                    )}
                </Container>
            </Navbar>
            <div>
                {/* Outlet to render child components/routes */}
                <Outlet />
            </div>
        </div>
    );
};

export default NavBar;