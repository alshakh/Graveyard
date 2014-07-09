#include "geometry.h"
#include "project.h"
#include "CSCIx229.h"

// phi= angle from +x in xy plane
void p2c(float phi, float * x, float *y){
	*x=Cos(phi);//*Cos(theta);
	*y=Sin(phi);//*Cos(theta);
	//*z=Sin(theta);
}

void s2c(float phi,float theta, float * x, float *y,float *z){
	*x = Cos(phi)*Sin(theta);
	*y = Sin(phi)*Sin(theta);
	*z =		  Cos(theta);
}

/*
 *  Draw planet
 */
void drawUnitSphere(int drawMode)
{
	int angleInc = DEFAULT_ANGLE_INC;
	glPushMatrix();
	if(drawMode == DRAW_XYCENTER){
		glTranslatef(0,0,1);
	} else if (drawMode == DRAW_ZERO_CORNER){
		glTranslatef(1,1,1);
	}
	
	
	float phi,theta;
	float x,y,z;
	for (theta=0+angleInc;theta<=180;theta+=angleInc)
   	{
      		glBegin(GL_QUAD_STRIP);
      		for (phi=0;phi<=360;phi+=angleInc)
      		{
			s2c(phi,theta-angleInc,&x,&y,&z);
			glNormal3f(x,y,z);
         		glVertex3f(x,y,z);
			s2c(phi,theta,&x,&y,&z);
			glNormal3f(x,y,z);
         		glVertex3f(x,y,z);
      		}
      		glEnd();
   	
	}
	glPopMatrix();
}
//
void drawUnitCylinder_Texture(int drawMode,unsigned int texture){
   glPushMatrix();
	if(drawMode == DRAW_CENTER){
		glTranslatef(0,0,-0.5);
	} else if (drawMode == DRAW_ZERO_CORNER){
		glTranslatef(1,1,0);
	}
	
	int angleInc = DEFAULT_ANGLE_INC;
	int phi=0;
	float x,y;
	//glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
	//
	if(texture>0){
	    glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D,texture);
    } else {
        glDisable(GL_TEXTURE_2D);
    }
	//
	glBegin(GL_TRIANGLE_FAN);
	
	glNormal3f(0,0,-1);
	glVertex3d(0,0,0);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glTexCoord2f(0.5+0.5*Cos(phi),0.5+0.5*Sin(phi));
		glVertex3d(x,y,0);
	}
	glEnd();
	//
	glBegin(GL_QUAD_STRIP);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glNormal3f(x,y,0);
        glTexCoord2f(phi/360.0,1);glVertex3d(x,y,1);
		glTexCoord2f(phi/360.0,0);glVertex3d(x,y,0);
	}
	glEnd();
	//
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(0,0,1);
	glVertex3d(0,0,1);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glTexCoord2f(0.5+0.5*Cos(phi),0.5+0.5*Sin(phi));
		glVertex3d(x,y,1);
	}
	glEnd();
	glPopMatrix();
    glDisable(GL_TEXTURE_2D);
}

void drawUnitCylinder(int drawMode){
	drawUnitCylinder_Texture(drawMode,0);
}

// cube with side=1 , 

void drawUnitCube(int drawMode){
	drawUnitCube_ColorTexture(drawMode, 0, NULL,0);
}
//
// draw cylinder with center in the base with r=1 , and h=1

