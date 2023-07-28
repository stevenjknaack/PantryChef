import React, {useRef, useEffect} from 'react';
import { useNavigate } from 'react-router-dom';


function Login ({ onLogin }) {

  const username = useRef("");
  const password = useRef("");
  const navigate = useNavigate();

  function handleSubmit (e) {
    e.preventDefault();

    const credentials = {
      username: username.current.value,
      password: password.current.value,
    };

    onLogin(credentials);
 
    
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

