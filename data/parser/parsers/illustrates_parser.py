from ..cache import Cache
from ..config import Config
from ..util import Util

class IllustratesParser :
    def parse(recipes_line, cache: Cache) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for Illustrates.csv
        
        Format: 
            ImageID, RecipeID 
        for each image and recipe
        """
        illustrates_string = ''

        # get RecipeID
        recipe_id_index = Config.ORIG_RECIPES_KEYS['RecipeId']
        recipe_id = recipes_line[recipe_id_index]
        
        # for every new image added to images, make a line
        for i in range(cache.num_illustrates, cache.num_images) :
            image_id = i + 1
            cache.num_illustrates += 1

            illustrates_string += f'{image_id},{recipe_id}{Config.LINE_END}'
            
        return illustrates_string[:-len(Config.LINE_END)]