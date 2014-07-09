CC = gcc
LIB = -lglut -lGLU -lGL -lm
FLAGS = -Wall -Wextra -O3

.PHONY: clean all c
all: gof.out

gof.out:2d-game-of-life.c gof.h
	$(CC) $(FLAGS) -o $@ $^ $(LIB)

run:gof.out
	./gof.out

clean:
	rm -rf *.o *.out

c:clean
