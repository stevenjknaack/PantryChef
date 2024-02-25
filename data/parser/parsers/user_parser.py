from .. import *
from ..cache import Cache
from ..config import Config
from re import sub

class UserParser :
    def _parse_user(username, cache: Cache) :
        """
        Given a username
        return csv formatted string for User.csv
        
        Format: 
        Username, Password
        for each image
        """
        user_string = ''
        
        # process username
        username = Util.process_string(username)
        username = Util.process_username(username)

        # check for existence
        if cache.users.__contains__(username.lower()) :
            return ''

        cache.users.add(username.lower())
        
        # create password
        user_num = len(cache.users)
        no_spaces = sub('\s', '', username)
        append_str = '_564!2023'

        password = f'{user_num}{no_spaces}{append_str}'

        if len(password) > 50 :
            password = password[:51]

        user_string = f'{username},{password}'

        return user_string
    
    def parse_recipe(recipes_line, cache) :
        """
        Given a line from recipes_original.csv
        return csv formatted string for User.csv
        
        Format: 
          Username, Password
        for each image
        """
        username_index = Config.ORIG_RECIPES_KEYS['AuthorName']
        username = recipes_line[username_index]

        return UserParser._parse_user(username)
    
    def parse_review(reviews_line, cache: Cache) :
        """
        Given a line from reviews_original.csv
        return csv formatted string for User.csv
        
        Format: 
          Username, Password
        for each image
        """
        # get username
        username_index = Config.ORIG_REVIEWS_KEYS['AuthorName']
        username = reviews_line[username_index]

        return UserParser._parse_user(username)
