import { Outlet } from 'react-router-dom';;
import useSessionStorage from '../hooks/useSessionStorage';
import LoggedInUserContext from '../contexts/LoggedInUserContext';
import { useState } from 'react';

export default function Layout(props) {
    const [loggedInUserContext, setLoggedInUserContext] = useSessionStorage("loggedInUser", null);

    return <LoggedInUserContext.Provider value={[loggedInUserContext, setLoggedInUserContext]}>
        <Outlet />
    </LoggedInUserContext.Provider>
}