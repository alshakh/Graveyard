
#define DRAW_CENTER 0
#define DRAW_XYCENTER 1
#define DRAW_ZERO_CORNER 2
//
#define DEFAULT_ANGLE_INC 10
//
void p2c(float phi, float * x, float *y);
void s2c(float phi,float theta, float * x, float *y,float *z);
void drawUnitSphere(int drawMode);
//
void drawUnitCylinder(int drawMode);
void drawUnitCylinder_Texture(int drawMode,unsigned int texture);
//
void drawUnitCube(int drawMode);
void drawUnitCube_Color(int drawMode, float faceColor[6][3]);
void drawUnitCube_Texture(int drawMode, unsigned int texture);
void drawUnitCube_ColorTexture(int drawMode, int isColored,float faceColor[6][3],unsigned int texture);



