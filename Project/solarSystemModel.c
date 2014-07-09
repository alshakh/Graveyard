#include "project.h"
#include "geometry.h"
#include "CSCIx229.h"
#define TWOPI (2*M_PI)

 typedef struct
{
   char* ball;
   char* ring;
   unsigned int balltex;
   unsigned int ringtex;
}  Planet;
static const int N=7;
static Planet planet[] = {

//   Name      Texture      RingTexture            
   {"images/sun.bmp"    ,0                ,0,0},
   {"images/mercury.bmp",0                ,0,0},
   {"images/venus.bmp"  ,0                ,0,0},
   {"images/earth.bmp"  ,0                ,0,0},
   {"images/mars.bmp"   ,0                ,0,0},
   {"images/jupiter.bmp",0                ,0,0},
   {"images/saturn.bmp" ,"images/saturnrings.bmp",0,0},
   };


void loadSolarSystemInit(){
	int k;
	for (k=0;k<N;k++)
   	{
      		planet[k].balltex = LoadTexBMP(planet[k].ball);
      		planet[k].ringtex = planet[k].ring !=0? LoadTexBMP(planet[k].ring) : 0;
   	}
}
static void Vertex(int th,int ph)
{
   double x = -Sin(th)*Cos(ph);
   double y =  Cos(th)*Cos(ph);
   double z =          Sin(ph);
   glNormal3d(x,y,z);
   glTexCoord2d(th/360.0,ph/180.0+0.5);
   glVertex3d(x,y,z);
}

static void drawPlanet(int n)
{
   int th,ph;

   /*
    *  Draw surface of the planet
    */
   //  Set texture
   glEnable(GL_TEXTURE_2D);
   glBindTexture(GL_TEXTURE_2D,planet[n].balltex);
   //  Latitude bands
   glColor3f(1,1,1);
   for (ph=-90;ph<90;ph+=5)
   {
      glBegin(GL_QUAD_STRIP);
      for (th=0;th<=360;th+=5)
      {
         Vertex(th,ph);
         Vertex(th,ph+5);
      }
      glEnd();
   }

   /*
    *  Draw rings for planets that have them
    *  We naively assume the ring width equals the radius
    */
   if (planet[n].ring)
   {
      int k;
      //  Make rings transparent grey (if enabled)
      glEnable(GL_BLEND);
      glBlendFunc(GL_ONE,GL_ONE_MINUS_SRC_COLOR);
      glColor3f(0.5,0.5,0.5);
      //  Set ring texture
      glBindTexture(GL_TEXTURE_2D,planet[n].ringtex);
      //  Draw ring plane
      glBegin(GL_QUAD_STRIP);
      for (k=0;k<=360;k+=2)
      {
         glTexCoord2f(1,0);glVertex2d(1.0*Cos(k),1.0*Sin(k));
         glTexCoord2f(0,0);glVertex2d(2.2*Cos(k),2.2*Sin(k));
      }
      glEnd();
      glDisable(GL_BLEND);
   }
   glDisable(GL_TEXTURE_2D);
}

void drawSolarSystemModel(float timeFactor){
	srand(845523637);
	glPushMatrix();
	glTranslated(0,0,-1);
	//
	glPushMatrix();
	glTranslated(0,0,-1);
	glRotated(timeFactor*360,0,0,1);
	drawPlanet(0);
	glPopMatrix();
	glPushMatrix();
	glScaled(0.15,0.15,1);
	drawUnitCylinder_Texture(DRAW_XYCENTER,getTextureId(BLACK_TILE_TEX ));
	glPopMatrix();
	//
	int i;
	for(i=1;i<N;i++){
		glPushMatrix();
		glRotated(rand()%360+(timeFactor*360),0,0,1);
		glPushMatrix();
		glTranslated(0,0,0.15*i);
		glRotated(90,0,1,0);
		glScaled(0.05,0.05,0.5+3*i/2.0+0.1);
		drawUnitCylinder_Texture(DRAW_XYCENTER,getTextureId(BLACK_TILE_TEX));
		glPopMatrix();
		//
		glPushMatrix();
		glTranslated(0.5+3*i/2.0+0.1,0,0);
		glScaled(0.05,0.05,0.15*i+0.025);
		drawUnitCylinder_Texture(DRAW_XYCENTER,getTextureId(BLACK_TILE_TEX));
		glPopMatrix();
		//
		//
		glPushMatrix();
		glTranslated(0.5+3*i/2.0+0.1,0,-0.5);
		glScaled(0.5,0.5,0.5);
		drawPlanet(i);
		glPopMatrix();
		//
		glPopMatrix();
	}
	
	glPopMatrix();
}
