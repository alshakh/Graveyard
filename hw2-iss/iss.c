/*
 *  Ahmed Alshakh : HW2: ISS (2011) 
 */
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <math.h>
#include "paramDisp.h"
//  OpenGL with prototypes for glext
#define GL_GLEXT_PROTOTYPES
#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif
//
//
//
#define bool int
#define false 0
#define true 1
#define COS(x) cos(M_PI/180*x)
#define SIN(x) sin(M_PI/180*x)
#define COS_S(th) cos(3.1415927/180*(th))
#define SIN_S(th) sin(3.1415927/180*(th))
#define LEN 8192  // Maximum length of text string
//  Globals
//: Position of ISS-2011 in space
float xPos = 0 ;
float yPos = 0 ;
float zPos = 0 ;
//:Rotation of Solar Panels //TODO TODO TODO TODO
int solarPhi[] = {0,0,0,0};
int solarPhiIdx=0;
int solarPhiSize=4;
//
int th=0;	// Azimuth of view angle
int ph=0;	// Elevation of view angle
bool drawMode = true; //Enable polygon GL_LINE | GL_FILL
bool showAxis = 1;
//
int fov=55;	//  Field of view (for perspective)
double asp=1;	//  Aspect ratio
int prjMode=0;	//  Projection mode
//
double angleInc = 20;
double dim=50;	// Dimension of orthogonal box
//#Prototypes
//: draw functions
void drawUnitCylinder(int angleInc);
void drawUnitSphere(int angleInc);
void drawUnitCube();
void s2c(float phi,float theta, float * x, float *y,float *z);
void p2c(float phi,float *x,float *y);
//: opengl functions
void display();
void special(int key,int x,int y);
void print(const char* format , ...);
void key(unsigned char ch,int x,int y);
void reshape(int width,int height);
//: paramDisplayFunctions
void dec(int i);
void inc(int i);
char* get(int i);
//: ISS 2011 functions
void drawIss();
void drawHalfIss();
void drawModules();
void drawArrayOfZigZagPanels();
void drawZigZagPanel(int numberOfParts);
void drawSolarWing();
void setDefaultColor();
//
//
//***********************************************
//************ OpenGL Functions *****************
//***********************************************
int main(int argc,char* argv[])
{
	//!: paramDisp Init
	paramInit(10,get,inc,dec);//!
	paramInfoSet(0,"Angle 1: %s");
	paramInfoSet(1,"Angle 2: %s");
	paramSet(2,"Pos. x: %s","Pos. x: <%s>");
	paramSet(3,"Pos. y: %s","Pos. y: <%s>");
	paramSet(4,"Pos. z: %s","Pos. z: <%s>");
	paramSet(5,"Dim: %s","Dim: <%s>");
	paramSet(6,"Draw Mode: %s","Draw Mode: <%s>");
	paramSet(7,"Prj. Mode: %s","Prj. Mode: <%s>");
	paramSet(8,"Aspect: %s","Aspect: <%s>");
	paramSet(9,"Fov: %s","Fov: <%s>");
	//
	//
	//  Initialize GLUT
	glutInit(&argc,argv);
	//  Request double buffered, true color window with Z buffering at 600x600
	glutInitDisplayMode(GLUT_RGB | GLUT_DEPTH | GLUT_DOUBLE);
	glutInitWindowSize(600,600);
	glutCreateWindow("Ahmed Alshakh - HW2: ISS 2011");
	//  Set callbacks
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutSpecialFunc(special);
	glutKeyboardFunc(key);
	//  Pass control to GLUT so it can interact with the user
	glutMainLoop();
	return 0;
}
//
//
void display()
{
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	 //  Undo previous transformations
	glLoadIdentity();  
	//  Enable Z-buffering in OpenGL
	glEnable(GL_DEPTH_TEST);
	// Return default color to white
	glColor3f(1,1,1);
	//
	// Draw the parameter list on the window
	paramDraw();
	//  Perspective - set eye position
	//
	if (prjMode)
	{
		double Ex = -2*dim*SIN_S(th)*COS_S(ph);
		double Ey = +2*dim		*SIN_S(ph);
		double Ez = +2*dim*COS_S(th)*COS_S(ph);
		gluLookAt(Ex,Ey,Ez , 0,0,0 , 0,COS_S(ph),0);
	}
	//  Orthogonal - set world orientation
	else
	{
		glRotatef(ph,1,0,0);
		glRotatef(th,0,1,0);  
	}
	glRotatef(-90,1,0,0);
	//
	//
	glPushMatrix();
	setDefaultColor();
	if(drawMode) glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
	else  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
	glTranslated(xPos,yPos,zPos);
	drawIss();
	glPopMatrix();
    //
	//  Draw axes
	if (showAxis){
		glColor3f(0,0,1);
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
		print("X");
		glRasterPos3d(0,dim/2,0);
		print("Y");
		glRasterPos3d(0,0,dim/2);
		print("Z");
	}
	//  Flush and swap	
	glFlush();
	glutSwapBuffers();
}
//
static void Project()
{
	//  Tell OpenGL we want to manipulate the projection matrix
	glMatrixMode(GL_PROJECTION);
	//  Undo previous transformations
	glLoadIdentity();
	//  Perspective transformation
	if (prjMode)
		gluPerspective(fov,asp,dim/4,4*dim);
	//  Orthogonal projection
	else
		glOrtho(-asp*dim,+asp*dim, -dim,+dim, -dim,+dim);
	//  Switch to manipulating the model matrix
	glMatrixMode(GL_MODELVIEW);
	//  Undo previous transformations
	glLoadIdentity();
}
//
void key(unsigned char ch,int x,int y)
{
	//  Exit on ESC
	if (ch == 27){
		exit(0);
	} else if (ch == '0'){//  Reset view angle
		th = ph = 0;
		xPos = yPos = zPos = 0;
	} else if (ch == 'h' || ch == 'H'){
		if(showAxis)
			paramHide();
		else
			paramShow();
		//
		showAxis=1-showAxis;
	} else if (ch=='1') {
	    solarPhi[0]+=3;
	    solarPhi[0]%=360;
	} else if (ch=='2') {
	    solarPhi[1]+=3;
	    solarPhi[1]%=360;
	} else if (ch=='3') {
	    solarPhi[2]+=3;
	    solarPhi[2]%=360;
	} else if (ch=='4') {
	    solarPhi[3]+=3;
	    solarPhi[3]%=360;
	}
	
	//
	paramKeyPressed(ch); // process keys to control paramDisplay
	//
	Project();
	//  Tell GLUT it is necessary to redisplay the scene
	glutPostRedisplay();
}
//
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
//
void reshape(int width,int height)
{
	//  Ratio of the width to the height of the window
	asp = (height>0) ? (double)width/height : 1;
	//  Set the viewport to the entire window
	glViewport(0,0, width,height);
	//  Set projection
	Project();
}
//***********************************************
//************ ParamDisp    Functions ***********
//***********************************************
//## : paramDisp Functions
#define INFO_LEN 100
char inf[INFO_LEN];
char* get(int i){
	//
	     if(i==0) sprintf(inf, "%d", ph);
	else if(i==1) sprintf(inf, "%d", th);
	else if(i==2) sprintf(inf, "%3.1f", xPos);
	else if(i==3) sprintf(inf, "%3.1f", yPos);
	else if(i==4) sprintf(inf, "%3.1f", zPos);
	//
	else if(i==5) sprintf(inf, "%3.2f",dim);
	else if(i==6) sprintf(inf, "%s", (drawMode?"Lines":"Fill"));
	//
	else if(i==7) sprintf(inf, "%s", (prjMode?"Prospective":"Ortho."));
	else if(i==8) sprintf(inf, "%3.3f", asp);
	else if(i==9) sprintf(inf, "%d", fov);
	//
	return inf;
}
void inc(int i){
	     if(i==2) xPos++;
	else if(i==3) yPos++;
	else if(i==4) zPos++;
	//
	else if(i==5) dim++;
	else if(i==6) drawMode = 1-drawMode;
	//
	else if(i==7) prjMode = 1-prjMode; 
	else if(i==8) asp+=0.2;
	else if(i==9) fov++;
	//
}
void dec(int i){
	     if(i==2) xPos--;
	else if(i==3) yPos--;
	else if(i==4) zPos--;
	//
	else if(i==5) dim--;
	else if(i==6) drawMode = 1-drawMode;
	//
	else if(i==7) prjMode = 1-prjMode; 
	else if(i==8) asp-=0.2;
	else if(i==9) fov--;
	//
}
//
//
//
//***********************************************
//************ Drawing ISS 2011 *****************
//***********************************************
//
//
void drawIss(){
	drawHalfIss();
	//
	glPushMatrix();
	glScaled(1,-1,1);
	drawHalfIss();
	glPopMatrix();
	//
	glPushMatrix();
	glTranslated(10,0,-3);
	glScaled(-1,-1,1);
	drawModules();
	glPopMatrix();
}
//
void setDefaultColor(){//FIXME 
	if(!drawMode) glColor3f(0.9,0.9,0.9);
}
//
void drawHalfIss(){
	// 54 X 78 From x:-39<->39 , y:-54,0
	/*
		Solar Array : 
			two wings on each side connected to the iss body.
		The length of half ISS is almost twice the solar array's length. 27*2
	*/
	float solarWingWidth = 12;
	//float solarWingHeight = 37;
	float gapWidth = 3;
	//
	float connectorWidth = 2;
	float connectorOffset = 5;
	//
	float issHalfWidth = 54;
	//Main push matrix
	glPushMatrix();
	//
	glTranslated(0,-issHalfWidth,0);
	
	//#### ZigZagPanels 
	/// vertical panel 1 (7 parts)
	glPushMatrix();
	glTranslated(0,solarWingWidth/2+gapWidth,-connectorWidth+0.1);//0.1 for panel to be connected with connector
	glRotated(90,0,1,0);
	glScaled(0.6,0.6,0.6);
	drawZigZagPanel(7);
	glPopMatrix();
	/// vertical panel 1 (7 parts)
	glPushMatrix();
	glTranslated(0,solarWingWidth*3.0/2+2*gapWidth,-connectorWidth+0.1);
	glRotated(90,0,1,0);
	glScaled(0.6,0.6,0.6);
	drawZigZagPanel(7);
	glPopMatrix();
	/// horizontal 3 panels
	glPushMatrix();
	glScaled(-1,1,1);
	glTranslated(connectorWidth,3/4.0*issHalfWidth,0);
	glRotated(90,1,0,0);
	glScaled(0.5,0.5,0.5);
	drawArrayOfZigZagPanels();
	glPopMatrix();
	 
	 
	//#### major connector 
	glPushMatrix();
	glTranslated(0,connectorOffset,0);
	glScaled(connectorWidth*2, issHalfWidth-connectorOffset  ,connectorWidth*2);
	glRotated(-90,1,0,0);	
	glTranslated(-0.5,-0.5,0);// center unit cube arown the z axis.
	drawUnitCube();
	glPopMatrix();
	//#### solar array
	// 4 solar wings .2nd two are mirror reversed
	int x;
	for(x=0;x<2;x++){	
		glPushMatrix();
		if(x==1) glScaled(-1,1,1);
		glTranslated(connectorWidth,solarWingWidth/2,0);
		//
		glPushMatrix();
		////// FIXME the way to make them rotates is really confusing. Find
		//////////// another way.
		// Rotation of solar panel
        glRotated(solarPhi[solarPhiIdx],1,0,0);
        solarPhiIdx=(solarPhiIdx+1)%solarPhiSize;
        //
		drawSolarWing();
		glPopMatrix();
		//
		glTranslated(0,solarWingWidth+gapWidth,0);
		//
	    // Rotation of solar panel
        glRotated(solarPhi[solarPhiIdx],1,0,0);
        //solarPhiIdx=(solarPhiIdx+1)%solarPhiSize;
		drawSolarWing();
		//
		glPopMatrix();
		//
		solarPhiIdx--;
	}
    solarPhiIdx+=2;
    solarPhiIdx%=solarPhiSize;
	// End of Main PushMatrix
	glPopMatrix();
}
//
void drawModules(){
	//
	float modulesSumHeight = 50;
	float r[] = {0.5 ,1	,2  ,1.5 ,0.7 ,0.5 ,1.2,1 ,0.7 ,1.8  ,1.2  ,0.5 }; // radii of modules
	float h[] = {0.02,0.02,0.3,0.15,0.07,0.02,0.2,0.06,0.01,0.1,0.025,0.025}; //XXX sum must be 1. ratio of module from the sum
	int size = 12;// number of modules
	// Long modules group
	glPushMatrix();
	glRotated(90,0,1,0);
	int i;
	for (i=0 ; i<size ; i++){
		//translate module based on height of previous module
		glTranslated(0,0,modulesSumHeight*(i==0?0:h[i-1])-0.1);//0.1 to make sure the connection is perfect
		glPushMatrix();
		glScaled(r[i],r[i],modulesSumHeight*h[i]+0.1);
		drawUnitCylinder(angleInc);
		glPopMatrix();
	}
	//
	glPopMatrix();
	//#Small module group
	glPushMatrix();
	//
	glTranslated(4,-modulesSumHeight*0.15,0);
	//
	glPushMatrix();
	glTranslated(0,0,1);
	glScaled(1.4,1.4,modulesSumHeight*0.05);
	drawUnitCylinder(angleInc);
	glPopMatrix();
	//
	glTranslated(0,-1.5,0);
	glRotated(-90,1,0,0);
	glScaled(1.5,1.5,modulesSumHeight*0.3);
	drawUnitCylinder(angleInc);
	//
	glPopMatrix();
	//# One cyliner in the middle
	glPushMatrix();
	glTranslated(10,0,-1.5);
	glScaled(2,2,-7);
	drawUnitCylinder(angleInc);
	glPopMatrix();
	//# solar panels on the long group
	int t;
	for (t=0;t<2;t++){
		glPushMatrix();
		if(t==1) glScaled(1,-1,1);
		glTranslated(modulesSumHeight*0.8,1,0);
		glRotated(90,0,0,1);
		glScaled(0.5,0.5,0.5);
		drawSolarWing();
		glPopMatrix();
	}
}
//
void drawSolarWing(){
	// 37 X 12
	// Connector : circle R=2 in (0,0,0) parallel to yz plane ( 0.2 for connecting)
	/*
		a wing is composed of two panels with gap between them and one major 
		connector to iss body and two small connectors to connect the two panels
	*/
	float oneSolarPanelHeight = 34;
	float oneSolarPanelWidht = 5;
	float oneSolarPanelThikness = 0.3;
	float gapWidth = 2;
	//
	float wingConnectorHeight = 2;
	float wingConnectorRadius= 0.5;
	//
	float smallConnectorRadius = 0.3;
	float gapBetweenSolarPanelAndSmallConnector=0.2;
	glPushMatrix();
	glTranslated(0,-(2*oneSolarPanelWidht+gapWidth)/2,0);
	// Wing Connector
	glPushMatrix();
	glTranslated(-0.2,(2*oneSolarPanelWidht+gapWidth)/2,0);//0.2 just to connect with the rest of iss
	glRotated(90,0,1,0);
	glScaled(wingConnectorRadius,wingConnectorRadius,wingConnectorHeight+0.2);//0.2 just to connect with the rest of iss
	drawUnitCylinder(angleInc);
	glPopMatrix();
	//
	glTranslated(wingConnectorHeight,0,0);
	//Small Connector 1
	glPushMatrix();
	glRotated(-90,1,0,0);
	glScaled(smallConnectorRadius,smallConnectorRadius,(2*oneSolarPanelWidht+gapWidth));
	drawUnitCylinder(angleInc);
	glPopMatrix();
	//Small Connector 2
	glPushMatrix();
	glTranslated(oneSolarPanelHeight+2*smallConnectorRadius+2*gapBetweenSolarPanelAndSmallConnector,0,0);
	glRotated(-90,1,0,0);
	glScaled(smallConnectorRadius,smallConnectorRadius,(2*oneSolarPanelWidht+gapWidth));
	drawUnitCylinder(angleInc);
	glPopMatrix();
	//
	glTranslated(gapBetweenSolarPanelAndSmallConnector+smallConnectorRadius,0,-oneSolarPanelThikness/2);
	//
	if(!drawMode) glColor3f(21.0/256,0,50.0/256);//TODO better system to color.
	//SolarPanel 1
	glPushMatrix();
	glScaled(oneSolarPanelHeight,oneSolarPanelWidht,oneSolarPanelThikness);
	drawUnitCube();
	glPopMatrix();
	//SolarPanel 2
	glPushMatrix();
	glTranslated(0,oneSolarPanelWidht+gapWidth,0);
	glScaled(oneSolarPanelHeight,oneSolarPanelWidht,oneSolarPanelThikness);
	drawUnitCube();
	glPopMatrix();
	//
	setDefaultColor();//FIXME 
	//
	glPopMatrix();
}
//
void drawZigZagPanel(int numberOfParts){
	// Height of whole structure is COS(phi)*width*numberOfParts
	//int numberOfParts = 8;
	float width=5;//XXX width and height must match width and height in drawArrayOfZigZagPanels
	float height =4;//XXX width and height must match width and height in drawArrayOfZigZagPanels
	float thikness = 0.1;
	//
	float phi =20;
	float smallConnectOffset = 0.02;
	//
	int t;
	for (t=0; t<numberOfParts;t++){
	  	glPushMatrix();
	  	glTranslated((2*t+1)*(width/2)*COS(phi)-smallConnectOffset,0,0);
	  	glRotated(pow(-1,t)*phi,0,1,0);
	  	glScaled(width,height,thikness);
		glTranslated(-0.5,-0.5,0);
		drawUnitCube();
		glPopMatrix();
	}
}
//
void drawArrayOfZigZagPanels(){
	float width=5;//XXX must be the same in drawZigZagPanel
	float height =4;
	//
	float gapWidth=1;
	//
	float minorConnectorThikness = 0.5;
	//
	float majorConnectorThikness = 0.3;
	float majorConnectorHeight = 2;
	//
	glPushMatrix();
	glTranslated(majorConnectorHeight,0,0);
	//
	glPushMatrix();
	glRotated(90,1,0,0);
	glTranslated(-minorConnectorThikness/2+0.1,-width,0);
	glRotated(90,0,0,1);
	glScaled(width*2,majorConnectorHeight,majorConnectorThikness);
	drawUnitCube();
	glPopMatrix();
	//
	glPushMatrix();
	glTranslated(-minorConnectorThikness/2,-3/2.0*height+-gapWidth,-width/2);
	glRotated(90,0,0,1);
	glRotated(90,1,0,0);
	glScaled(height*3+2*gapWidth,width,minorConnectorThikness);
	drawUnitCube();
	glPopMatrix();
	//
	glPushMatrix();
	glTranslated(minorConnectorThikness/2-0.1,0,0);
	glTranslated(0,-height/2-height/2-gapWidth,0);
	drawZigZagPanel(8);
	glTranslated(0,height/2+height/2+gapWidth,0);
	drawZigZagPanel(8);
	glTranslated(0,height/2+height/2+gapWidth,0);
	drawZigZagPanel(8);
	glPopMatrix();
	//
	glPopMatrix();
}
//
//
//
//
//
//***********************************************
//****** Basic shapes and simple Functinos ******
//***********************************************
//
// Start up GLUT and tell it what to do
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
		glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18,*ch++);
}
//
// polar to cartesian
// phi= angle from +x in xy plane
void p2c(float phi, float * x, float *y){
	*x=COS(phi);//*COS(theta);
	*y=SIN(phi);//*COS(theta);
	//*z=SIN(theta);
}
//
//sperical to cartesian
// phi= angle from +x in xy plane
// theta=angle from +z twords the +xy plane
void s2c(float phi,float theta, float * x, float *y,float *z){
	*x = COS(phi)*SIN(theta);
	*y = SIN(phi)*SIN(theta);
	*z =		  COS(theta);
}
//
// sphere with r = 1 , center in (0,0,0)
void drawUnitSphere(int angleInc){
	float phi,theta;
	float x,y,z;
	
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
//
// cube with side=1 , 
void drawUnitCube(){
	//
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
//
// draw cylinder with center in the base with r=1 , and h=1
void drawUnitCylinder(int angleInc){
	int phi=0;
	float x,y;
	//glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
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
