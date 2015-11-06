#!/bin/python2
import random

Ci = 0
Si = 1
Ri = 2
Wi = 3

up=0.5

left_Up=0.1
left_XUp=0.5

right_Up=0.8
right_XUp=0.2

bottom_LeftRight=0.99
bottom_LeftXRight=0.9
bottom_XLeftRight=0.9
bottom_XLeftXRight=0.0

ERROR = (-1,-1,-1,-1)
def getU(r):
    return 1 if r < up else 0

def getL(r,U):
    if U == 1:
        return 1 if r < left_Up else 0
    elif U == 0:
        return 1 if r < left_XUp else 0
    else:
        return -1

def getR(r,U):
    if U == 1:
        return 1 if r < right_Up else 0
    elif U == 0:
        return 1 if r < right_XUp else 0
    else:
        return -1

def getB(r,L,R):
    if R == 1 and L == 1:
        return 1 if r < bottom_LeftRight else 0
    elif R == 1 and L == 0:
        return 1 if r < bottom_LeftXRight else 0
    elif R == 0 and L == 1:
        return 1 if r < bottom_XLeftRight else 0
    elif R == 0 and R == 0:
        return 1 if r < bottom_XLeftXRight else 0
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
        case[0] = getU(myRandoms.pop(0))
        if not myRandoms:
            break
        case[1] = getL(myRandoms.pop(0), case[0])
        if not myRandoms:
            break
        case[2] = getR(myRandoms.pop(0), case[0])
        if not myRandoms:
            break
        case[3] = getB(myRandoms.pop(0), case[1], case[2])

        samples.append(case)

    return samples

def rejectionSample(randoms, wantIdx, evidence):
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
            case[0] = getU(myRandoms.pop(0))
            if evidence[0] != -1 and case[0] != evidence[0]:
                continue

        if evidence[1] == 1 or evidence[3] ==1  or wantIdx == 1 or wantIdx == 3 :
            if not myRandoms:
                break
            case[1] = getL(myRandoms.pop(0), case[0])
            if evidence[1] != -1 and case[1] != evidence[1]:
                continue

        if evidence[2] == 1 or evidence[3] ==1  or wantIdx == 2 or wantIdx == 3:
            if not myRandoms:
                break
            case[2] = getR(myRandoms.pop(0), case[0])
            if evidence[2] != -1 and case[2] != evidence[2]:
                continue

        if  evidence[3] ==1 or wantIdx == 3 :
            if not myRandoms:
                break
            case[3] = getB(myRandoms.pop(0), case[1], case[2])
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
    randoms = [0.82,	0.56,	0.08,	0.81,	0.34,	0.22,	0.37,	0.99,	0.55,	0.61,	0.31,	0.66,	0.28,	1.0,	0.95,
    0.71,	0.14,	0.1,	1.0,	0.71,	0.1,	0.6,	0.64,	0.73,	0.39,	0.03,	0.99,	1.0,	0.97,	0.54,	0.8,	0.97,
    0.07,	0.69,	0.43,	0.29,	0.61,	0.03,	0.13,	0.14,	0.13,	0.4,	0.94,	0.19, 0.6,	0.68,	0.36,	0.67,
    0.12,	0.38,	0.42,	0.81,	0.0,	0.2,	0.85,	0.01,	0.55,	0.3,	0.3,	0.11,	0.83,	0.96,	0.41,	0.65,
    0.29,	0.4,	0.54,	0.23,	0.74,	0.65,	0.38,	0.41,	0.82,	0.08,	0.39,	0.97,	0.95,	0.01,	0.62,	0.32,
    0.56,	0.68,	0.32,	0.27,	0.77,	0.74,	0.79,	0.11,	0.29,	0.69,	0.99,	0.79,	0.21,	0.2,	0.43,	0.81,
    0.9,	0.0,	0.91,	0.01]

    #randoms = []
    #for i in range(100000):
    #    randoms.append(random.random())



    T = 1 # true
    F = 0 # false
    Uk = -1 # doesn't matter (unkown)
    #####################
    wantIdx = Si
    # evidence
    eC = T
    eS = Uk
    eR = Uk
    eW = T
    #####################



    evidence = [eC, eS, eR, eW]
    #samples = priorSample(randoms)
    samples = rejectionSample(randoms,wantIdx, evidence)



    #for s in samples:
    #    print s

    result = calculate(samples, wantIdx, evidence)
    print str(result[0]) + " / " + str(result[1]) + " = " + str((1.0*result[0])/result[1])

if __name__ == '__main__':
    main()
