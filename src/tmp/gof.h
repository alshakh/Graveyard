#ifndef GOF
#define GOF
//
#include <stdbool.h>
//
#define DEBUG 1
#define LIMIT 50
//
#define CELL_COLOR 1,1,1
#define GRID_COLOR 0.3,0.3,0.3

//
extern bool doNextGeneration;
extern unsigned int numLiveCells;
extern unsigned int numGens;
//
typedef struct {
    char* name;
    char* disc;
    int width;
    int height;
} rle_info_t;
//////
// Functions 
//////
// life.c 
void lifeNext() ;
void lifeJumpNGens(int n);
void LifeLiveCell(int x, int y);
void lifePoplulateRandomly() ;
void lifeToggleCell(int x, int y);
bool lifeIsCellLive(int x, int y);
void lifeClearLife();
// grid.c
void gridDrawCells(bool cells[LIMIT][LIMIT]) ;
void gridDrawGrid() ;
void gridDrawCell(int x,int y);
// organisms
void organismAddToLife(int p);
// rle
void rleAddToLife(int limit,rle_info_t);

#endif
