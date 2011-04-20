import nltk
nltk.corpus.gutenberg.fileids()

emma = nltk.corpus.gutenberg.words('austen-emma.txt')
len(emma)

emma = nltk.Text(nltk.corpus.gutenberg.words('austen-emma.txt'))
emma.concordance("surprize")

from nltk.corpus import gutenberg
gutenberg.fileids()

emma = gutenberg.words('austen-emma.txt')

for fileid in gutenberg.fileids():
    num_chars = len(gutenberg.raw(fileid)) # [_raw-access]
    num_words = len(gutenberg.words(fileid))
    num_sents = len(gutenberg.sents(fileid))
    num_vocab = len(set([w.lower() for w in gutenberg.words(fileid)]))
    print int(num_chars/num_words), int(num_words/num_sents), int(num_words/num_vocab), fileid


macbeth_sentences = gutenberg.sents('shakespeare-macbeth.txt')
macbeth_sentences

macbeth_sentences[1037]

longest_len = max([len(s) for s in macbeth_sentences])
[s for s in macbeth_sentences if len(s) == longest_len]

from nltk.corpus import webtext
for fileid in webtext.fileids():
    print fileid, webtext.raw(fileid)[:65], '...'


from nltk.corpus import nps_chat 
chatroom = nps_chat.posts('10-19-20s_706posts.xml')
chatroom[123]

from nltk.corpus import brown
brown.categories()

brown.words(categories='news')

brown.words(fileids=['cg22'])

brown.sents(categories=['news', 'editorial', 'reviews'])

from nltk.corpus import brown
news_text = brown.words(categories='news')
fdist = nltk.FreqDist([w.lower() for w in news_text])
modals = ['can', 'could', 'may', 'might', 'must', 'will']
for m in modals:
    print m + ':', fdist[m],


cfd = nltk.ConditionalFreqDist(
          (genre, word)
          for genre in brown.categories()
          for word in brown.words(categories=genre))
genres = ['news', 'religion', 'hobbies', 'science_fiction', 'romance', 'humor']
modals = ['can', 'could', 'may', 'might', 'must', 'will']
cfd.tabulate(conditions=genres, samples=modals)

from nltk.corpus import reuters
reuters.fileids()

reuters.categories() 

reuters.categories('training/9865')

reuters.categories(['training/9865', 'training/9880'])

reuters.fileids('barley') 

reuters.fileids(['barley', 'corn']) 

reuters.words('training/9865')[:14]

reuters.words(['training/9865', 'training/9880'])

reuters.words(categories='barley')

reuters.words(categories=['barley', 'corn'])

from nltk.corpus import inaugural
inaugural.fileids()

[fileid[:4] for fileid in inaugural.fileids()]

cfd = nltk.ConditionalFreqDist(
          (target, fileid[:4])
          for fileid in inaugural.fileids()
          for w in inaugural.words(fileid)
          for target in ['america', 'citizen']
          if w.lower().startswith(target)) # [_lowercase-startswith]
cfd.plot()

nltk.corpus.cess_esp.words()

nltk.corpus.floresta.words()

nltk.corpus.indian.words('hindi.pos')

nltk.corpus.udhr.fileids()

nltk.corpus.udhr.words('Javanese-Latin1')[11:]

from nltk.corpus import udhr
languages = ['Chickasaw', 'English', 'German_Deutsch',
    'Greenlandic_Inuktikut', 'Hungarian_Magyar', 'Ibibio_Efik']
cfd = nltk.ConditionalFreqDist(
          (lang, len(word))
          for lang in languages
          for word in udhr.words(lang + '-Latin1'))
cfd.plot(cumulative=True)

raw = gutenberg.raw("burgess-busterbrown.txt")
raw[1:20]

words = gutenberg.words("burgess-busterbrown.txt")
words[1:20]

sents = gutenberg.sents("burgess-busterbrown.txt")
sents[1:20]

from nltk.corpus import PlaintextCorpusReader
corpus_root = '/usr/share/dict' # [_corpus-root-dict]
wordlists = PlaintextCorpusReader(corpus_root, '.*') # [_corpus-reader]
wordlists.fileids()

wordlists.words('connectives')

from nltk.corpus import BracketParseCorpusReader
corpus_root = r"C:\corpora\penntreebank\parsed\mrg\wsj" # [_corpus-root-treebank]
file_pattern = r".*/wsj_.*\.mrg" # [_file-pattern]
ptb = BracketParseCorpusReader(corpus_root, file_pattern)
ptb.fileids()

