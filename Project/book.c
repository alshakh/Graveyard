#include "CSCIx229.h"
#include "project.h"
#include <math.h>
//
//#define PAGE_CONSTANT 1/6.75
#define PAGE_CONSTANT 1/10.5
static float increment = 0.1;
//
//
//
static void bookHalf(Book book,int whichHalf,float timeFactor){
	
    // The normals are not exact when changing timeFactor but very acceptable
    glPushMatrix();
    float xMAX=3; // limits of equation calculation
	float yMAX =1;// limits of equation calculation
	float halfThickness=book.thickness/2;
	//
	glRotated(-90*(1-timeFactor),0,1,0);
	glTranslated(0,0,-halfThickness*(1-timeFactor));
	glScaled(book.width/xMAX,book.height,1);
	//
    glEnable(GL_TEXTURE_2D);
    //
   // float halfThickness=book.thickness/2;
    float x1,y;
	float x0;
    //
	float x1V,x0V;
	glPushMatrix();
	if(timeFactor!=0){ // if book is completely closed DO NOT DRAW PAGES
	    glTranslated(0,0,halfThickness);
	    glBindTexture(GL_TEXTURE_2D,book.pages);
	    for(x1=increment;x1<=xMAX+increment;x1+=increment){
		    if(x1>xMAX) x1=xMAX;
		    x0 = x1-increment;
		    float normal_x1 = -1/((exp(-1*x1)*(1-2*x1))/(2*sqrt(x1)));
		    float normal_x0 = -1/((exp(-1*x0)*(1-2*x0))/(2*sqrt(x0)));
		    //
		    glBegin(GL_QUAD_STRIP);
		    //
		    x0V = (sqrt(x0)/exp(x0))*PAGE_CONSTANT*timeFactor;
		    x1V =(sqrt(x1)/exp(x1))*PAGE_CONSTANT*timeFactor;
		    //
		    for(y=0;y<=yMAX+increment;y+=increment){
			    if(y>yMAX) y=yMAX;
			
			    glNormal3f(1/normal_x0,0,1);
			    if(whichHalf==0)  glTexCoord2f(0.5+1-(x0/xMAX)/2,(y/yMAX));
			    else if (whichHalf==1)  glTexCoord2f(0.5+(x0/xMAX)/2,(y/yMAX));
			    glVertex3d(x0,y,x0V);
			
			    glNormal3f(1/normal_x1,0,1);
			    if(whichHalf==0)  glTexCoord2f(0.5+1-(x1/xMAX)/2,(y/yMAX));
			    else if (whichHalf==1)  glTexCoord2f(0.5+(x1/xMAX)/2,(y/yMAX));
			    glVertex3d(x1,y,x1V);
			    if(y==yMAX) break;
		    }
		    glEnd();
		    if(x1==xMAX) break;
	    }
	}
    glPopMatrix();
    //
    // Edges //////////
    ///////////////////
    glPushMatrix();
    //
    
    glBindTexture(GL_TEXTURE_2D,book.pagesEdge);
    //
    int normalCount;
    for(normalCount=-1; normalCount < 2 ; normalCount +=2){
	    for(x1=increment;x1<=xMAX+increment;x1+=increment){
		    if(x1>xMAX) x1=xMAX;
		    x0 = x1-increment;
		    //
		    glBegin(GL_QUAD_STRIP);
		    //
		    x0V = (sqrt(x0)/exp(x0))*PAGE_CONSTANT*timeFactor+halfThickness;
		    x1V =(sqrt(x1)/exp(x1))*PAGE_CONSTANT*timeFactor+halfThickness;
		    //
		    glNormal3f(0,normalCount,0);
		    //
		    glTexCoord2f(x0/xMAX,1);glVertex3d(x0,(normalCount==-1?0:yMAX),x0V);
		    glTexCoord2f(x0/xMAX,0);glVertex3d(x0,(normalCount==-1?0:yMAX),0);
		    //
		    glTexCoord2f(x1/xMAX,1);glVertex3d(x1,(normalCount==-1?0:yMAX),x1V);
		    glTexCoord2f(x1/xMAX,0);glVertex3d(x1,(normalCount==-1?0:yMAX),0);
		    //
		    glEnd();
		    if(x1==xMAX) break;
	    }
	}
    glPopMatrix();
    
    // outer edge
    glBindTexture(GL_TEXTURE_2D,book.pagesEdge);
    glBegin(GL_QUADS);
    glNormal3f(1,0,0);
    glTexCoord2f(0,0); glVertex3f(xMAX,0,0);
    glTexCoord2f(0,1); glVertex3f(xMAX,0 ,(sqrt(xMAX)/exp(xMAX))*PAGE_CONSTANT*timeFactor+halfThickness);
    glTexCoord2f(1,1); glVertex3f(xMAX,yMAX ,(sqrt(xMAX)/exp(xMAX))*PAGE_CONSTANT*timeFactor+halfThickness);
    glTexCoord2f(1,0); glVertex3f(xMAX,yMAX ,0);
    glEnd();
    
    // inner edge
    glBindTexture(GL_TEXTURE_2D,book.cover);
    glBegin(GL_QUADS);
    glNormal3f(-1,0,0);
    //
    if(whichHalf==0) 
        glTexCoord2f(book.l1,0);
    else if(whichHalf==1) 
        glTexCoord2f(book.l2,0);
    glVertex3f(0,0,0);
    //
    
    if(whichHalf==0) 
        glTexCoord2f((book.l1+book.l2)/2,0);
    else if(whichHalf==1) 
        glTexCoord2f((book.l1+book.l2)/2,0);
       
    glVertex3f(0,0,halfThickness);
    //
    if(whichHalf==0) 
        glTexCoord2f((book.l2+book.l1)/2,1);
    else if(whichHalf==1) 
        glTexCoord2f((book.l2+book.l1)/2,1);
    glVertex3f(0,yMAX,halfThickness);
    //
    
    if(whichHalf==0) 
        glTexCoord2f(book.l1,1);
    else if(whichHalf==1) 
        glTexCoord2f(book.l2,1);
    glVertex3f(0,yMAX,0);
    //
    glEnd();
    // 
    glBindTexture(GL_TEXTURE_2D,book.cover);
    //////
    if(whichHalf==0){
    glBegin(GL_QUADS);
    glNormal3f(0,0,-1);
    glTexCoord2f(book.l1+(book.l2-book.l1)/2*timeFactor,0);glVertex3f(0,0,0);
    glTexCoord2f(0,0);glVertex3f(xMAX,0,0);
    glTexCoord2f(0,1);glVertex3f(xMAX,yMAX,0);
    glTexCoord2f(book.l1+(book.l2-book.l1)/2*timeFactor,1);glVertex3f(0,yMAX,0);
    glEnd();
   }else if(whichHalf==1){
    glBegin(GL_QUADS);
    glNormal3f(0,0,-1);
    glTexCoord2f(book.l2-(book.l2-book.l1)/2*timeFactor,0);glVertex3f(0,0,0);
    glTexCoord2f(1,0);glVertex3f(xMAX,0,0);
    glTexCoord2f(1,1);glVertex3f(xMAX,yMAX,0);
    glTexCoord2f(book.l2-(book.l2-book.l1)/2*timeFactor,1);glVertex3f(0,yMAX,0);
    glEnd();
   } 
    /////// 
    glDisable(GL_TEXTURE_2D);
    //
    glPopMatrix();
}
void bookStakByOneTexture(){
	float halfPoint = (1-131.0/383.0);
    unsigned int tex = getTextureId(BOOKSPINES_TEX);
    glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D,tex);
    glNormal3f(0,-1,0);
    glBegin(GL_QUADS);
    glTexCoord2f(0,0); glVertex3f(0,0,0);
    glTexCoord2f(1,0); glVertex3f(1,0,0);
    glTexCoord2f(1,halfPoint); glVertex3f(1,0,1);
    glTexCoord2f(0,halfPoint); glVertex3f(0,0,1);
    //
    glNormal3f(0,0,1);
    glTexCoord2f(0,halfPoint); glVertex3f(0,0,1);
    glTexCoord2f(1,halfPoint); glVertex3f(1,0,1);
    glTexCoord2f(1,1); glVertex3f(1,1,1);
    glTexCoord2f(0,1); glVertex3f(0,1,1);
    glEnd();
    
    glDisable(GL_TEXTURE_2D);
}
void drawBook(Book book, float timeFactor){
//
	timeFactor*=2;
    //
    if(timeFactor>1.0){
    	timeFactor=1-(timeFactor-1);
    } else {
    	timeFactor = timeFactor;
    }
    //
    glPushMatrix();
    
    bookHalf(book,1,timeFactor);
    glScaled(-1,1,1);
    bookHalf(book,0,timeFactor);
    glPopMatrix();
}

