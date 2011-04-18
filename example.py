from nltk.corpus import wordnet as wn
import nltk
from pprint import pprint

def ex2():
	import sys
	right = wn.synset('resident.n.01')
	left = wn.synset('house_physician.n.01')
	print right.lowest_common_hypernyms(left)
	sys.exit()

	synsets = wn.synsets('resident', pos=wn.NOUN)
	#synsets = wn.synsets('resident')
	print [ str(syns.definition) for syns in synsets ]
	#print [ str(syns.hypernyms) for syns in synsets ]

def ex5():
	#text = nltk.word_tokenize("And now for something completely different")
	sent = 'During aortic valvuloplasty a guidewire was broken, and the broken fragment remained in the left ventricle. This left ventricular foreign body was retrieved percutaneously by a snare'
	text = nltk.word_tokenize(sent)
	list = nltk.pos_tag(text)
	for tup in list:
		if tup[1] == 'NN':
			print tup[0]
			synsets = wn.synsets(tup[0], pos = wn.NOUN)
			print [ str(syns.name) for syns in synsets ]
			#print synsets
			""" fragment
			['fragment.n.01', 'shard.n.01', 'fragment.n.03']
			"""

def ex4():
	right = wn.synset('inhabitant.n.01')
	left = wn.synset('house_physician.n.01')
	print right.common_hypernyms(left)

def ex3():
	from nltk.chat import eliza
	eliza.eliza_chat()

def ex1():
	hyp = lambda s:s.hypernyms() # useful to print hypernym tree
	line = wn.synset('line.n.3')
	print line.name + ':', line.definition
	pprint(line.tree(hyp))

	for pentagon_sense in wn.synsets('pentagon', 'n'):
		print pentagon_sense.name + ':', pentagon_sense.definition
		pprint(pentagon_sense.tree(hyp))

if __name__ == '__main__':
	ex5()
	#ex4()
	#ex3()
	#ex2()
