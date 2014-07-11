
#include <cstring> /* memset */
#include <cmath>
//
#include "gof.h"
//

Life::Life(unsigned int limit){
    numLiveCells=0;
    numGens=0;
    //limit=LIMIT; // TEMP
    this->limit = limit;
    prevCells.resize(limit);
    nextCells.resize(limit);
    for(unsigned int i = 0 ; i< limit ; i++){
        prevCells.at(i).resize(limit);
        nextCells.at(i).resize(limit);
    }
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
void Life::resetCells(vector<vector<bool>> &cells) {
    for (unsigned int i = 0 ; i<limit ; i++){
        fill(cells.at(i).begin(),cells.at(i).end(),false);
    }
}
//
int Life::getValidIndex(int n) {
    return (n>=0?n%limit:limit+n);
}
bool Life::getCell(vector<vector<bool>> &cells,unsigned int i, unsigned int j) {
    return cells[getValidIndex(i)][getValidIndex(j)];
}
//
/**
 * drawNewCellsInGrid is meant to be used by jumbNGen since actuall drawing will be waste of time
 */
void Life::next(bool nextGen, bool drawNewCellsInGrid) {
    if(!nextGen) {
        drawCells(prevCells);
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
                if(drawNewCellsInGrid) drawCell(i,j);
                numLiveCells++;
            } else if ( ( neighbors ==2 || neighbors == 3) && getCell(prevCells,i,j)) { // any cell with exactly 2 or 3 stays alive
                nextCells[i][j] = true;
                 if(drawNewCellsInGrid) drawCell(i,j);
                numLiveCells++;
            }
        }
    }
    nextCells.swap(prevCells);//memcpy(prevCells, nextCells, sizeof (bool) * limit * limit);
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
        drawCell(x,y); // don't know if neccery when the whole scene will be drawn from scratch
    }
}
void Life::toggleCell(int x, int y){
    prevCells[x][y] = 1 -prevCells[x][y];
}
bool Life::isCellLive(int x, int y){
    return prevCells[x][y];
}

///////////////////////////
/// Grid
///////////////////////////


//
// Grid is a subclass of Life
//
#define GL_GLEXT_PROTOTYPES
#ifdef __APPLE__
#include <OpenGL/OpenGL.h>
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif
//
 void Life::drawCells(vector<vector<bool>> &cells){
    for(unsigned int i=0 ; i<limit ; i++) {
        for(unsigned int j=0 ; j<limit ; j++) {
            if(cells[i][j]) drawCell(i,j);
        }
    }
}
//
void Life::drawGrid() {
    glColor3f(GRID_COLOR);
    glBegin(GL_LINES);
    ;
    for(unsigned int i=0; i <=limit ; i++) {
        glVertex2i(0,i);
        glVertex2i(limit,i);
        //
        glVertex2i(i,0);
        glVertex2i(i,limit);
    }
    glEnd();
    glColor3f(CELL_COLOR);
}
//
void Life::drawCell(unsigned int x,unsigned int y) {
    glBegin(GL_QUADS);
    glVertex2i(x,y);
    glVertex2i(x,y+1);
    glVertex2i(x+1,y+1);
    glVertex2i(x+1,y);
    glEnd();
}
