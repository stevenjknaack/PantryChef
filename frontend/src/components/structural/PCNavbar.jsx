import { useContext, useState } from 'react';
import { Container, Nav, Navbar } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import LoggedInUserContext from '../../contexts/LoggedInUserContext';

export default function PCNavbar() {
    const [loggedInUser, setLoggedInUser] = useContext(LoggedInUserContext);
    const [expanded, setExpanded] = useState();

    const collapse = () => setExpanded(false);
    const toggleExpanded = () => setExpanded(curr => !curr);

    return <Navbar bg='dark' data-bs-theme='dark' fixed='top' expand='sm' expanded={expanded}>
        <Container>
            <Navbar.Brand as={Link} to='/'>PantryChef</Navbar.Brand>
            <Navbar.Toggle aria-controls="responsive-navbar-nav" onClick={toggleExpanded}/>
            <Navbar.Collapse id="responsive-navbar-nav">
                <Nav>
                    { 
                        loggedInUser ? <></> :
                            <>
                                <Nav.Link as={Link} to='/login' onClick={collapse}>Login</Nav.Link>
                                <Nav.Link as={Link} to='/register' onClick={collapse}>Register</Nav.Link>
                            </>
                    }
                </Nav>
            </Navbar.Collapse>
        </Container>
    </Navbar>
} 