import { useState, useEffect } from 'react';

export default function Home(props) {
    // const [exampleConnectionData, setExampleConnectionData] = useState(null);

    // useEffect(() => {
    //     fetch('http://localhost:8080/recipes/example', {
    //         headers: {
    //             'Access-Control-Allow-Origin': '*'
    //         },
    //         crossorigin: 'anonymous'
    //     })
    //         .then((response) => response.json())
    //         .then((data) => {
    //             console.log(data);
    //             setExampleConnectionData(data);
    //         })
    //         .catch((error) => {
    //             setExampleConnectionData(`error <${error}>`);
    //             console.log(error);
    //         });
    // }, []);

    return (
        <>
            <h1>Welcome to PantryChef!</h1>
            <p>More To Come :)</p>
        </>
    );
}
