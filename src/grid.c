#define GL_GLEXT_PROTOTYPES
#ifdef __APPLE__
#include <OpenGL/OpenGL.h>
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#include "gof.h"


void gridDrawCells(bool cells[LIMIT][LIMIT]) {
    int i,j;
    for(i=0 ; i<LIMIT ; i++) {
        for(j=0 ; j<LIMIT ; j++) {
            if(cells[i][j]) gridDrawCell(i,j);
        }
    }
}
//
void gridDrawGrid() {
    glColor3f(GRID_COLOR);
    glBegin(GL_LINES);
    int i;
    for(i=0; i <=LIMIT ; i++) {
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
void gridDrawCell(int x,int y) {
    glBegin(GL_QUADS);
    glVertex2i(x,y);
    glVertex2i(x,y+1);
    glVertex2i(x+1,y+1);
    glVertex2i(x+1,y);
    glEnd();
}