len(ptb.sents())

ptb.sents(fileids='20/wsj_2013.mrg')[19]

text = ['The', 'Fulton', 'County', 'Grand', 'Jury', 'said', ...] # [_seq-words]
pairs = [('news', 'The'), ('news', 'Fulton'), ('news', 'County'), ...] # [_seq-pairs]

from nltk.corpus import brown
cfd = nltk.ConditionalFreqDist(
          (genre, word)
          for genre in brown.categories()
          for word in brown.words(categories=genre))

genre_word = [(genre, word) # [_genre-word-pairs]
              for genre in ['news', 'romance'] # [_each-genre]
              for word in brown.words(categories=genre)] # [_each-word]
len(genre_word)

genre_word[:4]

genre_word[-4:]

cfd = nltk.ConditionalFreqDist(genre_word)
cfd # [_inspect-cfd]

cfd.conditions()

cfd['news']

cfd['romance']

list(cfd['romance'])

cfd['romance']['could']

from nltk.corpus import inaugural
cfd = nltk.ConditionalFreqDist(
          (target, fileid[:4]) # [_first-four-chars]
          for fileid in inaugural.fileids()
          for w in inaugural.words(fileid)
          for target in ['america', 'citizen'] # [_america-citizen]
          if w.lower().startswith(target))

from nltk.corpus import udhr
languages = ['Chickasaw', 'English', 'German_Deutsch',
    'Greenlandic_Inuktikut', 'Hungarian_Magyar', 'Ibibio_Efik']
cfd = nltk.ConditionalFreqDist(
          (lang, len(word)) # [_lang-len-word]
          for lang in languages
          for word in udhr.words(lang + '-Latin1'))

cfd.tabulate(conditions=['English', 'German_Deutsch'],
             samples=range(10), cumulative=True)

sent = ['In', 'the', 'beginning', 'God', 'created', 'the', 'heaven',
  'and', 'the', 'earth', '.']
nltk.bigrams(sent)

print cfd['living']

generate_model(cfd, 'living')

================================ RESTART ================================




from __future__ import division
def lexical_diversity(text):
    return len(text) / len(set(text))

def lexical_diversity(my_text_data):
    word_count = len(my_text_data)
    vocab_size = len(set(my_text_data))
    diversity_score = word_count / vocab_size
    return diversity_score

lexical_diversity(text3)

plural('fairy')

plural('woman')

from textproc import plural
plural('wish')

plural('fan')

unusual_words(nltk.corpus.gutenberg.words('austen-sense.txt'))

unusual_words(nltk.corpus.nps_chat.words())

from nltk.corpus import stopwords
stopwords.words('english')

def content_fraction(text):
    stopwords = nltk.corpus.stopwords.words('english')
    content = [w for w in text if w.lower() not in stopwords]
    return len(content) / len(text)
   
content_fraction(nltk.corpus.reuters.words())

puzzle_letters = nltk.FreqDist('egivrvonl')
obligatory = 'r'
wordlist = nltk.corpus.words.words()
[w for w in wordlist if len(w) >= 6 # [_length-constraint]
                     and obligatory in w # [_obligatory-letter]
                     and nltk.FreqDist(w) <= puzzle_letters] # [_freqdist-compare]

names = nltk.corpus.names
names.fileids()

male_names = names.words('male.txt')
female_names = names.words('female.txt')
[w for w in male_names if w in female_names]

cfd = nltk.ConditionalFreqDist(
          (fileid, name[-1])
          for fileid in names.fileids()
          for name in names.words(fileid))
cfd.plot()

entries = nltk.corpus.cmudict.entries()
len(entries)

for entry in entries[39943:39951]:
    print entry


for word, pron in entries: # [_word-pron]
    if len(pron) == 3: # [_len-pron-three]
        ph1, ph2, ph3 = pron # [_tuple-assignment]
        if ph1 == 'P' and ph3 == 'T':
            print word, ph2,


syllable = ['N', 'IH0', 'K', 'S']
[word for word, pron in entries if pron[-4:] == syllable]

[w for w, pron in entries if pron[-1] == 'M' and w[-1] == 'n']

sorted(set(w[:2] for w, pron in entries if pron[0] == 'N' and w[0] != 'n'))

