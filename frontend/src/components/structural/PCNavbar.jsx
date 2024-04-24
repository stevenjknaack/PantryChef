import { useContext, useState } from 'react';
import { Button, Container, Nav, Navbar, Stack } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import LoggedInUserContext from '../../contexts/LoggedInUserContext';
import './structural.css';

export default function PCNavbar() {
    const [loggedInUser, setLoggedInUser] = useContext(LoggedInUserContext);
    const [expanded, setExpanded] = useState();

    const collapse = () => setExpanded(false);
    const toggleExpanded = () => setExpanded((curr) => !curr);

    const handleLogout = () => {
        setLoggedInUser(null);
    };

    return (
        <Navbar
            data-bs-theme='dark'
            fixed='top'
            expand='sm'
            expanded={expanded}
        >
            <Container fluid>
                <Navbar.Brand as={Link} to='/'>
                    <h1 className='navbar-brand-text'>PANTRYCHEF</h1>
                </Navbar.Brand>
                <Navbar.Toggle
                    aria-controls='responsive-navbar-nav'
                    onClick={toggleExpanded}
                />
                <Navbar.Collapse
                    id='responsive-navbar-nav'
                    className='justify-content-end'
                >
                    {loggedInUser ? (
                        <>
                            <Nav style={{ marginRight: '1rem' }}>
                                <Nav.Link
                                    as={Link}
                                    to='/recipes'
                                    onClick={collapse}
                                >
                                    Search Recipes
                                </Nav.Link>
                            </Nav>
                            <Button variant='danger' onClick={handleLogout}>
                                Logout
                            </Button>
                        </>
                    ) : (
                        <Nav>
                            <Nav.Link as={Link} to='/login' onClick={collapse}>
                                Login
                            </Nav.Link>
                            <Nav.Link
                                as={Link}
                                to='/register'
                                onClick={collapse}
                            >
                                Register
                            </Nav.Link>
                        </Nav>
                    )}
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}
