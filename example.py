from nltk.corpus import wordnet as wn
from pprint import pprint
hyp = lambda s:s.hypernyms() # useful to print hypernym tree
line = wn.synset('line.n.3')
print line.name + ':', line.definition
pprint(line.tree(hyp))

for pentagon_sense in wn.synsets('pentagon', 'n'):
	print pentagon_sense.name + ':', pentagon_sense.definition
	pprint(pentagon_sense.tree(hyp))
