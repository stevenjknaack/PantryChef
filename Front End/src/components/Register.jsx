import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

function Register({ onRegister }) {

  // useRef hooks to capture form input values
  const username = useRef("");
  const password = useRef("");
  const passwordConfirm = useRef("")
  // Hook to navigate to different routes in the app
  const navigate = useNavigate();

  // Handle form submission
  function handleSubmit(e) {
    e.preventDefault();

    // Gather credentials from the form fields
    const credentials = {
      username: username.current.value,
      password: password.current.value,
      passwordConfirm: passwordConfirm.current.value,
    };
    onRegister(credentials); // Callback function to handle registration logic
    navigate('/');  // Redirect user to the home ('/') page
  }

  // Render the registration form
  return (
    <div className="page-content">
      <h1>Register</h1>
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
        <label>
          Confirm Password:
          <input id="DUP-PASSWORD" type={"text"} ref={passwordConfirm} />
        </label>
        <br />
        <button type="submit">Register</button>
      </form>
    </div>
  );
};

export default Register;
