CC = gcc
LIB = -lglut -lGLU -lGL -lm
CFLAGS = -Wall -O3
DEP = readRLE.o
.PHONY: clean all c
all: gof.out

gof.out:2d-game-of-life.c $(DEP)
	$(CC) $(CFLAGS) -o $@ $^ $(LIB)

%.o:%.c
	$(CC) $(CFLAGS) -c -o $@ $^ $(LIB)

run:gof.out
	./gof.out

clean:
	rm -rf *.o *.out *.tmp

c:clean
