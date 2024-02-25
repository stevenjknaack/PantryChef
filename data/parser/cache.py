from time import time

class Cache :
    def __init__(self) :
        self.input_files = []
        self.readers = []
        self.writers = []

        #self.recipe_ids = []
        self.recipe_ids_check = {}

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

        self.time_start = 0
        self.last_time = 0
        self.lines_read = 0
        
    def update_time(self) : 
        time_diff = time() - self.last_time
        if time_diff >= 5 :
            self.last_time = time()
        print(f'parsing... ({round(self.last_time - self.time_start, 3)}s) ({self.lines_read} lines)')
