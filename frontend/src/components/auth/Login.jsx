import { useContext, useEffect, useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import LoggedInUserContext from '../../contexts/LoggedInUserContext';
import { useNavigate } from 'react-router-dom';

export default function Login() {
    const [loggedInUser, setLoggedInUser] = useContext(LoggedInUserContext);
    const navigate = useNavigate();

    useEffect(() => {
        if (loggedInUser) {
            return navigate('/');
        }
    }, [loggedInUser]);

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = (e) => {
        e?.preventDefault();

        if (username.trim() === '' || password.trim() === '') {
            alert('Please enter a username and a password.');
            return;
        }

        // TODO test
        setLoggedInUser({ username: username });

        // fetch('', {
        //     method: 'POST',
        //     credentials: 'include',
        //     headers: {
        //         'Content-Type': 'application/json',
        //     },
        //     body: {
        //         username: username,
        //         password: password,
        //     },
        // }).then((response) => {
        //     if (response.status === 200) {
        //         response.json().then((data) => setLoggedInUser(data));
        //     } else {
        //         alert('Problem with login');
        //     }
        // });
    };

    return (
        <Form onSubmit={handleLogin}>
            <Form.Label htmlFor='input-username'>Username</Form.Label>
            <Form.Control
                id='input-username'
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <Form.Label htmlFor='input-password'>Password</Form.Label>
            <Form.Control
                id='input-password'
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                type='password'
            />
            <br />
            <Button type='submit' onClick={handleLogin}>
                Login
            </Button>
        </Form>
    );
}
