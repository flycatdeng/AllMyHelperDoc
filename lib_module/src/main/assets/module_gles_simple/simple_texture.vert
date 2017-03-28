precision highp float;

//attribute vec2 a_position;
attribute vec4 position;
void main() {
   //gl_Position = vec4(a_position,0.0,1.0);
    gl_Position = position;
}
