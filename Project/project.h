#ifndef PROJECT_H
#define PROJECT_H

#define TRUE 1
#define FALSE 0
//
// Texture names 
//
#define DARK_WOOD_TEX 0
#define RUBIK_CUBE_TEX 1
#define BLACK_TILE_TEX 2
#define CLOCK_FACE_TEX 3
#define DAVINCI_TEX 4
#define GAMMA_TEX 5
#define BOOKSPINES_TEX 6
#define DEBUG_TEX 7
#define GOLD_TEX 8
#define WHITE_CERAMIC_TEX 9
#define WALL_TEX 10
#define DOOR_TEX 11
#define WINDOW_TEX 12
//
typedef struct {
	float width; // from spine to edge
 	float height;// from edge to edge
 	float thickness; // from side to another
 	float l1; // begining of epine from left (0-->1)
 	float l2; // end of spine from left (0-->1 ) must be > l2
 	unsigned int cover; // texture of cover
 	unsigned int pages; // texture of pages when open
 	unsigned int pagesEdge; // texture of edges
} Book;
unsigned int getTextureId(int t); // in main.c
int getSizeOfBookList();
Book getBook(int i);
//////////
/// Objects 
//////////
void drawRoom();
void drawTrumpet();
void drawVaze();
void drawVaze2();
void drawLampHead();
//
void drawIss(float timeFactor);
void drawRubikCube(float timeFactor);
void  drawRubikCube_seed(float timeFactor,int seed);
void drawClock(float timeFactor);
void drawLamp();
void setEmissionIfnoLight();
//
void drawDesk();
void drawReadingTable();
void drawRoundTable();
void drawChair();
void drawBookShelves();
void bookStakByOneTexture();
//
void drawPainting(float widthToHeight, unsigned int texture);
//
void drawBook(Book book,float timeFactor);
//
void loadSolarSystemInit();
void drawSolarSystemModel(float timeFactor);
//
//
//
float getTimeFactor();
//////////
#define RUBIKCUBE_ON_DESK_ID 0
#define RUBIKCUBE_ON_SHELF_ID 1
#define SOLAR_SYSTEM_MODEL_ID 2
#define OPEN_BOOK_ON_DESK_ID 3
#define OPEN_BOOK_ON_TABLE_ID 4
#define ISS_MODEL_ID 5
#define CLOCK_ID 6
//
#define TIME_FACTOR_FOR_ALL -1
#define NO_TIME_FACTOR -5
////////
#endif
