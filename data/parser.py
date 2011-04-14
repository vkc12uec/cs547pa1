from nltk.corpus import wordnet as wn
import sys, re
import operator

def compute_ambiguity_score(text):
	word_count = 0
	ambiguity_count = 0
	words = re.findall('(\w+)', text)
	for word in words:
		if not(word in stopwords):
			#number of definitions of the word
			definitions = len(wn.synsets(word))
			if definitions:
				word_count+=1
				ambiguity_count+=definitions

	return float(ambiguity_count)/float(word_count)



#We import a list of stopwords
lines = open('stopwords.txt', 'r').readlines()
stopwords = []
for line in lines:
	stopwords.append(line.strip())

stopwords = set(stopwords)


#We open the documents
data = open(sys.argv[1], 'r').read()
documents = re.findall('(\.I (\d+)\n\.U\n(\d+)\n\.S\n(.+)\n\.M\n(.+)\n\.T\n(.+)\n\.P\n(.+)\n\.W\n(.+)\n\.A\n(.+)\n)', data)
ambiguities = []

for i, document in enumerate(documents):
	ambiguity = compute_ambiguity_score(document[7])
	ambiguities.append([i, ambiguity])

#We sort the list in descending order using the ambiguity score
ambiguities_sorted = sorted(ambiguities, key=operator.itemgetter(1), reverse=True)

#We create a new file with the top n number of ambiguous documents
output = open(sys.argv[1]+'_ambiguous', 'w')

for i in range(int(sys.argv[2])):
	output.write(documents[ambiguities_sorted[i][0]][0])

output.close()

output = open(sys.argv[1]+'_ambiguityinfo', 'w')

#We create a new file with the ambiguity informations
for i, ambiguity in enumerate(ambiguities_sorted):
	output.write(str(documents[ambiguities_sorted[i][0]][1])+' '+str(ambiguity[1])+'\n')

output.close()
