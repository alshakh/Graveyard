import sys
import math
import queue

# Consts
H = 8
W = 10
# directions
UP = 1
LEFT = 2
DOWN = 3
RIGHT = 4
# squares
OPEN = 0
MOUNTAIN = 1
WALL = 2
SNAKE = 3
BARN = 4
APPLE = 50

# matrices
values  = [[0 for x in range(W)] for x in range(H)]
utility = [[0 for x in range(W)] for x in range(H)]
gamma = 0.9

def getReward(i, j):
    n = values[i][j]
    if n == OPEN:
        return 0
    elif n == MOUNTAIN:
        return -1
    elif n == SNAKE:
        return -2
    elif n == BARN:
        return 1
    elif n == APPLE:
        return 50
def isLegalCoord(i,j):
    if (i >= 0 and i < H and j >= 0 and j < W and values[i][j] != WALL):
        return True
    else:
        return False;

def weightedSum(i, j, dir):

    up = lambda i,j : (utility[i-1][j] if isLegalCoord(i-1,j) else utility[i][j])
    left = lambda i,j : (utility[i][j-1] if isLegalCoord(i,j-1) else utility[i][j])
    down = lambda i,j : (utility[i+1][j] if isLegalCoord(i+1,j) else utility[i][j])
    right = lambda i,j : (utility[i][j+1] if isLegalCoord(i,j+1) else utility[i][j])

    if dir == UP:
        return 0.8 * up(i, j) + 0.1 * left(i, j) + 0.1 * right(i, j)
    if dir == LEFT:
        return 0.8 * left(i, j) + 0.1 * down(i, j) + 0.1 * up(i, j)
    if dir == DOWN:
        return 0.8 * down(i, j) + 0.1 * right(i, j) + 0.1 * left(i, j)
    if dir == RIGHT:
        return 0.8 * right(i, j) + 0.1 * up(i, j) + 0.1 * down(i, j)

def calculateUtility(i, j, delta):
    if values[i][j] == APPLE:
        return 0

    newUtility = getReward(i, j) +\
                 gamma * max(weightedSum(i, j, UP),  \
                             weightedSum(i, j, LEFT),\
                             weightedSum(i, j, DOWN),\
                             weightedSum(i, j, RIGHT))

    if newUtility > utility[i][j]:
        delta = max(delta, abs(utility[i][j] - newUtility))
        utility[i][j] = newUtility
    return delta




def iterationLoop(epsilon):
    q = queue.Queue()
    while True:
        delta = 0
        q.put((0, W-1)) # put goal coord
        while not q.empty():
            t = q.get()
            i = t[0]
            j = t[1]
            if (isLegalCoord(i+1,j)): # go down
                q.put((i+ 1, j))
            if (isLegalCoord(i,j-1)): # go left
                q.put((i,j-1))

            delta = max(delta, calculateUtility(i,j, delta))
        if delta < epsilon * (1 - gamma)/gamma:
            return

def calcPolicy():
    policy = [[0 for x in range(W)] for x in range(H)]
    for i in range(0, H):
        for j in range(0, W):
            if values[i][j] == WALL:
                continue
            thisPolicy = UP
            thisMax = weightedSum(i,j,UP)
            if weightedSum(i, j, LEFT) > thisMax:
                thisMax = weightedSum(i, j, LEFT)
                thisPolicy = LEFT
            if weightedSum(i, j, DOWN) > thisMax:
                thisMax = weightedSum(i, j, DOWN)
                thisPolicy = DOWN
            if weightedSum(i, j, RIGHT) > thisMax:
                thisPolicy = RIGHT
            policy[i][j] = thisPolicy
    return policy

def printMatrix(matrix):
    for i in range(0, H):
        print(" ")
        for j in range(0, W):
            print(int(matrix[i][j]), end = "    ")
    print(" ")
    print(" ")

def main():

    fileName = sys.argv[1]
    f = open(fileName, 'r')
    lines = f.read()

    ############################################
    # Populate values
    i = 0
    j = 0
    tmp = 1
    for line in lines:
        if line == '\n':
            i += 1
            j = 0
            tmp = 1
        elif line != ' ':
            if tmp == 1:
                values[i][j] = int(line)
                j += 1
            else:
                j -= 1
                values[i][j] = values[i][j] * 10 + int(line)
                if values[i][j] == 50:
                    utility[i][j] = 50
                j += 1
            tmp = 0
        else:
            tmp = 1
    ###############################################

    epsilon = 0.5

    iterationLoop(epsilon)

    policy = calcPolicy()

    i = H-1
    j = 0
    path = []
    while not (i == 0 and j == W-1):
        path.append((j, i, round(utility[i][j], 2)))
        if policy[i][j] == UP:
            i -= 1
        elif policy[i][j] == LEFT:
            j -= 1
        elif policy[i][j] == DOWN:
            i += 1
        elif policy[i][j] == RIGHT:
            j += 1
    path.append((W-1, 0, 50))

    print(path)



if __name__ == "__main__":
    main()
