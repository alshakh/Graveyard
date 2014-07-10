CC = gcc
LIB = -lglut -lGLU -lGL -lm
CFLAGS = -Wall -O3
DEP = src/rle.o src/life.o src/grid.o src/organism.o src/main.o
.PHONY: clean all c
all: gof.out

gof.out:$(DEP)
	$(CC) $(CFLAGS) -o $@ $^ $(LIB)

%.o:%.c
	$(CC) $(CFLAGS) -c -o $@ $^ $(LIB)

run:gof.out
	./gof.out

clean:
	rm -rf *.o src/*.o *.out src/*.out *.tmp src/*.tmp

c:clean
