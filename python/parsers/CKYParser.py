#!/opt/python-2.5/bin/python2.5
#
# Ling 571 - Homework 2
#   CKY Parser
#
# By Shu Wu (shuwu83@gmail.com)
#
import nltk
import sys

class CKYParser:

    # Default constructor, takes in Chomsky normalized grammar
    #
    def __init__(self, cnfGrammar):
        self._grammar = cnfGrammar

        self._productionTable = {}

        # Initialize LUT of LHS non-terminal pairs 
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
                print "ERROR: non-CNF grammar with multiple rules"

    # Returns a list of possible parses
    #   Uses CKY Algorithm (see Jurasky and Martin Chapter 13.4, 2nd edition)
    #
    def nbest_parse(self, tokens):
        self._grammar.check_coverage(tokens)
        
        tokensCount = len(tokens);

        table = [[0 for endIndex in range(tokensCount + 1)] for startIndex in range(tokensCount)]

        # Look left-to-right
        for endIndex in range(1, tokensCount + 1):

            # Match terminals
            # 
            productions = self._grammar.productions(rhs = tokens[endIndex-1])
            trees = []
            for production in productions:
                trees.append( nltk.Tree(production.lhs(), [production.rhs()]) )

            table[endIndex-1][endIndex] = trees;
            
            # Look bottom-to-top
            for startIndex in range(endIndex - 2, -1, -1):

                # Match productions
                #

                trees = []
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
                                    trees.append( nltk.Tree(production.lhs(), [leftTree, rightTree]) )
                    
                table[startIndex][endIndex] = trees;

        treesFound = []
        # Look for trees beginning with TOP
        for tree in table[0][len(tokens)]:
            if str(tree.node) == "TOP":
                treesFound.append(tree)

        return treesFound
        
if __name__ == "__main__":

    if (len(sys.argv) >= 2 ):
        sentencesLocation = sys.argv[0]
        grammarLocation = sys.argv[1]
    else:
        sentencesLocation = "sentences"
        grammarLocation = "chomsky_grammar.cfg"

    # Argument 1, grammar file
    grammar = nltk.data.load("file:" + grammarLocation)
    # Argument 2, sentences
    sentencesFile = open(sentencesLocation, "r")
    
    parser = CKYParser(grammar)
    
    while True: # loop over all sentences
        sentence = sentencesFile.readline()
        if sentence == "": break

        tokens = nltk.tokenize.wordpunct_tokenize(sentence)

        parses = parser.nbest_parse(tokens)

        for parse in parses:
            print ""
            print parse
