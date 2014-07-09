#input Header
fileList:=simpleShapeLib
I_Ext:=.c
O_Ext:=.e
COMMAND:=gcc -Wall -O3 -o &&O &&I -lglut -lGLU -lGL -lm

#----
sInput:=$(strip $(fileList))
Output:=$(patsubst %$( ),%$(O_Ext)$( ),$(sInput))
.PHONY : all clean c

all: $(Output)

%:$(patsubst %%$(O_Ext),%%,%)$(I_Ext)
	$(subst &&O,$@$(O_Ext),$(subst &&I, $<,$(COMMAND)))

%$(O_Ext):$(patsubst %%$(O_Ext),%%,%)$(I_Ext)
	$(subst &&O,$@,$(subst &&I, $<,$(COMMAND)))

c:clean
clean:
	rm -rf $(Output)
