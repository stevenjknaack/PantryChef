import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

// Define the Logout component which accepts a prop `onLogout`
function Logout({ onLogout }) {
    // useNavigate hook is used to programmatically navigate to other routes
    const navigate = useNavigate();

    // Use the useEffect hook to execute side effects
    useEffect(() => {
        // Call the onLogout prop function to handle the logout logic
        onLogout();
        
        // Navigate to the home page ('/') after logout
        navigate('/');
    }, 
    // Dependencies array for useEffect.
    // Only re-run the effect if onLogout or navigate functions change (which they shouldn't)
    [onLogout, navigate]);

    // This component doesn't render anything to the DOM
    return null;
}

// Export the Logout component for use in other parts of the application
export default Logout;
