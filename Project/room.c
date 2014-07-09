#include "geometry.h"
#include "project.h"
#include "CSCIx229.h"


#define WIDTH0 7
#define WIDTH1 5
#define HEIGHT 3

static void translateToShelf(int leftShelf,int upShelf){
	glTranslated(0,leftShelf+0.035,upShelf*0.5+0.04);

}
static void drawBookStack_slanted(int numBooks,float theta,int seed){
    glPushMatrix();
    glScaled(0.12,0.12,0.12);
    glScaled(10,10,10);
    srand(seed);
    glTranslated((getBook(rand()%getSizeOfBookList()).height)*Cos(90.0-theta),0,0);
    //
    float factor = 1/Cos(180-90-(90-theta));
    srand(seed);
    int i = 0 ;
    //numBooks=10;
    for (i=0 ; i<numBooks ; i ++ ){
        int r = rand()%(getSizeOfBookList());
        //glTranslated(0.05,0,0);
        glPushMatrix();
        glRotated(theta,0,0,1);
        glTranslated(getBook(r).thickness/2,0,0);
        drawBook(getBook(r),0);
        glPopMatrix();
        glTranslated(getBook(r).thickness*factor,0,0);
    }
    glPopMatrix();
}
static void drawBookStack_flat(int numBooks,int seed,int openBookOnTop,int range,float timeFactorOfOpenBook){
    glPushMatrix();
    srand(seed);
    int i;
    for(i=0; i<numBooks;i++){
        int r = rand()%(getSizeOfBookList());
        glPushMatrix();
        //
         glTranslated(getBook(r).width/2,-getBook(r).height/2,0);
        glRotated((rand()%range),0,0,1);
        glTranslated(-1*getBook(r).width/2,-1*getBook(r).height/2,0);
        //
        glTranslated(0,0,getBook(r).thickness/2);
        glRotated(90,0,1,0);
        drawBook(getBook(r),0);
        glPopMatrix();
        glTranslated(0,0,getBook(r).thickness);
    }
    if(openBookOnTop){
    int  r = rand()%(getSizeOfBookList());
    glTranslated(getBook(r).width/2,-1*getBook(r).height,0);
        drawBook(getBook(r),timeFactorOfOpenBook);
    }
    glPopMatrix();
}
static void drawDesk_ROOM()
{
    float heightOfTop = 1+0.05; // XXX came from furniture.c
    glPushMatrix();
    glTranslated(0,0.4,0);
    drawDesk();
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0.7,0,0);
    glRotated(90,0,0,1);
    drawChair();
    glPopMatrix();
    // draw things on table
    glPushMatrix();
    glTranslated(0.3,0.6+0.3,heightOfTop);
    glScaled(0.1,0.1,0.1);
    drawRubikCube_seed(getTimeFactor(RUBIKCUBE_ON_DESK_ID),8421);
    glColor3f(1,1,1);
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(1,1,heightOfTop);
    drawBookStack_flat(6,25482,0,50,0);
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0.2,0.7,heightOfTop);
    drawBookStack_flat(1,2582,1,50,fabs(getTimeFactor(OPEN_BOOK_ON_DESK_ID)-0.5));
    glPopMatrix();
}
static void drawReadingTable_ROOM(){
    glPushMatrix();
    glPushMatrix();
    glTranslated(1,0,0);
    glRotated(90,0,0,1);
    drawChair();
    glPopMatrix();
    glTranslated(0,0.3,0);
    drawReadingTable();
    //
    glTranslated(0,3,0);
    glScaled(1,-1,1);
    glPushMatrix();
    glTranslated(1.5,-0.2,0);
    glRotated(90,0,0,1);
    drawChair();
    glPopMatrix();
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(1,1,1.1);
    drawBookStack_flat(4,1254863,1,360,0.5);
    glTranslated(0.5,0.5,0);
    drawBookStack_flat(2,12563,0,360,0);
    glTranslated(0.2,0.9,0);
    drawBookStack_flat(5,12563,1,360,0.5);
    glTranslated(-0.3,0.9,0);
    drawBookStack_flat(2,54863,0,360,0);
    glTranslated(-0.5,-1.2,0);
    drawBookStack_flat(15,54863,0,360,0);
    glTranslated(-0.7,0,0);
    drawBookStack_flat(3,12563,1,360,fabs(getTimeFactor(OPEN_BOOK_ON_TABLE_ID)-0.5));
    glPopMatrix();
}

