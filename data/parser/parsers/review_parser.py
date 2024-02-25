from ..cache import Cache
from ..config import Config
from ..util import Util

class ReviewParser :
  def parse(reviews_line, cache: Cache) :
        """
        Given a line from reviews_original.csv
        return csv formatted string for Reviews.csv

        Format: 
            ReviewID, Review, Rating, RecipeID, 
            AuthorUsername, DateSubmitted, and DateModified 
        for each review
        """
        review_string = ''

         # Recipe ID; require the recipe to exist
        recipe_id_index = Config.ORIG_REVIEWS_KEYS['RecipeId'] 
        recipe_id_orig = reviews_line[recipe_id_index]

        if not cache.recipe_ids_check.__contains__(recipe_id_orig) and not Config.restrict_output :
            return ''
        
        recipe_id = cache.recipe_ids_check[recipe_id_orig]

        # ReviewID
        review_id_index = Config.ORIG_REVIEWS_KEYS['ReviewId']
        review_id = reviews_line[review_id_index]

        # Review
        review_index = Config.ORIG_REVIEWS_KEYS['Review']
        review = reviews_line[review_index]
        review = Util.process_string(review)

        # Rating 
        rating_index = Config.ORIG_REVIEWS_KEYS['Rating']
        rating = reviews_line[rating_index]
        rating = float(rating)
        rating = int(rating)
        rating = str(rating)

        # AuthorUsername
        author_username_index = Config.ORIG_REVIEWS_KEYS['AuthorName'] 
        author_username = reviews_line[author_username_index]
        author_username = Util.process_string(author_username)
        author_username = Util.process_username(author_username)

        # DateSubmitted
        date_submitted_index = Config.ORIG_REVIEWS_KEYS['DateSubmitted']
        date_submitted = reviews_line[date_submitted_index]

        # DateModified
        date_modified_index = Config.ORIG_REVIEWS_KEYS['DateModified']
        date_modified = reviews_line[date_modified_index]

        # create and return
        review_string += f'{review_id},{review},{rating},'
        review_string += f'{recipe_id},{author_username},'
        review_string += f'{date_submitted},{date_modified}'
                                    
        return review_string