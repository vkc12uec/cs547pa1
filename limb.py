import MicrosoftNgram
import sys

s = MicrosoftNgram.LookupService(token='6855f2b9-927e-4a6e-8766-fe907b235186',model='bing-body/jun09/3/')
#s = MicrosoftNgram.LookupService(token='6855f2b9-927e-4a6e-8766-fe907b235186',model='bing-body/apr10/1/')
#s = MicrosoftNgram.LookupService(token='6855f2b9-927e-4a6e-8766-fe907b235186',model='bing-body/apr10/5/')
#print s.GetConditionalProbability('happy cat is happy')
#sys.exit()

#sent = 'The bass part of the song was very moving'
mylist = ['bass', 'bass part', 'bass song', 'song moving']
mylist1 = ['tone', 'tone part', 'tone song', 'song moving']
mylist2 = ['fish', 'fish part', 'fish song', 'song moving']

# i went fishing for some sea bass
mylist3 = ['fishing for some sea tone', 'fishing for some sea fish']
mylist4 = ['heed to doctors prescription to marijuana', 'mind the doctors prescription to marijuana', 'listen to doctors prescription to marijuana']
mylist5 = ['he took the doctors prescription', 'he listened to the doctors prescription', 'he mind']

mylist6 = ['purchased drug for backache', 'purchased drug for the lumbar pain', 'purchased agent for the lumbar pain', 'purchased marijuana for the lumbar pain', 'purchased pain-killer for the lumbar pain', 'purchased pain-killer for the backache', 'purchased medicine for backache']

#mylist7 = ['purchased controlled substance for lumbar pain', 'purchased soft drug for lumbar pain']
#mylist7 = ['Achromatopsia due to low cell count', 'Achromatopsia due to low visual cell count', 'Achromatopsia due to low retinal cone count']
mylist7 = ['used prohibited drug for phantom limb pain','used soft drug for phantom limb pain', 'used controlled substance for phantom limb pain']
#mylist7 = ['uses of soft drug for phantom limb pain', 'uses of controlled substance for phantom limb pain']
#mylist8 = ['alcohol affects brain', 'alcohol affects mind', 'alcohol affects head']
mylist8 = ['parkinson disease behaved demented', 'parkinson disease behaved insanely', 'parkinson disease behaved crazily', 'parkinson disease behaved disorder']

#for t in s.Generate('doctor prescription', maxgen=5): 
#	print t

#sys.exit()

for item in mylist8:
    prob = s.GetConditionalProbability(item)
    print "%s => %s" % (item, str(prob))



# heed to doctors prescription to marijuana

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
