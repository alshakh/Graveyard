/*
 *  HW1: Lorenz 
 *  Ahmed Alshakh
 *  Color depends on the distance from the origin and positin in quadrants
 *  + change view angle UP,DOWN,LEFT and RIGHT
 *  + next Item in info list SPACE Bar
 *  + increase item value D or d 
 *  + Decrease item value a or A
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

//  Globals
int th=0;		 // Azimuth of view angle
int ph=0;		 // Elevation of view angle
double dim=70;	// Dimension of orthogonal box
//Lorenz parameters 
double s = 10;
double b = 2.6666;
double r = 28;
float dt = 0.01;
int numPts=1000;

// InfoBox parameters 
int selectedItem=0;

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
		glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12,*ch++);
}
/* 
 * Absolute value of float 
 */
float absf(float f){
	if (f<0) return f*-1;
	return f;
}
/*
 *  Display the scene
 */
void display()
{
	//  Clear the image
	glClear(GL_COLOR_BUFFER_BIT);
	//  Reset previous transforms
	glLoadIdentity();
	//  Info Box
	int initPosY=5;
	int spacing=13;
	int numberOfItems=5;
	selectedItem=selectedItem%numberOfItems;
	glWindowPos2i(2,initPosY+0*spacing);
	if(selectedItem==0) 
		Print("no. of points= <%d>",numPts);
	else
		Print("no. of points=  %d",numPts);
	glWindowPos2i(2,initPosY+1*spacing);
	if(selectedItem==1) 
		Print("s= <%3.2f>",s);
	else
		Print("s=  %3.2f",s);
	glWindowPos2i(2,initPosY+2*spacing);
	if(selectedItem==2) 
		Print("b= <%3.2f>",b);
	else
		Print("b=  %3.2f",b);
	glWindowPos2i(2,initPosY+3*spacing);
	if(selectedItem==3) 
		Print("r= <%3.2f>",r);
	else
		Print("r=  %3.2f",r);
	glWindowPos2i(2,initPosY+4*spacing);
	if(selectedItem==4) 
		Print("dt= <%3.3f>",dt);
	else
		Print("dt=  %3.3f",dt);
	glWindowPos2i(2,initPosY+5*spacing);
	Print("View Angle= %d , %d",th,ph);
	glWindowPos2i(2,initPosY+6*spacing);
	Print("---- Info List --");
	//  Set view angle
	glRotated(ph,1,0,0);
	glRotated(th,0,1,0);
	/// Draw Lorenz based on parameters
	int i;
	/* Coordinates */
	float x = 1;
	float y = 1;
	float z = 1;
	/* Time step */
	
	glBegin(GL_LINE_STRIP);
	for (i=0;i<numPts-1;i++)
	{	
		float dx = s*(y-x);
		float dy = x*(r-z)-y;
		float dz = x*y - b*z;
		x += dt*dx;
		y += dt*dy;
		z += dt*dz;
	 	glColor3f(1- absf(x)/(dim/1.5),1-absf(y)/(dim/1.5),1-absf(z)/(dim/1.5));
		glVertex3d(x,y,z);
	}
	glEnd();

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
	//  Reset 
	else if (ch == '0'){
		th = ph = 0;
		s = 10;
		b = 2.6666;
		r = 28;
		dt = 0.01;
		numPts=1000;
		selectedItem=0;
	} else if (ch == ' ') { // Select next item
		selectedItem++;
	} else if (ch == 'a' || ch == 'A'){/// Decrement Parameters
		switch(selectedItem){
   			case 0  ://numpoints
    				numPts-=10;
       				break;
    			case 1  ://s
				s-=0.1;
				break;
    			case 2  ://b
				b-=0.1;
				break;
    			case 3  ://r
				r-=0.1;
				break;
    			case 4  ://dt
				dt-=0.001;
				break;
		}
	} else if (ch == 'd' || ch == 'D'){ /// Increment Parameters
		switch(selectedItem){
   			case 0  ://numpoints
    				numPts+=10;
       				break;
    			case 1  ://s
				s+=0.1;
				break;
    			case 2  ://b
				b+=0.1;
				break;
    			case 3  ://r
				r+=0.1;
				break;
    			case 4  ://dt
				dt+=0.001;
				break;
		}
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
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE);
	//  Request 500 x 500 pixel window
	glutInitWindowSize(500,500);
	//  Create the window
	glutCreateWindow("Ahmed Alshakh : HW1 : Lorenz");
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
