/*
 *  Particle Shaders
 *
 *  Demonstrate a small particle shader using the Orange Book confetti cannon
 *  and a large particle shader using the bambi fire.
 *
 *  Key bindings:
 *  m/M        Toggle between particle shaders
 *  a          Toggle axes
 *  arrows     Change view angle
 *  PgDn/PgUp  Zoom in and out
 *  0          Reset view angle
 *  ESC        Exit
 */

#include "CSCIx239.h"
int axes=1;       //  Display axes
int mode=1;       //  Shader mode
int th=0;         //  Azimuth of view angle
int ph=0;         //  Elevation of view angle
int n;            //  Particle count
double asp=1;     //  Aspect ratio
double dim=3;   //  Size of world
#define FOV 10
double distance=200;
#define MODE 2
int shader=0; //  Shader programs
char* text[] = {"Confetti Cannon","Faster than light"};

//  Set up array indexes for program
#define VELOCITY_ARRAY   4
#define START_ARRAY 5
char* Name[] = {"","","","","Vel","Start",NULL};
//  Point arrays
#define N 100
float Vert[3*N*N];
float Color[3*N*N];
float Vel[3*N*N];
float Start[N*N];

/*
 *  Random numbers with range and offset
 */
static float frand(float rng,float off)
{
   return rand()*rng/RAND_MAX+off;
}

/*
 *  Initialize particles
 */
void InitPart(void)
{
   //  Array Pointers
   float* vert  = Vert;
   float* color = Color;
   float* vel   = Vel;
   float* start = Start;
   //  Loop over NxN patch
   int i,j;
   n =30;
   int k = 6;
   for (i=0;i<n;i++)
      for (j=0;j<n;j++)
      {
         //  Location x,y,z
         *vert++ = frand(dim*k,-dim*k*0.5);
         *vert++ = 0;
         *vert++ = frand(dim*k,-dim*k*0.5);
         //  Color r,g,b (0.5-1.0)
         *color++ = frand(0.5,0.5);
         *color++ = frand(0.5,0.5);
         *color++ = frand(0.5,0.5);
                  //  Velocity
         *vel++ = frand( 1.0,3.0);
         *vel++ = frand(10.0,0.0);
         *vel++ = frand( 1.0,3.0);
         //  Launch time
         *start++ = frand(4.0,1.0);
      }
}

/*
 *  Draw particles
 */
void DrawPart(void)
{
glEnable(GL_VERTEX_PROGRAM_POINT_SIZE);
   //  Set particle size
   glPointSize(50);
   //  Point vertex location to local array Vert
   glVertexPointer(3,GL_FLOAT,0,Vert);
   //  Point color array to local array Color
   glColorPointer(3,GL_FLOAT,0,Color);
   //  Point attribute arrays to local arrays
   glVertexAttribPointer(VELOCITY_ARRAY,3,GL_FLOAT,GL_FALSE,0,Vel);
   glVertexAttribPointer(START_ARRAY,1,GL_FLOAT,GL_FALSE,0,Start);
   //  Enable arrays used by DrawArrays
   glEnableClientState(GL_VERTEX_ARRAY);
   glEnableClientState(GL_COLOR_ARRAY);
   glEnableVertexAttribArray(VELOCITY_ARRAY);
   glEnableVertexAttribArray(START_ARRAY);
   //  Set transparent large particles
   if (mode)
   {
      glEnable(GL_POINT_SPRITE);
      glTexEnvi(GL_POINT_SPRITE,GL_COORD_REPLACE,GL_TRUE);
      glEnable(GL_BLEND);
      glBlendFunc(GL_SRC_ALPHA,GL_ONE);
      glDepthMask(0);
   }
   //  Draw arrays
   glDrawArrays(GL_POINTS,0,n*n);
   //  Reset
   if (mode)
   {
      glDisable(GL_POINT_SPRITE);
      glDisable(GL_BLEND);
      glDepthMask(1);
   }
   //  Disable arrays
   glDisableClientState(GL_VERTEX_ARRAY);
   glDisableClientState(GL_COLOR_ARRAY);
   glDisableVertexAttribArray(VELOCITY_ARRAY);
   glDisableVertexAttribArray(START_ARRAY);
}

