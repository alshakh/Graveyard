#ifndef GOF
#define GOF

#define DEBUG 1

typedef struct {
    char* name;
    char* disc;
    int x;
    int y;
} rle_info_t;

typedef struct {
    int x;
    int y;
} point_t;
//
void readRLE(int limit,rle_info_t);
void liveCell(int x,int y);

#endif
