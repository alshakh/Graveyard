import getopt, sys

# generate a list of tuples of size n which will contain evey possible tuple
# number of tuples in the list is 2^n
# Example: for 2 we have 8 cases
def allCases(n):
    if(n == 1):
        return [(True,),(False,)]
    preCases = allCases(n-1) # all cases with n-1 binary variables
    cases = [];
    for p in preCases:
        cases.append((True,) + p)
        cases.append((False,)+ p)
    return cases

class Event(object): # abstract class for events
    def __init__(self):
        self.children = []
    def setChildren(self,childList):
        self.children = childList
    def diagnosticProbability(self, evidence):
        # formula P(Cause|Effect) = P(Effect|Cause)*P(Cause)/P(Effect)

        # TODO Check if the operation is OR
        probability = 0
        for evidenceChild in (set(evidence.keys()) & set(self.children)):
            # For P(Effect|Cause) because to calculate that we need to pass self as evidence
            evidenceCE = evidence.copy()
            evidenceCE[self] = True
            del evidenceCE[evidenceChild]

            # For P(Effect) and P(Cause) calculate these without the evidence of Effect
            evidenceC = evidence.copy()
            del evidenceC[evidenceChild]

            if evidence[evidenceChild]: # evidenceChild is given to be True
                probability += evidenceChild.calcProbability(evidenceCE)*self.calcProbability(evidenceC)/evidenceChild.calcProbability(evidenceC)
            else:# evidenceChild is given to be False
                probability += (1-evidenceChild.calcProbability(evidenceCE))*self.calcProbability(evidenceC)/(1-evidenceChild.calcProbability(evidenceC))
        return probability

class RootEvent(Event):
    def __init__(self,probability):
            Event.__init__(self)
            self.probability = probability
    def calcProbability(self,evidence):
        # if evidence just return the 100% or 0% probability
        if self in evidence.keys():
            return 1 if evidence[self] else 0

        if (set(evidence) & set(self.children)):
            return self.diagnosticProbability(evidence)

        return self.probability

class NonRootEvent(Event):
    def __init__(self,parents):
        Event.__init__(self)
        self.parents = parents

        # We need to setup a probabilityMap
        # probabilityMap describes the probability dependency P(self|case)
        #                it is dictionary ( case -> probability )
        # a case is a tuple contains True or False in index i which describes
        #      status of parent i in this case
        # for example. if self has 2 parents it will have 4 cases in the probabilityMap
        #               (T,T),(T,F),(F,T),(F,F) and the probability of self with each one

        self.probabilityMap = {}

        for p in allCases(len(parents)):
            self.probabilityMap[p] = 0
    def setCaseProbability(self, case, probability):
        self.probabilityMap[case] = probability

    def predictiveProbability(self,evidence):
        # evidence is map from Event -> Value

        # if self event is evidence just return it.
        if(self in evidence.keys()):
            return 1 if evidence[self] else 0

        # cases to calculate probability from
        casesToCheck = list(self.probabilityMap.keys())
        for parent in self.parents:
            if parent in evidence.keys(): ## if the parent is in the evidence values`
                for case in casesToCheck:
                    # if the case has different value than the evidence value, remove it
                    if case[self.parents.index(parent)] != evidence[parent]:
                        casesToCheck.remove(case)

        # calculate probability
        # get probability of parent
        def getProbabiltyOfParent(parent):
            if parent in evidence.keys():
                return 1 if givne[p] else 0
            else:
                return parent.calcProbability(evidence)

        def claculateProbabiltyOfCase(case):
            caseProbability = 1
            for i in range(len(case)):
                if case[i]: # if case has parent i as true
                    caseProbability*= self.parents[i].calcProbability(evidence)
                else:# if case has parent i as false
                    caseProbability*=(1-self.parents[i].calcProbability(evidence))
            return caseProbability

        probability = 0
        for case in casesToCheck:
            probability += self.probabilityMap[case]*claculateProbabiltyOfCase(case)

        return probability
    def calcProbability(self, evidence):
        # if evidence just return the 100% or 0% probability
        if self in evidence.keys():
            return 1 if evidence[self] else 0

        elif (set(evidence)&set(self.children)):
            return self.diagnosticProbability(evidence)
        else:
            return self.predictiveProbability(evidence)

        # TODO : the case of child and parent evidence is ignored for now.
###########

