precision highp float;
uniform sampler2D uTexture;//纹理内容数据,可能不传

varying highp vec2 vTextureCoord;
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;

void main()
{
   //gl_FragColor=vec4(0.0,1.0,0.0,1.0);

   vec4 finalColor=texture2D(uTexture, vTextureCoord);
   //gl_FragColor=finalColor*ambient+finalColor*specular+finalColor*diffuse;
   gl_FragColor=finalColor;
}