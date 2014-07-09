/*
 */
#include "CSCIx229.h"
int axes=0;		 //  Display axes
int th=-30;			//  Azimuth of view angle
int ph=25;			//  Elevation of view angle
int zh=0;			//  Azimuth of light
int eA=0;           // rotation of earth
int fov=55;		 //  Field of view (for perspective)
double asp=1;	  //  Aspect ratio
double dim=5.0;	//  Size of world
int	 light=1;	 //  Lighting
unsigned int woodTex,blackTileTex,earthBalltex;
int booknum = 0;
int mode = 0;
//
float ylight = 0.4;
float lightMove = 1;
float lightDistance = 5;
//
float increment = 0.1;
/*
 *  Draw a ball
 *	  at (x,y,z)
 *	  radius r
 */
typedef struct {
	float x;
 	float y;
 	float z;
 	float l1;
 	float l2;
 	unsigned int cover;
 	unsigned int pages;
} Book;
Book books[3];
//
static void ball(double x,double y,double z,double r)
{
	//  Save transformation
	glPushMatrix();
	//  Offset, scale and rotate
	glTranslated(x,y,z);
	glScaled(r,r,r);
	//  White ball
	//glColor3f(1,1,1);
	glutSolidSphere(1.0,16,16);
	//  Undo transofrmations
	glPopMatrix();
}
// polar to cartesian
// phi= angle from +x in xy plane
void p2c(float phi, float * x, float *y){
	*x=Cos(phi);//*COS(theta);
	*y=Sin(phi);//*COS(theta);
	//*z=SIN(theta);
}
//
void cylinder(){
    glEnable(GL_TEXTURE_2D);
    float mat_ambient[] ={0.5f ,	0.5f 	,0.5f ,1.0f};
    float mat_diffuse []={0.7, 	0.7,	0.7  ,1.0f};
    float mat_specular []={ 	1.0f ,	1.0f,	1.0f ,1.0f};
    float shine =0.9*128 ;
    glMaterialfv(GL_FRONT_AND_BACK,GL_SPECULAR,mat_specular);
    glMaterialfv(GL_FRONT_AND_BACK,GL_DIFFUSE,mat_diffuse);
    glMaterialfv(GL_FRONT_AND_BACK,GL_AMBIENT,mat_ambient);
    
    glMaterialf(GL_FRONT_AND_BACK,GL_SHININESS,shine);
    
    int angleInc = 5;
	int phi=0;
	float x,y;
	//glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
    glBindTexture(GL_TEXTURE_2D,blackTileTex);
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(0,0,-1);
	glVertex3d(0,0,0);
	glColor3f(1,1,1);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glTexCoord2f(0.5+0.5*Cos(phi),0.5+0.5*Sin(phi));
		glVertex3d(x,y,0);
	}
	glEnd();
	//
	glBegin(GL_QUAD_STRIP);
	glColor3f(1,1,1);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glNormal3f(x,y,0);
		glTexCoord2f(phi/360.0,1);glVertex3d(x,y,1);
		glTexCoord2f(phi/360.0,0);glVertex3d(x,y,0);
	}
	glEnd();
	//
	glColor3f(1,1,1);
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(0,0,1);
	glVertex3d(0,0,1);
	for(phi=0; phi<=360; phi+=angleInc){
		p2c(phi,&x,&y);
		glTexCoord2f(0.5+0.5*Cos(phi),0.5+0.5*Sin(phi));
		glVertex3d(x,y,1);
	}
	glEnd();
	glDisable(GL_TEXTURE_2D);
}



/*
 *  Draw vertex in polar coordinates
 */
static void Vertex(int th,int ph)
{
   double x = -Sin(th)*Cos(ph);
   double y =  Cos(th)*Cos(ph);
   double z =          Sin(ph);
   glNormal3d(x,y,z);
   glTexCoord2d(th/360.0,ph/180.0+0.5);
   glVertex3d(x,y,z);
}

/*
 *  Draw planet
 */
