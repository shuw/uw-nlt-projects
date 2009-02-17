#!/opt/python-2.5/bin/python2.5
#
# By Shu Wu
#
# command-line parameters
#       [path to grammar cfg file] [path to sentences]

from decimal import *
import sys
import nltk
import re

pathToGrammar = sys.argv[1]
pathToSentences = sys.argv[2]

# Read grammar
#
myGrammar = nltk.data.load("file:" + pathToGrammar)
myParser = nltk.parse.EarleyChartParser(myGrammar, trace=0)

# Read sentences
#
sentencesFile = open(pathToSentences, "r")

totalParses = 0.0;
sentences = 0.0;

while True: # loop over all sentences
    sentence = sentencesFile.readline()
    if sentence == "": break
    
    sentence = sentence.lower()

    # Insert whitespace before punctuation
    sentence = re.sub(r'([.?!])',r' \1',sentence)

    sentenceParses = myParser.nbest_parse(sentence.split())    

    if len(sentenceParses) > 0 :
        # Print the top parse
        print(sentenceParses[0])

    print("\n" + str(len(sentenceParses)))
    print("------------------------------------");
        
    sentences = sentences + 1;
    totalParses = totalParses + len(sentenceParses);

print("There are on average " + str(totalParses / sentences) + " parses per sentence.");
