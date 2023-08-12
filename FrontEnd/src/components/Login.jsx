import React, {useRef, useEffect} from 'react';
import { useNavigate } from 'react-router-dom';


function Login ({ onLogin }) {

  // Initialize useRef hooks to manage and reference username and password input elements
  const username = useRef("");
  const password = useRef("");
  // useNavigate hook is used to programmatically navigate to other routes
  const navigate = useNavigate();

   // Define handleSubmit function to handle form submission
  function handleSubmit (e) {
    e.preventDefault();

    // Construct a credentials object based on the values from the input fields
    const credentials = {
      username: username.current.value,
      password: password.current.value,
    };

     // Call onLogin prop function with the credentials as an argument
    onLogin(credentials);
    // Navigate to the home page ('/') after login
    navigate('/');
  }

  return (
    <div className="page-content">
      <h1>Login</h1>
        <form onSubmit={handleSubmit}>
            <label>
                Username:
                <input id="USERNAME" type="text" ref={username} />
            </label>
            <br />
            <label>
                Password:
                <input id="PASSWORD" type={"text"} ref={password} />
            </label>
            <br />
            <button type="submit">Login</button>
        </form>
    </div>
  );
};

export default Login;

