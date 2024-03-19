import { Outlet } from 'react-router-dom';
import useSessionStorage from '../../hooks/useSessionStorage';
import LoggedInUserContext from '../../contexts/LoggedInUserContext';
import { useState } from 'react';
import PCNavbar from './PCNavbar';

export default function Layout(props) {
    const [loggedInUser, setLoggedInUser] = useSessionStorage(
        'loggedInUser',
        null
    );

    return (
        <>
            <LoggedInUserContext.Provider
                value={[loggedInUser, setLoggedInUser]}
            >
                <PCNavbar />
                <Outlet />
            </LoggedInUserContext.Provider>
        </>
    );
}
