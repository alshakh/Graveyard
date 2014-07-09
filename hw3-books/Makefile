#  List of all programs you want to build
EX=scene

#  Libraries - Linux and various flavors of Unix
LIBS=-lglut -lGLU -lGL -lm
CLEAN=rm -f $(EX) *.o
#  Libraries - OSX
ifeq "$(OSTYPE)" "darwin"
LIBS=-framework GLUT -framework OpenGL
endif
#  Libraries - MinGW
ifeq "$(OS)" "Windows_NT"
LIBS=-lglut32cu -lglu32 -lopengl32
CLEAN=del *.exe *.o *.a
endif

#  Main target
all: $(EX)

#  Generic compile rules
.c.o:
	gcc -c -O -Wall $<

#  Generic compile and link
%: %.c CSCIx229.a
	gcc -Wall -O3 -o $@ $^ $(LIBS)

#  Delete unwanted files
clean:;$(CLEAN)
