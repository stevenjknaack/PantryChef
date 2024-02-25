package pantrychef.backend.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

//TODO figure out naming conventions for paths
//TODO add comments
//TODO Figure out good return and Exception types
//TODO add bulk method for each one
//TODO Implement searching by recipeId and authorUsername
@RestController
@RequestMapping(path="/review")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping(path="/create") // Map ONLY POST Requests
    public Review addReview (@RequestBody Review review) throws Exception {
        if (review.getId() != null) {
            throw new Exception("Addition failed: Cannot set a Review's Id");
        }
        
        return reviewRepository.save(review);
    }

    @GetMapping(path="/read/{id}")
    public Review getReviewById(@PathVariable Integer id) throws Exception {
        Optional<Review> review = reviewRepository.findById(id);

        if (!review.isPresent()) {
            throw new Exception("Get Failed: Review with id(" + id + ") not found");
        }
        return review.get();
    }

    @PutMapping("/update/{id}")
    public Review updateReview(@PathVariable Integer id, @RequestBody Review update) throws Exception { 
        //TODO throw if trying to change "final" values
        if (!reviewRepository.existsById(id)) {
            throw new Exception("Update failed: Review with id (" + id + ") doesn't exist");
        }

        if (update.getId() != null && update.getId() != id) {
            throw new Exception("Update failed: Can't change a Review's id");
        } 

        update.setId(id);
        reviewRepository.save(update);

        return reviewRepository.findById(id).get();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteReview(@PathVariable Integer id) { //TODO validate existence
        Review r = reviewRepository.findById(id).get();
        reviewRepository.delete(r);
        return "Deleted";
    }

    @GetMapping(path="/read/all")
    public Iterable<Review> getAllReviews() {
        // This returns a JSON or XML with the users
        return reviewRepository.findAll();
    }
}