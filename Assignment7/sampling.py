#!/bin/python2

# Ahmed Alshakh
# github.com/alshakh/ai3202

# Constants :
## indexes of events
Ci = 0
Si = 1
Ri = 2
Wi = 3
## probabilities
cloudy=0.5

sprinkler_Cloudy=0.1
sprinkler_XCloudy=0.5

rain_Cloudy=0.8
rain_XCloudy=0.2

wetGrass_SprinklerRain=0.99
wetGrass_SprinklerXRain=0.9
wetGrass_XSprinklerRain=0.9
wetGrass_XSprinklerXRain=0.0
## random list
randoms = [0.82,	0.56,	0.08,	0.81,	0.34,	0.22,	0.37,	0.99,	0.55,	0.61,	0.31,	0.66,	0.28,	1.0,	0.95,
0.71,	0.14,	0.1,	1.0,	0.71,	0.1,	0.6,	0.64,	0.73,	0.39,	0.03,	0.99,	1.0,	0.97,	0.54,	0.8,	0.97,
0.07,	0.69,	0.43,	0.29,	0.61,	0.03,	0.13,	0.14,	0.13,	0.4,	0.94,	0.19, 0.6,	0.68,	0.36,	0.67,
0.12,	0.38,	0.42,	0.81,	0.0,	0.2,	0.85,	0.01,	0.55,	0.3,	0.3,	0.11,	0.83,	0.96,	0.41,	0.65,
0.29,	0.4,	0.54,	0.23,	0.74,	0.65,	0.38,	0.41,	0.82,	0.08,	0.39,	0.97,	0.95,	0.01,	0.62,	0.32,
0.56,	0.68,	0.32,	0.27,	0.77,	0.74,	0.79,	0.11,	0.29,	0.69,	0.99,	0.79,	0.21,	0.2,	0.43,	0.81,
0.9,	0.0,	0.91,	0.01]

def getCloudy(r):
    return 1 if r < cloudy else 0

def getSprinkler(r,C):
    if C == 1:
        return 1 if r < sprinkler_Cloudy else 0
    elif C == 0:
        return 1 if r < sprinkler_XCloudy else 0
    else:
        return -1

def getRain(r,C):
    if C == 1:
        return 1 if r < rain_Cloudy else 0
    elif C == 0:
        return 1 if r < rain_XCloudy else 0
    else:
        return -1

def getWetgrass(r,S,R):
    if R == 1 and S == 1:
        return 1 if r < wetGrass_SprinklerRain else 0
    elif R == 1 and S == 0:
        return 1 if r < wetGrass_SprinklerXRain else 0
    elif R == 0 and S == 1:
        return 1 if r < wetGrass_XSprinklerRain else 0
    elif R == 0 and S == 0:
        return 1 if r < wetGrass_XSprinklerXRain else 0
    else:
        return -1



def priorSample(randoms):
    samples = []
    myRandoms = list(randoms)
    while True:
        # [U,L,R,D]
        case = [-1,-1,-1,-1]

        if not myRandoms:
            break
        case[0] = getCloudy(myRandoms.pop(0))
        if not myRandoms:
            break
        case[1] = getSprinkler(myRandoms.pop(0), case[0])
        if not myRandoms:
            break
        case[2] = getRain(myRandoms.pop(0), case[0])
        if not myRandoms:
            break
        case[3] = getWetgrass(myRandoms.pop(0), case[1], case[2])

        samples.append(case)

    return samples

def rejectionSample(randoms, wantIdx, evidence):
    # right now it ignores the negative probabilities.
    if evidence == None:
        evidence = [-1,-1,-1,-1]

    samples = []
    myRandoms = list(randoms)
    while True:
        # [U,L,R,D]
        case = [-1,-1,-1,-1]

        if 1 in evidence or wantIdx>=0:
            if not myRandoms:
                break
            case[0] = getCloudy(myRandoms.pop(0))
            if evidence[0] != -1 and case[0] != evidence[0]:
                continue

        if evidence[1] == 1 or evidence[3] ==1  or wantIdx == 1 or wantIdx == 3 :
            if not myRandoms:
                break
            case[1] = getSprinkler(myRandoms.pop(0), case[0])
            if evidence[1] != -1 and case[1] != evidence[1]:
                continue

        if evidence[2] == 1 or evidence[3] ==1  or wantIdx == 2 or wantIdx == 3:
            if not myRandoms:
                break
            case[2] = getRain(myRandoms.pop(0), case[0])
            if evidence[2] != -1 and case[2] != evidence[2]:
                continue

        if  evidence[3] ==1 or wantIdx == 3 :
            if not myRandoms:
                break
            case[3] = getWetgrass(myRandoms.pop(0), case[1], case[2])
            if evidence[3] != -1 and case[3] != evidence[3]:
                continue

        samples.append(case)

    return samples

def calculate(samples, wantIdx, evidence):
    if evidence == None:
        evidence = [-1,-1,-1,-1]

    count = 0
    base = 0
    for s in samples:
        reject = False
        for i in range(4):
            if (evidence[i] != -1 and s[i] != evidence[i]):
                reject = True
                break
        if reject:
            continue

        base+= 1
        if s[wantIdx] == 1:
            count+=1

    return (count,base)


def main():
    T = 1 # true
    F = 0 # false
    Uk = -1 # doesn't matter (unkown)
    ##################################
    # for example the current input is : P(S|C,W)
    # Input
    wantIdx = Si
    # evidence
    eC = T
    eS = Uk
    eR = Uk
    eW = T
    #####################


    evidence = [eC, eS, eR, eW]
    samples = priorSample(randoms)
    #samples = rejectionSample(randoms,wantIdx, evidence)

    #for s in samples:
    #    print s

    result = calculate(samples, wantIdx, evidence)
    print str(result[0]) + " / " + str(result[1]) + " = " + str((1.0*result[0])/result[1])

if __name__ == '__main__':
    main()
