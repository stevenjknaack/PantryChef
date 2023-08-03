import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

function Register({ onRegister }) {

  const username = useRef("");
  const password = useRef("");
  const passwordConfirm = useRef("")
  const navigate = useNavigate();

  function handleSubmit(e) {
    e.preventDefault();

    const credentials = {
      username: username.current.value,
      password: password.current.value,
      passwordConfirm: passwordConfirm.current.value,
    };
    onRegister(credentials);
    navigate('/');
  }


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
          <input id="PASSWORD" type={"text"} ref={passwordConfirm} />
        </label>
        <br />
        <button type="submit">Register</button>
      </form>
    </div>
  );
};

export default Register;
