#include "CSCIx229.h"
#include "geometry.h"
#include "project.h"
#include <time.h>

int zh=0;		 //  Azimuth of light
int fov=55;		 //  Field of view (for perspective)
double asp=1;	         //  Aspect ratio
double dim=5.0;	         //  Size of world
int	 movingLight=1;	 //  Lighting
float ylight =2.5;
float lightMove = 1;
float lightDistance = 2.4;
// time
float timeFactor=0;
int moveTime=0;
int activeObjID=TIME_FACTOR_FOR_ALL; // TIME_FACTOR_FOR_ALL: all active , ID objID , NO_TIME_FACTOR return 0;
//
int viewMode = 0 ; // 0 room , 1 objects
////
float eX=-7/2.0;
float eY=5/2.0;
float eZ=3;
float xToy=-35;
float yToz=-20;
// For object 
int th=0;			//  Azimuth of view angle
int ph=0;			//  Elevation of view angle
// Object Modes
#define OBJ_NUM 6
static int objMode=0;
static char* text[OBJ_NUM] = {
    "Book 1 (a/d to open and close)",
    "\"Trumpet\" (No movement, light responds inside & outside",
    "Rubik's Cube (a/d to rotate)",
    "Vaze 1",
    "Book 2 (a/d to open and close)",
    "Vaze 2"
    };
//
static unsigned int* texture_list;
// Books
Book* bookList;
//
//
//
static void drawLightBall(double x,double y,double z,double r)
{
	glPushMatrix();
	glTranslated(x,y,z);
	glScaled(r,r,r);
	glutSolidSphere(1.0,16,16);
	glPopMatrix();
}

