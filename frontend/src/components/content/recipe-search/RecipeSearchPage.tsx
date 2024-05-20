import { FC, useEffect, useState } from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import RecipeCard from './RecipeCard';
import './RecipeSearchPage.css';
import { Page, RecipeSearchResult } from '../../../types';

const RecipeSearchPage: FC = () => {
    const [pageOfRecipes, setPageOfRecipes] =
        useState<Page<RecipeSearchResult> | null>(null);
    const [loading, setLoading] = useState(true);

    const refreshRecipePage = async () => {
        setLoading(true);

        const response = await fetch('http://localhost:8080/recipes', {
            method: 'GET',
            credentials: 'include'
        });

        const data: Page<RecipeSearchResult> = await response.json();
        console.log(data);
        setPageOfRecipes(data);

        setLoading(false);
    };

    useEffect(() => {
        refreshRecipePage();
    }, []);

    return (
        <Container>
            <Row>
                {loading ? (
                    <p>Loading...</p>
                ) : (
                    pageOfRecipes?.content.map((recipe) => (
                        <Col key={recipe.id} md={3}>
                            <RecipeCard {...recipe} />
                        </Col>
                    ))
                )}
            </Row>
        </Container>
    );
};

export default RecipeSearchPage;
