precision highp float;

uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uMMatrix; //变换矩阵
uniform vec3 uLightLocation;	//光源位置
uniform vec3 uCamera;	//摄像机位置

attribute vec3 aPosition;  //顶点位置
attribute vec3 aNormal;    //顶点法向量
attribute vec2 aTexCoor;    //顶点纹理坐标

varying highp vec2 vTextureCoord;

void main()
{
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置

   vTextureCoord.x=aTexCoor.x;
   vTextureCoord.y=1.0-aTexCoor.y;
}