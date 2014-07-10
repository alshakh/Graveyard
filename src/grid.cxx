#define GL_GLEXT_PROTOTYPES
#ifdef __APPLE__
#include <OpenGL/OpenGL.h>
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#include "gof.h"


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
void Life::Grid::drawCells(bool cells[LIMIT][LIMIT]) {
    for(unsigned int i=0 ; i<LIMIT ; i++) {
        for(unsigned int j=0 ; j<LIMIT ; j++) {
            if(cells[i][j]) drawCell(i,j);
        }
    }
}
//
void Life::Grid::drawGrid() {
    glColor3f(GRID_COLOR);
    glBegin(GL_LINES);
    ;
    for(unsigned int i=0; i <=LIMIT ; i++) {
        glVertex2i(0,i);
        glVertex2i(LIMIT,i);
        //
        glVertex2i(i,0);
        glVertex2i(i,LIMIT);
    }
    glEnd();
    glColor3f(CELL_COLOR);
}
//
void Life::Grid::drawCell(unsigned int x,unsigned int y) {
    glBegin(GL_QUADS);
    glVertex2i(x,y);
    glVertex2i(x,y+1);
    glVertex2i(x+1,y+1);
    glVertex2i(x+1,y);
    glEnd();
}