def main():
    Pollution = RootEvent(0.9)
    Smoker = RootEvent(0.3)

    Cancer = NonRootEvent([Pollution,Smoker])
    Cancer.setCaseProbability((False,True),0.05)
    Cancer.setCaseProbability((False,False),0.02)
    Cancer.setCaseProbability((True,True),0.03)
    Cancer.setCaseProbability((True,False),0.001)

    XRay = NonRootEvent([Cancer])
    XRay.setCaseProbability((True,),0.90)
    XRay.setCaseProbability((False,),0.20)

    Dyspnoea = NonRootEvent([Cancer])
    Dyspnoea.setCaseProbability((True,),0.65)
    Dyspnoea.setCaseProbability((False,),0.30)

    Pollution.setChildren([Cancer])
    Smoker.setChildren([Cancer])
    Cancer.setChildren([XRay, Dyspnoea])

    # parse ps~c into [p,s,~c]
    # and Ps into [~p,p,s]
    def createListOfThingsToCheck(args):
        ret = []

        for t in ["S","C","X","P","D"]:
            if t in args:
                ret.append(t.lower())
                ret.append("~"+t.lower())

        for t in ["s","c","x","p","d"]:
            if (("~" + t) not in args) and (t in args):
                ret.append(t)
            elif ("~" + t) in args:
                ret.append('~'+t)

        return ret

    def getEventByName(name):
        if name in ["S","s","~s"]:
            return Smoker
        if name in ["P","p","~p"]:
            return Pollution
        if name in ["C","c","~c"]:
            return Cancer
        if name in ["D","d","~d"]:
            return Dyspnoea
        if name in ["X","x","~x"]:
            return XRay

    # give name (s or ~s) and evidence object and get probability
    def getProbabiltyOf(name,evidence):
        event = getEventByName(name)
        if name[0] is "~":
            return 1-event.calcProbability(evidence)
        else:
            return event.calcProbability(evidence)

    # Create a list of evidence given the passed names
    # example ps is one evidence where p =true and s = true
    #   but PS is 4 evidence objects covering all combinations
    def createEvidenceList(names):

        # Create evidence for variables we know their values (small letters)
        baseEvidence = {}
        RandomVariables = []
        for name in names:
            if name in ['S','C','P','D','X']:
                RandomVariables.append(name)
            elif name[0] is "~":
                baseEvidence[getEventByName(name)] = False
            else:
                baseEvidence[getEventByName(name)] = True

        # if no Random variables (capital Letters) just return the base evidence
        if(len(RandomVariables) == 0):
            return [baseEvidence]

        # Create evidence for every combination of capital lettes
        evidenceList = []
        for case in allCases(len(RandomVariables)):
            currentEvidence = baseEvidence.copy()
            for i in range(len(case)):
                if(case[i]):
                    currentEvidence[getEventByName(RandomVariables[i])] = True
                else:
                    currentEvidence[getEventByName(RandomVariables[i])] = False

            evidenceList.append(currentEvidence)

        return evidenceList

    def printEvidence(ev):
        retStr = ""
        for e in ev.keys():
            retStr += ", "
            if e is Smoker:
                retStr += "S"
            elif e is Pollution:
                retStr += "P"
            elif e is Cancer:
                retStr += "C"
            elif e is XRay:
                retStr += "X"
            elif e is Dyspnoea:
                retStr += "D"

            retStr += " = " + str(ev[e])
        return retStr
    #####
    # parse options
    # from Email
    try:
        opts, args = getopt.getopt(sys.argv[1:], "m:g:j:p:")
    except getopt.GetoptError as err:
        # print help information and exit:
        print str(err) # will print something like "option -a not recognized"
        sys.exit(2)
    for o, a in opts:
        if o in ("-p"):
            print a[0] + " = " + a[1:]
            if a[0] is "S":
                Smoker.probability = float(a[1:])
            elif a[0] is "P":
                Pollution.probability =  float(a[1:])
            #setting the prior here works if the Bayes net is already built
            #setPrior(a[0], float(a[1:])
        elif o in ("-m"):
            print "Marginal for = " + a

            probability = None
            thingsToCheck = createListOfThingsToCheck(a) # parse names

            for name in thingsToCheck:
                print name + " : " + str(getProbabiltyOf(name, {}))

        elif o in ("-g"):
            p = a.find("|")

            thingsToCheck = createListOfThingsToCheck(a[:p])
            evidenceToCheck = createEvidenceList(a[p+1:])

            for nameToCalcualte in thingsToCheck:
                for evidence in evidenceToCheck:
                    print nameToCalcualte + " given " + printEvidence(evidence) + " = " + str(getProbabiltyOf(nameToCalcualte, evidence))

        elif o in ("-j"):

            # ignore big letter ( TODO : No time for implementation)
            a = a.lower()
            print "SORRY, Capital Letters are ignored for the joint option for now"

            probability = 1

            variablesJoint = createListOfThingsToCheck(a)
            for nameToCalcualte in variablesJoint:
                tmpVariablesJoint = list(variablesJoint)
                tmpVariablesJoint.remove(nameToCalcualte)
                evidence = createEvidenceList(tmpVariablesJoint)[0] # there will be only one since no capital letters
                probability *= getProbabiltyOf(nameToCalcualte, evidence)

            print "joint prob of " + str(variablesJoint) + " = " + str(probability)
        else:
            assert False, "unhandled option"

    return
    # Just for testing
    print Dyspnoea.calcProbability({Cancer:True, Smoker:True})*Cancer.calcProbability({Dyspnoea:True, Smoker:True})*Smoker.calcProbability({Dyspnoea:True, Cancer:True})

    print 1-Pollution.calcProbability({Dyspnoea:True})
    print Smoker.calcProbability({Dyspnoea:True})
    print Cancer.calcProbability({Dyspnoea:True})

    print XRay.calcProbability({Dyspnoea:True})
    print Dyspnoea.calcProbability({Dyspnoea:True})

if __name__ == "__main__":
    main()
