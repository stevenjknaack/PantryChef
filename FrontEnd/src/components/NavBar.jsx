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
                    {/* Check if user is not logged in */}
                    {(loggedIn === null) && (
                        <Navbar bg="primary" data-bs-theme="dark" fixed="top" className="navbar-custom centered" >
                        {/* Define a Navbar component with bootstrap */}
                        {/* Brand name of the navbar which redirects to homepage when clicked */}
                            <div className='main-title-container'>
                                <Navbar.Brand as={Link} to="/" className="main-title"> PANTRY CHEF </Navbar.Brand>
                            </div>
                            <Container>
                                <Nav className="nav-links">
                                    <Nav.Link as={Link} to="/login" className='nav-link'><p>Login</p></Nav.Link>
                                    <Nav.Link as={Link} to="/register" className='nav-link'><p>Register</p></Nav.Link>
                                </Nav>
                            </Container>
                        </Navbar>
                    )}

                    {/* Check if user is logged in <Nav.Link as={Link} to="/recipe"></Nav.Link>*/}
                    {(loggedIn !== null) && (
                        <Navbar bg="primary" data-bs-theme="dark" fixed="top" className="navbar-custom" >
                            {/* Brand name of the navbar which redirects to homepage when clicked */}
                            <div className='main-title-container flex'>
                                <Navbar.Brand as={Link} to="/" className="main-title"> PANTRY CHEF </Navbar.Brand>
                                <button><svg width="100" height="59" viewBox="0 0 100 59" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <g id="Selection tab" filter="url(#filter0_i_6_51)">
                                            <path id="Rectangle 5" d="M0 5C0 2.23858 2.23858 0 5 0H95C97.7614 0 100 2.23858 100 5C100 7.76142 97.7614 10 95 10H5C2.23858 10 0 7.76142 0 5Z" fill="white"/>
                                            <path id="Rectangle 6" d="M0 29C0 26.2386 2.23858 24 5 24H95C97.7614 24 100 26.2386 100 29C100 31.7614 97.7614 34 95 34H5C2.23858 34 0 31.7614 0 29Z" fill="white"/>
                                            <path id="Rectangle 7" d="M0 54C0 51.2386 2.23858 49 5 49H95C97.7614 49 100 51.2386 100 54C100 56.7614 97.7614 59 95 59H5C2.23858 59 0 56.7614 0 54Z" fill="white"/>
                                        </g>
                                        <defs>
                                            <filter id="filter0_i_6_51" x="0" y="0" width="100" height="63" filterUnits="userSpaceOnUse" color-interpolation-filters="sRGB">
                                                <feFlood flood-opacity="0" result="BackgroundImageFix"/>
                                                <feBlend mode="normal" in="SourceGraphic" in2="BackgroundImageFix" result="shape"/>
                                                <feColorMatrix in="SourceAlpha" type="matrix" values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0" result="hardAlpha"/>
                                                <feOffset dy="4"/>
                                                <feGaussianBlur stdDeviation="2"/>
                                                <feComposite in2="hardAlpha" operator="arithmetic" k2="-1" k3="1"/>
                                                <feColorMatrix type="matrix" values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.25 0"/>
                                                <feBlend mode="normal" in2="shape" result="effect1_innerShadow_6_51"/>
                                            </filter>
                                        </defs>
                                </svg></button>
                            </div>
                            <Container>
                                <Nav className="nav-links column">
                                    <Nav.Link as={Link} to="/search" className='nav-link'><p> Search</p></Nav.Link>
                                    <Nav.Link as={Link} to="/favorites" className='nav-link'><p>Favorites</p></Nav.Link>
                                    <Nav.Link as={Link} to="/add-recipe" className='nav-link'><p>Add Recipe</p></Nav.Link>
                                    <Nav.Link as={Link} to="/your-recipes" className='nav-link'><p>Your Recipes</p></Nav.Link>
                                    <Nav.Link as={Link} to="/logout" className='nav-link'><p>Logout</p></Nav.Link>
                                </Nav>
                            </Container>
                        </Navbar>
                    )}
                
            <div>
                {/* Outlet to render child components/routes */}
                <Outlet />
            </div>
        </div>
    );
};

export default NavBar;