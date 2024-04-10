import { useContext, useEffect, useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import LoggedInUserContext from '../../contexts/LoggedInUserContext';
import { useNavigate } from 'react-router-dom';

export default function Register() {
    const [loggedInUser, setLoggedInUser] = useContext(LoggedInUserContext);
    const navigate = useNavigate();

    useEffect(() => {
        if (loggedInUser) {
            return navigate('/');
        }
    }, [loggedInUser]);

    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const handleRegister = (e) => {
        e?.preventDefault();

        if (
            email.trim() === '' ||
            username.trim() === '' ||
            password.trim() === ''
        ) {
            alert('Please enter an email, username, and password.');
            return;
        }

        if (password !== confirmPassword) {
            alert('Password and confirmed password do not match!');
            return;
        }

        fetch('http://localhost:8080/auth/register', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                username: username,
                password: password
            })
        }).then((response) => {
            if (response.status === 200) {
                response.json().then((data) => setLoggedInUser(data.user));
                alert(data.user.username + ' signed in');
            } else {
                alert('Problem with registration');
            }
        });
    };

    return (
        <Form onSubmit={handleRegister}>
            <Form.Label htmlFor='input-email'>Email</Form.Label>
            <Form.Control
                id='input-email'
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
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
            <Form.Label htmlFor='input-confirm-password'>
                Confirm Password
            </Form.Label>
            <Form.Control
                id='input-confirm-password'
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                type='password'
            />
            <br />
            <Button type='submit' onClick={handleRegister}>
                Register
            </Button>
        </Form>
    );
}
