// Large point texture

#version 120

uniform sampler2D img;

void main()
{
   //  Sample texture
   vec4 color = texture2D(img,gl_PointCoord.st);
   //  Blend point color with texture
   gl_FragColor = gl_Color*color;
}
