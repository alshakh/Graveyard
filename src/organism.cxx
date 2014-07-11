#include "gof.h"

void Organism::addToLife(int p,Life & life) {
    if(p==1) {
        //glider-gun
        life.makeLive(8,26);
        life.makeLive(9,26);
        life.makeLive(9,25);
        life.makeLive(8,25);
        life.makeLive(18,26);
        life.makeLive(18,25);
        life.makeLive(18,24);
        life.makeLive(19,23);
        life.makeLive(20,22);
        life.makeLive(21,22);
        life.makeLive(19,27);
        life.makeLive(20,28);
        life.makeLive(21,28);
        life.makeLive(23,27);
        life.makeLive(24,26);
        life.makeLive(24,25);
        life.makeLive(24,24);
        life.makeLive(23,23);
        life.makeLive(25,25);
        life.makeLive(22,25);
        life.makeLive(28,26);
        life.makeLive(28,27);
        life.makeLive(28,28);
        life.makeLive(29,28);
        life.makeLive(29,27);
        life.makeLive(29,26);
        life.makeLive(30,29);
        life.makeLive(30,25);
        life.makeLive(32,29);
        life.makeLive(32,30);
        life.makeLive(32,25);
        life.makeLive(32,24);
        life.makeLive(42,27);
        life.makeLive(42,28);
        life.makeLive(43,28);
        life.makeLive(43,27);
        // fish-hook
        life.makeLive(12,39);
        life.makeLive(12,40);
        life.makeLive(13,40);
        life.makeLive(14,39);
        life.makeLive(14,38);
        life.makeLive(14,37);
        life.makeLive(15,37);
    }
}
