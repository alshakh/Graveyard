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
#include <stdbool.h>
#include <string.h> /* memset */


// rgb of color of cell
#define CELL_R 1
#define CELL_G 1
#define CELL_B 1
//
#define LIMIT 50

int dim = LIMIT/2;
bool prevCells[LIMIT][LIMIT];
bool nextCells[LIMIT][LIMIT];
bool doNextGeneration = false;
bool hasGrid = true;
unsigned int generation=0;
unsigned int liveCells=0;
//
void reshape(int width,int height);
void display();
void key(unsigned char,int,int);
void special(int,int,int); 
void mouse(int,int,int,int);
void print(const char* format , ...);
//
void drawCell(int,int);
bool getCell(bool a[LIMIT][LIMIT] , int,int);
void drawGrid();
void drawLife();
void resetCells(bool a[LIMIT][LIMIT]);
void drawCells(bool a[LIMIT][LIMIT]);
void populateLifeRandom();
void clearLife();
void nextNGeneration(int n);
void drawPattern(int i);
void initLife();
//
int main(int argc, char **argv){
  //
  initLife();
  //
  glutInit(&argc,argv);
  glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE);
  glutInitWindowSize(500,500);
  glutCreateWindow("2d Conway's Game of Life");
  glutDisplayFunc(display);
  glutReshapeFunc(reshape);
  glutSpecialFunc(special);
  glutKeyboardFunc(key);
  glutMouseFunc(mouse);

  glutMainLoop();
  return EXIT_SUCCESS;
}
void initLife(){
	drawPattern(1); // TEMP
	//doNextGeneration= true ; //TEMP
}
//
void populateLifeRandom(){
  int i = 0;
  for(i = 0 ; i<(LIMIT*LIMIT)/(2*5) ; i++){ // because there's a probability of 50%, divide by 2.
  	int x = rand()%LIMIT;
  	int y = rand()%LIMIT;
  	prevCells[x][y] = true;
  	drawCell(x,y);
	}
}
//
void clearLife(){
	resetCells(prevCells);
	resetCells(nextCells);
	generation=0;
  liveCells=0;
}
//
void drawCells(bool cells[LIMIT][LIMIT]){
	int i,j;
	for(i=0 ; i<LIMIT ; i++){
		for(j=0 ; j<LIMIT ; j++) {
			if(cells[i][j]) drawCell(i,j);
		}
	}
}
//
void drawGrid(){
glColor3f(0.3,0.3,0.3);
  glBegin(GL_LINES);
  int i;
  for(i=0; i <=LIMIT ; i++){
    glVertex2i(0,i);
    glVertex2i(LIMIT,i);
    //
    glVertex2i(i,0);
    glVertex2i(i,LIMIT);
  }
  glEnd();
  glColor3f(CELL_R,CELL_G,CELL_B);
}
//
void drawCell(int x,int y){
  glBegin(GL_QUADS);
  glVertex2i(x,y);
  glVertex2i(x,y+1);
  glVertex2i(x+1,y+1);
  glVertex2i(x+1,y);
  glEnd();
}
//
bool getCell(bool cells[LIMIT][LIMIT],int i,int j){
	// wrap number around 
	i=(i>=0?i%LIMIT:LIMIT+i);
	j=(j>=0?j%LIMIT:LIMIT+j);
	
  return cells[i][j];
}
//
void resetCells(bool cells[LIMIT][LIMIT]){
  memset(cells, 0, sizeof(cells[0][0]) * LIMIT * LIMIT);
}
//
void drawLife(){
  if(!doNextGeneration) {
  		drawCells(prevCells);
     
      return;
  } else {
  	 doNextGeneration = false;
  }
  generation++;
	liveCells=0;
  //
  resetCells(nextCells);
  //
  int i,j;
  for(i=0 ; i<LIMIT ; i++){
    for(j=0 ; j<LIMIT ; j++){
        int neighbors =  getCell(prevCells,i-1,j) +
              getCell(prevCells,i+1,j) +
              getCell(prevCells,i,j-1) +
              getCell(prevCells,i,j+1) +
              getCell(prevCells,i+1,j+1) +
              getCell(prevCells,i+1,j-1) +
              getCell(prevCells,i-1,j+1) +
              getCell(prevCells,i-1,j-1);

         if ( neighbors == 3) { // any cell with exactly 3 neigbors comes to lives
         	nextCells[i][j] = true;
          drawCell(i,j);
          liveCells++;
         } else if (neighbors ==2 &&  getCell(prevCells,i,j)) { // any cell with exactly 2 or 3 stays alive
         	nextCells[i][j] = true;
         	drawCell(i,j);
          liveCells++;
         }
    }
  }
  memcpy(prevCells, nextCells, sizeof (bool) * LIMIT * LIMIT);
}
//
void nextNGeneration(int n){
	for(;n>=0 ; n--){
	doNextGeneration = true;
	drawLife();
	}
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
  if(hasGrid) drawGrid();
  drawLife();
  //
 	glColor3f(1,0,0);
  glWindowPos2i(5,5);
  print("# of Gen. : %d , # of live Cells = %d " , generation, liveCells);
 	glColor3f(CELL_R,CELL_G,CELL_B);
  //
  //
  glFlush();
  glutSwapBuffers();
}
//
void key(unsigned char ch,int x,int y)
{
  if (ch == 27){
    exit(0);
  } else if (ch == 'n'){
    doNextGeneration = true;
  } else if (ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5'){
  	drawPattern(ch-'0');
  } else if (ch=='c'){
  	clearLife();
  } else if (ch=='j'){
  	nextNGeneration(100);
  } else if (ch=='g'){
  	hasGrid = 1-hasGrid;
  } else if (ch=='p'){
  	populateLifeRandom();
  }
  glutPostRedisplay();
}
//
void special(int key,int x,int y)
{
  glutPostRedisplay();
}
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
GLdouble ox=0.0,oy=0.0,oz=0.0;
  GLint viewport[4];
  GLdouble modelview[16],projection[16];
  GLfloat wx=x,wy,wz;

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
  prevCells[(int)ox][(int)oy] = 1 - prevCells[(int)ox][(int)oy];
  //
  if(prevCells[(int)ox][(int)oy]){
  	printf("XYX(%d, %d)\n",(int)ox,(int)oy);
 	} else {
 		printf("!!!(%d, %d)\n",(int)ox,(int)oy);
 	}
 	fflush(stdout);
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
void drawPattern(int p){
	if(p==1){
		//glider-gun
    prevCells[8][26]=true;
    prevCells[9][26]=true;
    prevCells[9][25]=true;
    prevCells[8][25]=true;
    prevCells[18][26]=true;
    prevCells[18][25]=true;
    prevCells[18][24]=true;
    prevCells[19][23]=true;
    prevCells[20][22]=true;
    prevCells[21][22]=true;
    prevCells[19][27]=true;
    prevCells[20][28]=true;
    prevCells[21][28]=true;
    prevCells[23][27]=true;
    prevCells[24][26]=true;
    prevCells[24][25]=true;
    prevCells[24][24]=true;
    prevCells[23][23]=true;
    prevCells[25][25]=true;
    prevCells[22][25]=true;
    prevCells[28][26]=true;
    prevCells[28][27]=true;
    prevCells[28][28]=true;
    prevCells[29][28]=true;
    prevCells[29][27]=true;
    prevCells[29][26]=true;
    prevCells[30][29]=true;
    prevCells[30][25]=true;
    prevCells[32][29]=true;
    prevCells[32][30]=true;
    prevCells[32][25]=true;
    prevCells[32][24]=true;
    prevCells[42][27]=true;
    prevCells[42][28]=true;
    prevCells[43][28]=true;
    prevCells[43][27]=true;
    // fish-hook
    prevCells[12][39] = true;
    prevCells[12][40] = true;
    prevCells[13][40] = true;
    prevCells[14][39] = true;
    prevCells[14][38] = true;
    prevCells[14][37] = true;
    prevCells[15][37] = true;
	}
}
