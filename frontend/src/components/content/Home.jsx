import { useState, useEffect } from 'react';
import { Button } from 'react-bootstrap';
//
export default function Home(props) {
    const alertIngredient = () => {
        fetch('http://localhost:8080/recipes', {
            credentials: 'include',
            method: 'GET'
        })
            .then((response) => response.json())
            .then((data) => console.log(data));
    };

    return (
        <>
            <h1>Welcome to PantryChef!</h1>
            <p>More To Come :)</p>
            <Button onClick={alertIngredient}>Press</Button>
        </>
    );
}
