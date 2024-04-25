import { Card } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

export default function RecipeCard(props) {
    const navigate = useNavigate();
    return (
        <Card onClick={() => navigate(`./${props.id}`)}>
            <img
                src={props.mainImage.url}
                alt={props.mainImage.altText}
                height='200'
                width='200'
            />
            <h2>{props.name}</h2>
            <h3>{props.authorUsername}</h3>
            <p>{props.description}</p>
        </Card>
    );
}
