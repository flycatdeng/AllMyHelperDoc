precision highp float;

attribute vec3 aPosition;  //顶点位置
attribute vec2 aTexCoor;    //顶点纹理坐标

uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uModelMatrix; //变换矩阵
uniform mat4 uViewMatrix; //相机位置矩阵
uniform mat4 uModelViewMatrix; //变换矩阵

//用于传递给片元着色器的变量
varying highp vec2 vTextureCoord;

void main()
{
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置

   vTextureCoord.x=aTexCoor.x;
   vTextureCoord.y=1.0-aTexCoor.y;
}