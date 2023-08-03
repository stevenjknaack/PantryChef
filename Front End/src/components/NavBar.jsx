import React from 'react';
import { Link } from 'react-router-dom';
import { Container, Nav, Navbar, NavDropdown } from 'react-bootstrap';
import { Outlet } from 'react-router-dom';
import '../App.css'; 


function NavBar (props) {
    const handleFavoritesClick = (e) => {
        if (!props.user) {
            e.preventDefault();
            alert('Please log in or register to access Favorites');
        }
    }

    return (
        <div>
            <Navbar bg="primary" data-bs-theme="dark" fixed="top" className="navbar-custom" >
            <Navbar.Brand as={Link} to="/"> Pantry Chef </Navbar.Brand>
                <Container>
                    <Nav className="me-auto ">
                        <Nav.Link as={Link} to="/search"> Search </Nav.Link>

                        {!props.user && (
                            <>
                                <Nav.Link as={Link} to="/login"> Login </Nav.Link>
                                <Nav.Link as={Link} to="/register"> Register </Nav.Link>
                            </>
                        )}
                        {props.user && (
                            <>
                                <Nav.Link as={Link} to="/favorites"> Favorites </Nav.Link>
                                <Nav.Link as={Link} to="/add-recipe"> Add Recipe </Nav.Link>
                                <Nav.Link as={Link} to="/logout"> Logout </Nav.Link>
                                <Nav.Link as={Link} to="/recipe"></Nav.Link>
                            </>
                        )}
                    </Nav>
                </Container>
            </Navbar>
            <div>
                <Outlet />
            </div>
        </div>
    );
};

export default NavBar;