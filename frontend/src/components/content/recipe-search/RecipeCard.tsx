import { FC } from 'react';
import { Card } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { RecipeSearchResult } from '../../../types';
import './RecipeSearchPage.css';

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
        <Card onClick={() => navigate(`./${id}`)} className='recipe-card'>
            <div className='img-box'>
                <img src={mainImage.url} alt={`An image of ${name}`} />
            </div>
            <h2>{name}</h2>
            <h3>{authorUsername}</h3>
            <p>{description}</p>
        </Card>
    );
};

export default RecipeCard;
