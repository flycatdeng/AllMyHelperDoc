precision highp float;
uniform sampler2D sTexture;//纹理内容数据,可能不传

varying highp vec2 vTextureCoord;

void main()
{
   //将计算出的颜色给此片元
   vec4 finalColor=texture2D(sTexture, vTextureCoord);
   gl_FragColor=finalColor;
}