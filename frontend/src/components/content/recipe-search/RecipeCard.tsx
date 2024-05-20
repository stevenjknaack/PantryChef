import { FC } from 'react';
import { Card } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { RecipeSearchResult } from '../../../types';

const RecipeCard: FC<RecipeSearchResult> = ({
    id,
    name,
    description,
    authorUsername,
    mainImage,
    aggregateRating
}) => {
    const navigate = useNavigate();
    return (
        <Card onClick={() => navigate(`./${id}`)}>
            <img
                src={mainImage.url}
                alt={mainImage.altText}
                height='200'
                width='200'
            />
            <h2>{name}</h2>
            <h3>{authorUsername}</h3>
            <p>{description}</p>
        </Card>
    );
};

export default RecipeCard;