void drawEarth()
{
   int th,ph;

   /*
    *  Draw surface of the planet
    */
   //  Set texture
   glEnable(GL_TEXTURE_2D);
   glBindTexture(GL_TEXTURE_2D,earthBalltex);
   //  Latitude bands
   glColor3f(1,1,1);
   for (ph=-90;ph<90;ph+=5)
   {
      glBegin(GL_QUAD_STRIP);
      for (th=0;th<=360;th+=5)
      {
         Vertex(th,ph);
         Vertex(th,ph+5);
      }
      glEnd();
   }
   glDisable(GL_TEXTURE_2D);
}
void globe(){
    glPushMatrix();
    glTranslated(0,0,0.25);
    glScaled(0.15,0.15,0.15);
    glRotated(-23.5,1,0,0);
    glRotated(eA,0,0,1);
    drawEarth();
    glPopMatrix();
    glPushMatrix();
    glScaled(0.07,0.07,0.04);
    cylinder();
    glPopMatrix();
    glPushMatrix();
    glTranslated(0,0,0.04);
    glScaled(0.02,0.02,0.1);
    cylinder();
    glPopMatrix();
}
static void closedBook(Book book)
{
	glEnable(GL_TEXTURE_2D);
	//  Save transformation
	glPushMatrix();
	glScaled(book.x/2,book.y/2,book.z/2);
	glTranslated(1,1,1);
	//  Cube
	//glColor3f(1,1,1);
	glBindTexture(GL_TEXTURE_2D,book.cover);	
	glBegin(GL_QUADS);
	//  Front
	glNormal3f( 0, 0, 1);
	glColor3f(1,1,1);
	glTexCoord2f(book.l2,1); glVertex3f(-1,-1, 1);
	glTexCoord2f(book.l2,0); glVertex3f(+1,-1, 1);
	glTexCoord2f(1,0); glVertex3f(+1,+1, 1);	
	glTexCoord2f(1,1); glVertex3f(-1,+1, 1);
	glEnd();
	//
	//  Back
	glBindTexture(GL_TEXTURE_2D,book.cover);	
	glBegin(GL_QUADS);
	glNormal3f( 0, 0,-1);
	glColor3f(1,1,1);
	glTexCoord2f(book.l1,0) ;glVertex3f(+1,-1,-1);
	glTexCoord2f(book.l1,1) ;glVertex3f(-1,-1,-1);
	glTexCoord2f(0,1);glVertex3f(-1,+1,-1);
	glTexCoord2f(0,0);glVertex3f(+1,+1,-1);
	glEnd();
	//
	glBindTexture(GL_TEXTURE_2D,book.pages);	
	glBegin(GL_QUADS);
	//  Right
	glColor3f(1,1,1);
	glNormal3f(+1, 0, 0);
	glTexCoord2f(0,0) ;glVertex3f(+1,-1,+1);
	glTexCoord2f(0,1) ;glVertex3f(+1,-1,-1);
	glTexCoord2f(1,1) ;glVertex3f(+1,+1,-1);
	glTexCoord2f(1,0) ;glVertex3f(+1,+1,+1);
		glEnd();
	//
	glBindTexture(GL_TEXTURE_2D,book.pages);	
	glBegin(GL_QUADS);
	//  Left
	glColor3f(1,1,1);
	glNormal3f(-1, 0, 0);
	glTexCoord2f(0,0) ;glVertex3f(-1,-1,-1);
	glTexCoord2f(0,1) ;glVertex3f(-1,-1,+1);
	glTexCoord2f(1,1) ;glVertex3f(-1,+1,+1);
	glTexCoord2f(1,0) ;glVertex3f(-1,+1,-1);
	glEnd();
	//
	//  Top
	glBindTexture(GL_TEXTURE_2D,book.pages);	
	glBegin(GL_QUADS);

	glColor3f(1,1,1);
	glNormal3f( 0,+1, 0);
	glTexCoord2f(0,1) ;glVertex3f(-1,+1,+1);
	glTexCoord2f(1,1) ;glVertex3f(+1,+1,+1);
	glTexCoord2f(1,0) ;glVertex3f(+1,+1,-1);
	glTexCoord2f(0,0) ;glVertex3f(-1,+1,-1);
		glEnd();
	//
	//  Bottom
	glBindTexture(GL_TEXTURE_2D,book.cover);	
	glBegin(GL_QUADS);

	glColor3f(1,1,1);
	
	glNormal3f( 0,-1, 0);
	glTexCoord2f(book.l1,1) ;glVertex3f(-1,-1,-1);
	glTexCoord2f(book.l1,0) ;glVertex3f(+1,-1,-1);
	glTexCoord2f(book.l2,0) ;glVertex3f(+1,-1,+1);
	glTexCoord2f(book.l2,1) ;glVertex3f(-1,-1,+1);
	//  End
	glEnd();
	//  Undo transofrmations
	glPopMatrix();
	glDisable(GL_TEXTURE_2D);
}
static void tableCube()
{	
	glEnable(GL_TEXTURE_2D);
   //  Save transformation
   glPushMatrix();
   //  Cube
   glTranslated(-0.5,-0.5,0);
   glScaled(0.5,0.5,0.5);
   glTranslated(1,1,1);
   //
   glBindTexture(GL_TEXTURE_2D,woodTex);
   glBegin(GL_QUADS);
   //  Front
   glTexCoord2f(0.5,0.5);
   glColor3f(1,1,1);
   glNormal3f( 0, 0, 1);
   glTexCoord2f(0,0);glVertex3f(-1,-1, 1);
   glTexCoord2f(0,1);glVertex3f(+1,-1, 1);
   glTexCoord2f(1,1);glVertex3f(+1,+1, 1);
   glTexCoord2f(1,0);glVertex3f(-1,+1, 1);
   //  Back
   glColor3f(1,1,1);
   glNormal3f( 0, 0,-1);
   glTexCoord2f(0,0);glVertex3f(+1,-1,-1);
   glTexCoord2f(0,1);glVertex3f(-1,-1,-1);
   glTexCoord2f(1,1);glVertex3f(-1,+1,-1);
   glTexCoord2f(1,0);glVertex3f(+1,+1,-1);
   //  Right
   glColor3f(1,1,1);
   glNormal3f(+1, 0, 0);
   glTexCoord2f(0,0);glVertex3f(+1,-1,+1);
   glTexCoord2f(0,1);glVertex3f(+1,-1,-1);
   glTexCoord2f(1,1);glVertex3f(+1,+1,-1);
   glTexCoord2f(1,0);glVertex3f(+1,+1,+1);
   //  Left
   glColor3f(1,1,1);
   glNormal3f(-1, 0, 0);
   glTexCoord2f(0,0);glVertex3f(-1,-1,-1);
   glTexCoord2f(0,1);glVertex3f(-1,-1,+1);
   glTexCoord2f(1,1);glVertex3f(-1,+1,+1);
   glTexCoord2f(1,0);glVertex3f(-1,+1,-1);
   //  Top
   glColor3f(1,1,1);
   glNormal3f( 0,+1, 0);
   glTexCoord2f(0,1);glVertex3f(-1,+1,+1);
   glTexCoord2f(1,1);glVertex3f(+1,+1,+1);
   glTexCoord2f(1,0);glVertex3f(+1,+1,-1);
   glTexCoord2f(0,0);glVertex3f(-1,+1,-1);
   //  Bottom
   glColor3f(1,1,1);
   glNormal3f( 0,-1, 0);
   glTexCoord2f(0,1);glVertex3f(-1,-1,-1);
   glTexCoord2f(1,1);glVertex3f(+1,-1,-1);
   glTexCoord2f(1,0);glVertex3f(+1,-1,+1);
   glTexCoord2f(0,0);glVertex3f(-1,-1,+1);
   //  End
   glEnd();
   //  Undo transofrmations
   glPopMatrix();
   glDisable(GL_TEXTURE_2D);
}
void legs(){
    glPushMatrix();
    glScaled(0.07,0.07,0.4);
    tableCube();
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0.5,0,0);
    glScaled(0.07,0.07,0.4);
    tableCube();
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0,0.5,0);
    glScaled(0.07,0.07,0.4);
    tableCube();
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0.5,0.5,0);
    glScaled(0.07,0.07,0.4);
    tableCube();
    glPopMatrix();
}
void bookStack(){
    int phi[] = {25,0,40};
    int i = 0 ;
    for(i=0 ; i<3; i ++){
        if(i>0) glTranslated(0,0,books[i-1].z);
        glPushMatrix();
        glRotated(phi[i] , 0 , 0 ,1);
        closedBook(books[i]);
        glPopMatrix();
    }
}
void table(){
    glPushMatrix();
    glTranslated(0.07/2,0.07/2,0);
    legs();
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0,0,0.4);
    glScaled(0.5+0.07,0.5+0.07,0.07);
    glTranslated(0.5,0.5,0);
    tableCube();
    glPopMatrix();
    //
    glPushMatrix();
    glTranslated(0.25,0.25,0.4+0.07);
    bookStack();
    glPopMatrix();
     glPushMatrix();
    glTranslated(0.1,0.1,0.4+0.07);
       globe();
    glPopMatrix();

}

