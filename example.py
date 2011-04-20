from nltk.corpus import wordnet as wn
import nltk
from pprint import pprint
import sys
import re

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

def ex6(fname):
	fname = 'data/tty'
	lines = open(fname, 'r').readlines()
	for line in lines:
		ex5(line)

def list_of_nouns(sent):
	elist = []
	text = nltk.word_tokenize(sent)
	list = nltk.pos_tag(text)
	for t in list:
		if t[1] == 'NN':
			elist.append(t)
	return elist

def ex5(sent):
	#listofn = list_of_nouns(sent)
	text = nltk.word_tokenize(sent)
	list = nltk.pos_tag(text)
	for t in list:
		if t[1] != 'NN':
			continue
		else:
			tup = t[1]
			synsets = wn.synsets(tup, pos=wn.NOUN)
			print synsets[0]
			print synsets[1]
			sexp1 = synsets[0].common_hypernyms(synsets[1])
			print sexp1
			for s in sexp1:
				print synsets[0].path_similarity(s)
				print '----'
				print synsets[1].path_similarity(s)

			sexp = synsets[0].lowest_common_hypernyms(synsets[1])
			print sexp[0].name.split('.')[0]
			print sexp[0].shortest_path_distance(synsets[0])
			print sexp[0].shortest_path_distance(synsets[1])
			print synsets[0].root_hypernyms()
			#print synsets[0].lowest_common_hypernyms(synsets[1])
			sys.exit()

	S = wn.synset
	print S('resident.n.01').lowest_common_hypernyms(S('house_physician.n.01'))

	orca = wn.synset('orca.n.01')
	minke = wn.synset('minke_whale.n.01')
	tortoise = wn.synset('tortoise.n.01')
	novel = wn.synset('novel.n.01')
	print orca.lowest_common_hypernyms(minke)

	sys.exit()
	#text = nltk.word_tokenize("And now for something completely different")
	#sent = 'During aortic valvuloplasty a guidewire was broken, and the broken fragment remained in the left ventricle. This left ventricular foreign body was retrieved percutaneously by a snare'
	text = nltk.word_tokenize(sent)
	list = nltk.pos_tag(text)
	for tup in list:
		if tup[1] == 'NN':
			print "00 %s 00" % tup[0]
			synsets = wn.synsets(tup[0], pos = wn.NOUN)
			for syns in synsets:
				print str(syns.name)
				print str(syns.pos)
				print str(syns.lemmas)
				print str(syns.definition)
			#print [ str(syns.name) for syns in synsets ]
			#print synsets[0].hypernyms
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
	ex6('resident')
	#ex4()
	#ex3()
	#ex2()
