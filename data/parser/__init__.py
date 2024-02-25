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

### TODO ensure no duplicates in callsfor
### TODO find max length of all fields
### TODO separete fields?
### TODO allow for parsing only certain files
### TODO implement sha2 hashing
### TODO separate into

### other methods

from .parsers.user_parser import UserParser
from .parsers.recipe_parser import RecipeParser
from .parsers.image_parser import ImageParser
from .parsers.illustrates_parser import IllustratesParser
from .parsers.calls_for_parser import CallsForParser
from .parsers.has_ingredient_parser import HasIngredientParser
from .parsers.favorites_parser import FavoritesParser
from .parsers.review_parser import ReviewParser

from .io_manager import IOManager
from .main_parser import Parser
from .util import Util
