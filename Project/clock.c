#include "geometry.h"
#include "project.h"
#include "CSCIx229.h"



static unsigned int faceTex;
static unsigned int blackTileTex;

static void cylinder(){
    glEnable(GL_TEXTURE_2D);
    
    int angleInc = 5;
	int phi=0;
	float x,y;
	//glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
    glBindTexture(GL_TEXTURE_2D,faceTex);
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(0,0,-1);
	glTexCoord2f(0.5,0.5);
	glVertex3d(0,0,0);
	glColor3f(1,1,1);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		// glTexCoord2f(0.5*Cos(phi)+0.5,0.5*Sin(phi)+0.5);
        // glVertex3f(Cos(phi),Sin(phi),0);
		glTexCoord2f(0.5*Cos(phi)+0.5,0.5*Sin(phi)+0.5);
		glVertex3d(y,x,0);
	}
	glEnd();
	//
	glBindTexture(GL_TEXTURE_2D,blackTileTex);
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

void drawClock(float timeFactor){
    blackTileTex =getTextureId(BLACK_TILE_TEX);
    faceTex =getTextureId(CLOCK_FACE_TEX);
    glPushMatrix();
    glTranslated(0,1,0);
    //
    
    //
    timeFactor*=20; // speed by 20 multiple
    timeFactor = fmod(timeFactor,1);
    //
    float angle = 20;
    int sign = 1;
    float initPos=0; 
    if(timeFactor<0.25){
    	sign=1;
    	initPos=0;
    } else if (timeFactor>=0.25 && timeFactor<0.5){
    	timeFactor-=0.25;
    	sign=-1;
    	initPos=angle;
    }else if (timeFactor>=0.5 && timeFactor<0.75){
    	timeFactor-=0.5;
    	sign=-1;
    	initPos=0;
    }else if (timeFactor>=0.75 && timeFactor<=1){
    	timeFactor-=0.75;
    	sign=1;
    	initPos=-angle;
    }
    //
    glRotated(sign*timeFactor*angle*4,1,0,0);
    glRotated(initPos,1,0,0);
    //
    glTranslated(1,1,0);
    glRotated(-180,0,0,1);
        //
    glPushMatrix();
    glTranslated(1,1,1);
    glRotated(-90,0,1,0);
    cylinder();
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(-0.01,0,0);
    glScaled(0.9,2,1);
    drawUnitCube_Texture(DRAW_ZERO_CORNER,blackTileTex);
    glPopMatrix();
    //
    glPopMatrix();
}
