#include <stdio.h>
#include <stdarg.h>
#include <math.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
//
#include "gof.h"
//
#define END_OF_LINE '$'
#define END_OF_ORGANISM '!'
#define DEAD_CELL 'b'
#define LIVE_CELL 'o'
//
static void traverseCells(const int x,const int y,const char* organism_disc);
//
void rleAddToLife(int limit,rle_info_t file) {
    if(DEBUG) printf("Adding organism : %s \n width=%d , height=%d",file.name,file.width,file.height);
    //
    if(limit<file.width || limit<file.height) {
        printf("	organism is out of bounds > LIMIT");
        //
        return;
    }
    //
    traverseCells(file.width,file.height,file.disc);
}
//
static void traverseCells(const int width,const int height,const char* organism_disc) {
    int tmpX = 0;
    int tmpY = height;
    //
    int repeatNum = 1;
    //
    int currentIndex=0;
    //
    for(currentIndex = 0 ; currentIndex < strlen(organism_disc) ; currentIndex++) {
        char ch = organism_disc[currentIndex];
        //
        if(DEBUG) printf("%c  %d times ",ch,repeatNum);
        //
        if(ch == END_OF_LINE) {
            if(DEBUG) printf(" END OF LINE\n");
            //
            tmpY-=repeatNum;
            tmpX=0;
            //
            // reset repeatNum
            repeatNum=1;
        }
        else if (ch == END_OF_ORGANISM) {
            if(DEBUG) printf(" END OF ORGANISM\n");
            //
            break;
        }
        else if (ch == DEAD_CELL) {
            if(DEBUG) printf(" DEAD CELL(S)\n");
            //
            tmpX+=repeatNum;
            //
            // reset repeatNum
            repeatNum=1;
        }
        else if (ch == LIVE_CELL) {
            if(DEBUG) printf(" LIVE CELL(S)\n");
            //
            int q;
            for(q=0; q <repeatNum ; q++) {
                LifeLiveCell(tmpX, tmpY);
                tmpX++;
            }
            //
            // reset repeatNum
            repeatNum=1;
        }
        else if (ch >= '0' && ch <= '9') { // Num
            if(DEBUG) printf(" NUMBERS(S)\n");
            //
            char tmpNumStr[100];
            memset(tmpNumStr,0,sizeof(tmpNumStr));
            //
            int tmpNumIdx=0;
            //
            for(; currentIndex < strlen(organism_disc) ; currentIndex++) {
                ch = organism_disc[currentIndex];
                if(ch >= '0' && ch <= '9') {
                    tmpNumStr[tmpNumIdx] = ch;
                    tmpNumIdx++;
                } else {
                    break;
                }
            }
            //
            currentIndex--; // return to char just behind the number
            //
            repeatNum = atoi(tmpNumStr);
            //
        }
    }
}