/*
 *  OpenGL (GLUT) calls this routine to display the scene
 */
void display()
{
   int id;
   float time = 0.001*glutGet(GLUT_ELAPSED_TIME);

   //  Erase the window and the depth buffer
   glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

   //  Enable Z-buffering in OpenGL
   glEnable(GL_DEPTH_TEST);
   //  Undo previous transformations
   glLoadIdentity();
   //
   gluLookAt(0,distance,0 , 0,0,0 , 0,0,1);
      
   // glRotatef(90.0,1,0,0);
   //
   //  Draw scene
   //
   //  Select shader (0 => no shader)
   glUseProgram(shader);
   //  Set time
   id = glGetUniformLocation(shader,"time");
   if (id>=0) glUniform1f(id,time);
   id = glGetUniformLocation(shader,"distance");
   if (id>=0) glUniform1f(id,distance);
   id = glGetUniformLocation(shader,"img");
   if (id>=0) glUniform1i(id,0);

   //  Draw the particles
   DrawPart();

   //  No shader for what follows
   glUseProgram(0);

   //  Draw axes - no lighting from here on
   glColor3f(1,1,1);
   if ( 0){
   float len = 2;
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
   Print("FPS=%d ::  %s",
     FramesPerSecond(),text[mode]);
   //  Render the scene and make it visible
   ErrCheck("display");
   glFlush();
   glutSwapBuffers();
}

/*
 *  GLUT calls this routine when the window is resized
 */
void idle()
{
   //  Tell GLUT it is necessary to redisplay the scene
   glutPostRedisplay();
}

/*
 *  GLUT calls this routine when an arrow key is pressed
 */
void special(int key,int x,int y)
{
   //  Update projection
   Project(FOV,asp,dim);
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
   Project(FOV,asp,dim);
}

//
//  Create Shader Program with Location Names
//
int CreateShaderProgLoc(char* VertFile,char* FragFile,char* Name[])
{
   int k;
   //  Create program
   int prog = glCreateProgram();
   //  Create and compile vertex shader
   if (VertFile) CreateShader(prog,GL_VERTEX_SHADER,VertFile);
   //  Create and compile fragment shader
   if (FragFile) CreateShader(prog,GL_FRAGMENT_SHADER,FragFile);
   //  Set names
   for (k=0;Name[k];k++)
      if (Name[k][0])
         glBindAttribLocation(prog,k,Name[k]);
   ErrCheck("CreateShaderProg");
   //  Link program
   glLinkProgram(prog);
   //  Check for errors
   PrintProgramLog(prog);
   //  Return name
   return prog;
}

/*
 *  Start up GLUT and tell it what to do
 */
int main(int argc,char* argv[])
{
   //  Initialize GLUT
   glutInit(&argc,argv);
   //  Request double buffered, true color window with Z buffering at 600x600
   glutInitDisplayMode(GLUT_RGB | GLUT_DEPTH | GLUT_DOUBLE | GLUT_ALPHA);
   glutInitWindowSize(600,600);
   glutCreateWindow("Faster than light");
#ifdef USEGLEW
   //  Initialize GLEW
   if (glewInit()!=GLEW_OK) Fatal("Error initializing GLEW\n");
   if (!GLEW_VERSION_2_0) Fatal("OpenGL 2.0 not supported\n");
#endif
   //  Set callbacks
   glutDisplayFunc(display);
   glutReshapeFunc(reshape);
   glutSpecialFunc(special);
   glutKeyboardFunc(key);
   glutIdleFunc(idle);
   //  Confetti Cannon needs no fragment shader, but adds Vel and Start
   shader = CreateShaderProgLoc("ftl.vert","ftl.frag",Name);
   //  Load random texture
   CreateNoise3D(GL_TEXTURE1);
   //  Load smoke particle
   LoadTexBMP("particle.bmp");
   //  Initialize particles
   InitPart();
   //  Pass control to GLUT so it can interact with the user
   ErrCheck("init");
   glutMainLoop();
   return 0;
}
