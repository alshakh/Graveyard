/**
Conway's game of life
Author : Ahmed Alshakh
Repo.  : github.com/alshakh/game-of-life
License: GPLv3
*/
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <math.h>

#define GL_GLEXT_PROTOTYPES
#ifdef __APPLE__
#include <OpenGL/OpenGL.h>
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#include <stdlib.h>



//
#include "gof.h"


//
int dim = LIMIT/2;
bool hasGrid = true;
//
void reshape(int width,int height);
void display();
void key(unsigned char,int,int);
void special(int,int,int);
void mouse(int,int,int,int);
void print(const char* format , ...);
//
void initLife();
//
int main(int argc, char **argv) {
    //
    initLife();
    //
    glutInit(&argc,argv);
    glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE);
    glutInitWindowSize(500,500);
    glutCreateWindow("2d Conway's Game of Life");
    glutDisplayFunc(display);
    glutReshapeFunc(reshape);
    //glutSpecialFunc(special);
    glutKeyboardFunc(key);
    glutMouseFunc(mouse);

    glutMainLoop();
    return EXIT_SUCCESS;
}
void initLife() {
    organismAddToLife(1); // TEMP
    //doNextGeneration= true ; //TEMP
}
//

void display()
{
    glClear(GL_COLOR_BUFFER_BIT);
    glLoadIdentity();
    //
    // point 0,0 is the point in the south-west
    glTranslatef(-dim,-dim,0);
    //
    if(hasGrid) gridDrawGrid();
    lifeNext();
    //
    glColor3f(1,0,0);
    glWindowPos2i(5,5);
    print("# of Gen. : %d , # of live Cells = %d " , numGens, numLiveCells);
    glColor3f(CELL_COLOR);
    //
    //
    glFlush();
    glutSwapBuffers();
}
//
void key(unsigned char ch,int x,int y)
{
    //
    if (ch == 27) {
        exit(0);
    } else if (ch == 'n') {
        doNextGeneration = true;
    } else if (ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5') {
        organismAddToLife(ch-'0');
    } else if (ch=='c') {
        lifeClearLife();
    } else if (ch=='j') {
        lifeJumpNGens(100);
    } else if (ch=='g') {
        hasGrid = 1-hasGrid;
    } else if (ch=='p') {
        lifePoplulateRandomly();
    } else if (ch=='e') {
        rle_info_t file = {"glider gun","24bo$22bobo$12b2o6b2o12b2o$11bo3bo4b2o12b2o$2o8bo5bo3b2o$2o8bo3bob2o4bobo$10bo5bo7bo$11bo3bo$12b2o!",36,9};
        rleAddToLife(LIMIT,file);
    }
    glutPostRedisplay();
}
//
/*void special(int key,int x,int y)
{
  glutPostRedisplay();
}*/
//
void reshape(int width,int height)
{
    double w2h = (height>0) ? (double)width/height : 1;
    glViewport(0,0, width,height);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(-dim*w2h,+dim*w2h, -dim,+dim, -dim,+dim);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
}
//
void mouse(int button,int state,int x,int y) {
    //
    GLdouble ox=0.0,oy=0.0,oz=0.0;
    GLint viewport[4];
    GLdouble modelview[16],projection[16];
    GLfloat wx=x,wy,wz;
    //
    if(state!=GLUT_DOWN)
        return;
    if(button==GLUT_RIGHT_BUTTON)
        exit(0);
    glGetIntegerv(GL_VIEWPORT,viewport);
    y=viewport[3]-y;
    wy=y;
    glGetDoublev(GL_MODELVIEW_MATRIX,modelview);
    glGetDoublev(GL_PROJECTION_MATRIX,projection);
    glReadPixels(x,y,1,1,GL_DEPTH_COMPONENT,GL_FLOAT,&wz);
    gluUnProject(wx,wy,wz,modelview,projection,viewport,&ox,&oy,&oz);
    glutPostRedisplay();
    //
    lifeToggleCell((int)ox,(int)oy);
    //
    if(DEBUG) {
        if(lifeIsCellLive((int)ox, (int)oy)){
    	   printf("XYX(%d, %d)\n",(int)ox,(int)oy);
         } else {
    	    printf("!!!(%d, %d)\n",(int)ox,(int)oy);
        }
        fflush(stdout);
    }
}
//
#define LEN 8192  // Maximum length of text string
void print(const char* format , ...)
{
    char	 buf[LEN];
    char*	ch=buf;
    va_list args;
    //  Turn the parameters into a character string
    va_start(args,format);
    vsnprintf(buf,LEN,format,args);
    va_end(args);
    //  Display the characters one at a time at the current raster position
    while (*ch)
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12,*ch++);
}
//


