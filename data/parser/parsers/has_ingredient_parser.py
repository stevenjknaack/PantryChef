from .. import *
from ..cache import Cache
from ..config import Config

class HasIngredientParser :
    def parse(recipes_line, cache: Cache) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for HasIngredient.csv

        Format: 
            IngredientName, Username * 5 (if possible)
        for each new user
        """
        has_ingredient_string = ''
        
        # get Username 
        username_index = Config.ORIG_RECIPES_KEYS['AuthorName']
        username = recipes_line[username_index]
        username = Util.process_string(username)
        username = Util.process_username(username)

        # check if just added, increment if it was
        if len(cache.users) == cache.num_users_with_ingredient :
            return ''
        elif len(cache.users) - 1 != cache.num_users_with_ingredient :
            raise ValueError('Problem parsing HasIngredient')
        
        cache.num_users_with_ingredient += 1

        # get 5 ingredients and add
        used_ingredients = set()
        for i in range(min(5, len(cache.ingredients))) :
            ingredient_index = (i + cache.ingredients_index) % len(cache.ingredients)
            ingredient = cache.ingredients[ingredient_index]

            if used_ingredients.__contains__(ingredient) :
             continue

            used_ingredients.add(ingredient)

            has_ingredient_string += f'{ingredient},{username}{Config.LINE_END}'

        cache.ingredients_index += 5
        cache.ingredients_index %= len(cache.ingredients)

        return has_ingredient_string[:-len(Config.LINE_END)]