static void display()
{
    /////////////////////////////////////
    /// LIMIT X,Y,Z
    float offset=0.5;
    if(eX>7/2.0-offset) eX=7/2.0-offset;
    else if (eX<-7/2.0+offset) eX=-7/2.0+offset;
    //
    if(eY>5/2.0-offset) eY=5/2.0-offset;
    else if (eY<-5/2.0+offset) eY=-5/2.0+offset;
    //
    if(eZ>3-offset) eZ=3-offset;
    else if (eZ<0+offset) eZ=0+offset;
///////////////////////////////////
     //  Erase the window and the depth buffer
   glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
   //  Enable Z-buffering in OpenGL
   glEnable(GL_DEPTH_TEST);
   //  Undo previous transformations
   glLoadIdentity();
   
     if(viewMode==0){     
        gluLookAt(eX,eY,eZ , eX+Cos(xToy),eY+Sin(xToy),eZ+Sin(yToz) , 0,0,1);
   } else {
        double Ex = -2*dim*Sin(th)*Cos(ph);
        double Ey = +2*dim        *Sin(ph);
        double Ez = +2*dim*Cos(th)*Cos(ph);
        //
         gluLookAt(Ex,Ey,Ez , 0,0,0 , 0,Cos(ph),0);
   }
   
   //  Light switch
   if (movingLight)
   {
      //  Translate intensity to color vectors
      float Ambient[]   = {0.3,0.3,0.3,1.0};
      float Diffuse[]   = {1,1,1,1};
      float Specular[]  = {1,1,0,1};
      float white[]     = {1,1,1,1};
      //  Light direction
      float Position[]  = {lightDistance*Cos(zh),lightDistance*Sin(zh),ylight,1.0};
      //  Draw light position as ball (still no lighting here)
      drawLightBall(Position[0],Position[1],Position[2] , 0.1);
      //  Enable lighting with normalization
      glEnable(GL_LIGHTING);
      glEnable(GL_NORMALIZE);
      //  glColor sets ambient and diffuse color materials
      glColorMaterial(GL_FRONT_AND_BACK,GL_AMBIENT_AND_DIFFUSE);
      glEnable(GL_COLOR_MATERIAL);
      //  Enable light 0
      glEnable(GL_LIGHT0);
      glLightfv(GL_LIGHT0,GL_AMBIENT ,Ambient);
      glLightfv(GL_LIGHT0,GL_DIFFUSE ,Diffuse);
      glLightfv(GL_LIGHT0,GL_SPECULAR,Specular);
      glLightfv(GL_LIGHT0,GL_POSITION,Position);
      glMaterialf(GL_FRONT_AND_BACK,GL_SHININESS,32.0f);
      glMaterialfv(GL_FRONT_AND_BACK,GL_SPECULAR,white);
    }
    else
    {
        	//  Translate intensity to color vectors
        float Ambient[]   = {0.3,0.3,0.3,1.0};
        float Diffuse[]   = {1,1,1,1};
      	float Specular[]  = {0.4,0.4,0,1};
      	float white[]     = {1,1,1,1};
      	//  Light direction
      	float Position[]  = {3-7/2.0,0.5-5/2.0,1.3,1.0};
      	//  Draw light position as ball (still no lighting here)
      	drawLightBall(Position[0],Position[1],Position[2] , 0.1);
      	//  Enable lighting with normalization
      	glEnable(GL_LIGHTING);
      	glEnable(GL_NORMALIZE);
      	//  glColor sets ambient and diffuse color materials
      	glColorMaterial(GL_FRONT_AND_BACK,GL_AMBIENT_AND_DIFFUSE);
      	glEnable(GL_COLOR_MATERIAL);
      	//  Enable light 0
      	glEnable(GL_LIGHT0);
      	glLightfv(GL_LIGHT0,GL_AMBIENT ,Ambient);
      	glLightfv(GL_LIGHT0,GL_DIFFUSE ,Diffuse);
		glLightfv(GL_LIGHT0,GL_SPECULAR,Specular);
      	glLightfv(GL_LIGHT0,GL_POSITION,Position);
      	glMaterialf(GL_FRONT_AND_BACK,GL_SHININESS,32.0f);
      	glMaterialfv(GL_FRONT_AND_BACK,GL_SPECULAR,white);
        }
    //


    ///////////////////////
    ///////////////////////
    glPushAttrib(GL_ALL_ATTRIB_BITS);
    glPushMatrix();
    ////////
    if(viewMode==0){
        glTranslated(-7/2.0,-5/2.0,0);
        drawRoom();
    } else if (viewMode==1){
        if(objMode==0){
            glScaled(10,10,10);
            drawBook(bookList[0],timeFactor/2.0);
        } else if(objMode==1){
            //glScaled(10,10,10);
            drawTrumpet();
        } else if(objMode==2){// rubik cube
            //glScaled(10,10,10);
            drawRubikCube(timeFactor);
        } else if(objMode==3){//vaze 1
            //glScaled(10,10,10);
            drawVaze();
        }else if(objMode==4){//book 2
            glScaled(10,10,10);
            drawBook(bookList[3],timeFactor/2.0);
        }else if(objMode==5){//vaze 2
            //glScaled(10,10,10);
            drawVaze2();
        }
        
    }
    //////////////////////////////////////
    glPopMatrix();
    glPopAttrib();
    ///////////////////////
    ///////////////////////
    
    
	glDisable(GL_LIGHTING);
	if(viewMode!=0){
	    glWindowPos2i(5,5);
	    Print("%s",text[objMode]);
	}
	//  Render the scene and make it visible
	ErrCheck("display");
	glFlush();
	glutSwapBuffers();
}
//
static void special(int key,int x,int y)
{
    if(viewMode==0){
    
	    if (key == GLUT_KEY_RIGHT){
		    xToy--;
	    }else if (key == GLUT_KEY_LEFT){
	    	xToy++;
	    }else if (key == GLUT_KEY_PAGE_UP){
	    	eX+=Cos(xToy)*0.1;
	    	eY+=Sin(xToy)*0.1;
	    	eZ+=Sin(yToz)*0.1;
	    }else if (key == GLUT_KEY_PAGE_DOWN){
	    	eX-=Cos(xToy)*0.1;
	    	eY-=Sin(xToy)*0.1;
	    	eZ-=Sin(yToz)*0.1;
	    }else if (key == GLUT_KEY_DOWN)
	    	yToz--;
	    else if (key == GLUT_KEY_UP)
	    	yToz++;
	    }
	else if(viewMode==1){
	    if (key == GLUT_KEY_RIGHT)
		    th += 5;
	    else if (key == GLUT_KEY_LEFT)
		    th -= 5;
	    else if (key == GLUT_KEY_UP)
		    ph += 5;
	    else if (key == GLUT_KEY_DOWN)
	    	ph -= 5;
	    else if (key == GLUT_KEY_PAGE_DOWN)
	    	dim++;
	    else if (key == GLUT_KEY_PAGE_UP)
	    	dim--;
	}
	Project(fov,asp,dim);
	glutPostRedisplay();
}
static void idle()
{
	//  Elapsed time in seconds
	double t = glutGet(GLUT_ELAPSED_TIME)/1000.0;
	if(lightMove) 
	    zh = fmod(90*t,360.0);
	if(moveTime){
		timeFactor= fmod(t/3.0,1);
	}
	//  Tell GLUT it is necessary to redisplay the scene
	glutPostRedisplay();
}

