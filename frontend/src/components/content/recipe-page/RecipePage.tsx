import { FC, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Carousel } from 'react-bootstrap';
import './RecipePage.css';
import { Recipe, RecipeImage } from '../../../types';

const RecipePage: FC = () => {
    const { recipeId } = useParams(); // TODO figure out typing
    const [recipe, setRecipe] = useState<Recipe>(getExampleRecipe(recipeId)); // TODO change default

    const refreshRecipe = async (recipeId: number) => {
        console.log(`ayy ${recipeId}`); // TODO remove
        const response = await fetch(
            `http://localhost:8080/recipes/${recipeId}`
        );

        if (!response.ok) {
            console.error(response.status);
            return;
        }

        const data: Recipe = await response.json();
        setRecipe(data);
    };

    // useEffect(() => {
    //     refreshRecipe(recipeId);
    // }, []);

    const buildCarousel = (images: Array<RecipeImage>) => {
        const carouselItems = [];
        for (let i = 0; i < images.length; i++) {
            const image = images[i];
            carouselItems.push(
                <Carousel.Item key={image.id}>
                    <img
                        src={image.url}
                        alt={image.altText}
                        className='carousel-img'
                    />
                </Carousel.Item>
            );
        }
        return carouselItems;
    };

    return (
        <div className='recipe-page'>
            <h1 className='margin-bt-none'>{recipe.name}</h1>

            <div className='recipe-subheader-info'>
                <p>
                    <span className='rating'>4.5</span> stars
                </p>
                <p className='info-separator'>|</p>
                <p>
                    by <span className='bold'>{recipe.author.username}</span>
                </p>
                <p className='info-separator'>|</p>
                <p className='light'>Updated on May 3, 2023</p>
            </div>

            <Carousel>{buildCarousel(recipe.images)}</Carousel>

            <div className='margin-bt-md'>
                <h2>Description</h2>
                <p>{recipe.description}</p>
            </div>

            <div className='margin-bt-md'>
                <h2>Ingredients</h2>
                <ul>
                    {recipe.ingredients.map((ing) => (
                        <li key={ing.id} className='ingredient'>
                            {ing.quantity} {ing.name}
                        </li>
                    ))}
                </ul>
            </div>

            <div className='margin-bt-md'>
                <h2>Instructions</h2>
                <ol>
                    {recipe.instructions
                        .sort((a, b) => a.stepNumber - b.stepNumber)
                        .map((inst) => (
                            <li key={inst.id}>{inst.text}</li>
                        ))}
                </ol>
            </div>
        </div>
    );
};

function getExampleRecipe(recipeId) {
    return {
        id: recipeId,
        name: 'Awesome Egg Rolls',
        description:
            "This shrimp egg roll recipe takes some time to make, but it's definitely worth the effort. Serve with hot mustard.",
        category: 'chinese',
        nutritionalContent: {
            calories: 31267,
            fat: '110g',
            saturatedFat: '10g',
            cholesterol: '48mg',
            sodium: '1050mg',
            carbohydrate: '57g',
            fiber: '7g',
            sugar: '3g',
            protein: '17g'
        },
        portionFacts: {
            servings: '10',
            yield: '20 egg rolls'
        },
        timeFacts: {
            prepTime: '35 mins',
            cookTime: '15 mins',
            totalTime: '1 hr'
        },
        author: {
            username: 'ROUVER'
        },
        images: [
            {
                id: 1,
                url: 'https://www.allrecipes.com/thmb/pNDz7aGrP9OipVSc52-Oq8xNcjY=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/1385110-1b4a60c950ad49af8360ade4b87ed44d.jpg',
                altText: 'an awesome egg roll'
            },
            {
                id: 2,
                url: 'https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fimages.media-allrecipes.com%2Fuserphotos%2F8733299.jpg&q=60&c=sc&poi=auto&orient=true&h=512',
                altText: 'an awesome egg roll'
            },
            {
                id: 3,
                url: 'https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fimages.media-allrecipes.com%2Fuserphotos%2F8680144.jpg&q=60&c=sc&poi=auto&orient=true&h=512',
                altText: 'an awesome egg roll'
            },
            {
                id: 4,
                url: 'https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fimages.media-allrecipes.com%2Fuserphotos%2F7411308.jpg&q=60&c=sc&poi=auto&orient=true&h=512',
                altText: 'an awesome egg roll'
            }
        ],
        ingredients: [
            {
                id: 1,
                name: 'vegetable oil',
                quantity: '1 tsp'
            },
            {
                id: 2,
                name: 'beaten large egg',
                quantity: '1'
            },
            {
                id: 3,
                name: 'shredded cabbage',
                quantity: '6 cups'
            },
            {
                id: 4,
                name: 'fresh bean sprouts',
                quantity: '1/2 cup'
            },
            {
                id: 5,
                name: 'shredded carrot',
                quantity: '1'
            },
            {
                id: 6,
                name: 'diced celery stalk',
                quantity: '1'
            },
            {
                id: 7,
                name: 'chopped onion (Optional)',
                quantity: '2 tablespoons'
            }
        ],
        instructions: [
            {
                id: 5,
                stepNumber: 5,
                text: 'Working in batches, carefully fry egg rolls in the preheated fryer until golden brown, 2 to 4 minutes. Drain on paper towels. '
            },
            {
                id: 1,
                stepNumber: 1,
                text: 'Heat 1 teaspoon oil in a nonstick skillet over medium heat. Add egg and cook, without stirring, until the bottom has set. Flip and cook until set on the other side, 1 to 2 minutes. Transfer to a cutting board and finely chop. '
            },
            {
                id: 2,
                stepNumber: 2,
                text: 'Mix cabbage, bean sprouts, carrot, celery, and onion together in a large bowl. Stir in shrimp, soy sauce, garlic powder, and pepper. Stir in chopped egg, then sprinkle 1 tablespoon cornstarch over top. Mix to combine and let sit for 10 minutes. '
            },
            {
                id: 3,
                stepNumber: 3,
                text: 'Heat 3 to 4 inches vegetable oil in a deep fryer to 350 degrees F (175 degrees C). Mix water and remaining 1 tablespoon cornstarch together in a small bowl. '
            },
            {
                id: 4,
                stepNumber: 4,
                text: 'Place an egg roll wrapper onto a work surface with one corner pointing away from you like a diamond. Spoon 2 to 3 tablespoons shrimp mixture into the center. Moisten the top and side corners with the wet cornstarch mixture. Fold the dry bottom corner up and over the filling, making a tight tube. Fold the side corners in, pressing to stick against the folded roll, then roll toward the top corner to complete. Repeat to make remaining egg rolls. '
            }
        ]
    };
}

export default RecipePage;
