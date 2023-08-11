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
                <Navbar.Brand as={Link} to="/"> Pantry Chef </Navbar.Brand>
                <Container>
                    <Nav className="me-auto ">

                        {/* Check if user is not logged in */}
                        {(loggedIn === null) && (
                            <>
                                <Nav.Link as={Link} to="/login"> Login </Nav.Link>
                                <Nav.Link as={Link} to="/register"> Register </Nav.Link>
                            </>
                        )}

                        {/* Check if user is logged in */}
                        {(loggedIn !== null) && (
                            <>
                                <Nav.Link as={Link} to="/search"> Search </Nav.Link>
                                <Nav.Link as={Link} to="/favorites"> Favorites </Nav.Link>
                                <Nav.Link as={Link} to="/add-recipe"> Add Recipe </Nav.Link>
                                <Nav.Link as={Link} to="/your-recipes">Your Recipes</Nav.Link>
                                <Nav.Link as={Link} to="/logout"> Logout </Nav.Link>
                                <Nav.Link as={Link} to="/recipe"></Nav.Link>

                            </>
                        )}
                    </Nav>
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