static void drawBookStack_inShelf(int leftShelf,int upShelf){
    glPushMatrix();
    //glTranslated(0,leftShelft,-2*0.5); // one shelf to left and one down
    translateToShelf( leftShelf, upShelf);
    glRotated(-90,0,0,1);
    glTranslated(-1+0.07,0,0);
    glScaled(1-0.07,0.3,0.4);
    bookStakByOneTexture();
    glPopMatrix();
}

static void drawBookShelves_ROOM() {
    
    //
    glPushMatrix();
    drawBookShelves();
    glTranslated(0,1,0);
    drawBookShelves();
    glPopMatrix();
    //
    drawBookStack_inShelf(1,0);
    drawBookStack_inShelf(0,4);
    drawBookStack_inShelf(0,1);
    drawBookStack_inShelf(1,2);
    // Clock
    glPushMatrix();
    translateToShelf(0,3);
    glTranslated(0,0.2,0);
    glScaled(0.22,0.22,0.22);
    drawClock(getTimeFactor(CLOCK_ID));
    glPopMatrix();
    //Books Slanted
    glPushMatrix();
    glTranslated(0,0.85,0);
    translateToShelf(1,3);
    glRotated(90,0,1,0);
    glRotated(90,0,0,1);
    drawBookStack_slanted(3,-25,81752343);
    glPopMatrix();
    //BooksSlanted
    glPushMatrix();
    translateToShelf(1,3);

    glRotated(90,0,1,0);
    glRotated(90,0,0,1);
    drawBookStack_slanted(10,30,81752343);
    glPopMatrix();
    //RubikCube
    glPushMatrix();
    translateToShelf(0,2);
    glTranslated(-0.1,0.2,0.12);
    glScaled(0.15,0.15,0.15);
    glRotated(45,0,1,0);
    glRotated(45,1,0,0);
    
    glTranslated(1,1,0);
    drawRubikCube(getTimeFactor(RUBIKCUBE_ON_SHELF_ID));
    glColor3f(1,1,1);
    glPopMatrix();
    // flat books
    glPushMatrix();
    translateToShelf(0,0);
    glTranslated(0,0.5,0);
    drawBookStack_flat(6,25482,0,50,0);
    glRotated(90,0,1,0);
    glRotated(90,0,0,1);
    drawBookStack_slanted(5,30,817523);
    glPopMatrix();
    //
    glPushMatrix();
    translateToShelf(0,2);
    //glTranslated(1,0.16,1.5);
    glTranslated(0,0.40,0);
    glRotated(90,0,0,1);
    glRotated(20,1,0,0);
    glScaled(0.3,1,0.3);
    drawPainting(512/330.0, getTextureId(GAMMA_TEX));
    glPopMatrix();
    
    glPushMatrix();
    translateToShelf(1,4);
    //glTranslated(1,0.16,1.5);
    glTranslated(0.04,0.1,0.18);
    glScaled(0.1,0.1,0.1);
    glRotated(-15,1,0,1);
    glRotated(90,0,0,1);
    drawTrumpet();
    glPopMatrix();
    //
    glPushMatrix();
    translateToShelf(1,1);
    glTranslated(0.1,0.2,0);
    glTranslated(0.04,0.1,0);
    glScaled(0.1,0.1,0.1);
    //glRotated(-15,1,0,1);
    glRotated(90,0,1,0);
    glScaled(-1,1,1);
    drawVaze();
    glPopMatrix();
    
    glPushMatrix();
    translateToShelf(1,1);
    glTranslated(0.1,0.55,0);
    glTranslated(0.04,0.1,0);
    glScaled(0.1,0.1,0.1);
    //glRotated(-15,1,0,1);
    glRotated(90,0,1,0);
    glScaled(-1,1,1);
    drawVaze2();
    glPopMatrix();
   //
   glPushMatrix();
    translateToShelf(0,5);
    glTranslated(0,0.5,0);
    drawBookStack_flat(6,25445782,0,50,0);
    glRotated(90,0,1,0);
    glRotated(90,0,0,1);
    drawBookStack_slanted(10,30,815487523);
    glPopMatrix();
     
}
//
static void drawWalls(float width0,float width1, float height){
    float thickness=0.1;
    glPushMatrix();
    glScaled(width0,thickness,height);
    drawUnitCube_Texture(DRAW_ZERO_CORNER,getTextureId(WALL_TEX));
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0,width1,0);
    glScaled(width0,thickness,height);
    drawUnitCube_Texture(DRAW_ZERO_CORNER,getTextureId(WALL_TEX));
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(width0,0,0);
    glScaled(thickness,width1,height);
    drawUnitCube_Texture(DRAW_ZERO_CORNER,getTextureId(WALL_TEX));
    glPopMatrix();
    //
    glPushMatrix();
    glScaled(thickness,width1,height);
     drawUnitCube_Texture(DRAW_ZERO_CORNER,getTextureId(WALL_TEX));
    glPopMatrix();
    //
    float dark[3] = {0.8,0.8,0.8};
    float color[6][3];
    int i,j;
    for (i=0;i<6;i++)
    	for(j=0;j<3;j++)
    	color[i][j] = dark[j];
    //
    glPushMatrix();
    glTranslated(0,0,-thickness);
    glScaled(width0,width1,thickness);
     drawUnitCube_ColorTexture(DRAW_ZERO_CORNER,TRUE,color,getTextureId(WALL_TEX));
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0,0,height);
    glScaled(width0,width1,thickness);
    drawUnitCube_ColorTexture(DRAW_ZERO_CORNER,TRUE,color,getTextureId(WALL_TEX));
    glPopMatrix();
    //
    glPushMatrix();
    // DOOR 256/581
    glTranslated(thickness+0.01,2.3,0);
    glScaled(2.2,2.2,2.2);
    glRotated(90,0,0,1);
    drawPainting(256/581.0,getTextureId(DOOR_TEX));
    glPopMatrix();
}
static void drawSmallTableWithIss(){
    glPushMatrix();
    drawRoundTable();
    glTranslated(0,0,0.5+0.03);
    drawIss(getTimeFactor(ISS_MODEL_ID));
    glPopMatrix();
}
void drawRoom(){
 
//
 //drawBookShelves_ROOM();
 //
    //return;
    
    
   //drawArt(512/234.0, getTextureId(DAVINCI_TEX));
    //drawClock();
    drawWalls(WIDTH0,WIDTH1,HEIGHT);
    
    glPushMatrix();
    glTranslated(1,0.4,0);
    drawDesk_ROOM() ;
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(6.5,0.2,0);
    drawBookShelves_ROOM();
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(6.5,2.5,0);
    glRotated(90,0,0,1);
    drawReadingTable_ROOM();
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0.8,4.3,0);
    glRotated(-45,0,0,1);
    drawSmallTableWithIss();
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(1,0.16,1.5);
     drawPainting(512/234.0, getTextureId(DAVINCI_TEX));
    // DAVINCI 512/234
    // GAMMA 512/330
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(4,5-0.02,1);
    glScaled(1.5,-1.5,1.5);
     drawPainting(1, getTextureId(WINDOW_TEX));
    // DAVINCI 512/234
    // GAMMA 512/330
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(WIDTH0/2.0,WIDTH1/2.0,HEIGHT);
    glScaled(0.2,0.2,0.2);
    drawSolarSystemModel(getTimeFactor(SOLAR_SYSTEM_MODEL_ID));
    glPopMatrix();
    // 
    glPushMatrix();
    glTranslated(3,0.5,0);
     drawLamp();
    glPopMatrix();

}
