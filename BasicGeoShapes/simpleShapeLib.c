/*
 *  Coordinates
 */
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <math.h>
//  OpenGL with prototypes for glext
#define GL_GLEXT_PROTOTYPES
#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif
//
#define COS(x) cos(M_PI/180*x)
#define SIN(x) sin(M_PI/180*x)
//  Globals
int th=0;		 // Azimuth of view angle
int ph=0;		 // Elevation of view angle
//
double dim=2;	// Dimension of orthogonal box

void p2c(float phi, float * x, float *y){
    *x=COS(phi);//*COS(theta);
    *y=SIN(phi);//*COS(theta);
    //*z=SIN(theta);
}
void s2c(float phi,float theta, float * x, float *y,float *z){
   *x = COS(phi)*SIN(theta);
   *y = SIN(phi)*SIN(theta);
   *z =          COS(theta);
}
void drawUnitSphere(int angleInc){
	float phi,theta;
	float x,y,z;
	glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
	for (theta=0+angleInc;theta<=180;theta+=angleInc)
   	{
      		glBegin(GL_QUAD_STRIP);
      		for (phi=0;phi<=360;phi+=angleInc)
      		{
			s2c(phi,theta-angleInc,&x,&y,&z);
         		glVertex3f(x,y,z);
			s2c(phi,theta,&x,&y,&z);
         		glVertex3f(x,y,z);
      		}
      		glEnd();
   	}
}
void drawUnitCube(){
	glBegin(GL_QUAD_STRIP);
	glVertex3f(0,0,0);
	glVertex3f(0,1,0);
	glVertex3f(0,0,1);
	glVertex3f(0,1,1);
	glVertex3f(1,0,1);
	glVertex3f(1,1,1);
	glVertex3f(1,0,0);
	glVertex3f(1,1,0);
	glVertex3f(0,0,0);
	glVertex3f(0,1,0);
	glEnd();
	//
	glBegin(GL_QUADS);
	glColor3f(1,0,0);
	glVertex3f(0,0,0);
	glVertex3f(1,0,0);
	glVertex3f(1,0,1);
	glVertex3f(0,0,1);
	//
	glVertex3f(0,1,0);
	glVertex3f(1,1,0);
	glVertex3f(1,1,1);
	glVertex3f(0,1,1);
	glEnd();
	
}
void drawUnitCylinder(int angleInc){
	int phi=0;
	float x,y;
	glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
	glBegin(GL_TRIANGLE_FAN);
	glVertex3d(0,0,0);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glVertex3d(x,y,0);
	}
	glEnd();
	//
	glBegin(GL_QUAD_STRIP);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glVertex3d(x,y,1);
		glVertex3d(x,y,0);
	}
	glEnd();
	//
	glBegin(GL_TRIANGLE_FAN);
	glVertex3d(0,0,1);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glVertex3d(x,y,1);
	}
	glEnd();
}


/*
 *  Convenience routine to output raster text
 *  Use VARARGS to make this more flexible
 */
#define LEN 8192  // Maximum length of text string
void Print(const char* format , ...)
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
		glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18,*ch++);
}

/*
 *  Display the scene
 */
void display()
{
	//  Clear the image
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	//  Reset previous transforms
	glLoadIdentity();
	glEnable(GL_DEPTH_TEST);
	//  Set view angle
	glRotated(ph,1,0,0);
	glRotated(th,0,1,0);
	//  Draw
	glPushMatrix();
	drawUnitSphere(10);
	glPopMatrix();
	//
	//
	//
	//  Draw axes in white
	glColor3f(1,1,1);
	glBegin(GL_LINES);
	glVertex3d(0,0,0);
	glVertex3d(dim/2,0,0);
	glVertex3d(0,0,0);
	glVertex3d(0,dim/2,0);
	glVertex3d(0,0,0);
	glVertex3d(0,0,dim/2);
	glEnd();
	//  Label axes
	glRasterPos3d(dim/2,0,0);
	Print("X");
	glRasterPos3d(0,dim/2,0);
	Print("Y");
	glRasterPos3d(0,0,dim/2);
	Print("Z");
	//  Display parameters
	glWindowPos2i(5,5);
	Print("View Angle=%d,%d ",th,ph);
	//  Flush and swap
	glFlush();
	glutSwapBuffers();
}

/*
 *  GLUT calls this routine when a key is pressed
 */
void key(unsigned char ch,int x,int y)
{
	//  Exit on ESC
	if (ch == 27)
		exit(0);
	//  Reset view angle
	else if (ch == '0'){
		th = ph = 0;
	}else if (ch == 'a'||ch=='A') {

	}else if (ch == 'd' || ch == 'D'){

	}else if (ch=='-' || ch=='_'){

	} else if (ch=='=' || ch=='+') {

	}
	//  Tell GLUT it is necessary to redisplay the scene
	glutPostRedisplay();
}

/*
 *  GLUT calls this routine when an arrow key is pressed
 */
void special(int key,int x,int y)
{
	//  Right arrow key - increase azimuth by 5 degrees
	if (key == GLUT_KEY_RIGHT)
		th += 5;
	//  Left arrow key - decrease azimuth by 5 degrees
	else if (key == GLUT_KEY_LEFT)
		th -= 5;
	//  Up arrow key - increase elevation by 5 degrees
	else if (key == GLUT_KEY_UP)
		ph += 5;
	//  Down arrow key - decrease elevation by 5 degrees
	else if (key == GLUT_KEY_DOWN)
		ph -= 5;
	//  Keep angles to +/-360 degrees
	th %= 360;
	ph %= 360;
	//  Tell GLUT it is necessary to redisplay the scene
	glutPostRedisplay();
}

/*
 *  GLUT calls this routine when the window is resized
 */
void reshape(int width,int height)
{
	//  Ratio of the width to the height of the window
	double w2h = (height>0) ? (double)width/height : 1;
	//  Set the viewport to the entire window
	glViewport(0,0, width,height);
	//  Tell OpenGL we want to manipulate the projection matrix
	glMatrixMode(GL_PROJECTION);
	//  Undo previous transformations
	glLoadIdentity();
	//  Orthogonal projection box adjusted for the
	//  aspect ratio of the window
	glOrtho(-dim*w2h,+dim*w2h, -dim,+dim, -dim,+dim);
	//  Switch to manipulating the model matrix
	glMatrixMode(GL_MODELVIEW);
	//  Undo previous transformations
	glLoadIdentity();
}

/*
 *  Start up GLUT and tell it what to do
 */
int main(int argc,char* argv[])
{
	//  Initialize GLUT and process user parameters
	glutInit(&argc,argv);
	//  Request double buffered, true color window 
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	//  Request 500 x 500 pixel window
	glutInitWindowSize(500,500);
	//  Create the window
	glutCreateWindow("");
	//  Tell GLUT to call "display" when the scene should be drawn
	glutDisplayFunc(display);
	//  Tell GLUT to call "reshape" when the window is resized
	glutReshapeFunc(reshape);
	//  Tell GLUT to call "special" when an arrow key is pressed
	glutSpecialFunc(special);
	//  Tell GLUT to call "key" when a key is pressed
	glutKeyboardFunc(key);
	//  Pass control to GLUT so it can interact with the user
	glutMainLoop();
	//  Return code
	return 0;
}
