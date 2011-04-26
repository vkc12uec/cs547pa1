from nltk.corpus import wordnet as wn
import nltk
from pprint import pprint
import sys 
import re
import logging

LOG_FILENAME = 'index.log'
logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG)

def ex2():
    '''right = wn.synset('resident.n.01')
    left = wn.synset('house_physician.n.01')
    print right.lowest_common_hypernyms(left)
    sys.exit()'''

    synsets = wn.synsets('risk', pos=wn.NOUN)
	print 
    print [ syns.hypernym_paths() for syns in synsets ]

if __name__ == '__main__':
	ex2()
