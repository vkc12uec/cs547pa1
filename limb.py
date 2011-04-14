import MicrosoftNgram

s = MicrosoftNgram.LookupService(token='6855f2b9-927e-4a6e-8766-fe907b235186',model='bing-body/apr10/5/')
#print s.GetConditionalProbability('happy cat is happy')

#sent = 'The bass part of the song was very moving'
mylist = ['bass', 'bass part', 'bass song', 'song moving']
mylist1 = ['tone', 'tone part', 'tone song', 'song moving']
mylist2 = ['fish', 'fish part', 'fish song', 'song moving']
# i went fishing for some sea bass
mylist3 = ['fishing for some sea tone', 'fishing for some sea fish']

md = {}

for item in mylist3:
    prob = s.GetConditionalProbability(item)
    print "%s => %s" % (item, str(prob))
"""
fishing for some sea tone => -6.354662
fishing for some sea fish => -2.908921
"""
"""
for item in mylist1:
    prob = s.GetConditionalProbability(item)
    print "%s => %s" % (item, str(prob))

for item in mylist2:
    prob = s.GetConditionalProbability(item)
    print "%s => %s" % (item, str(prob))
"""
"""
tone => -4.725941
tone part => -3.850021
tone song => -4.224385
song moving => -4.443039
fish => -4.159991
fish part => -3.507205
fish song => -4.258046
song moving => -4.443039

"""
"""
for k,v in md.iteritems():
    print "%s => %s" % (k, str(v))"""