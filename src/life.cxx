
#include <cstring> /* memset */
#include <cmath>
//
#include "gof.h"
//

Life::Life(){
    numLiveCells=0;
    numGens=0;
    limit=LIMIT; // TEMP
    // TODO : change size
}
unsigned int Life::getNumGens(){
    return numGens;
    }
unsigned int Life::getNumLiveCells(){
    return numLiveCells;
}
unsigned int Life::getLimit(){
    return limit;
}
void Life::resetCells(bool cells[LIMIT][LIMIT]) {
    //std::fill(cells, myarray+N, 0);
    memset(cells, 0, sizeof(cells[0][0]) * limit * limit);
}
//
int Life::getValidIndex(int n) {
    return (n>=0?n%limit:limit+n);
}
bool Life::getCell(bool cells[LIMIT][LIMIT],int i,int j) {
    return cells[getValidIndex(i)][getValidIndex(j)];
}
//
/**
 * drawNewCellsInGrid is meant to be used by jumbNGen since actuall drawing will be waste of time
 */
void Life::next(bool nextGen, bool drawNewCellsInGrid) {
    if(!nextGen) {
        Grid::drawCells(prevCells);
        //
        return;
    }
    numGens++;
    numLiveCells=0;
    // reset next cell generations to zero
    resetCells(nextCells);
    //
    for(unsigned int i=0 ; i<limit ; i++) {
        for(unsigned int j=0 ; j<limit ; j++) {
            int neighbors =  getCell(prevCells,i-1,j) +
                             getCell(prevCells,i+1,j) +
                             getCell(prevCells,i,j-1) +
                             getCell(prevCells,i,j+1) +
                             getCell(prevCells,i+1,j+1) +
                             getCell(prevCells,i+1,j-1) +
                             getCell(prevCells,i-1,j+1) +
                             getCell(prevCells,i-1,j-1);

            if ( ( neighbors == 3 ) && !getCell(prevCells,i,j)){ // any cell with exactly 3 neigbors comes to lives
                nextCells[i][j] = true;
                if(drawNewCellsInGrid) Grid::drawCell(i,j);
                numLiveCells++;
            } else if ( ( neighbors ==2 || neighbors == 3) && getCell(prevCells,i,j)) { // any cell with exactly 2 or 3 stays alive
                nextCells[i][j] = true;
                 if(drawNewCellsInGrid) Grid::drawCell(i,j);
                numLiveCells++;
            }
        }
    }
    memcpy(prevCells, nextCells, sizeof (bool) * limit * limit);
}
//
void Life::jumpNGens(unsigned int n) {
    for(; n!=0 ; n--) {
        next(true,false);
    }
}
//
void Life::makeLive(int x, int y) {
    prevCells[getValidIndex(x)][getValidIndex(y)]=true;
}
//
void Life::clear() {
    resetCells(prevCells);
    numGens=0;
    numLiveCells=0;
}
//
void Life::populateRandomly() {
    for(unsigned int i  = 0 ; i<(limit*limit)/(2*5) ; i++) { // because there's a probability of 50%, divide by 2.
        int x = rand()%limit;
        int y = rand()%limit;
        makeLive(x,y);
        Grid::drawCell(x,y); // don't know if neccery when the whole scene will be drawn from scratch
    }
}
void Life::toggleCell(int x, int y){
    prevCells[x][y] = 1 -prevCells[x][y];
}
bool Life::isCellLive(int x, int y){
    return prevCells[x][y];
}


void Life::drawGrid(){
    Grid::drawGrid();
}
