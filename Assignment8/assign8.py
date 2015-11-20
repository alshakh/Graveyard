#!/bin/python2
# Ahmed Alshakh
# Assignment 8

from __future__ import division # / will always mean float division
from math import log, exp
import sys
#

STATE_SPACE = list('abcdefghijklmnopqrstuvwxyz_')
STATE_SPACE_SIZE = len(STATE_SPACE)
#
OBSERVATION_SPACE = STATE_SPACE
OBSERVATION_SPACE_SIZE = len(OBSERVATION_SPACE)

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
        self.myNextStates = dict.fromkeys(STATE_SPACE,0) # keys are from STATE_SPACE
        self.myEvidence = dict.fromkeys(OBSERVATION_SPACE,0) # keys are from OBSERVATION_SPACE

    def learn(self, evidence, nextState):
        self.myEvidence[evidence] += 1

        if(nextState is not None): # in case of last observation, no next state
            self.myNextStates[nextState] += 1

    def createState(self,totalInputLetters):

        def calcProbability(numberOfObservations, total):
            # calculate smoothed probabilities
            return ((1.0 + numberOfObservations)/(OBSERVATION_SPACE_SIZE + total))

        # calculate emission probabilty
        pEmission = {}
        emissionTotal = sum([self.myEvidence[e] for e in self.myEvidence])
        for e in self.myEvidence:
            pEmission[e] = calcProbability(self.myEvidence[e],emissionTotal)
        pTransition = {}

        transitionTotal = sum([self.myNextStates[ns] for ns in self.myNextStates])
        for ns in self.myNextStates:
            pTransition[ns] = calcProbability(self.myNextStates[ns],transitionTotal)

        pMarginal = (1.0 + emissionTotal)/(STATE_SPACE_SIZE + totalInputLetters)

        # used emissionTotal to count the total number of appearance of this letter, instead of transitionTotal,
        #    because if this state is the last state, then transitionTotal = (emissionTotal - 1). because it doesn't have next state

        return State(self.myValue, pMarginal, pEmission, pTransition)



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
    builders[lastState].learn(dataSet[-1][1], None) # no next state for the last line
    ###
    # Create states
    states = {}
    for bld in builders:
        states[bld] = builders[bld].createState(len(dataSet))

    return states

def viterbi(observedSequence, states):
    previousV = {}
    currentV = {}

    path = {}

    # Init previousV
    for s in states:
        previousV[s] = log(states[s].pMarginal) + log(states[s].pEmission[observedSequence[0]])
        path[s] = [s]

    # calculate path and probability for the obeservationSequence
    for t in range(1, len(observedSequence)):
        currentV = {}
        newpath = {}

        for s in states:
            # get max previous state
            maxPrevState = None
            maxLogProb = 0
            for s0 in states:
                currentLogProb = previousV[s0] + log(states[s0].pTransition[s]) + log(states[s].pEmission[observedSequence[t]])
                if(maxPrevState == None):
                    maxPrevState = s0
                    maxLogProb = currentLogProb
                    continue
                if(currentLogProb > maxLogProb):
                    maxLogProb = currentLogProb
                    maxPrevState = s0
            # connect this state "s" to max previous state
            currentV[s] = maxLogProb
            newpath[s] = path[maxPrevState] + [s]

        # prepare for next observation
        path = newpath
        previousV = currentV

    (logProb, lastState) = max((currentV[s], s) for s in states)
    return (logProb, path[lastState])

def main(learningFile, testingFile):
    with open(learningFile) as f:
        learningLines = f.readlines()
    with open(testingFile) as f:
        testingLines = f.readlines()

    ### clean and split
    splitAndClean = lambda l:l.rstrip("\n").split()
    learningPairs = map(splitAndClean,learningLines)
    testingPairs = map(splitAndClean,testingLines)
    # there is a line in testing lines with no space ".." (must be ignored)
    for t in testingPairs:
        if(len(t) < 2):
            testingPairs.remove(t)

    ### Learn & Build state nodes
    states = buildStates(learningPairs)

    ### fix the test sequence
    originalSequence = [q[0] for q in testingPairs]
    testSequence = [q[1] for q in testingPairs]

    ## part 2
    def part2():
        print "!!! Part 2 ---------------------------------------"
        def calcErrorRate(l1, l2):
            if(len(l1) != len(l2)):
                return 1.0

            correct = 0.0
            for i in range(len(l1)):
                if (l1[i] == l2[i]):
                    correct += 1

            return 1 - correct/len(l1)

        print "orig error rate", calcErrorRate(originalSequence, testSequence)
        (logProb, path) = viterbi(testSequence, states)

        print "new  error rate", calcErrorRate(originalSequence, path)
        print "Probability = e^(" + str(logProb) + ")"
        print "============= new sequence ==============="
        print ''.join(map(str,path))
        print "=========================================="

    ## part 1
    def part1():
        print "!!! Part 1 ---------------------------------------"
        print "Emission Probabilites :-"
        for s in STATE_SPACE:
            for e in OBSERVATION_SPACE:
                #print e
                print "P("+e+"|"+s+") =", states[s].pEmission[e]

        print
        print "Transition Probabilites :-"
        for s in STATE_SPACE:
            for t in STATE_SPACE:
                #print e
                print "P(next="+t+"|current="+s+") =", states[s].pTransition[t]

        print
        print "Marginal Probabilites :-"
        for s in STATE_SPACE:
            print "P("+s+") =", states[s].pMarginal

    if(len(sys.argv) != 2):
        part1()
        part2()
    else:
        if(sys.argv[1] == '1'):
            part1()
        elif (sys.argv[1] == '2'):
            part2()
        else:
            part1()
            part2()

if __name__ == "__main__":
    main("typos20.data","typos20Test.data")
