//  Lame fire

#version 120

uniform   float time;      //  Time
uniform float distance;
attribute float Start;     //  Start time

void main(void)
{
   //  Particle life is mod Start time
   float t = mod(time,Start);
   float tRatio = t/Start;
   //  Initial position
   vec4 vert = gl_Vertex;
   vert.y += tRatio*distance;//tRatio*factor;
   gl_PointSize = tRatio*60.0;
   //  Noisy yellow trending to red
   gl_FrontColor = gl_Color;
   //  Transform particle location
   gl_Position = gl_ModelViewProjectionMatrix*vert;
}
