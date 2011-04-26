from nltk.corpus import wordnet as wn
import nltk
from pprint import pprint
import sys
import re
import logging

LOG_FILENAME = 'ex.log'
logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG)

def ex22(mynoun):
	synsets = wn.synsets(mynoun, pos=wn.NOUN)
	#print [ str(syns.name) for syns in synsets ]
	if len(synsets) < 2:
		return 'doom'
	#lch = synsets[0].lowest_common_hypernyms(synsets[1])
	#print str(lch)
	ch = synsets[0].common_hypernyms(synsets[1])
	#logging.debug (str(ch))
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
	#logging.debug( 'sim = %s and its synset = %s' % (sim, maxs.name) )
	return maxs.name.split('.')[0]


def ex6(fname):
	#fname = 'data/tty1'
	fname = 'data/tty'
	lines = open(fname, 'r').readlines()
	for line in lines:
		line = line.strip()
		logging.debug ( str("O:")+str(line) )
		ex5(line)

	
def ex5(sent):
	#listofn = list_of_nouns(sent)
	text = nltk.word_tokenize(sent)
	list = nltk.pos_tag(text)
	new_sentence = []
	for t in list:
		if t[1] != 'NN':
			new_sentence.append(t[0])
			continue
		else:
			tup = t[0]
			print "tup before calling ex7= "+tup		# the noun
			replace_with = ex7(tup)	# returns synset.name
			nuWord = replace_with.split('.')[0].replace('_', ' ')
			new_sentence.append (nuWord)
			#logging.debug ('noun = %s parent = %s' % (tup, nuWord))	#str(replace_with.split('.')[0].replace('_', ' '))))
			#new_sentence.append(replace_with.name.replace('_', ' '))
	logging.debug ('')
	logging.debug ('N:' +  ' '.join(new_sentence))
	logging.debug ('')
	return

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

def ex2():
	synsets = wn.synsets('resident', pos=wn.NOUN)
	#print [ str(syns.name) for syns in synsets ]
	print synsets
	print "\n"
	print "1 ="+str(synsets[0].hypernym_paths())
	print "\n"
	print "2 ="+str(synsets[1].hypernym_paths())
	print "\n"
	sys.exit()

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

def ex8():
	elist = [1, 2, 3, 4, 5, 6]
	for i in range(0, len(elist), 2):
		print "i = " +str(i)
		if (i+2) > len(elist):
			break;
		print "=="+str(elist[i]) + str(elist[i+1])

def ex9(mynoun):
	mynoun = 'resident'
	synsets = wn.synsets(mynoun, pos=wn.NOUN)
	for syns in synsets:
		print 'for syn = %s' % syns
		print 'paths = '
		for e in syns.hypernym_paths():
			print e
			print '\n'


def get_index_longest(paths):
	ind = 0
	if len(paths) == 1:
		return ind
		
	max_len = 0
	for i in range(0, len(paths)):
		if len(paths[i]) > max_len:
			max_len = len(paths[i])
			ind = i
	return ind
			
def common_parent (syn1 , syn2):
	print 'me = common_parent'
	paths1 = syn1.hypernym_paths()
	paths2 = syn2.hypernym_paths()

	#print 'paths1 and paths2'
	#print str(paths1)
	#print str(paths2)

	i1 = get_index_longest(paths1)
	i2 = get_index_longest(paths2)
	#print 'i1 i2 || %s %s' % (i1,i2)
	#print 'paths(i1) || paths(i2)\n'
	#print 'doom'

	l1 = list (paths1[i1])
	l2 = list (paths2[i2])

	com_p = findCommonofList(l1, l2)
	#paths1[i1][::-1], paths2[i2][::-1])
	return com_p

def findCommonofList(l1 , l2):
	# the lists need not start with same names
	print 'me = findCommonofList'
	print str(l1)
	print str(l2)
	p1 = 0
	p2 = 0

	'''if len(l1) == 1 or len(l2) == 1:
		return l1[p1]					# a list just contains 1 element'''
	while (1):
		s1 = l1[p1].name
		s2 = l2[p2].name
		#print 's1 %s || s2 %s' % (s1,s2)
		if s1 == s2:
			p1 += 1
			p2 += 1
			if p1 == len(l1) or p2 == len(l2):
				break
		else:
			break
	if (p1 == 0):
		return l1[p1]
	else:
		return l1[p1-1]	# common synset


