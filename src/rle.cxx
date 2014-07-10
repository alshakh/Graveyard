#include <string>
#include <sstream>
#include <algorithm>
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
void RLE::addToLife(Life * life) {
    if(DEBUG) cout<<"Adding organism : "<< name <<" \n width="<< width <<" , height="<< height <<endl;
    //
    if(life->getLimit()<width || life->getLimit()<height) {
         if(DEBUG) cout<<"Organism is too large " <<endl;
        //
        return;
    }
    //
    traverseCells(life);
}
RLE::RLE(string fileContent){
    istringstream iss(fileContent);
    for (string line;getline(iss, line); )
    {
        // remove all spaces from line 
        line.erase(remove(line.begin(), line.end(), ' '), line.end());
        //
        //processing name and comments
        if(line.find(COMMENT_PREFIX) == 0) {
            // TODO 
        }
        //processing x,y and rules
        else if(line.find(META_PREFIX) == 0){
            unsigned int endOfX = line.find(META_SPARATOR);
            width = stoi(line.substr(line.find(META_EQUAL)+1,endOfX ));
            line.erase(0,endOfX+1);
            
            unsigned int endOfY = line.find(META_SPARATOR);
            if(endOfY==string::npos) endOfY = line.length();
            height = stoi(line.substr(line.find(META_EQUAL)+1,endOfY));
            //TODO rule
        }
        //cout << (unsigned int)line.find(META_PREFIX) << ">> " + line << endl;
    }
    //
    directions=fileContent;
}
//
void RLE::traverseCells(Life * life) {
    int tmpX = 0;
    int tmpY = height;
    //
    int repeatNum = 1;
    //
    unsigned int currentIndex=0;
    //
    for(currentIndex = 0 ; currentIndex < directions.length(); currentIndex++) {
        char ch = directions.at(currentIndex);
        //
        if(DEBUG) cout<< ch << "  " << repeatNum << endl;
        //
        if(ch == END_OF_LINE) {
            //
            tmpY-=repeatNum;
            tmpX=0;
            //
            // reset repeatNum
            repeatNum=1;
        }
        else if (ch == END_OF_ORGANISM) {
            //
            break;
        }
        else if (ch == DEAD_CELL) {
            //
            tmpX+=repeatNum;
            //
            // reset repeatNum
            repeatNum=1;
        }
        else if (ch == LIVE_CELL) {
            //
            int q;
            for(q=0; q <repeatNum ; q++) {
                life->makeLive(tmpX, tmpY);
                tmpX++;
            }
            //
            // reset repeatNum
            repeatNum=1;
        }
        else if (ch >= '0' && ch <= '9') { // Num
            //
            string numberStr;
            //
            for(; currentIndex < directions.length() ; currentIndex++) {
                ch = directions[currentIndex];
                if(ch >= '0' && ch <= '9') {
                    numberStr.append(&ch);
                } else {
                    break;
                }
            }
            //
            currentIndex--; // return to char just behind the number
            //
            repeatNum = stoi(numberStr);
            //
        }
    }
}
