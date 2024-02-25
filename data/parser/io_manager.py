from . import *
from .cache import Cache
from io import open
from csv import reader 
from .config import Config


class IOManager :
    def open_io(cache: Cache) :
        """ 
        Populates self.input_files, self.readers and self.writers.
        There will be one reader/input file or writer for each
        of the INPUTS and OUTPUTS as specified above. 
        Raises IOError if any reader or writer is already open.
        """

        # check if ready to open
        if len(cache.input_files) != 0 or len(cache.readers) != 0 or len(cache.writers) != 0 :
            IOManager.close_io(cache)
            raise IOError('Files already open when trying to open IO')
        
        # open input files and readers 
        prefix = ''
        if Config.windows :
            prefix = './'

        for input_name in Config.INPUTS:
            input_file = open(f'{prefix}original_data/{input_name}.csv', 
                              'r', encoding='utf8', newline='')
            cache.input_files.append(input_file) 

            reader_ = reader(input_file) 
            cache.readers.append(reader_)

        # open output files (writers) 
        for output_name in Config.OUTPUTS :
            output_file = open(f'{prefix}csv_tables/{output_name}.csv', 'w', encoding='utf8', newline='')
            cache.writers.append(output_file)

    def close_io(cache: Cache) :
        """
        Close all input files and output files (writers)
        in this parser. 
        """
        for input_file in cache.input_files :
            input_file.close()
        
        for output_file in cache.writers :
            output_file.close()
        
        cache.input_files = []
        cache.readers = []
        cache.writers = []