import { Outlet } from 'react-router-dom';

import { LoggedInUserContextProvider } from '../../contexts/LoggedInUserContext';

import PCNavbar from './PCNavbar';

export default function Layout() {
    return (
        <LoggedInUserContextProvider>
            <PCNavbar />
            <Outlet />
        </LoggedInUserContextProvider>
    );
}
