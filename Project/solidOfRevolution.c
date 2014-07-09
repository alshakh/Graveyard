#include "CSCIx229.h"
#include "project.h"
#include "geometry.h"
#include <float.h>


#define TRUMPET 0
#define VAZE 1
#define VAZE2 2
#define LAMP 3
static float value(float x,int id){
	//return sqrt(1-pow((0.2*x-0.75),2));
	//
	if(id==LAMP) return 0.25*(x+1);
	//
	if(id==TRUMPET) return 1/(x+0.5);
	if(id==VAZE || id==VAZE2) return sqrt(1-pow((0.2*x-0.75),2));
	return 0;
	//
	//return 1;
	//
	//return (sqrt(x)/exp(x));
}
static float normalSlope(float x,int id){
	if(id==VAZE || id==VAZE2) return -1/(   (0.15-0.04*x)/sqrt(1-0.04*pow((x-3.75),2))  );
	//
	//return 
	if(id==LAMP) return -1/(0.25);
	//
	if(id==TRUMPET) return -1*(-1/pow((x+0.5),2));
	return 0;
	//
	//return FLT_MAX;
	//
	//return -1/((exp(-1*x)*(1-2*x))/(2*sqrt(x)));
}
static float limit(int id){
	if(id==TRUMPET) return 7;
	if(id==VAZE) return 7;
	if(id==VAZE2) return 3;
	if(id==LAMP) return 3;
	return 0;
}
static float getIncrement(int id){
	if(id==TRUMPET) return 0.1;
	if(id==VAZE) return 0.1;
	if(id==VAZE2) return 0.1;
	if(id==LAMP) return 0.5;
	return 0;
}
static void drawSolidOfRevolution(int id, unsigned int tex){
	//glScaled(10,10,10);
	glEnable(GL_TEXTURE_2D);
	glBindTexture(GL_TEXTURE_2D,tex);
	glPushMatrix();
	float xMAX = limit(id);
	float increment = getIncrement(id);
	float x0,x1;
	for(x1=increment ; x1<=xMAX ; x1+=increment){
		x0=x1-increment;
		//
		float y0 =value(x0,id);
		float y1 =value(x1,id);
		//
		float nX0 = normalSlope(x0,id);
		float nX1 = normalSlope(x1,id);
		//
		glBegin(GL_QUAD_STRIP);
		int ph;
		for(ph=0; ph<=360 ; ph+=2){
			int a =1;
			glTexCoord2f(ph/360.0,x0/xMAX);
		    	glNormal3f(a/nX0,a*Cos(ph),a*Sin(ph));
		    	glVertex3d(x0,y0*Cos(ph),y0*Sin(ph));
		    	glTexCoord2f(ph/360.0,x1/xMAX);
		    	glNormal3f(a/nX1,a*Cos(ph),a*Sin(ph));
		    	glVertex3d(x1,y1*Cos(ph),y1*Sin(ph));
		}
		glEnd();
		// Inside Same as above but with inversed normals
		glPushMatrix();
		glScaled(1,0.999,0.999);
		glBegin(GL_QUAD_STRIP);
		for(ph=0; ph<=360 ; ph+=2){
			int a =1;
			glTexCoord2f(ph/360.0,x0/xMAX);
		    	glNormal3f(-1*a/nX0,-1*a*Cos(ph),-1*a*Sin(ph));
		    	glVertex3d(x0,y0*Cos(ph),y0*Sin(ph));
		    	glTexCoord2f(ph/360.0,x1/xMAX);
		    	glNormal3f(-1*a/nX1,-1*a*Cos(ph),-1*a*Sin(ph));
		    	glVertex3d(x1,y1*Cos(ph),y1*Sin(ph));
		}
		glEnd();
		glPopMatrix();
	}
	glPopMatrix();
	glDisable(GL_TEXTURE_2D);
}
void drawTrumpet(){
	drawSolidOfRevolution(TRUMPET, getTextureId(GOLD_TEX));
	
}
void drawVaze(){
	glPushMatrix();
	glScaled(0.5,1,1);
	drawSolidOfRevolution(VAZE, getTextureId(WHITE_CERAMIC_TEX));
	glEnable(GL_TEXTURE_2D);
	glBindTexture(GL_TEXTURE_2D,getTextureId(WHITE_CERAMIC_TEX));
	glScaled(1,value(0,VAZE),value(0,VAZE));
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(1,0,0);
	glTexCoord2f(0.5,0.5);
	glVertex3d(0,0,0);
	glColor3f(1,1,1);
	float y,z;
	int phi;
	for(phi=0; phi<=360; phi+=5){
		p2c(phi,&y,&z);
		// glTexCoord2f(0.5*Cos(phi)+0.5,0.5*Sin(phi)+0.5);
        // glVertex3f(Cos(phi),Sin(phi),0);
		glTexCoord2f(0.5*Cos(phi)+0.5,0.5*Sin(phi)+0.5);
		glVertex3d(0,y,z);
	}
	glEnd();
	glDisable(GL_TEXTURE_2D);
	glPopMatrix();
}
void drawVaze2(){
	glPushMatrix();
	glScaled(0.5,1,1);
	drawSolidOfRevolution(VAZE2, getTextureId(WHITE_CERAMIC_TEX));
	glEnable(GL_TEXTURE_2D);
	glBindTexture(GL_TEXTURE_2D,getTextureId(WHITE_CERAMIC_TEX));
	glScaled(1,value(0,VAZE2),value(0,VAZE2));
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(1,0,0);
	glTexCoord2f(0.5,0.5);
	glVertex3d(0,0,0);
	glColor3f(1,1,1);
	float y,z;
	int phi;
	for(phi=0; phi<=360; phi+=5){
		p2c(phi,&y,&z);
		glTexCoord2f(0.5*Cos(phi)+0.5,0.5*Sin(phi)+0.5);
		glVertex3d(0,y,z);
	}
	glEnd();
	glDisable(GL_TEXTURE_2D);
	glPopMatrix();
}
void drawLampHead(){
	// XXX : Emission is all on 
	glPushMatrix();
	glScaled(0.5,1,1);
	drawSolidOfRevolution(LAMP, getTextureId(WHITE_CERAMIC_TEX));
	glEnable(GL_TEXTURE_2D);
	glBindTexture(GL_TEXTURE_2D,getTextureId(WHITE_CERAMIC_TEX));
	//
	glPushMatrix();
	glTranslated(limit(LAMP),0,0);
	glScaled(1,value(limit(LAMP),LAMP),value(limit(LAMP),LAMP));
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(1,0,0);
	glTexCoord2f(0.5,0.5);
	glVertex3d(0,0,0);
	glColor3f(1,1,1);
	float y,z;
	int phi;
	for(phi=0; phi<=360; phi+=5){
		p2c(phi,&y,&z);
		glTexCoord2f(0.5*Cos(phi)+0.5,0.5*Sin(phi)+0.5);
		glVertex3d(0,y,z);
	}
	glEnd();
	glPopMatrix();
	//
	glPushMatrix();
	glBindTexture(GL_TEXTURE_2D,getTextureId(WHITE_CERAMIC_TEX));
	glScaled(-1,value(0,LAMP),value(0,LAMP));
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(1,0,0);
	glTexCoord2f(0.5,0.5);
	glVertex3d(0,0,0);
	glColor3f(1,1,1);
	for(phi=0; phi<=360; phi+=5){
		p2c(phi,&y,&z);
		// glTexCoord2f(0.5*Cos(phi)+0.5,0.5*Sin(phi)+0.5);
        // glVertex3f(Cos(phi),Sin(phi),0);
		glTexCoord2f(0.5*Cos(phi)+0.5,0.5*Sin(phi)+0.5);
		glVertex3d(0,y,z);
	}
	glEnd();
	glDisable(GL_TEXTURE_2D);
	glPopMatrix();
	glPopMatrix();
}
