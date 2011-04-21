from nltk.corpus import wordnet as wn
import nltk
from pprint import pprint
import sys
import re
import logging

LOG_FILENAME = 'index.log'
logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG)
def ex22(mynoun):
	synsets = wn.synsets(mynoun, pos=wn.NOUN)
	#print [ str(syns.name) for syns in synsets ]
	if len(synsets) < 2:
		return 'doom'
		return
	#lch = synsets[0].lowest_common_hypernyms(synsets[1])
	#print str(lch)
	ch = synsets[0].common_hypernyms(synsets[1])
	logging.debug (str(ch))
	sim = 0

	for c in ch:
		a =  synsets[0].path_similarity(c)
		b =  synsets[1].path_similarity(c)
		if sim < (a+b):
			sim = (a+b)
			maxs = c
		'''print 'scores %s || %s' % (a,b)
		continue
		if c == lch[0]:
			continue
		s = lch[0].path_similarity(c)
		if sim < s:
			sim = s
			maxs = c'''
	logging.debug( 'sim = %s and its synset = %s' % (sim, maxs.name) )
	return maxs.name.split('.')[0]


def ex2():
	synsets = wn.synsets('resident', pos=wn.NOUN)
	print [ str(syns.name) for syns in synsets ]
	if len(synsets) < 2:
		print 'doom'
		return
	lch = synsets[0].lowest_common_hypernyms(synsets[1])
	print str(lch)
	ch = synsets[0].common_hypernyms(synsets[1])
	print str(ch)
	sim = 0

	for c in ch:
		a =  synsets[0].path_similarity(c)
		b =  synsets[1].path_similarity(c)
		if sim < (a+b):
			sim = (a+b)
			maxs = c
		'''print 'scores %s || %s' % (a,b)
		continue
		if c == lch[0]:
			continue
		s = lch[0].path_similarity(c)
		if sim < s:
			sim = s
			maxs = c'''
	print 'sim = %s and its synset = %s' % (sim, maxs.name)

def ex6(fname):
	fname = 'data/tty'
	lines = open(fname, 'r').readlines()
	for line in lines:
		line = line.strip()
		logging.debug ( line )
		ex5(line)

	
def ex5(sent):
	#listofn = list_of_nouns(sent)
	text = nltk.word_tokenize(sent)
	list = nltk.pos_tag(text)
	for t in list:
		if t[1] != 'NN':
			continue
		else:
			tup = t[0]
			print "tup = "+tup		# the noun
			replace_with = ex22(tup)
			if replace_with == 'doom':
				continue
			else:
				logging.debug (sent.replace(tup , replace_with.replace('_', ' ')))
			#continue
			'''synsets = wn.synsets(tup, pos=wn.NOUN)
			if len(synsets) < 2:
				continue
			logging.debug (synsets[0])
			logging.debug (synsets[1])
			sexp1 = synsets[0].lowest_common_hypernyms(synsets[1])
			logging.debug (tup + "===" + str(sexp1))		# this is organism but has to be person
			myrepl (sent, tup, sexp1[0].name.split('.')[0])	# replace in the sentence tup by its common parent'''

			"""for s in sexp1:
				print synsets[0].path_similarity(s)
				print '----'
				print synsets[1].path_similarity(s)

			sexp = synsets[0].lowest_common_hypernyms(synsets[1])
			print sexp[0].name.split('.')[0]
			print sexp[0].shortest_path_distance(synsets[0])
			print sexp[0].shortest_path_distance(synsets[1])
			print synsets[0].root_hypernyms()"""
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

def list_of_nouns(sent):
	elist = []
	text = nltk.word_tokenize(sent)
	list = nltk.pos_tag(text)
	for t in list:
		if t[1] == 'NN':
			elist.append(t)
	return elist

def myrepl (sent, x, y):
	logging.debug (sent + "||" + x + "||"+ y.replace('_', ' '))
	#sent.replace(x, y)
	logging.debug('N: '+sent.replace(x, y.replace('_', ' ')))
	return

if __name__ == '__main__':
	ex6('junk')
	#ex4()
	#ex3()
	#ex2()
