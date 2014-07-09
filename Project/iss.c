#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <math.h>
#include "project.h"
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
#define Cos(x) cos(M_PI/180*x)
#define Sin(x) sin(M_PI/180*x)
//
//:Rotation of Solar Panels //TODO TODO TODO TODO
static int solarPhi[] = {0,0,0,0};
static int solarPhiIdx=0;
static int solarPhiSize=4;
static double angleInc = 20;
static unsigned int blackTileTex;
//#Prototypes
//: draw functions
static void drawUnitCylinder(int angleInc);
static void drawUnitCube();
static void p2c(float phi,float *x,float *y);
//: ISS 2011 functions
 void drawIss(float timeFactor);
static void drawHalfIss();
static void drawModules();
static void drawArrayOfZigZagPanels();
static void drawZigZagPanel(int numberOfParts);
static void drawSolarWing();
//***********************************************
//************ ParamDisp    Functions ***********
//***********************************************
//## : paramDisp Functions
/*
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
*/
//
//
//
//***********************************************
//************ Drawing ISS 2011 *****************
//***********************************************
//
//
static void drawIss__();
static void cylinder(){
    blackTileTex = getTextureId(BLACK_TILE_TEX);
    glEnable(GL_TEXTURE_2D);
    float mat_ambient[] ={0.5f ,	0.5f 	,0.5f ,1.0f};
    float mat_diffuse []={0.7, 	0.7,	0.7  ,1.0f};
    float mat_specular []={ 	1.0f ,	1.0f,	1.0f ,1.0f};
    float shine =0.9*128 ;
    glMaterialfv(GL_FRONT_AND_BACK,GL_SPECULAR,mat_specular);
    glMaterialfv(GL_FRONT_AND_BACK,GL_DIFFUSE,mat_diffuse);
    glMaterialfv(GL_FRONT_AND_BACK,GL_AMBIENT,mat_ambient);
    
    glMaterialf(GL_FRONT_AND_BACK,GL_SHININESS,shine);
    
    int angleInc = 5;
	int phi=0;
	float x,y;
	//glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
    glBindTexture(GL_TEXTURE_2D,blackTileTex);
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(0,0,-1);
	glVertex3d(0,0,0);
	glColor3f(1,1,1);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glTexCoord2f(0.5+0.5*Cos(phi),0.5+0.5*Sin(phi));
		glVertex3d(x,y,0);
	}
	glEnd();
	//
	glBegin(GL_QUAD_STRIP);
	glColor3f(1,1,1);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glNormal3f(x,y,0);
		glTexCoord2f(phi/360.0,1);glVertex3d(x,y,1);
		glTexCoord2f(phi/360.0,0);glVertex3d(x,y,0);
	}
	glEnd();
	//
	glColor3f(1,1,1);
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(0,0,1);
	glVertex3d(0,0,1);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glTexCoord2f(0.5+0.5*Cos(phi),0.5+0.5*Sin(phi));
		glVertex3d(x,y,1);
	}
	glEnd();
	glDisable(GL_TEXTURE_2D);
}
//
void drawIss(float timeFactor){
    //
    srand(54238727);
    solarPhi[0] = timeFactor*360*(rand()%2+1);
    solarPhi[1] = timeFactor*360*(rand()%2+1);
    solarPhi[2] = -1*timeFactor*360*(rand()%2+1);
    solarPhi[3] = -1*timeFactor*360*(rand()%2+1);
    //
    glPushMatrix();
    glTranslated(0,0,0.25);
    drawIss__();
    glPopMatrix();
    //
    glPushMatrix();
    glScaled(0.12,0.12,0.04);
    cylinder();
    glPopMatrix();
    glPushMatrix();
    glTranslated(0,0,0.04);
    glScaled(0.02,0.02,0.15);
    cylinder();
    glPopMatrix();
}
//
static void drawIss__(){
    glPushMatrix();
    glScaled(0.01,0.01,0.01);
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
glPopMatrix();
}
//
static void drawHalfIss(){
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
static void drawModules(){
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
static void drawSolarWing(){
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
	glColor3f(21.0/256,0,50.0/256);
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
	glColor3f(0.9,0.9,0.9);
	//
	glPopMatrix();
}
//
static void drawZigZagPanel(int numberOfParts){
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
static void drawArrayOfZigZagPanels(){
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
// polar to cartesian
// phi= angle from +x in xy plane
static void p2c(float phi, float * x, float *y){
	*x=COS(phi);//*COS(theta);
	*y=SIN(phi);//*COS(theta);
	//*z=SIN(theta);
}
//
//
//
// cube with side=1 , 
static void drawUnitCube(){
glPushMatrix();
   //  Cube
   glScaled(0.5,0.5,0.5);
   glTranslated(1,1,1);
   glBegin(GL_QUADS);
   //  Front
   glNormal3f( 0, 0, 1);
   glVertex3f(-1,-1, 1);
   glVertex3f(+1,-1, 1);
   glVertex3f(+1,+1, 1);
   glVertex3f(-1,+1, 1);
   //  Back
   glNormal3f( 0, 0,-1);
   glVertex3f(+1,-1,-1);
   glVertex3f(-1,-1,-1);
   glVertex3f(-1,+1,-1);
   glVertex3f(+1,+1,-1);
   //  Right
   glNormal3f(+1, 0, 0);
   glVertex3f(+1,-1,+1);
   glVertex3f(+1,-1,-1);
   glVertex3f(+1,+1,-1);
   glVertex3f(+1,+1,+1);
   //  Left
   glNormal3f(-1, 0, 0);
   glVertex3f(-1,-1,-1);
   glVertex3f(-1,-1,+1);
   glVertex3f(-1,+1,+1);
   glVertex3f(-1,+1,-1);
   //  Top
   glNormal3f( 0,+1, 0);
   glVertex3f(-1,+1,+1);
   glVertex3f(+1,+1,+1);
   glVertex3f(+1,+1,-1);
   glVertex3f(-1,+1,-1);
   //  Bottom
   glNormal3f( 0,-1, 0);
   glVertex3f(-1,-1,-1);
   glVertex3f(+1,-1,-1);
   glVertex3f(+1,-1,+1);
   glVertex3f(-1,-1,+1);
   //  End
   glEnd();
   //  Undo transofrm
   glPopMatrix();
   
   return;	
}
//
// draw cylinder with center in the base with r=1 , and h=1
static void drawUnitCylinder(int angleInc){
	int phi=0;
	float x,y;
	//glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(0,0,-1);
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
		glNormal3f(x,y,0);
		glVertex3d(x,y,1);
		glVertex3d(x,y,0);
	}
	glEnd();
	//
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(0,0,1);
	glVertex3d(0,0,1);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		
		glVertex3d(x,y,1);
	}
	glEnd();
}
