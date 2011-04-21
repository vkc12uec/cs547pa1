import sys
import re

data = open(sys.argv[1], 'r').read()
documents = re.findall('(\.I (\d+)\n\.U\n(\d+)\n\.S\n(.+)\n\.M\n(.+)\n\.T\n(.+)\n\.P\n(.+)\n\.W\n(.+)\n\.A\n(.+)\n)', data)
ambiguities = []

count = 0

for i, document in enumerate(documents):
	print document[7]
	count += 1
	#if count == 5:
		#break;
	"""ambiguity = compute_ambiguity_score(document[7])
	ambiguities.append([i, ambiguity])"""