static void key(unsigned char ch,int x,int y)
{
	//  Exit on ESC
	if (ch == 27)
		exit(0);
	//  Reset view angle
	else if (ch == '0'){
	    eX=-7/2.0;
        eY=5/2.0;
        eZ=3;
        xToy=-35;
        yToz=-20;
        moveTime=0;
        viewMode=0;
    }
    //
	else if (ch == 'l' || ch == 'L')
		movingLight = 1-movingLight;
	else if (ch == 's' || ch == 'S')
      		lightMove = 1-lightMove;
    else if (ch=='[')
      		ylight -= 0.1;
   	else if (ch==']')
      		ylight += 0.1;
    else if (ch=='{')
      		lightDistance -= 0.1;
   	else if (ch=='}')
      		lightDistance += 0.1;
    else if (ch=='m' || ch=='M'){
            if(viewMode==1){
                objMode++;
                objMode%=OBJ_NUM;
                timeFactor=0;
            }
    }
    //
    else if(ch=='z'){
        eZ+=0.01;
    }else if(ch=='Z'){
        eZ-=0.01;
    }else if(ch=='x'){
        eX+=0.01;
    }else if(ch=='X'){
        eX-=0.01;
    }else if(ch=='y'){
        eY+=0.01;
    }else if(ch=='Y'){
        eY-=0.01;
    }
    //
    else if (ch=='o' || ch=='O'){
        viewMode=1-viewMode;
        if(viewMode==1){
            moveTime=0;
            timeFactor=0;
        } else {
            dim=5;
            timeFactor=0;
        }
    }
    //
    else if (ch=='t' || ch == 'T'){
    	moveTime=1-moveTime;
    	if(moveTime==0){
    	if(activeObjID!= TIME_FACTOR_FOR_ALL)
    	        timeFactor=0;   
    	    activeObjID= TIME_FACTOR_FOR_ALL;
    	    
    	}
    }
    else if (ch=='a'||ch=='A'){
        if(viewMode==1){
            timeFactor-=0.01;
            if(timeFactor<0) timeFactor=0;
        }
   } else if (ch=='d' || ch=='D'){
        if(viewMode==1) 
            timeFactor+=0.01;
            if(timeFactor>1) timeFactor=1;
   }
   //// Choose active Object
   else if(moveTime && viewMode==0){
        if(ch=='1') activeObjID=0;
        if(ch=='2') activeObjID=1;
        if(ch=='3') activeObjID=2;
        if(ch=='4') activeObjID=3;
        if(ch=='5') activeObjID=4;
        if(ch=='6') activeObjID=5;
        if(ch=='7') activeObjID=6;
        if(ch=='8') activeObjID= TIME_FACTOR_FOR_ALL;
        timeFactor=0;
   }
    
    
    //
	//  Reproject
	Project(fov,asp,dim);
	//
	glutPostRedisplay();
}

