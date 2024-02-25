from .. import *
from ..cache import Cache
from ..config import Config
from re import sub

class RecipeParser :
    def parse(recipes_line, cache: Cache) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for Recipe.csv
        
        Format: 
          RecipeID, Name, Instructions,
          TimeDescription, Servings, NutrionalContent,
          Description, AuthorUsername, DatePublished,
          DateModified
        """
        recipe_string = ''

        # RecipeID (make new one)
        recipe_id_index = Config.ORIG_RECIPES_KEYS['RecipeId']
        recipe_id_orig = recipes_line[recipe_id_index]
        
        recipe_id = len(cache.recipe_ids_check) + 1
        recipes_line[recipe_id_index] = str(recipe_id)

        #cache.recipe_ids.append(recipe_id)
        cache.recipe_ids_check[recipe_id_orig] = recipe_id

        # Name
        name_index = Config.ORIG_RECIPES_KEYS['Name']
        name = recipes_line[name_index]
        name = Util.process_string(name)

        # Instructions
        instructions_index = Config.ORIG_RECIPES_KEYS['RecipeInstructions']
        instructions = recipes_line[instructions_index]
        instructions = Util.process_split_csv_array(instructions, '", ')
        
        instructions_string = ''
        for i in range(len(instructions)) :
            instruction = instructions[i]
            instruction = sub('"', '', instruction)
            instruction = sub('(^\n)|(\n$)', '', instruction)

            instructions_string += f'{i + 1}. {instruction}\n'
            if len(instructions_string) >= 11000 :
                instructions_string = instructions_string[:11000]
                break
        
        instructions_string = f'{instructions_string[:-1]}'
        instructions_string = Util.process_string(instructions_string)

        # TimeDescription 
        cook_time_index = Config.ORIG_RECIPES_KEYS['CookTime']
        cook_time = recipes_line[cook_time_index]

        if cook_time != 'NA' :
            cook_time = sub('PT', '', cook_time)

        prep_time_index = Config.ORIG_RECIPES_KEYS['PrepTime']
        prep_time = recipes_line[prep_time_index]

        if prep_time != 'NA' :
            prep_time = sub('PT', '', prep_time)

        total_time_index = Config.ORIG_RECIPES_KEYS['TotalTime']
        total_time = recipes_line[total_time_index]

        if total_time != 'NA' :
            total_time = sub('PT', '', total_time)

        time_description = f'Cook Time: {cook_time} {Config.DELIMINATOR} '
        time_description += f'Prep Time: {prep_time} {Config.DELIMINATOR} '
        time_description += f'Total Time: {total_time}'

        time_description = Util.process_string(time_description)

        # Servings
        servings__index = Config.ORIG_RECIPES_KEYS['RecipeServings']
        servings_ = recipes_line[servings__index]

        yield__index = Config.ORIG_RECIPES_KEYS['RecipeYield']
        yield_ = recipes_line[yield__index]

        servings = f'Servings: {servings_} {Config.DELIMINATOR} ' 
        servings += f'Yield: {yield_}'

        servings = Util.process_string(servings)

        # NutrionalContent
        nutrional_content = ''
        for key in Config.NUTRIONAL_CONTENT :
            index = Config.ORIG_RECIPES_KEYS[key]
            value = recipes_line[index]

            nutrional_content += f'{key}: {value} {Config.DELIMINATOR} '
        
        nutrional_content = nutrional_content[:-len(Config.DELIMINATOR) - 2]

        # Description
        description_index = Config.ORIG_RECIPES_KEYS['Description']
        description = recipes_line[description_index]
        if len(description) > 3000 :
            description = description[:3000]
        description = Util.process_string(description)

        # AuthorUsername
        author_username_index = Config.ORIG_RECIPES_KEYS['AuthorName']
        author_username = recipes_line[author_username_index]
        author_username = Util.process_string(author_username)
        author_username = Util.process_username(author_username)

        # DatePublished
        date_published_index = Config.ORIG_RECIPES_KEYS['DatePublished']
        date_published = recipes_line[date_published_index]

        # DateModified
        date_modified = 'NA'

        # format and return

        recipe_string += f'{recipe_id},{name},{instructions_string},'
        recipe_string += f'{time_description},{servings},{nutrional_content},'
        recipe_string += f'{description},{author_username},'
        recipe_string += f'{date_published},{date_modified}'

        return recipe_string 
