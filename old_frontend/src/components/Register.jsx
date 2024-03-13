import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

function Register({ onRegister }) {

  // push up main title
  const navBar = document.querySelector('.navbar-custom');
  if (navBar !== null) {
    navBar.classList.remove('centered');

    const navLinks = document.querySelector('.nav-links');
    navLinks.classList.add('hidden');
  }

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
    <form onSubmit={handleSubmit} className="page-content login-form register">
      <div>
        <label className='text-entry'>
          <p>Username</p>
          <input id="USERNAME" type="text" ref={username} placeholder='John Doe' />
        </label>
        <br />
        <label className='text-entry'>
          <p>Password</p>
          <input id="PASSWORD" type={"text"} ref={password} placeholder='Powerful Password'/>
        </label>
        <br />
        <label className='text-entry'>
          <p>Confirm Password</p>
          <input id="DUP-PASSWORD" type={"text"} ref={passwordConfirm} placeholder='Powerful Password'/>
        </label>
      </div>
      <br />
      <button type="submit" className='nav-link'>Register</button>
    </form>
  );
};

export default Register;
