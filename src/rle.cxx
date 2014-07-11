/**
 * RLE files contain the info to build organisms.
 * 
 * ~ It has three components
 * 1) (single/multi line) Comments start with #
 * 2) (single line) meta line with x=(number) , y=(number) , rule=(rule)
 *          rule is not mandatory some files do not have rules.
 * 3) (multi line) instructions never more than 77 characters in one line
 * 
 * ~ Instructions are composed of five parts
 * b : dead cell
 * o : live cell
 * $ : end of line
 * (number) : multiplied by the next instruction (lines never end with numbers)
 * ! : end of organism
 * 
 * ~ RLE file example
 * #N Gosper glider gun
 * #C This was the first gun discovered.
 * #C As its name suggests, it was discovered by Bill Gosper.
 * x = 36, y = 9, rule = B3/S23
 * 24bo$22bobo$12b2o6b2o12b2o$11bo3bo4b2o12b2o$2o8bo5bo3b2o$2o8bo3bob2o4b
 * obo$10bo5bo7bo$11bo3bo$12b2o! 
 */

#include <string>
#include <sstream>
#include <algorithm>
#include <utility> 
#include <iostream>
#include <fstream>

#include <stdio.h>
//
#include "gof.h"
//
#define END_OF_LINE '$'
#define END_OF_ORGANISM '!'
#define DEAD_CELL 'b'
#define LIVE_CELL 'o'
#define COMMENT_PREFIX '#'
#define META_PREFIX "x="
#define META_SPARATOR ','
#define META_EQUAL '='
//
//

void RLE::addToLife(Life & life) {
    if (DEBUG) cout << "Adding organism" << endl;
    //
    if (life.getLimit() < width || life.getLimit() < height) {
        if (DEBUG) cout << "Organism is too large " << endl;
        //
        return;
    }
    //
    istream * stream;
    if (isFile) {
        stream = new ifstream(fileOrContent);
    } else {

        stream = new istringstream(fileOrContent);

    }
    string line;
    // bypass header
    while (getline(*stream, line)) {

        line.erase(remove(line.begin(), line.end(), ' '), line.end());
        if (line.find(META_PREFIX) == 0) {
            break;
        }
    }
    // Add content
    unsigned int xPos = 0;
    unsigned int yPos = height;
    pair<unsigned int, unsigned int> newXYPos;
    while (getline(*stream, line)) {
        newXYPos = addLineToLife(life, line, xPos, yPos);
        xPos = newXYPos.first;
        yPos = newXYPos.second;
    }

    delete stream;
}
//

void RLE::readHeader() {
    //istream stream

    istream * stream;
    if (isFile) {
        stream = new ifstream(fileOrContent);
    } else {
        stream = new istringstream(fileOrContent);
    }
    //getStream(stream);
    string line;
    // Strip Header 
    while (getline(*stream, line)) {
        cout << line << endl;
        // remove all spaces from line 
        line.erase(remove(line.begin(), line.end(), ' '), line.end());
        //
        //processing name and comments
        if (line.find(COMMENT_PREFIX) == 0) {
            // TODO  comments and name
        }//processing x,y and rules
        else if (line.find(META_PREFIX) == 0) {

            unsigned int endOfX = line.find(META_SPARATOR);
            width = stoi(line.substr(line.find(META_EQUAL) + 1, endOfX));
            line.erase(0, endOfX + 1);
            //
            unsigned int endOfY = line.find(META_SPARATOR);
            if (endOfY == string::npos) endOfY = line.length();
            height = stoi(line.substr(line.find(META_EQUAL) + 1, endOfY));
            //TODO rule 

            // get out when end header (header ends with meta line)
            break;
        }
    }
    delete stream;
    //
}
//

RLE::RLE(string fileOrContent, bool isFile) {
    this->isFile = isFile;
    this->fileOrContent = fileOrContent;
    //
    readHeader();
}
//

pair<unsigned int, unsigned int> RLE::addLineToLife(Life & life, string line, unsigned int currentXPos, unsigned int currentYPos) {
    //
    int repeatNum = 1;
    //
    unsigned int idx = 0;
    //
    for (idx = 0; idx < line.length(); idx++) {
        char ch = line.at(idx);
        //
        if (DEBUG) cout << ch << "  " << repeatNum << endl;
        //
        if (ch == END_OF_LINE) {
            //
            currentYPos -= repeatNum;
            currentXPos = 0;
            //
            // reset repeatNum
            repeatNum = 1;
        } else if (ch == END_OF_ORGANISM) {
            //
            break;
        } else if (ch == DEAD_CELL) {
            //
            currentXPos += repeatNum;
            //
            // reset repeatNum
            repeatNum = 1;
        } else if (ch == LIVE_CELL) {
            //
            int q;
            for (q = 0; q < repeatNum; q++) {
                life.makeLive(currentXPos, currentYPos);
                currentXPos++;
            }
            //
            // reset repeatNum
            repeatNum = 1;
        } else if (ch >= '0' && ch <= '9') { // Num
            //
            string numberStr = "";
            //
            for (; idx < line.length(); idx++) {
                //
                ch = line.at(idx);
                if (ch >= '0' && ch <= '9') {
                    numberStr += ch;
                } else {
                    break;
                }
            }
            //
            idx--; // return to char just behind the number
            //
            repeatNum = stoi(numberStr);
            //
        }
    }
    // 

    return make_pair(currentXPos, currentYPos);
}

void RLE::exportToRLE(Life & life, string filePath) {
    //TODO
}