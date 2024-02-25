from . import *
from .config import Config
from re import sub, search

class Util :
    def process_username(raw_username) :
        """ Processes raw_username into correct username"""
        raw_username = sub('[^\S ]|"|,', '', raw_username)
        if len(raw_username) > 50 :
            raw_username = raw_username[:51]
        return raw_username
    
    def process_split_csv_array(csv_string_array, split_re = ', ') :
        """ 
        remove 
            extra newlines,
            'c(' from beginning,
            and ')' from end of string 
        then split

        given an array string , return a string array
        """
        csv_string_array = sub('\r\n', '', csv_string_array)
        csv_string_array = sub(' +', ' ', csv_string_array)
        csv_string_array = csv_string_array[2:-1]
        csv_string_array = csv_string_array.split(split_re)

        return csv_string_array
    
    def process_string(string) :
        """ 
        formats a single element string 
            a string should have opening and closing braces
            if and only if it contains matching non-beginning/ending
            double parenthesis, \n, or a comma.
        """
        string = sub('(\r\n)|[^\S\n ]', '', string)
        string = sub('\\\\', '', string)
        #string = sub('[^\S\n ]', '', string)
        string = sub('  +', ' ', string)

        if Config.blunt_string :
            if string == '' :
                return ''
            
            string = sub('"', '', string)
            
            if len(string) == 1 or (string[0] != '"' or string[len(string) - 1] != '"') :
                string = f'"{string}"'
        else :
            has_parenthesis = search('^".*"$', sub('\n', '', string))
            need_parenthesis = search('^[^"](.*".*".*)+[^"]$', string)
            need_parenthesis = need_parenthesis or search('^"(.*".*".*)+"$', string)
            need_parenthesis = need_parenthesis or search('^[^"](.*".*)+"$', string)
            need_parenthesis = need_parenthesis or search('^"(.*".*)+[^"]$', string)
            #need_parenthesis = search('(^[^"](.*".*".*)+[^"]$)|(^"(.*".*".*)+"$)|(^[^"](.*".*)+"$)|(^"(.*".*)+[^"]$)', string)
            need_parenthesis = need_parenthesis or search(',', string)
            need_parenthesis = need_parenthesis or search('\n', string)
            
            if need_parenthesis and not has_parenthesis :
                string = f'"{string}"'
            elif not need_parenthesis and has_parenthesis :
                string = string[1:-1]
        
        #print(has_parenthesis, need_parenthesis)
        return string
        
