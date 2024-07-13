import { FC, FormEvent, useContext, useEffect, useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import LoggedInUserContext from '../../contexts/LoggedInUserContext';
import { useNavigate } from 'react-router-dom';
import { AuthResponseData } from '../../types';

const RegisterPage: FC = () => {
    const { loggedInUser, setLoggedInUser } = useContext(
        LoggedInUserContext
    ) ?? {
        loggedInUser: null,
        setLoggedInUser: () => alert('Error with user context.')
    };

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

    const handleRegister = async (e: FormEvent) => {
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

        const response = await fetch(
            'http://localhost:8080/api/auth/register',
            {
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
            }
        );

        if (!response.ok) alert('Problem with registration');

        const data: AuthResponseData = await response.json();
        setLoggedInUser(data.user);
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
};

export default RegisterPage;
