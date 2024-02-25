from .. import *
from ..cache import Cache
from ..config import Config
from re import sub

class CallsForParser :
    def parse(recipes_line, cache: Cache) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for CallsFor.csv

        Format: 
            RecipeID, IngredientName, Quantity 
        for each ingredient and recipe
        """
        calls_for_string = ''

        # get RecipeID, ingredients and quantites 
        recipe_id_index = Config.ORIG_RECIPES_KEYS['RecipeId']
        recipe_id = recipes_line[recipe_id_index]

        ingredients_index = Config.ORIG_RECIPES_KEYS['RecipeIngredientParts']
        ingredients = recipes_line[ingredients_index]

        quantities_index = Config.ORIG_RECIPES_KEYS['RecipeIngredientQuantities']
        quantities = recipes_line[quantities_index]

        # process array strings 
        ingredients = Util.process_split_csv_array(ingredients)
        quantities = Util.process_split_csv_array(quantities)

        ingredients_seen = set()
        for i in range(len(ingredients)) :
            # get values
            ingredient = ingredients[i].lower()
            ingredient = sub('[",\\\\]', '', ingredient)
            ingredient = Util.process_string(ingredient)

            if ingredients_seen.__contains__(ingredient) :
                continue

            cache.ingredients.append(ingredient)
            ingredients_seen.add(ingredient)

            quantity = None
            if i < len(quantities) :
                quantity = quantities[i]
                if quantity == 'NA' :
                    quantity = '"NA"'
            else :
                quantity = '"NA"'

            # remove quotes
            quantity = quantity[1:-1]
            quantity = sub('[ ,\\\\]', '', quantity)
            if len(quantity) > 8:
                quantity = quantity[:8 + 1]

            calls_for_string += f'{recipe_id},{ingredient},"{quantity}"{Config.LINE_END}'

        return calls_for_string[:-len(Config.LINE_END)]