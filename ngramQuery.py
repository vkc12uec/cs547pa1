from nltk.corpus import wordnet as wn
import MicrosoftNgram
import nltk
from pprint import pprint
import sys 
import re
import logging

LOG_FILENAME = 'ngram.log'
logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG)

def ex3():
	#s = MicrosoftNgram.LookupService(model='bing-body/apr10/5')
	s = MicrosoftNgram.LookupService(token='6855f2b9-927e-4a6e-8766-fe907b235186',model='bing-body/apr10/5')
	#for t in s.Generate('happy cat is always', maxgen=10): 
	for t in s.Generate('alcohol affects', maxgen=10): 
		print t

def ex5():
	fname = 'data/tty1'
	lines = open(fname, 'r').readlines()
	for line in lines:
		line = line.strip()
		logging.debug ( str("O:")+str(line) )
		ex6(line)

def ex6(sent):
	sent = 'During aortic valvuloplasty a guidewire was broken, and the broken fragment remained in the left ventricle. This left ventricular foreign body was retrieved percutaneously by a snare'
	text = nltk.word_tokenize(sent)
	tagged_list = nltk.pos_tag(text)
	new_sentence = []
	expected_tags = ['NN', 'NNS']

	posi = -1
	for t in tagged_list:
		combina = []
		posi += 1
		if t[1] not in expected_tags:
			new_sentence.append(t[0])
		else:
			mynoun = t[0]
			synsets = wn.synsets(mynoun, pos=wn.NOUN)
			if len(synsets) == 0:
				print '%s synsets len = 0' % mynoun
				continue
			first_syn_first_hyp = synsets[0].hypernyms()[0].name
			first_syn_first_hyp = first_syn_first_hyp.split('.')[0].replace('_', ' ')
			logging.debug('noun = %s and its 1st hypernym = %s' % (mynoun, first_syn_first_hyp))
			queryNgram(sent, posi, first_syn_first_hyp)

#def queryNgram(sent, posi, hypname):
def queryNgram(sent, posi):
	#sent = 'alpha beta gamma delta phi'
	list_words = sent.split()
	logging.debug ('pivot is %s' % list_words[posi])
	logging.debug('')
	siz = len(list_words)
	elist = []
	if (posi == 0):
		elist.append( ' '.join(list_words[0:2]))
		elist.append( ' '.join(list_words[0:3]))	#collects first 3 elements
	elif(posi == (siz-1)):
		elist.append( ' '.join(list_words[siz-2:]))	
		elist.append( ' '.join(list_words[siz-3:]))	# collects last 3 elements
		elist.append( ' '.join(list_words[siz-4:]))	# collects last 4 elements
	else:
		#tap from this position
		tp = posi
		'''i = 2
		while tp+i < siz:
			elist.append( ' '.join(list_words[tp: (tp+i)]))		#collects first 2 elements starting from tp
			i += 1
			if i==5:
				break;'''
		if tp+2 <= siz:
			elist.append( ' '.join(list_words[tp: (tp+2)]))		#collects first 2 elements starting from tp
		if tp+3 <= siz:
			elist.append( ' '.join(list_words[tp: (tp+3)]))		#collects first 3 elements starting from tp
		if tp+4 <= siz:
			elist.append( ' '.join(list_words[tp: (tp+4)]))		#collects first 4 elements starting from tp
		'''elist.append( ' '.join(list_words[tp: (tp+3)]))
		elist.append( ' '.join(list_words[tp: (tp+4)]))'''

		#go 1 back, take of len 3/4
		tp = posi-1
		if (tp >= 0):
			if tp+2 <= siz:
				elist.append( ' '.join(list_words[tp: (tp+2)]))		#collects first 2 elements starting from tp
			if tp+3 <= siz:
				elist.append( ' '.join(list_words[tp: (tp+3)]))		#collects first 2 elements starting from tp
			if tp+4 <= siz:
				elist.append( ' '.join(list_words[tp: (tp+4)]))		#collects first 2 elements starting from tp

		#go 2 back, take of len 3/4
		tp = posi-2
		if (tp >= 0):
			if tp+3 <= siz:
				elist.append( ' '.join(list_words[tp: (tp+3)]))		#collects first 2 elements starting from tp
			if tp+4 <= siz:
				elist.append( ' '.join(list_words[tp: (tp+4)]))		#collects first 2 elements starting from tp
			if tp+5 <= siz:
				elist.append( ' '.join(list_words[tp: (tp+5)]))		#collects first 5 elements starting from tp
	logging.debug ('\n'.join(elist))
	logging.debug('----------------------------------')

def ex4():
	sent = 'alpha beta gamma delta phi'
	ll = sent.split()
	posi = -1
	for w in ll:
		posi += 1
		queryNgram(sent, posi)

def ex2():
    '''right = wn.synset('resident.n.01')
    left = wn.synset('house_physician.n.01')
    print right.lowest_common_hypernyms(left)
    sys.exit()'''

    synsets = wn.synsets('risk', pos=wn.NOUN)
    print [ syns.hypernym_paths() for syns in synsets ]

if __name__ == '__main__':
	#ex2()
	#ex3()
	ex4()
