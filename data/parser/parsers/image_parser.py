from .. import *
from ..cache import Cache
from ..config import Config
from re import sub

class ImageParser :
    def parse(recipes_line, cache: Cache) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for Images.csv
        
        Format: 
          ImageID, Link 
        for each image
        """
        # get raw images and remove any new line characters
        images_string = ''
        images_index = Config.ORIG_RECIPES_KEYS['Images']
        images = recipes_line[images_index]
        
        # process
        images = sub('\n|\r', '', images)

        if images == "character(0)":
            return ''

        if images[0] == 'c' :
            images = Util.process_split_csv_array(images)
        else :
            images = [images]

        # generate an ID and create a line for each image
        for image in images :
            if len(image) > 2000 :
                continue

            image_id = cache.num_images + 1
            cache.num_images += 1

            images_string += f'{image_id},{image}{Config.LINE_END}'

        return images_string[:-len(Config.LINE_END)]

    