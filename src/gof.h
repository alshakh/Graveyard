#ifndef GOF
#define GOF
//
#define DEBUG 0
#define LIMIT 50
//
#define CELL_COLOR 1,1,1
#define GRID_COLOR 0.3,0.3,0.3
//
#include<string>
#include<iostream>
#include<cstdio>
//
using namespace std;
//
extern bool doNextGeneration;
extern unsigned int numLiveCells;
extern unsigned int numGens;
/// Life Class
class Life{
    public:
        Life();
        void next(bool,bool a = true);
        void jumpNGens(unsigned int gens);
        void makeLive(int x , int y);
        void clear();
        void populateRandomly();
        void toggleCell(int x, int y);
        bool isCellLive(int x , int y);
        unsigned int getNumGens();
        unsigned int getNumLiveCells();
        unsigned int getLimit();
        //
        void drawGrid();
        //
    private:
        unsigned int numLiveCells;
        unsigned int numGens;
        bool prevCells[LIMIT][LIMIT];
        bool nextCells[LIMIT][LIMIT];
        unsigned int limit;
        //
        void resetCells(bool cells[LIMIT][LIMIT]);
        int getValidIndex(int n) ;
        bool getCell(bool cells[LIMIT][LIMIT],int i,int j); // TO REMOVE

        //
        class Grid{
            public:
                static void drawCells(bool cells[LIMIT][LIMIT]);
                static void drawGrid();
                static void drawCell(unsigned int x,unsigned int y);
        };
};
/// GridClass

// RLE
class RLE{
    public:
        RLE(string s);
        void addToLife(Life * life);
    private:
        unsigned int width;
        unsigned int height;
        string name;
        string directions;
        void traverseCells(Life * life);
};
// Organism
class Organism {
    public:
        static void addToLife(int p,Life * life);
};
#endif