/*
 *  OpenGL (GLUT) calls this routine to display the scene
 */
void display()
{
   const double len=dim/2;  //  Length of axes
   double Ex = -2*dim*Sin(th)*Cos(ph);
   double Ey = +2*dim        *Sin(ph);
   double Ez = +2*dim*Cos(th)*Cos(ph);
   //  Erase the window and the depth buffer
   glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
   //  Enable Z-buffering in OpenGL
   glEnable(GL_DEPTH_TEST);
   //  Undo previous transformations
   glLoadIdentity();
   //  Perspective - set eye position
   gluLookAt(Ex,Ey,Ez , 0,0,0 , 0,Cos(ph),0);
   //  Light switch
   if (light)
   {
      //  Translate intensity to color vectors
      float Ambient[]   = {0.3,0.3,0.3,1.0};
      float Diffuse[]   = {1,1,1,1};
      float Specular[]  = {1,1,0,1};
      float white[]     = {1,1,1,1};
      //  Light direction
      float Position[]  = {lightDistance*Cos(zh),ylight,lightDistance*Sin(zh),1.0};
      //  Draw light position as ball (still no lighting here)
      ball(Position[0],Position[1],Position[2] , 0.1);
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
    glDisable(GL_LIGHTING);
    glRotated(-90,1,0,0);
    if(mode==0){
        glPushMatrix();
        glScaled(5,5,5);
        glTranslated(-(0.5+0.07)/2 , -(0.5+0.07)/2 , -0.3 );
	    //globe();
        table();
	    glPopMatrix();
    } else if (mode==2){
        glPushMatrix();
        glRotated(90,1,0,0);
        glScaled(10,10,10);
        glTranslated(-0.5*books[0].x,-0.5*books[0].y,-0.5*books[0].z);
        closedBook(books[0]);
	    glPopMatrix();
    }else if (mode==1){
        glPushMatrix();
        glRotated(90,1,0,0);
        glScaled(10,10,10);
        glTranslated(-0.5*books[1].x,-0.5*books[1].y,-0.5*books[1].z);
        closedBook(books[1]);
	    glPopMatrix();
    }else if (mode==3){
        glPushMatrix();
        glRotated(90,1,0,0);
        glScaled(10,10,10);
        glTranslated(-0.5*books[2].x,-0.5*books[2].y,-0.5*books[2].z);
        closedBook(books[2]);
	    glPopMatrix();
    }else if (mode==4){
        glPushMatrix();
        glScaled(5,5,5);
        globe();
	    glPopMatrix();
    }
	//  Draw axes
	glColor3f(1,1,1);
	glDisable(GL_LIGHTING);
	//glColor3f(1,1,1);
	if (axes)
	{
		glBegin(GL_LINES);
		glVertex3d(0.0,0.0,0.0);
		glVertex3d(len,0.0,0.0);
		glVertex3d(0.0,0.0,0.0);
		glVertex3d(0.0,len,0.0);
		glVertex3d(0.0,0.0,0.0);
		glVertex3d(0.0,0.0,len);
		glEnd();
		//  Label axes
		glRasterPos3d(len,0.0,0.0);
		Print("X");
		glRasterPos3d(0.0,len,0.0);
		Print("Y");
		glRasterPos3d(0.0,0.0,len);
		Print("Z");
	}
	//  Display parameters
	glWindowPos2i(5,5);
	Print("Angle=%d,%d  Dim=%.1f Light=%s",th,ph,dim,light?"On":"Off");
	//  Render the scene and make it visible
	ErrCheck("display");
	glFlush();
	glutSwapBuffers();
}

/*
 *  GLUT calls this routine when an arrow key is pressed
 */
void special(int key,int x,int y)
{
	//  Right arrow key - increase angle by 5 degrees
	if (key == GLUT_KEY_RIGHT)
		th += 5;
	//  Left arrow key - decrease angle by 5 degrees
	else if (key == GLUT_KEY_LEFT)
		th -= 5;
	//  Up arrow key - increase elevation by 5 degrees
	else if (key == GLUT_KEY_UP)
		ph += 5;
	//  Down arrow key - decrease elevation by 5 degrees
	else if (key == GLUT_KEY_DOWN)
		ph -= 5;
	//  PageUp key - increase dim
	else if (key == GLUT_KEY_PAGE_DOWN)
		dim += 0.1;
	//  PageDown key - decrease dim
	else if (key == GLUT_KEY_PAGE_UP && dim>1)
		dim -= 0.1;
	//  Keep angles to +/-360 degrees
	th %= 360;
	ph %= 360;
	//  Update projection
	Project(fov,asp,dim);
	//  Tell GLUT it is necessary to redisplay the scene
	glutPostRedisplay();
}
/*
 *  GLUT calls this routine when the window is resized
 */
void idle()
{
	//  Elapsed time in seconds
	double t = glutGet(GLUT_ELAPSED_TIME)/1000.0;
	if(lightMove) 
	    zh = fmod(90*t,360.0);
	    
	    eA = fmod(30*t,360.0);
	//  Tell GLUT it is necessary to redisplay the scene
	glutPostRedisplay();
}

/*
 *  GLUT calls this routine when a key is pressed
 */
void key(unsigned char ch,int x,int y)
{
	//  Exit on ESC
	if (ch == 27)
		exit(0);
	//  Reset view angle
	else if (ch == '0')
		th = ph = 0;
	//  Toggle axes
	else if (ch == 'a' || ch == 'A')
		axes = 1-axes;
	//  Toggle light
	else if (ch == 'l' || ch == 'L')
		light = 1-light;
	else if (ch=='1')
	    mode = 0;
	else if (ch=='2')
	    mode = 1;
	else if (ch=='3')
	    mode = 2;
	else if (ch=='4')
	    mode = 3;
	else if (ch=='5')
	    mode = 4;
	else if (ch == 'm' || ch == 'M')
      lightMove = 1-lightMove;
       else if (ch=='[')
      ylight -= 0.1;
   else if (ch==']')
      ylight += 0.1;
	//  Reproject
	Project(fov,asp,dim);
	
	//  Tell GLUT it is necessary to redisplay the scene
	glutPostRedisplay();
}

/*
 *  GLUT calls this routine when the window is resized
 */
void reshape(int width,int height)
{
	//  Ratio of the width to the height of the window
	asp = (height>0) ? (double)width/height : 1;
	//  Set the viewport to the entire window
	glViewport(0,0, width,height);
	//  Set projection
	Project(fov,asp,dim);
}


/*
 *  Start up GLUT and tell it what to do
 */
int main(int argc,char* argv[])
{
	//  Initialize GLUT
	glutInit(&argc,argv);
	//  Request double buffered, true color window with Z buffering at 600x600
	glutInitDisplayMode(GLUT_RGBA | GLUT_DEPTH | GLUT_DOUBLE);
	glutInitWindowSize(600,600);
	glutCreateWindow("Ahmed Alshakh - HW 3");
	//  Set callbacks
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutSpecialFunc(special);
	glutKeyboardFunc(key);
	glutIdleFunc(idle);
	//  Load textures
	//  Pass control to GLUT so it can interact with the user
	ErrCheck("init");
	//////////////////////////////////////
	unsigned int pages = LoadTexBMP("images/pages.bmp");
	woodTex = LoadTexBMP("images/darkwood.bmp"); 
	blackTileTex = LoadTexBMP("images/black_tile.bmp");
	
	earthBalltex = LoadTexBMP("images/earth.bmp");
	//
	books[0].x = 0.3;
	books[0].y = 0.25,
	books[0].z = 0.07;
	books[0].l1 =0.426;
	books[0].l2 =0.6;
	books[0].cover =  LoadTexBMP("images/1.bmp");
	books[0].pages = pages;
	//
	books[1].x = 0.40;
	books[1].y = 0.30,
	books[1].z = 0.10;
	books[1].l1 =0.4256;
	books[1].l2 =0.5901;
	books[1].cover =  LoadTexBMP("images/2.bmp");
	books[1].pages = pages;
	//
	books[2].x = 0.3;
	books[2].y = 0.2,
	books[2].z = 0.03;
	books[2].l1 = 0.4567;
	books[2].l2 = 0.6;
	books[2].cover =  LoadTexBMP("images/3.bmp");
	books[2].pages = pages;
	//
	
	///////////////////////////////////////
	glutMainLoop();
	return 0;
}



