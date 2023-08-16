import React, {useRef, useEffect} from 'react';
import { useNavigate } from 'react-router-dom';


function Login ({ onLogin }) {

  // push up main title
  const navBar = document.querySelector('.navbar-custom');
  if (navBar !== null) {
    navBar.classList.remove('centered');

    const navLinks = document.querySelector('.nav-links');
    navLinks.classList.add('hidden');
  }

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
      <form onSubmit={handleSubmit} className="page-content login-form">
        <div>
            <label className='text-entry top'>
                <p>Username</p>
                <input id="USERNAME" type="text" ref={username} placeholder='John Doe'/>
            </label>
            <br />
            <label className='text-entry'>
                <p>Password</p>
                <input id="PASSWORD" type={"text"} ref={password} placeholder='Secure Password' />
            </label>
          </div>
          <br />
          <button type="submit" className='nav-link'>Login</button>
      </form>
  );
};

export default Login;

