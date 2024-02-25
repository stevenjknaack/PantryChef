from .cache import Cache
from .main_parser import Parser

def main() :
    """ 
    Main method to be called because I 
    like main methods
    """
    cache = Cache()
    Parser.parse(cache)

### method calls
if __name__ == '__main__':
    main()


#print(Parser._process_string('Steven \Knaack'))