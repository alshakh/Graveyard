
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <math.h>
#include <string.h> /* memset */
//
#include "gof.h"
//
bool doNextGeneration = false;
unsigned int numLiveCells = 0;
unsigned int numGens = 0;
//
static bool prevCells[LIMIT][LIMIT];
static bool nextCells[LIMIT][LIMIT];
//
static void resetCells(bool cells[LIMIT][LIMIT]) {
    memset(cells, 0, sizeof(cells[0][0]) * LIMIT * LIMIT);
}
//
static int lifeGetValidIndex(int n) {
    return (n>=0?n%LIMIT:LIMIT+n);
}
static bool lifeGetCell(bool cells[LIMIT][LIMIT],int i,int j) {
    return cells[lifeGetValidIndex(i)][lifeGetValidIndex(j)];
}
//
void lifeNext() {
    if(!doNextGeneration) {
        gridDrawCells(prevCells);
        //
        return;
    } else {
        doNextGeneration = false;
    }
    numGens++;
    numLiveCells=0;
    // reset next cell generations to zero
    resetCells(nextCells);
    //
    int i,j;
    for(i=0 ; i<LIMIT ; i++) {
        for(j=0 ; j<LIMIT ; j++) {
            int neighbors =  lifeGetCell(prevCells,i-1,j) +
                             lifeGetCell(prevCells,i+1,j) +
                             lifeGetCell(prevCells,i,j-1) +
                             lifeGetCell(prevCells,i,j+1) +
                             lifeGetCell(prevCells,i+1,j+1) +
                             lifeGetCell(prevCells,i+1,j-1) +
                             lifeGetCell(prevCells,i-1,j+1) +
                             lifeGetCell(prevCells,i-1,j-1);

            if ( neighbors == 3) { // any cell with exactly 3 neigbors comes to lives
                nextCells[i][j] = true;
                gridDrawCell(i,j);
                numLiveCells++;
            } else if (neighbors ==2 &&  lifeGetCell(prevCells,i,j)) { // any cell with exactly 2 or 3 stays alive
                nextCells[i][j] = true;
                gridDrawCell(i,j);
                numLiveCells++;
            }
        }
    }
    memcpy(prevCells, nextCells, sizeof (bool) * LIMIT * LIMIT);
}
//
void lifeJumpNGens(int n) {
    for(; n>=0 ; n--) {
        doNextGeneration = true;
        lifeNext();
    }
}
//
void LifeLiveCell(int x, int y) {
    prevCells[lifeGetValidIndex(x)][lifeGetValidIndex(y)]=true;
}
//
void lifeClearLife() {
    resetCells(prevCells);
    numGens=0;
    numLiveCells=0;
}
//
void lifePoplulateRandomly() {
    int i = 0;
    for(i = 0 ; i<(LIMIT*LIMIT)/(2*5) ; i++) { // because there's a probability of 50%, divide by 2.
        int x = rand()%LIMIT;
        int y = rand()%LIMIT;
        LifeLiveCell(x,y);
        gridDrawCell(x,y);
    }
}
void lifeToggleCell(int x, int y){
    prevCells[x][y] = 1 -prevCells[x][y];
}
bool lifeIsCellLive(int x, int y){
    return prevCells[x][y];
}
