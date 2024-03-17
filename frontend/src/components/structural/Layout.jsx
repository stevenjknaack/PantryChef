import { Outlet } from 'react-router-dom';;
import useSessionStorage from '../../hooks/useSessionStorage';
import LoggedInUserContext from '../../contexts/LoggedInUserContext';
import { useState } from 'react';
import PCNavbar from './PCNavbar';

export default function Layout(props) {
    const [loggedInUserContext, setLoggedInUserContext] = useSessionStorage("loggedInUser", null);

    return <>
        <PCNavbar />
        <LoggedInUserContext.Provider value={[loggedInUserContext, setLoggedInUserContext]}>
            <Outlet />
        </LoggedInUserContext.Provider>
    </>
}