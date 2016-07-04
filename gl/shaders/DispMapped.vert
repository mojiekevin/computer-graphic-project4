#version 120

// Note: We multiply a vector with a matrix from the left side (M * v)!
// mProj * mView * mWorld * pos

// RenderCamera Input
uniform mat4 mViewProjection;

// RenderObject Input
uniform mat4 mWorld;
uniform mat3 mWorldIT;
uniform float dispMagnitude;

// RenderMesh Input
attribute vec4 vPosition; // Sem (POSITION 0)
attribute vec3 vNormal; // Sem (NORMAL 0)
attribute vec2 vUV; // Sem (TEXCOORD 0)
// Shading Information

varying vec2 fUV;
varying vec3 fN; // normal at the vertex
varying vec4 worldPos; // vertex position in world-space coordinates

void main() {
  // TODO A4
  vec4 heimvalue = getNormalColor(vUV);
  float aveheimvalue = (heimvalue[0]+heimvalue[1]+heimvalue[2])/3.0;
  vec4 Position = vPosition + vec4(vNormal,0.0) * aveheimvalue * dispMagnitude;
  worldPos = mWorld * Position;
  gl_Position = mViewProjection * worldPos;
  
  // We have to use the inverse transpose of the world transformation matrix for the normal
  fN = normalize((mWorldIT * vNormal).xyz);
  fUV = vUV;
}