#!/bin/python2
# Ahmed Alshakh
# Assignment 8 Part 1

STATES = list('abcdefghijklmnopqrstuvwxyz_')
EVIDENCE = STATES
NUMBER_OF_POSSIBLE_EVIDENCE = len(EVIDENCE)

## class to hold a node information
class State:
    def __init__(self, value, pMarginal, pEmission, pTransition):
        self.value = value
        self.pMarginal = pMarginal
        self.pEmission = pEmission
        self.pTransition = pTransition

## class used to cllect datat and then calculates probabilites and create State
class StateBuilder:
    def __init__(self, value):
        self.myValue = value
        self.myNextStates = {} # keys are from STATES
        self.myEvidence = {} # keys are from EVIDENCE

        for s in STATES:
            self.myNextStates[s] = 0
        for e in EVIDENCE:
            self.myEvidence[e] = 0

    def learn(self, evidence, nextState):
        self.myEvidence[evidence] += 1

        if(nextState is not None): # in case of last observation, no next state
            self.myNextStates[nextState] += 1

    def createState(self,totalInputLetters):

        def calcProbability(numberOfObservations, total):
            # calculate smoothed probabilities
            return ((1.0 + numberOfObservations)/(NUMBER_OF_POSSIBLE_EVIDENCE + total))

        # calculate emission probabilty
        pEmission = {}
        emissionTotal = sum([self.myEvidence[e] for e in self.myEvidence])
        for e in self.myEvidence:
            pEmission[e] = calcProbability(self.myEvidence[e],emissionTotal)
        pTransition = {}

        transitionTotal = sum([self.myNextStates[ns] for ns in self.myNextStates])
        for ns in self.myNextStates:
            pTransition[ns] = calcProbability(self.myNextStates[ns],transitionTotal)

        pMarginal = emissionTotal*1.0/totalInputLetters
        # used emissionTotal instead of transitionTotal,
        #    because if this state is the last state, then transitionTotal = (emissionTotal - 1). because it doesn't have next state

        return State(self.myValue, pMarginal, pEmission, pTransition)


## takes the input as list of tuples with fixed spaces and makes states form it.
## dataSet should be like this: [('a','b'),(' ',' '),...]
def buildStates(dataSet):
    builders = {}
    for idx, __ in enumerate(dataSet):
        ## data 0 will be added with index 1
        if idx == 0:
            continue
        ##
        st = dataSet[idx-1][0]
        ev = dataSet[idx-1][1]
        nxt = dataSet[idx][0]

        # create if not in builders
        if st not in builders:
            builders[st] = StateBuilder(st)
        # collect data in builders
        builders[st].learn(ev,nxt)
    # add last line
    lastState = dataSet[-1][0]
    if lastState not in builders:
        builders[lastState] = StateBuilder(lastState)
    builders[lastState].learn(dataSet[-1][1], None)
    ###
    # Create states
    states = {}
    for bld in builders:
        states[bld] = builders[bld].createState(len(dataSet))

    return states

def printInformation(states):
    print "## Emission Probabilites :-"
    for s in STATES:
        for e in EVIDENCE:
            #print e
            print "P("+e+"|"+s+") =", states[s].pEmission[e]

    print "  Transition Probabilites :-"
    for s in STATES:
        for t in STATES:
            #print e
            print "P(next="+t+"|current="+s+") =", states[s].pTransition[t]

    print "  Transition Probabilites :-"
    for s in STATES:
        print "P("+s+") =", states[s].pMarginal


def main(inputFile):
    with open(inputFile) as f:
        typos = f.readlines()

    ## Fix and prepare typos
    def fixTypoLine(l):
        q = l.rstrip("\n").split()
        return (q[0],q[1])

    typos = map(fixTypoLine, typos)
    ## Build state nodes
    states = buildStates(typos)
    ##
    printInformation(states)

if __name__ == "__main__":
    main("typos20.data")
