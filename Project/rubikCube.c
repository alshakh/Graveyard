#include "geometry.h"
#include "project.h"
#include "CSCIx229.h"

static unsigned int texId;
static void getRandomColorFace(float *a);
static float colors[6][3]= {
			{1,1,1},
			{1,0,0},
			{0,1,0},
			{0,0,1},
			{1,1,0},
			{1,0.5,0}
			};

static void drawSubCube(){
	float faceColor[6][3];
	int i=0;
	for (i=0 ; i<6; i++){
		getRandomColorFace(faceColor[i]);
	}
	drawUnitCube_ColorTexture(DRAW_ZERO_CORNER,TRUE, faceColor,texId);
}
static void getRandomColorFace(float *a){
	int i = rand()%6;
	a[0]=colors[i][0];
	a[1]=colors[i][1];
	a[2]=colors[i][2];
}
//

// Two good seeds 8421 , 8800
void drawRubikCube_seed(float timeFactor,int seed){
    srand(seed);
	texId = getTextureId(RUBIK_CUBE_TEX);
	int i,j,k;
	glPushMatrix();
	glScaled(1.0/3,1.0/3,1.0/3);
	for(k=0; k<3 ; k++){
	    glPushMatrix();
		glRotated((k==2?2:-1)*((k+1)%2)*360*timeFactor,0,0,1);
		for (i = 0 ; i<3 ; i++){
			for(j=0; j<3 ; j++){
				glPushMatrix();
				glTranslatef(i-1.5,j-1.5,k);
				drawSubCube();
				glPopMatrix();
			}
		}
		glPopMatrix();
	}
	glPopMatrix();
}
void drawRubikCube(float timeFactor){
	srand(8800);
	drawRubikCube_seed(timeFactor,8800);
}
//

