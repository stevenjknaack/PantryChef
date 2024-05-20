import {
    ChangeEvent,
    FC,
    FormEvent,
    useContext,
    useEffect,
    useState
} from 'react';
import { Button, Form } from 'react-bootstrap';
import LoggedInUserContext from '../../contexts/LoggedInUserContext';
import { useNavigate } from 'react-router-dom';
import { AuthResponseData } from '../../types';

const LoginPage: FC = () => {
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

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async (e: FormEvent) => {
        e?.preventDefault();

        if (username.trim() === '' || password.trim() === '') {
            alert('Please enter a username and a password.');
            return;
        }

        const response = await fetch('http://localhost:8080/auth/login', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        if (!response.ok) alert('Problem with login');

        const data: AuthResponseData = await response.json();
        setLoggedInUser(data.user);
    };

    return (
        <Form
            onSubmit={(e) => {
                handleLogin(e);
            }}
        >
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
};

export default LoginPage;
