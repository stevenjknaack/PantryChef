import { FC, useContext, useEffect, useState } from 'react';
import LoggedInUserContext from '../../../contexts/LoggedInUserContext';
import { Col, Container, Row } from 'react-bootstrap';
import { Page, Recipe, RecipeSearchResult, User } from '../../../types';
import RecipeCard from '../recipe-search/RecipeCard';

const UserRecipesPage: FC = () => {
    const { loggedInUser } = useContext(LoggedInUserContext) ?? {
        loggedInUser: null,
        setLoggedInUser: () => alert('Error with user context.')
    };
    const [userRecipes, setUserRecipes] =
        useState<Page<RecipeSearchResult> | null>(null);
    const [loading, setLoading] = useState(true);

    const refreshUserRecipes = async () => {
        setLoading(true);
        const response = await fetch(
            `http://localhost:8080/api/recipes?authorUsername=${loggedInUser?.username}`
        );

        if (!response.ok) {
            console.error(response.status);
            return;
        }

        const data: Page<RecipeSearchResult> = await response.json();
        console.log(data); // TODO remove
        setUserRecipes(data);
        setLoading(false);
    };

    useEffect(() => {
        refreshUserRecipes();
    }, []);

    return (
        <>
            <h1>{loggedInUser?.username}</h1>
            <h2>My Recipes</h2>
            <Container fluid>
                <Row>
                    {userRecipes?.content.map((recipe) => (
                        <Col key={recipe.id} md={3}>
                            <RecipeCard {...recipe}></RecipeCard>
                        </Col>
                    ))}
                </Row>
            </Container>
            <h2>My Reviews</h2>
        </>
    );
};

export default UserRecipesPage;
