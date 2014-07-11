#ifndef GOF
#define GOF
//
#define DEBUG 1
#define LIMIT 300
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
/// Life Class

class Life {
public:
    Life();
    void next(bool, bool a = true);
    void jumpNGens(unsigned int gens);
    void makeLive(int x, int y);
    void clear();
    void populateRandomly();
    void toggleCell(int x, int y);
    bool isCellLive(int x, int y);
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
    int getValidIndex(int n);
    bool getCell(bool cells[LIMIT][LIMIT], int i, int j); // TO REMOVE

    //

    class Grid {
    public:
        static void drawCells(bool cells[LIMIT][LIMIT]);
        static void drawGrid();
        static void drawCell(unsigned int x, unsigned int y);
    };
};
/// GridClass

// RLE

class RLE {
public:
    RLE(string s, bool isFile);
    void addToLife(Life & life);
    static void exportToRLE(Life & life, string filePath);
private:
    string fileOrContent;
    unsigned int width;
    unsigned int height;
    string name;
    bool isFile;
    pair<unsigned int, unsigned int> addLineToLife(Life & life, string line, unsigned int xPos, unsigned int yPos);
    void readHeader();
};
// Organism

class Organism {
public:
    static void addToLife(int p, Life & life);
};
#endif