static void reshape(int width,int height)
{
	//  Ratio of the width to the height of the window
	asp = (height>0) ? (double)width/height : 1;
	//  Set the viewport to the entire window
	glViewport(0,0, width,height);
	//  Set projection
	Project(fov,asp,dim);
}
//
static Book createBook(float width,float heightToWidth,float l1,float l2,unsigned int cover,unsigned int pagesEdge,unsigned int pages){
	Book b;
	b.l1=l1;
	b.l2=l2;
	b.width=width;
	b.height=heightToWidth*width;
	b.thickness=(l2-l1)*2*width;
	b.cover = cover;
	b.pagesEdge = pagesEdge;
	b.pages=pages;
	return b;
}
static int bookListSize =  4;
static void initBooks(){

    unsigned int pages1 = LoadTexBMP("images/bp0.bmp");
    unsigned int pages2 = LoadTexBMP("images/pages_1.bmp");
    unsigned int pagesEdge = LoadTexBMP("images/pages.bmp");
    //
    bookList = (Book *)(malloc(bookListSize*sizeof(Book)));
    // Just change the width (first argument)
    // *2 because I calculated height to 2*widht instead of height*width
    bookList[0] = createBook(0.2,2*727/1140.0,486/1140.0,657/1140.0,LoadTexBMP("images/1.bmp"),pagesEdge,pages1);
    bookList[1] = createBook(0.2,2*428/601.0,287/601.0,314/601.0,LoadTexBMP("images/bookCover_1.bmp"),pagesEdge,pages2);
    bookList[2] = createBook(0.2,2*307/512.0,230/512.0,284/512.0,LoadTexBMP("images/bookCover_2.bmp"),pagesEdge,pages2);
    bookList[3] = createBook(0.2,2*356/512.0,232/512.0,274/512.0,LoadTexBMP("images/bookCover_3.bmp"),pagesEdge,pages2);
}

static void loadTextures(){
    int n = 13;
    texture_list = (unsigned int*)malloc(n*sizeof(unsigned int));
    texture_list[DARK_WOOD_TEX] = LoadTexBMP("images/darkwood.bmp");
    texture_list[RUBIK_CUBE_TEX] =  LoadTexBMP("images/b.bmp");
    texture_list[BLACK_TILE_TEX] =  LoadTexBMP("images/black_tile.bmp");
    texture_list[CLOCK_FACE_TEX] =  LoadTexBMP("images/clock.bmp");
    texture_list[DAVINCI_TEX] =  LoadTexBMP("images/davinci0.bmp");
    texture_list[GAMMA_TEX] =  LoadTexBMP("images/gamma.bmp");
    texture_list[BOOKSPINES_TEX] =  LoadTexBMP("images/bookSpines.bmp");
    texture_list[DEBUG_TEX] =  LoadTexBMP("images/debug.bmp");
    texture_list[GOLD_TEX] =  LoadTexBMP("images/gold.bmp");
    texture_list[WHITE_CERAMIC_TEX] =  LoadTexBMP("images/c.bmp");
    texture_list[WALL_TEX] =  LoadTexBMP("images/wall.bmp");
    texture_list[DOOR_TEX] = LoadTexBMP("images/door.bmp");
    texture_list[WINDOW_TEX] = LoadTexBMP("images/window.bmp");
}
/////
static void initialize(){
    loadSolarSystemInit();
    loadTextures();
    initBooks();

}
//
int main(int argc,char* argv[])
{
	////////////////////////////////////////////////
	/////////////// !!!!!!!!!!!!!! ////////////////
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//  Initialize GLUT
	glutInit(&argc,argv);
	//  Request double buffered, true color window with Z buffering at 600x600
	glutInitDisplayMode(GLUT_RGB | GLUT_DEPTH | GLUT_DOUBLE | GLUT_ALPHA);
	glutInitWindowSize(600,600);
	glutCreateWindow("Alshakh - Project");
	//  Set callbacks
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutSpecialFunc(special);
	glutKeyboardFunc(key);
	glutIdleFunc(idle);
	glutFullScreen();
	//  Load textures
	//  Pass control to GLUT so it can interact with the user
	ErrCheck("init");
	///////
	initialize();
	//////////////////////////////////////
	glutMainLoop();
	return 0;
}
/// 
unsigned int getTextureId(int t){
    return texture_list[t];
}
///
int getSizeOfBookList(){
    return bookListSize;
}
//
Book getBook(int i){
    return bookList[i];
}
//
void setEmissionIfnoLight(){ // used in draw lamp in room
	if(movingLight) return;
	float white[] = {1,1,1,1};
    glMaterialfv(GL_FRONT_AND_BACK,GL_EMISSION,white);
}
//
float getTimeFactor(int objID_){
    if( objID_==NO_TIME_FACTOR) return 0;
    if( activeObjID==TIME_FACTOR_FOR_ALL) return timeFactor;
    if( objID_==activeObjID) return timeFactor;
	return 0;
}
