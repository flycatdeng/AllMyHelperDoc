precision highp float;

// textures
uniform sampler2D u_texture;
// the texCoords passed in from the vertex shader.
uniform vec2 u_resolution;//屏幕宽高信息
uniform float u_textureRatio;//图片的宽高比

vec2 texCoord(){//gl_FragCoord:当前片元相对于窗口位置的坐标值（绝对值，不是相对值哦，也就是说最大值是窗口的宽高）
  return vec2(gl_FragCoord.x, u_resolution.y-gl_FragCoord.y)/u_resolution;//得到当前片元的纹理坐标（相对值）
}

// scales the bg up and proportionally to fill the container
vec2 scaledTexCoord(){
  float ratio=u_resolution.x/u_resolution.y;//屏幕宽高比
  vec2 scale=vec2(1.0,1.0);
  vec2 offset=vec2(0.0,0.0);
  float ratioDelta=ratio-u_textureRatio;
  if(ratioDelta>=0.0){//如果屏幕宽高比大于图片宽高比
    scale.y=(1.0+ratioDelta);
    offset.y=ratioDelta/2.0;
  }else{//如果屏幕宽高比小于等于图片宽高比
    scale.x=(1.0-ratioDelta);
    offset.x=-ratioDelta/2.0;
  }
  return (texCoord()+offset)/scale;
}


void main() {//主入口
    vec2 bgOriginCoor=scaledTexCoord();
    vec4 bg=texture2D(u_texture,bgOriginCoor);
    gl_FragColor = bg;
}
