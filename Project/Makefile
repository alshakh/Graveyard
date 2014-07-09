#temp Makefile
all=project
DEPENDENCY=solidOfRevolution.o clock.o room.o geometry.o rubikCube.o furniture.o iss.o book.o solarSystemModel.o
LIB=CSCIx229.a
.PHONY: run clean

project:main.c $(LIB) $(DEPENDENCY)
	gcc -Wall -O3 $^ -o $@ -lglut -lGLU -lGL -lm

%.o:%.c
	gcc -c -Wall -O3 $^ -o $@ -lglut -lGLU -lGL -lm
CSCIx229.a:lib/fatal.o lib/loadtexbmp.o lib/print.o lib/project.o lib/errcheck.o lib/object.o
	ar -rcs CSCIx229.a $^
    
run:project
	./project
	make clean
clean:
	rm -rf project *.o *.a lib/*.o
	
