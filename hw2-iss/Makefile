#temp Makefile
all=iss.e

.PHONY: run clean

iss.e:iss.c paramDisp.o
	gcc -Wall -O3 $^ -o $@ -lglut -lGLU -lGL -lm
run:iss.e
	./iss.e
paramDisp.o:paramDisp.c
	gcc -Wall -O3 -c $< -o $@ -lglut -lGLU -lGL -lm
clean:
	rm -rf *.e *.o iss.e
