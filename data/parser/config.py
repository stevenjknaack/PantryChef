from . import *

class Config:
    # test parameters 
    restrict_output = False # switch to false to parse entire files
    upper_bound_recipes = 60 # max recipeID to parse
    upper_bound_reviews = 40 # max reviewID to parse
    blunt_string = True # speeds up runtime but processes strings slightly worse
    windows = True # true if running on windows

    ### global parameters and constants

    DELIMINATOR = '|'
    LINE_END = '\r\n'

    ORIG_RECIPES_FILE_NAME = 'recipes'
    ORIG_REVIEWS_FILE_NAME = 'reviews'
    INPUTS = [ORIG_RECIPES_FILE_NAME, ORIG_REVIEWS_FILE_NAME]

    OUTPUTS = ['User', 'Recipe', 
            'Image', 'Illustrates', 
            'CallsFor', 'HasIngredient',
            'Favorites', 'Review']

    ORIG_RECIPES_KEYS = {'RecipeId': 0,'Name': 1,'AuthorId': 2,
                        'AuthorName': 3,'CookTime': 4,'PrepTime': 5,
                        'TotalTime': 6,'DatePublished': 7,'Description': 8,
                        'Images': 9,'RecipeCategory': 10,'Keywords': 11,
                        'RecipeIngredientQuantities': 12,'RecipeIngredientParts': 13,
                        'AggregatedRating': 14,'ReviewCount': 15,'Calories': 16,
                        'FatContent': 17,'SaturatedFatContent': 18,'CholesterolContent': 19,
                        'SodiumContent': 20,'CarbohydrateContent': 21,'FiberContent': 22,
                        'SugarContent': 23,'ProteinContent': 24,'RecipeServings': 25,
                        'RecipeYield': 26,'RecipeInstructions': 27}

    ORIG_REVIEWS_KEYS = {'ReviewId': 0,'RecipeId': 1,'AuthorId': 2,
                        'AuthorName': 3,'Rating': 4,'Review': 5,
                        'DateSubmitted': 6,'DateModified': 7}

    NUTRIONAL_CONTENT = ['Calories', 'FatContent', 'SaturatedFatContent', 
                        'CholesterolContent', 'SodiumContent', 'CarbohydrateContent',
                        'FiberContent', 'SugarContent', 'ProteinContent']
    
    from .parsers.user_parser import UserParser
    from .parsers.recipe_parser import RecipeParser
    from .parsers.image_parser import ImageParser
    from .parsers.illustrates_parser import IllustratesParser
    from .parsers.calls_for_parser import CallsForParser
    from .parsers.has_ingredient_parser import HasIngredientParser
    from .parsers.favorites_parser import FavoritesParser

    recipe_parse_functions = [UserParser.parse_recipe, RecipeParser.parse, 
                                    ImageParser.parse, IllustratesParser.parse, 
                                    CallsForParser.parse, HasIngredientParser.parse,
                                    FavoritesParser.parse]
    