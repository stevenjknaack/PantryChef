import { useEffect, useState } from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import RecipeCard from './RecipeCard';
import './RecipeSearchPage.css';

export default function RecipeSearchPage(props) {
    const [recipePage, setRecipePage] = useState({});
    const [loading, setLoading] = useState(true);

    const refreshRecipePage = () => {
        fetch('http://localhost:8080/recipes', {
            method: 'GET',
            credentials: 'include'
        })
            .then((response) => response.json())
            .then((data) => {
                console.log(data);
                setRecipePage(data);
            });
    };

    useEffect(refreshRecipePage, []);
    useEffect(() => {
        if (recipePage.content) setLoading(false);
    }, [recipePage]);

    return (
        <Container>
            <Row>
                {loading ? (
                    <p>Loading...</p>
                ) : (
                    recipePage.content.map((recipe) => (
                        <Col key={recipe.id} md={3}>
                            <RecipeCard {...recipe} />
                        </Col>
                    ))
                )}
            </Row>
        </Container>
    );
}
