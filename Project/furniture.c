#include "geometry.h"
#include "project.h"
#include "CSCIx229.h"

static unsigned int woodTex;
static void legs(float width0 , float width1,float height, float thickness){
    glPushMatrix();
    glScaled(thickness,thickness,height);
    drawUnitCube_Texture(DRAW_XYCENTER,woodTex);
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(width0,0,0);
    glScaled(thickness,thickness,height);
    drawUnitCube_Texture(DRAW_XYCENTER,woodTex);
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0,width1,0);
    glScaled(thickness,thickness,height);
    drawUnitCube_Texture(DRAW_XYCENTER,woodTex);
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(width0,width1,0);
    glScaled(thickness,thickness,height);
    drawUnitCube_Texture(DRAW_XYCENTER,woodTex);
    glPopMatrix();
}

static void drawRectangleTable_Exact(float width0,float width1,float height,float thickness,float topToLegOffset){
    //legs(0.5,1.0,0.7,0.04);
    glPushMatrix();
    glTranslated(topToLegOffset,topToLegOffset,0);
    legs(width0-2*topToLegOffset,width1-2*topToLegOffset,height,thickness);
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0,0,height);
    glScaled(width0,width1,thickness);
    drawUnitCube_Texture(DRAW_ZERO_CORNER,woodTex);
    glPopMatrix();
}

static void drawOfficeDisk_Exact(float width0 , float width1, float height, float legThickness , float percentageOfWidthOfDrawers){
    glPushMatrix();
    glTranslated((1-percentageOfWidthOfDrawers)*width0,0,0);
    glScaled(percentageOfWidthOfDrawers*width0,width1,height);
    drawUnitCube_Texture(DRAW_ZERO_CORNER,woodTex);
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0,width1-legThickness,0);
    glScaled(legThickness,legThickness,height);
    drawUnitCube_Texture(DRAW_ZERO_CORNER,woodTex);
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0,0,0);
    glScaled(legThickness,legThickness,height);
    drawUnitCube_Texture(DRAW_ZERO_CORNER,woodTex);
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0,0,height);
    glScaled(width0,width1,legThickness);
    drawUnitCube_Texture(DRAW_ZERO_CORNER,woodTex);
    glPopMatrix();
}
static void drawRoundTable_Exact(float radius,float height,float thickness,float basePercentage,float legRadius){
    glPushMatrix();
    glTranslated(0,0,height);
    glScaled(radius,radius,thickness);
    drawUnitCylinder_Texture(DRAW_XYCENTER,woodTex);
    glPopMatrix();
    //
    glPushMatrix();
    glScaled(radius*basePercentage,radius*basePercentage,thickness*0.7);
    drawUnitCylinder_Texture(DRAW_XYCENTER,woodTex);
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0,0,thickness*0.3);
    glScaled(legRadius,legRadius,height);
    drawUnitCylinder_Texture(DRAW_XYCENTER,woodTex);
    glPopMatrix();
}
static void drawChair_Exact(float baseSide, float height, float thickness, float legOffset, float backHeight){
	 glPushMatrix();
	 drawRectangleTable_Exact(baseSide,baseSide, height, thickness, legOffset);
	 glTranslated(0,0,height+thickness-0.01);//0.01 is for back to get inside base
	 glScaled(thickness,baseSide,backHeight);
	 drawUnitCube_Texture(DRAW_ZERO_CORNER,woodTex);
	 glPopMatrix();
}
static void drawBookShelf_Exact(float width0 , float width1,float height, float thickness,int haveTop){
	// Base
	glPushMatrix();
	glScaled(width0,width1,thickness);
	drawUnitCube_Texture(DRAW_ZERO_CORNER,woodTex);
	glPopMatrix();
	// top
	if(haveTop) {
		glPushMatrix();
		glTranslated(0,0,height);
		glScaled(width0,width1,thickness);
		drawUnitCube_Texture(DRAW_ZERO_CORNER,woodTex);
		glPopMatrix();
	}
	//right side
	glPushMatrix();
	glScaled(thickness,width1,height);
	drawUnitCube_Texture(DRAW_ZERO_CORNER,woodTex);
	glPopMatrix();
	//left side
	glPushMatrix();
	glTranslated(width0-thickness,0,0);
	glScaled(thickness,width1,height);
	drawUnitCube_Texture(DRAW_ZERO_CORNER,woodTex);
	glPopMatrix();
	//back
	glPushMatrix();
	glTranslated(0,width1-thickness,0);
	glScaled(width0,thickness,height);
	drawUnitCube_Texture(DRAW_ZERO_CORNER,woodTex);
	glPopMatrix();
}
static void drawBookShelfStack(int number ,float width0 , float width1,float height, float thickness){
	int i =0 ;
	for( i = 0 ; i<number ; i++){
		glTranslated(0,0,(i==0?0:height));
		drawBookShelf_Exact(width0 ,  width1, height,  thickness,(number-i==1?1:0));
	}
}
//
void drawReadingTable(){
    woodTex = getTextureId(DARK_WOOD_TEX);
    
    drawRectangleTable_Exact(2,3,1,0.1,0.2);
}
//
void drawDesk() {
    woodTex = getTextureId(DARK_WOOD_TEX);
    drawOfficeDisk_Exact(1.5,0.7,1,0.05,0.4);
    //drawOfficeDisk_Exact(float width0 , float width1, float height, float legThickness , float percentageOfWidthOfDrawers){
}
//
void drawRoundTable() {
    woodTex = getTextureId(DARK_WOOD_TEX);
    //(float radius,float height,float thickness,float basePercentage,float legRadius){
    drawRoundTable_Exact(0.3,0.5,0.05,0.5,0.03);
    //drawUnitCyliner_Texture(DRAW_XYCENTER,woodTex);
}
//
void drawChair() {
// TODO draw different chars depending on given argument 1,2,3...
	woodTex = getTextureId(DARK_WOOD_TEX);
	drawChair_Exact(0.5, 0.6, 0.07, 0.05,0.5);
}
//
void drawBookShelves(){
woodTex = getTextureId(DARK_WOOD_TEX);
glPushMatrix();
glRotated(-90,0,0,1);
glScaled(-1,1,1);
drawBookShelfStack(5, 1,0.3,0.5,0.04);// num, width0 , float width1,float height,   float thickness)
    glPopMatrix();
}
void drawPainting(float widthToHeight, unsigned int texture){
    // DAVINCI 512/234
    // GAMMA 512/330
    // DOOR 256/581
    glPushMatrix();
    glTranslated(widthToHeight,0,0);
    glScaled(-widthToHeight,0.01,1);
    drawUnitCube_Texture(DRAW_ZERO_CORNER,texture);
    glPopMatrix();
}
void drawLamp(){
    setEmissionIfnoLight();
    glPushMatrix();
    glTranslated(0,0,1.5);
    glScaled(0.2,0.2,0.2);
    glRotated(90,0,1,0);
    drawLampHead();
    glPopMatrix();
    //
    glPushMatrix();
    glScaled(0.02,0.02,1.5-0.01);
    drawUnitCylinder_Texture(DRAW_XYCENTER,getTextureId(BLACK_TILE_TEX));
    glPopMatrix();
    glPushMatrix();
    glScaled(0.2,0.2,0.04);
    drawUnitCylinder_Texture(DRAW_XYCENTER,getTextureId(BLACK_TILE_TEX));
    glPopMatrix();
}
