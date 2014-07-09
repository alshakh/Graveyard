
.PHONY: clean all c
all: gof.out

gof.out:2d-game-of-life.c
	gcc -Wall -O3 -o $@ $^ -lglut -lGLU -lGL -lm
	
run:gof.out
	./gof.out

clean:
	rm -rf *.o *.out

c:clean
