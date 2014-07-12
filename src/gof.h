#ifndef GOF
#define GOF
//
#define DEBUG 1
//
#define CELL_COLOR 1,1,1
#define GRID_COLOR 0.3,0.3,0.3
//
#include<string>
#include<iostream>
#include<cstdio>
#include <vector>

//
using namespace std;
//
extern bool doNextGeneration;
/// Life Class

class Life {
public:
    Life(unsigned int limit);
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
    // Grid
    void drawGrid();
private:
    unsigned int numLiveCells;
    unsigned int numGens;
    vector<vector<bool>> prevCells;
    vector<vector<bool>> nextCells;
    unsigned int limit;
    //
    int getValidIndex(int n);
    bool getCell(vector<vector<bool>> &cells, unsigned int i, unsigned int j);
    void resetCells(vector<vector<bool>> & cells);
    // Grid
    void drawCells(vector<vector<bool>> &cells);
    void drawCell(unsigned int x, unsigned int y);
};
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
