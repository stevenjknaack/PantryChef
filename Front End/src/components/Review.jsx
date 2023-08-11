import { useState } from "react"

function Review({ RecipeID }) {
    // State to hold the reviews as a string
    const [reviews, setReviews] = useState('');
    // State to indicate whether the component is in loading mode (fetched reviews)
    const [isLoading, setLoading] = useState(false);
    // Convert the reviews string into an array by splitting at each '|'
    const reviewsArray = reviews.split('|')

    // Function to fetch and view reviews for a given recipe ID
    const seeReviews = (ID) => {

        // API call to fetch reviews
        fetch("http://localhost:8000/reviews", {
            method: "PUT",
            body: JSON.stringify({
                recipeID: ID,
            }),
        }).then(res => {
            // Check the response status and handle accordingly
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
            // If no reviews found, exit early
            if (json === false) {
                return
            }
            // Otherwise, set the reviews state and indicate loading completion
            else {
                setReviews(json.reviews);
                setLoading(true);
            }
        })
    }

    // If reviews have been loaded, display them
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
    } else { // If reviews are not loaded, display the button to fetch and view reviews
        return (
            <button onClick={() => seeReviews(RecipeID)}>See Reviews</button>
        )
    }
}

export default Review;