def all_same(items):
    return all(x == items[0] for x in items)
	
def ex77(mynoun):				# return the common parent synset
	temp = ex7(mynoun)
	print '#########' + str(temp)

def ex7(mynoun):				# return the common parent synset
	#mynoun = 'resident'
	#mynoun = 'snare'
	synsets = wn.synsets(mynoun, pos=wn.NOUN)	# len = 5
	print 'synset len = '+ str(len(synsets))
	elist = []
	print "all synsets :\n\n" + str(synsets)
	print '**********************'

	if len(synsets) == 0:
		print 'len = 0'
		parent_syn_name = mynoun
		return parent_syn_name				# return the same word
	elif len(synsets) == 1:
		print 'len = 1'
		parent_syn = synsets[0]
		parent_syn_name = parent_syn.name
		return parent_syn_name
	elif len(synsets) == 2:
		print 'len = 2'
		print 'API CALL ' + str(synsets[0].lowest_common_hypernyms(synsets[1]))
		parent_syn = common_parent(synsets[0], synsets[1])
		print 'my parent = ' + str(parent_syn)	
		parent_syn_name = parent_syn.name
		return parent_syn_name
	else:
		while ( len(synsets) > 2):
			elist = []
			lsyn = len(synsets)
			flag = check(lsyn)

			for i in range(0, lsyn, 2):
				if (i+2) > lsyn:
					break	# take care for last odd
				print "finding common parent of:: \ns1 = %s || s2 = %s" % (synsets[i], synsets[i+1])
				commonParent = common_parent ( synsets[i], synsets[i+1])
				print 'parent found = '+ str(commonParent) + '\n\n'
				#commonParent = synsets[i].lowest_common_hypernyms(synsets[i+1])
				elist.append(commonParent)		# we r just returning one element now
				#elist.extend(commonParent)

			if (flag == 0):
				elist.append(synsets[lsyn-1])

			print "elist = " + str(elist)
			synsets = []
			synsets = list(elist)
			#print "****" + str(synsets)

	print '###############################################'

	if len(synsets) == 1:
		parent_syn = synsets[0]
		parent_syn_name = parent_syn.name
		return parent_syn_name
	elif len(synsets) == 2:
		print 'len = 2'
		#print 'API CALL ' + str(synsets[0].lowest_common_hypernyms(synsets[1]))
		parent_syn = common_parent(synsets[0], synsets[1])
		parent_syn_name = parent_syn.name
		return parent_syn_name
		#print 'my parent = ' + str(parent_syn)

	print 'comm parent of %s = %s' % (mynoun, (parent_syn.name))

def check(number):
    if number%2==0:
        return 1
    else:
        return 0

if __name__ == '__main__':
	ex6('junk')
	#ex4()
	#ex3()
	#ex2()
	#ex77("mind")		# find the parent of 5 nodes
	#ex8()
	#ex9('jun')
	#common_node_for_a_sense('jun')

def common_node_for_a_sense(asynset):
	#input is asynset
	mynoun = 'resident'
	synsets = wn.synsets(mynoun, pos=wn.NOUN)
	paths = synsets[0].hypernym_paths()
	print str(paths)
	for c in range(0, len(paths)):
		paths[c].reverse()

	print str(paths)
	#sys.exit()
	lsyn = len(paths)
	if lsyn == 1:
		print paths[0][0]
	else:
		print 'vkc'
		ptrs = [0] * lsyn	# reading pointers
		# say there are 2 pointers
		while (1):
			pointer_values = []
			for i in range(0, lsyn):
				n = paths[i][ptrs[i]].name
				pointer_values.append(n)
			check = all_same(pointer_values)

			if (check == 0):
				break;
			else:
				for i in range(0, lsyn):		# check here for out of index
					ptrs[i]+=1
				
			'''s1 = paths[0][ptrs[0]].name
			s2 = paths[1][ptrs[1]].name
			print 's1 %s || s2 %s' % (s1,s2)
			if s1 == s2:
				ptrs[0] += 1
				ptrs[1] += 1
			else:
				break;'''
		if (ptrs[0] == 0):
			print paths[0][0]
		else:
			print paths[0][ptrs[0]-1]		# return the latest commmon one