def stress(pron):
    return [char for phone in pron for char in phone if char.isdigit()] 
[w for w, pron in entries if stress(pron) == ['0', '1', '0', '2', '0']]

[w for w, pron in entries if stress(pron) == ['0', '2', '0', '1', '0']]

p3 = [(pron[0]+'-'+pron[2], word) # [_group-first-last]
      for (word, pron) in entries
      if pron[0] == 'P' and len(pron) == 3] # [_p3-words]
cfd = nltk.ConditionalFreqDist(p3)
for template in cfd.conditions():
    if len(cfd[template]) > 10:
        words = cfd[template].keys()
        wordlist = ' '.join(words)
        print template, wordlist[:70] + "..."


prondict = nltk.corpus.cmudict.dict()
prondict['fire'] # [_dict-key]

prondict['blog'] # [_dict-key-error]

prondict['blog'] = [['B', 'L', 'AA1', 'G']] # [_dict-assign]
prondict['blog']

text = ['natural', 'language', 'processing']
[ph for w in text for ph in prondict[w][0]]

from nltk.corpus import swadesh
swadesh.fileids()

swadesh.words('en')

fr2en = swadesh.entries(['fr', 'en'])
fr2en

translate = dict(fr2en)
translate['chien']

translate['jeter']

de2en = swadesh.entries(['de', 'en'])    # German-English
es2en = swadesh.entries(['es', 'en'])    # Spanish-English
translate.update(dict(de2en))
translate.update(dict(es2en))
translate['Hund']

translate['perro']

languages = ['en', 'de', 'nl', 'es', 'fr', 'pt', 'la']
for i in [139, 140, 141, 142]:
    print swadesh.entries(languages)[i]


from nltk.corpus import toolbox
toolbox.entries('rotokas.dic')

from nltk.corpus import wordnet as wn
wn.synsets('motorcar')

wn.synset('car.n.01').lemma_names

wn.synset('car.n.01').definition

wn.synset('car.n.01').examples

wn.synset('car.n.01').lemmas # [_get-lemmas]

wn.lemma('car.n.01.automobile') # [_lookup-lemma]

wn.lemma('car.n.01.automobile').synset # [_get-synset]

wn.lemma('car.n.01.automobile').name # [_get-name]

wn.synsets('car')

for synset in wn.synsets('car'):
    print synset.lemma_names


wn.lemmas('car')

motorcar = wn.synset('car.n.01')
types_of_motorcar = motorcar.hyponyms()
types_of_motorcar[26]

sorted([lemma.name for synset in types_of_motorcar for lemma in synset.lemmas])

motorcar.hypernyms()

paths = motorcar.hypernym_paths()
len(paths)

[synset.name for synset in paths[0]]

[synset.name for synset in paths[1]]

motorcar.root_hypernyms()

wn.synset('tree.n.01').part_meronyms()

wn.synset('tree.n.01').substance_meronyms()

wn.synset('tree.n.01').member_holonyms()

for synset in wn.synsets('mint', wn.NOUN):
    print synset.name + ':', synset.definition


wn.synset('mint.n.04').part_holonyms()

wn.synset('mint.n.04').substance_holonyms()

wn.synset('walk.v.01').entailments()

wn.synset('eat.v.01').entailments()

wn.synset('tease.v.03').entailments()

wn.lemma('supply.n.02.supply').antonyms()

wn.lemma('rush.v.01.rush').antonyms()

wn.lemma('horizontal.a.01.horizontal').antonyms()

wn.lemma('staccato.r.01.staccato').antonyms()

right = wn.synset('right_whale.n.01')
orca = wn.synset('orca.n.01')
minke = wn.synset('minke_whale.n.01')
tortoise = wn.synset('tortoise.n.01')
novel = wn.synset('novel.n.01')
right.lowest_common_hypernyms(minke)

right.lowest_common_hypernyms(orca)

right.lowest_common_hypernyms(tortoise)

right.lowest_common_hypernyms(novel)

wn.synset('baleen_whale.n.01').min_depth()

wn.synset('whale.n.02').min_depth()

wn.synset('vertebrate.n.01').min_depth()

wn.synset('entity.n.01').min_depth()

right.path_similarity(minke)

right.path_similarity(orca)

right.path_similarity(tortoise)

right.path_similarity(novel)

