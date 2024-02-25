from .cache import Cache
from time import time
from .config import Config
from .io_manager import IOManager
from .parsers.user_parser import UserParser
from .parsers.recipe_parser import RecipeParser
from .parsers.image_parser import ImageParser
from .parsers.illustrates_parser import IllustratesParser
from .parsers.calls_for_parser import CallsForParser
from .parsers.has_ingredient_parser import HasIngredientParser
from .parsers.favorites_parser import FavoritesParser
from .parsers.review_parser import ReviewParser

class Parser :
    """
    Holds input_files, readers, and writers
    and variables to keep track of parsing.
    """
    def parse(cache: Cache) :
        """ 
        given files 
          recipes_original.csv
          reviews_original.csv 
        generate files
          Recipe.csv, Image.csv, Illustrates.csv, 
          CallsFor.csv, User.csv, HasIngredient.csv,
          Favorites.csv, Review.csv
        according to the schemas
        """
        IOManager.open_io(cache)
        print('Parsing started.')
        
        # parse recipes_original.csv
        recipes_orig_reader = cache.readers[0]
        cache.recipes_orig_keys = next(recipes_orig_reader)

        cache.last_time = time()
        cache.time_start = time()
        for line in recipes_orig_reader :
            cache.update_time()

            if Config.restrict_output and (int(line[0]) < 38 or int(line[0]) > Config.upper_bound_recipes) : # TEST
                    continue
            
            #
            #if line[ORIG_RECIPES_KEYS['AuthorName']] == 'Sarah' :
            #    continue
            #
            recipe_parse_functions = [UserParser.parse_recipe, RecipeParser.parse, 
                                    ImageParser.parse, IllustratesParser.parse, 
                                    CallsForParser.parse, HasIngredientParser.parse,
                                    FavoritesParser.parse]
    
            for i in range(len(cache.writers) - 1) :
                parse_f = recipe_parse_functions[i]
                parsed_str = parse_f(line, cache)

                if (parsed_str == '') : 
                    continue

                writer = cache.writers[i]
                writer.write(parsed_str + Config.LINE_END)

            cache.lines_read += 1

        print('(recipes.csv done)')                    
        
        # parse reviews_original.csv
        reviews_orig_reader = cache.readers[1]
        cache.reviews_orig_keys = next(reviews_orig_reader)
        for line in reviews_orig_reader :
            cache.update_time()
            if Config.restrict_output and (int(line[0]) < 2 or int(line[0]) > Config.upper_bound_reviews) : # TEST
                continue

            #
            #if line[ORIG_REVIEWS_KEYS['AuthorName']] == 'Sarah' :
            #    continue
            #

            # user
            parsed_str = ReviewParser.parse(line, cache)

            if (parsed_str != '') : 
                writer = cache.writers[0]
                writer.write(parsed_str + Config.LINE_END)

            # review
            parsed_str = ReviewParser.parse(line, cache)

            if (parsed_str != '') : 
                writer = cache.writers[len(cache.writers) - 1]
                writer.write(parsed_str + Config.LINE_END)

            cache.lines_read += 1

        print('(reviews.csv done)')

        print(f'Parsing complete. ({round(time() - cache.time_start, 3)}s) ({cache.lines_read} lines)')
        IOManager.close_io(cache)




