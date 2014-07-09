
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <math.h>
 
#ifdef __APPLE__
#include <OpenGL/OpenGL.h>
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#include <stdlib.h>


#define LIMIT 10
int dim = LIMIT/2;


void reshape(int width,int height);
void display();
void key(unsigned char,int,int);
void special(int,int,int); 

int main(int argc, char **argv){
  glutInit(&argc,argv);
  glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE);
  glutInitWindowSize(500,500);
  glutCreateWindow("2d Conway's Game of Life");
  glutDisplayFunc(display);
  glutReshapeFunc(reshape);
  glutSpecialFunc(special);
  glutKeyboardFunc(key);
  glutMainLoop();
  return EXIT_SUCCESS;
}


void display()
{
  glClear(GL_COLOR_BUFFER_BIT);
  glLoadIdentity();
  //
  // point 0,0 is the point in the south-west
  glTranslatef(-dim,-dim,0);
  //
  glBegin(GL_QUADS);
  glVertex2i(0,0);
  glVertex2i(0,1);
  glVertex2i(1,1);
  glVertex2i(1,0);
  glEnd();
  //
  glFlush();
  glutSwapBuffers();
}

void key(unsigned char ch,int x,int y)
{
  if (ch == 27)
    exit(0);

  glutPostRedisplay();
}

void special(int key,int x,int y)
{
  glutPostRedisplay();
}

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
