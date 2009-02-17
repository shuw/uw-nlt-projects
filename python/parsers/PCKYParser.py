#!/opt/python-2.5/bin/python2.5
#
# Ling 571 - Homework 3
#   Probabilistic CKY Parser
#
# By Shu Wu (shuwu83@gmail.com)
#
import re
import nltk
import nltk.treetransforms
from nltk.draw.tree import draw_trees

# Enable this for additional details
#
debug = False

"""
To run this:

parser.py [path to pickled PCFG grammar] [path to pickled ngramTagger] < STDIN: Sentences

"""

class PCKYParser:

    # Default constructor, takes in Chomsky normalized grammar
    #
    def __init__(self, cnfGrammar, tagger):
        self._grammar = cnfGrammar
        self._tagger = tagger

        self._productionTable = {}

        # Initialize LUT of RHS non-terminal pairs 
        for production in cnfGrammar.productions():
            rhs = production.rhs()
            rhsLen = len(rhs)

            # RHS Non-production pair detected
            if rhsLen == 2:
                key = (rhs[0].symbol(), rhs[1].symbol())
                
                if not self._productionTable.has_key(key):
                    self._productionTable[key] = []
                
                self._productionTable[key].append( production )
            elif rhsLen > 2:
                print production
                print 'ERROR: non-CNF grammar with multiple rules'

    # Compare trees
    #   Reverse sort by probability
    #
    def compare_tree(cls, x, y):
        if x.prob() > y.prob(): return -1
        elif x.prob() == y.prob(): return 0
        else: return 1

    compare_tree = classmethod(compare_tree)
        
    # Returns a list of possible parses
    #   Uses CKY Algorithm (see Jurasky and Martin Chapter 13.4, 2nd edition)
    #
    def nbest_parse(self, tokens, debug = False):
        tokensCount = len(tokens);
        tagged = self._tagger.tag(tokens)


        table = [[None for endIndex in range(tokensCount + 1)] for startIndex in range(tokensCount)]

        # Look left-to-right
        for endIndex in range(1, tokensCount + 1):

            trees = []

            # Match terminals using ngramTagger output (prob == 1)
            #  
            token, tag = tagged[endIndex - 1]
            trees.append(nltk.ProbabilisticTree(tag, [token], prob=1.0))

            # Match terminals using generated PCFG grammar (prob <  1)
            # 
            productions = self._grammar.productions(rhs = tokens[endIndex-1])

            for production in productions:
                if str(production.lhs()) != tag: trees.append( nltk.ProbabilisticTree(production.lhs().symbol(), [production.rhs()], prob=production.prob()) )

            table[endIndex-1][endIndex] = trees;

            if debug:
                # Print matched POS
                print str(endIndex - 1) + " " + str(endIndex) + ": " + token + " ",
                for tree in trees: print str(tree.node) + " ",
                print
                
            
            # Look bottom-to-top
            for startIndex in range(endIndex - 2, -1, -1):

                trees = []

                # A* Parser
                mostLikelyTrees = {}
                
                # Iterate over possible split positions
                for split in range(startIndex + 1, endIndex):
                    leftTrees = table[startIndex][split]
                    rightTrees = table[split][endIndex]


                    # Iterate through all posibile combination of trees
                    for leftTree in leftTrees:
                        for rightTree in rightTrees:
                            key = (str(leftTree.node), str(rightTree.node))

                            if self._productionTable.has_key(key):
                                
                                # Iterate over possible productions
                                for production in self._productionTable[key]:
                                    prob = leftTree.prob() * rightTree.prob()

                                    existingTree = None
                                    if mostLikelyTrees.has_key(production.lhs().symbol()): existingTree = mostLikelyTrees[production.lhs().symbol()]
                                    
                                    if existingTree == None or prob > existingTree.prob():                                        
                                        mostLikelyTrees[production.lhs().symbol()] = nltk.ProbabilisticTree(production.lhs().symbol(), [leftTree, rightTree],
                                                                     prob= prob)

                treesToKeep = mostLikelyTrees.values()
                treesToKeep.sort(cmp=PCKYParser.compare_tree) # Sort trees, with highest probabilities first

                # Prune all trees except the end node (we want to maximize our chances of finding the top node)
                if not(startIndex == 0 and endIndex == tokensCount):
                    treesToKeep = treesToKeep[0:20]

                table[startIndex][endIndex] = treesToKeep

                if debug:
                    print str(startIndex) + " " + str(endIndex) + ": " + str(len(mostLikelyTrees))

        finalTrees = table[0][len(tokens)]

        # Create LUT of topProductions
        topProductions = {}
        for prod in self._grammar.productions():
            if prod.lhs().symbol() == "TOP":
                topProductions[prod.rhs()[0].symbol()] = prod


        mostLikelyTree = None

        # Match against top productions, tree should already be sorted
        #   Keep in mind that they are already sorted with highest prob first
        #        
        for tree in finalTrees:            
            if topProductions.has_key(tree.node):
                if debug: print "Matched top production"
                mostLikelyTree = tree
                break
    
        # As backup, look for top Sentence nodes
        #
        if mostLikelyTree == None:
            for tree in finalTrees:
                # Look for sentence marker, which is not a markov smoothing tag
                if re.match("^S[A-Z]*[^\|]*$", tree.node) != None:
                    if debug: print "Matched sentence node"
                    mostLikelyTree = tree
                    break

        
        if mostLikelyTree != None:
            mostLikelyTree = nltk.tree.ProbabilisticTree("TOP", [tree], prob = tree.prob())
            mostLikelyTree.un_chomsky_normal_form(expandUnary=True) # de-compress CNF
            return [mostLikelyTree]
        else:
            return []
        
if __name__ == '__main__':
    import pickle
    import sys

    if (len(sys.argv) > 1):
        # running from command line
        sentencesFile = sys.__stdin__
        grammarLocation = sys.argv[1]
        ngramTaggerLocation = sys.argv[2]
    else:
        sentencesFile = open('sentences_23_short_tokenized')
        grammarLocation = 'wsj_02-21_h2v1.pcfg'
        ngramTaggerLocation = 'ngramTagger'

    #load the pickled tagger & grammar 
    ngramTagger = pickle.load(open(ngramTaggerLocation,'rb'))
    grammar = pickle.load(open(grammarLocation, 'rb'))
        
    parser = PCKYParser(grammar, ngramTagger)

    for sentence in sentencesFile:
        if debug: print sentence
        tokens = str.split(sentence)
        parses = parser.nbest_parse(tokens, debug = debug)

        if len(parses) == 0:
            print
        else:        
            for parse in parses:
                if debug:
                    print parse.pprint()
                    print parse.prob()
                else:
                    print parse._pprint_flat(nodesep='', parens='()', quotes=False)
