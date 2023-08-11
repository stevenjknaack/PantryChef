import { useState } from "react"

function Review({ RecipeID }) {
    const [reviews, setReviews] = useState('');
    const [isLoading, setLoading] = useState(false);
    const reviewsArray = reviews.split('|')


    const seeReviews = (ID) => {
        console.log(ID)

        fetch("http://localhost:8000/reviews", {
            method: "PUT",
            body: JSON.stringify({
                recipeID: ID,
            }),
        }).then(res => {
            if (res.status === 401) {
                alert('no reviews')
                return false;
            } else if (res.status === 200) {
                return res.json();
            } else if (res.status === 500) {
                alert('unknown error')
                return false;
            }
        }).then(json => {
            if (json === false) {
                return
            }
            else {
                setReviews(json.reviews);
                setLoading(true);
            }
        })
    }

    if (isLoading) {
        return (
            <>
                <h3>Reviews:</h3>
                <ul>
                    {reviewsArray.map((review, idx) => (
                        <li key={idx}>{review}</li>
                    ))}
                </ul>
            </>
        )
    } else {
        return (
            <button onClick={() => seeReviews(RecipeID)}>See Reviews</button>
        )
    }
}

export default Review;