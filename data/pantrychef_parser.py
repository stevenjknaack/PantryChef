""" 
given files 
  recipes_original.csv
  reviews_original.csv 
generate files
  Recipe.csv, Image.csv, Illustrates.csv, 
  CallsFor.csv, User.csv, HasIngredient.csv,
  Favorites.csv, Review.csv
for use in application PantryChef

owners: 
  Steven Knaack (author), 
  Neha Talluri, 
  Joseph Frazier
"""

from io import open
from csv import reader 
from re import sub
from time import time

### global parameters and constants

DELIMINATOR = '|'

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

### Parser class
class Parser :
    """
    Holds input_files, readers, and writers
    and variables to keep track of parsing.
    """
    def __init__(self) :
        """ Initialize Parser instance """
        self.input_files = []
        self.readers = []
        self.writers = []

        self.recipe_ids = []
        self.recipe_ids_check = set()
        self.num_images = 0
        self.num_illustrates = 0
        self.users = set()

        self.favorites_index = 0
        self.num_users_with_favs = 0

        self.ingredients = []
        self.ingredients_index = 0
        self.num_users_with_ingredient = 0

        self.recipes_orig_keys  = None
        self.reviews_orig_keys = None

        self.recipe_parse_functions = [self._parse_user_recipe, self._parse_recipe, 
                                self._parse_image, self._parse_illustrates, 
                                self._parse_calls_for, self._parse_has_ingredient,
                                self._parse_favorites]
        
        self.time_start = 0
        self.last_time = 0
        self.lines_read = 0

    def _open_io(self) :
        """ 
        Populates self.input_files, self.readers and self.writers.
        There will be one reader/input file or writer for each
        of the INPUTS and OUTPUTS as specified above. 
        Raises IOError if any reader or writer is already open.
        """

        # check if ready to open
        if len(self.input_files) != 0 or len(self.readers) != 0 or len(self.writers) != 0 :
            self.close_io()
            raise IOError('Files already open when trying to open IO')
        
        # open input files and readers 
        for input_name in INPUTS:
            input_file = open(f'data/original_data/{input_name}.csv', 
                              'r', encoding='utf8', newline='')
            self.input_files.append(input_file) 

            reader_ = reader(input_file) 
            self.readers.append(reader_)

        # open output files (writers) 
        for output_name in OUTPUTS :
            output_file = open(f'data/{output_name}.csv', 'w', encoding='utf8', newline='')
            self.writers.append(output_file)

    def _close_io(self) :
        """
        Close all input files and output files (writers)
        in this parser. 
        """
        for input_file in self.input_files :
            input_file.close()
        
        for output_file in self.writers :
            output_file.close()
        
        self.input_files = []
        self.readers = []
        self.writers = []
    
    def parse(self) :
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
        self._open_io()
        print('Parsing started.')
        
        # parse recipes_original.csv
        recipes_orig_reader = self.readers[0]
        self.recipes_orig_keys = next(recipes_orig_reader)

        self.last_time = time()
        self.time_start = time()
        for line in recipes_orig_reader :
            for i in range(len(self.writers) - 1) :
                #if int(line[0]) < 38 or int(line[0]) > 50 : # TEST
                #    continue

                parse_f = self.recipe_parse_functions[i]
                parsed_str = parse_f(line)

                writer = self.writers[i]
                writer.write(parsed_str)

            self.lines_read += 1
            self._update_time()
                    
        
        # parse reviews_original.csv
        reviews_orig_reader = self.readers[1]
        self.reviews_orig_keys = next(reviews_orig_reader)
        for line in reviews_orig_reader :
            #if int(line[0]) < 2 or int(line[0]) > 20 : # TEST
            #    continue

            # user
            parsed_str = self._parse_user_review(line)
            writer = self.writers[0]
            writer.write(parsed_str)

            # review
            parsed_str = self._parse_review(line)
            writer = self.writers[len(self.writers) - 1]
            writer.write(parsed_str)

            self.lines_read += 1
            self._update_time()

        print(f'Parsing complete. ({round(time() - self.time_start, 3)}s) ({self.lines_read} lines)')
        self._close_io()

    def _update_time(self) : 
        time_diff = time() - self.last_time
        if time_diff >= 15 :
           self.last_time = time()
           print(f'parsing... ({round(self.last_time - self.time_start, 3)}s) ({self.lines_read} lines)')

    def _process_username(raw_username) :
        """ Processes raw_username into correct username"""
        if len(raw_username) > 50 :
            raw_username = raw_username[:51]
        return raw_username

    def _parse_user(self, username) :
        """
        Given a username
        return csv formatted string for User.csv
        
        Format: 
          Username, Password
        for each image
        """
        user_string = ''
        
        # process username
        username = Parser._process_username(username)

         # check for existence
        if self.users.__contains__(username) :
            return ''

        self.users.add(username)
        
        # create password
        user_num = len(self.users)
        no_spaces = sub('\s', '', username)
        append_str = '_564!2023'

        password = f'{user_num}{no_spaces}{append_str}'

        if len(password) > 50 :
            password = password[:51]

        user_string = f'{username},{password}\n'

        return user_string
    
    def _parse_user_recipe(self, recipes_line) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for User.csv
        
        Format: 
          Username, Password
        for each image
        """
        username_index = ORIG_RECIPES_KEYS['AuthorName']
        username = recipes_line[username_index]

        return self._parse_user(username)

    
    def _parse_user_review(self, reviews_line) :
        """
        Given a line from reveiws_original.csv
        return csv formatted string for User.csv
        
        Format: 
          Username, Password
        for each image
        """
        # get username
        username_index = ORIG_REVIEWS_KEYS['AuthorName']
        username = reviews_line[username_index]

        return self._parse_user(username)


    def _parse_recipe(self, recipes_line) :
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

        # RecipeID
        recipe_id_index = ORIG_RECIPES_KEYS['RecipeId']
        recipe_id = recipes_line[recipe_id_index]
        self.recipe_ids.append(recipe_id)
        self.recipe_ids_check.add(recipe_id)

        # Name
        name_index = ORIG_RECIPES_KEYS['Name']
        name = recipes_line[name_index]

        # Instructions
        instructions_index = ORIG_RECIPES_KEYS['RecipeInstructions']
        instructions = recipes_line[instructions_index]
        instructions = Parser._process_split_csv_array(instructions, '", ')
        
        instructions_string = ''
        for instruction in instructions :
            instructions_string += f'{instruction[1:]}{DELIMINATOR}'
        
        instructions_string = f'"{instructions_string[:-2]}"'

        # TimeDescription 
        cook_time_index = ORIG_RECIPES_KEYS['CookTime']
        cook_time = recipes_line[cook_time_index]

        if cook_time != 'NA' :
            cook_time = sub('PT', '', cook_time)

        prep_time_index = ORIG_RECIPES_KEYS['PrepTime']
        prep_time = recipes_line[prep_time_index]

        if prep_time != 'NA' :
            prep_time = sub('PT', '', prep_time)

        total_time_index = ORIG_RECIPES_KEYS['TotalTime']
        total_time = recipes_line[total_time_index]

        if total_time != 'NA' :
            total_time = sub('PT', '', total_time)

        time_description = f'Cook Time: {cook_time} {DELIMINATOR} '
        time_description += f'Prep Time: {prep_time} {DELIMINATOR} '
        time_description += f'Total Time: {total_time}'

        # Servings
        servings__index = ORIG_RECIPES_KEYS['RecipeServings']
        servings_ = recipes_line[servings__index]

        yield__index = ORIG_RECIPES_KEYS['RecipeYield']
        yield_ = recipes_line[yield__index]

        servings = f'Servings: {servings_} {DELIMINATOR} ' 
        servings += f'Yield: {yield_}'

        # NutrionalContent
        nutrional_content = ''
        for key in NUTRIONAL_CONTENT :
            index = ORIG_RECIPES_KEYS[key]
            value = recipes_line[index]

            nutrional_content += f'{key}: {value} {DELIMINATOR} '
        
        nutrional_content = nutrional_content[:-3]

        # Description
        description_index = ORIG_RECIPES_KEYS['Description']
        description = recipes_line[description_index]
        description = sub('[^\S ]+', '', description)
        description = f'"{description}"'

        # AuthorUsername
        author_username_index = ORIG_RECIPES_KEYS['AuthorName']
        author_username = recipes_line[author_username_index]
        author_username = Parser._process_username(author_username)

        # DatePublished
        date_published_index = ORIG_RECIPES_KEYS['DatePublished']
        date_published = recipes_line[date_published_index]

        # DateModified
        date_modified = 'NA'

        # format and return

        recipe_string += f'{recipe_id},{name},{instructions_string},'
        recipe_string += f'{time_description},{servings},{nutrional_content},'
        recipe_string += f'{description},{author_username},'
        recipe_string += f'{date_published},{date_modified}\n'

        return recipe_string 
    
    def _parse_image(self, recipes_line) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for Images.csv
        
        Format: 
          ImageID, Link 
        for each image
        """
        # get raw images and remove any new line characters
        images_string = ''
        images_index = ORIG_RECIPES_KEYS['Images']
        images = recipes_line[images_index]

        if images == 'character(0)' :
            return ''

        # process
        if images[0] == 'c' :
            images = Parser._process_split_csv_array(images)
        else :
            images = [images]

        # generate an ID and create a line for each image
        for image in images :
            image_id = self.num_images + 1
            self.num_images += 1

            images_string += f'{image_id},{image}\n'

        return images_string
    
    def _parse_illustrates(self, recipes_line) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for Illustrates.csv
        
        Format: 
          ImageID, RecipeID 
        for each image and recipe
        """
        illustrates_string = ''

        # get RecipeID
        recipe_id_index = ORIG_RECIPES_KEYS['RecipeId']
        recipe_id = recipes_line[recipe_id_index]
        
        # for every new image added to images, make a line
        for i in range(self.num_illustrates, self.num_images) :
            image_id = i + 1
            self.num_illustrates += 1

            illustrates_string += f'{image_id},{recipe_id}\n'
            
        return illustrates_string
    
    def _parse_calls_for(self, recipes_line) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for CallsFor.csv

        Format: 
            RecipeID, IngredientName, Quantity 
        for each ingredient and recipe
        """
        calls_for_string = ''

        # get RecipeID, ingredients and quantites 
        recipe_id_index = ORIG_RECIPES_KEYS['RecipeId']
        recipe_id = recipes_line[recipe_id_index]

        ingredients_index = ORIG_RECIPES_KEYS['RecipeIngredientParts']
        ingredients = recipes_line[ingredients_index]
        

        quantities_index = ORIG_RECIPES_KEYS['RecipeIngredientQuantities']
        quantities = recipes_line[quantities_index]

        # process array strings 
        ingredients = Parser._process_split_csv_array(ingredients)
        quantities = Parser._process_split_csv_array(quantities)

        for i in range(len(ingredients)) :
            # get values
            ingredient = ingredients[i]
            self.ingredients.append(ingredient)

            quantity = None
            if i < len(quantities) :
                quantity = quantities[i]
                if quantity == 'NA' :
                    quantity = '"NA"'
            else :
                quantity = '"NA"'

            # remove quotes
            ingredient = ingredient[1:-1]
            quantity = quantity[1:-1]

            calls_for_string += f'{recipe_id},{ingredient},{quantity}\n'

        return calls_for_string
    
    def _parse_has_ingredient(self, recipes_line) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for HasIngredient.csv

        Format: 
            IngredientName, Username * 5 (if possible)
        for each new user
        """
        has_ingredient_string = ''
        
        # get Username 
        username_index = ORIG_RECIPES_KEYS['AuthorName']
        username = recipes_line[username_index]
        username = Parser._process_username(username)

        # check if just added, increment if it was
        if len(self.users) == self.num_users_with_ingredient :
            return ''
        elif len(self.users) - 1 != self.num_users_with_ingredient :
            raise ValueError('Problem parsing HasIngredient')
        
        self.num_users_with_ingredient += 1

        # get 5 ingredients and add
        for i in range(min(5, len(self.ingredients))) :
            ingredient_index = (i + self.ingredients_index) % len(self.ingredients)
            ingredient = self.ingredients[ingredient_index]
            has_ingredient_string += f'{ingredient[1:-1]},{username}\n'

        self.ingredients_index += 5
        self.ingredients_index %= len(self.ingredients)

        return has_ingredient_string
    
    def _parse_favorites(self, recipes_line) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for Favorites.csv

        Format: 
            RecipeID, Username * 5 (if possible)
        for each new user
        """
        favorites_string = ''
        
        # get Username 
        username_index = ORIG_RECIPES_KEYS['AuthorName']
        username = recipes_line[username_index]
        username = Parser._process_username(username)

        # check if just added, increment if it was
        if len(self.users) == self.num_users_with_favs :
            return ''
        elif len(self.users) - 1 != self.num_users_with_favs :
            raise ValueError('Problem parsing Favorites')
        
        self.num_users_with_favs += 1

        # get 5 ingredients and add
        for i in range(min(5, len(self.recipe_ids))) :
            favorite_index = (i + self.favorites_index) % len(self.recipe_ids)
            favorite = self.recipe_ids[favorite_index]
            favorites_string += f'{favorite},{username}\n'

        self.favorites_index += 5
        self.favorites_index %= len(self.recipe_ids)

        return favorites_string
    
    
    def _parse_review(self, reviews_line) :
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
        recipe_id_index = ORIG_REVIEWS_KEYS['RecipeId'] 
        recipe_id = reviews_line[recipe_id_index]

        if not self.recipe_ids_check.__contains__(recipe_id) :
            return ''

        # ReviewID
        review_id_index = ORIG_REVIEWS_KEYS['ReviewId']
        review_id = reviews_line[review_id_index]

        # Review
        review_index = ORIG_REVIEWS_KEYS['Review']
        review = reviews_line[review_index]
        review = sub('\n', '', review)

        # Rating 
        rating_index = ORIG_REVIEWS_KEYS['Rating']
        rating = reviews_line[rating_index]
        rating = float(rating)
        rating = int(rating)
        rating = str(rating)

        # AuthorUsername
        author_username_index = ORIG_REVIEWS_KEYS['AuthorName'] 
        author_username = reviews_line[author_username_index]
        author_username = Parser._process_username(author_username)

        # DateSubmitted
        date_submitted_index = ORIG_REVIEWS_KEYS['DateSubmitted']
        date_submitted = reviews_line[date_submitted_index]

        # DateModified
        date_modified_index = ORIG_REVIEWS_KEYS['DateModified']
        date_modified = reviews_line[date_modified_index]

        # create and return
        review_string += f'{review_id},{review},{rating},'
        review_string += f'{recipe_id},{author_username},'
        review_string += f'{date_submitted},{date_modified}\n'
                                    
        return review_string
    
    def _process_split_csv_array(csv_string_array, split_re = ', ') :
        """ 
        remove 
            extra newlines,
            'c(' from beginning,
            and ')' from end of string 
        then split

        given an array string , return a string array
        """
        csv_string_array = sub('[^\S ]+', '', csv_string_array)
        csv_string_array = sub(' +', ' ', csv_string_array)
        csv_string_array = csv_string_array[2:-1]
        csv_string_array = csv_string_array.split(split_re)

        return csv_string_array

### other methods
def main() :
    """ 
    Main method to be called because I 
    like main methods
    """
    parser = Parser()
    parser.parse()

### method calls
main()