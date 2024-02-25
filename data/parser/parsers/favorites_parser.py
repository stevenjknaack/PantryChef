from .. import *
from ..cache import Cache
from ..config import Config

class FavoritesParser :
  def parse(recipes_line, cache: Cache) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for Favorites.csv

        Format: 
            RecipeID, Username * 5 (if possible)
        for each new user
        """
        favorites_string = ''
        
        # get Username 
        username_index = Config.ORIG_RECIPES_KEYS['AuthorName']
        username = recipes_line[username_index]
        username = Util.process_string(username)
        username = Util.process_username(username)

        # check if just added, increment if it was
        if len(cache.users) == cache.num_users_with_favs :
            return ''
        elif len(cache.users) - 1 != cache.num_users_with_favs :
            raise ValueError('Problem parsing Favorites')
        
        cache.num_users_with_favs += 1

        # get 5 ingredients and add
        used_recipeids = set()
        for i in range(min(5, len(cache.recipe_ids_check))) :
            favorite_index = (i + cache.favorites_index) % len(cache.recipe_ids_check)
            favorite = favorite_index + 1

            if used_recipeids.__contains__(favorite) :
                continue

            used_recipeids.add(favorite)
            favorites_string += f'{favorite},{username}{Config.LINE_END}'

        cache.favorites_index += 5
        cache.favorites_index %= len(cache.recipe_ids_check)

        return favorites_string[:-len(Config.LINE_END)]