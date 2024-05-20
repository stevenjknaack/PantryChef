import { FC, ReactNode, createContext } from 'react';
import { User } from '../types';
import useSessionStorage from '../hooks/useSessionStorage';

interface LoggedInUserContextState {
    loggedInUser: User | null;
    setLoggedInUser: (user: User | null) => void;
}

const LoggedInUserContext = createContext<LoggedInUserContextState | undefined>(
    undefined
);

export const LoggedInUserContextProvider: FC<{ children: ReactNode }> = ({
    children
}) => {
    const [loggedInUser, setLoggedInUser] = useSessionStorage(
        'loggedInUser',
        null
    );

    return (
        <LoggedInUserContext.Provider value={{ loggedInUser, setLoggedInUser }}>
            {children}
        </LoggedInUserContext.Provider>
    );
};

export default LoggedInUserContext;
