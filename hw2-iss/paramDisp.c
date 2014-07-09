/** 
	@authour Ahmed Alshakh alshakh.net
	@date june 9, 2014
	Usage info in paramDisp.h
*/
#include "paramDisp.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>

#define GL_GLEXT_PROTOTYPES
#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif


#define LEN 8192  // Maximum length of text strings
#define MAX_PARAM_N 50
/* TODO 
 * number of parameters from MAX_param_N to generic
 * option to change font and other things
 * chenge color of selected Item.
*/
/* FIXME
 * When all items are info/disabled items. fixSelectedInfo() goes to infinite loop
 */
char* paramUnselectedTitle[MAX_PARAM_N];
char* paramSelectedTitle[MAX_PARAM_N];
char  isInfo[MAX_PARAM_N];
char isDisabled[MAX_PARAM_N];
int paramN=0; // for now up to MAX_param_N ;
void (*incFunc)(int);
void (*decFunc)(int);
char* (*getFunc)(int);
void fixSelectedInfo();//Make sure that chose item is not info Item.
///
int selectedItem=0;
int shown=1;
///

void paramInit(int num,char* get(int), void inc(int), void dec(int)) {
	if(paramN > MAX_PARAM_N) { //TODO allow generic number of parameters
		printf("ERROR too many parameters");
	}
	paramN=num;
	incFunc=inc;
	decFunc=dec;
	getFunc=get;
}
void paramSet(int n,char* unselectedTitle,char* selectedTitle){
	if(n>=paramN) return;
	if(n<0) return;
	paramUnselectedTitle[n]=unselectedTitle; 
	paramSelectedTitle[n]=selectedTitle;
}
void paramInfoSet(int n,char* unselectedTitle){
	if(n>=paramN) return;
	if(n<0) return;
	paramUnselectedTitle[n]=unselectedTitle; 
	isInfo[n]=1;//true;
}
void paramDraw(){
	if(!shown)return;
	// 
	   //FIXME infinite loop if all params are info/disabled param
  
		selectedItem=selectedItem%paramN;
		while(isInfo[selectedItem] || isDisabled[selectedItem]){
		    selectedItem++;
		    selectedItem=selectedItem%paramN;
		}
	//
	int initPosY=5;
	int spacing=13;

	int i;
	for(i=0;i<paramN;i++){
		glWindowPos2i(2,initPosY+i*spacing);	

		if(selectedItem==i)
			paramPrint(paramSelectedTitle[i],(*getFunc)(i));
		else
			paramPrint(paramUnselectedTitle[i],(*getFunc)(i));
	}
}

void paramPrint(const char* format , ...)
{
	char	 buf[LEN];
	char*	ch=buf;
	va_list args;
	//  Turn the parameters into a character string
	va_start(args,format);
	vsnprintf(buf,LEN,format,args);
	va_end(args);
	//  Display the characters one at a time at the current raster position
	while (*ch)
		glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12,*ch++); //TODO
}
void paramKeyPressed(char ch){
	// info items will never be selected se
	if(!shown)return;
	if (ch =='w' || ch == 'W'){
	
    //FIXME infinite loop if all params are info/disabled param
  
		selectedItem++;
		selectedItem=selectedItem%paramN;
		while(isInfo[selectedItem] || isDisabled[selectedItem]){
		    selectedItem++;
		    selectedItem=selectedItem%paramN;
		}
	} else if (ch=='s'|| ch=='S') {
 //FIXME infinite loop if all params are info/disabled param
  
		selectedItem--;
		if(selectedItem==-1) selectedItem=paramN-1;
		selectedItem=selectedItem%paramN;
		while(isInfo[selectedItem] || isDisabled[selectedItem]){
		    selectedItem--;
		    if(selectedItem==-1) selectedItem=paramN-1;
		    selectedItem=selectedItem%paramN;
		}
	} else if (ch == 'd' || ch == 'D'){
		(*incFunc)(selectedItem);
	} else if (ch == 'a' || ch == 'A'){
		(*decFunc)(selectedItem);
	}
}
void paramShow(){
	shown=1;
}
void paramHide(){
	if(paramN==0)return; // if everything is reset or not init'ed do not hide;
	shown=0;
}
void paramReset(){
	paramN=0;
	incFunc=NULL;
	decFunc=NULL;
	getFunc=NULL;
	selectedItem=0;
	shown=1;
}
void paramDisable(int i){
	if(i<0) return;
	if(i>=paramN) return;
	isDisabled[i]=1;
}
void paramEnable(int i){
	if(i<0) return;
	if(i>=paramN) return;
	isDisabled[i]=0;
}