void drawUnitCube_Color(int drawMode, float faceColor[6][3]){
	drawUnitCube_ColorTexture( drawMode,1, faceColor,0);
}
//
void drawUnitCube_Texture(int drawMode, unsigned int texture){
    drawUnitCube_ColorTexture( drawMode,FALSE,NULL, texture);
}
/*
	drawMode as in geometry.h
	isColored 0 OR 1 
	faceColor (NULL if 0) 
	texture ( 0 no texture ) (>0 texture)
*/
void drawUnitCube_ColorTexture(int drawMode, int isColored, float faceColor[6][3],unsigned int texture){
	glPushMatrix();
	glPushAttrib(GL_CURRENT_BIT);
	glScaled(0.5,0.5,0.5);
	if(drawMode == DRAW_XYCENTER){
		glTranslatef(0,0,1);
	} else if (drawMode == DRAW_ZERO_CORNER){
		glTranslated(1,1,1);
	}
	//  Cube
	if(texture>0) { 
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D,texture);
	}
	//
	glBegin(GL_QUADS);
	//  Front
	glNormal3f( 0, 0, 1);
	if(isColored) glColor3fv(faceColor[0]);
	else glColor3f(1,1,1);
	
   	if(texture>0) glTexCoord2f(0,0);
   	glVertex3f(-1,-1, 1);
   	if(texture>0) glTexCoord2f(0,1);glVertex3f(+1,-1, 1);
   	if(texture>0) glTexCoord2f(1,1);glVertex3f(+1,+1, 1);
   	if(texture>0) glTexCoord2f(1,0);glVertex3f(-1,+1, 1);
	//  Back
	if(isColored) glColor3fv(faceColor[1]);
	else glColor3f(1,1,1);
	
	glNormal3f( 0, 0,-1);
  	if(texture>0) glTexCoord2f(0,0);glVertex3f(+1,-1,-1);
   	if(texture>0) glTexCoord2f(0,1);glVertex3f(-1,-1,-1);
   	if(texture>0) glTexCoord2f(1,1);glVertex3f(-1,+1,-1);
  	if(texture>0) glTexCoord2f(1,0);glVertex3f(+1,+1,-1);
	//  Right
	if(isColored) glColor3fv(faceColor[2]);
	else glColor3f(1,1,1);
	
	glNormal3f(+1, 0, 0);
   	if(texture>0) glTexCoord2f(0,0);glVertex3f(+1,-1,+1);
   	if(texture>0) glTexCoord2f(0,1);glVertex3f(+1,-1,-1);
  	if(texture>0) glTexCoord2f(1,1);glVertex3f(+1,+1,-1);
  	if(texture>0) glTexCoord2f(1,0);glVertex3f(+1,+1,+1);
	//  Left
	if(isColored) glColor3fv(faceColor[3]);
	else glColor3f(1,1,1);
	
	glNormal3f(-1, 0, 0);
   	if(texture>0) glTexCoord2f(0,0);glVertex3f(-1,-1,-1);
   	if(texture>0) glTexCoord2f(0,1);glVertex3f(-1,-1,+1);
  	if(texture>0) glTexCoord2f(1,1);glVertex3f(-1,+1,+1);
 	if(texture>0) glTexCoord2f(1,0);glVertex3f(-1,+1,-1);
	//  Top
	if(isColored) glColor3fv(faceColor[4]);
	else glColor3f(1,1,1);
	//
	glNormal3f( 0,+1, 0);
	if(texture>0) glTexCoord2f(0,1);glVertex3f(-1,+1,+1);
  	if(texture>0) glTexCoord2f(1,1);glVertex3f(+1,+1,+1);
  	if(texture>0) glTexCoord2f(1,0);glVertex3f(+1,+1,-1);
  	if(texture>0) glTexCoord2f(0,0);glVertex3f(-1,+1,-1);
	//  Bottom
	if(isColored) glColor3fv(faceColor[5]);
	else glColor3f(1,1,1);
	//
	glNormal3f( 0,-1, 0);
  	if(texture>0) glTexCoord2f(0,1);glVertex3f(-1,-1,-1);
  	if(texture>0) glTexCoord2f(1,1);glVertex3f(+1,-1,-1);
  	if(texture>0) glTexCoord2f(1,0);glVertex3f(+1,-1,+1);
  	if(texture>0) glTexCoord2f(0,0);glVertex3f(-1,-1,+1);
	//  End
	glEnd();
	//  Undo transofrm
	glPopAttrib();
	glPopMatrix();
	if(texture>0) glDisable(GL_TEXTURE_2D);
	
}
