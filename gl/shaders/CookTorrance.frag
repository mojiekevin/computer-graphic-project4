#version 120

// You May Use The Following Functions As RenderMaterial Input
// vec4 getDiffuseColor(vec2 uv)
// vec4 getNormalColor(vec2 uv)
// vec4 getSpecularColor(vec2 uv)

// Lighting Information
const int MAX_LIGHTS = 16;
uniform int numLights;
uniform vec3 lightIntensity[MAX_LIGHTS];
uniform vec3 lightPosition[MAX_LIGHTS];
uniform vec3 ambientLightIntensity;

// Camera Information
uniform vec3 worldCam;
uniform float exposure;

// Shading Information
uniform float roughness;
// 0 : smooth, 1: rough

varying vec2 fUV;
varying vec3 fN; // normal at the vertex
varying vec4 worldPos; // vertex position in world coordinates

void main() {
  // TODO A4
	vec3 N = normalize(fN);
	vec3 V = normalize(worldCam - worldPos.xyz);
	
	vec4 finalColor = vec4(0.0, 0.0, 0.0, 0.0);

	for (int i = 0; i < numLights; i++) {
	  float r = length(lightPosition[i] - worldPos.xyz);
	  vec3 L = normalize(lightPosition[i] - worldPos.xyz); 
	  vec3 H = normalize(L + V); 
	  
	  // calculate Fresnel term
	     float Fbeta = 0.04 + (1.0-0.04) * pow(1.0-dot(V, H), 5.0);
	  // calculate Microfacet Distribution
	  	 float Dtheta = (1.0 / (pow(roughness,2.0) * pow(dot(N, H),4.0))) * (pow(2.71828, (pow(dot(N, H),2.0)-1.0) / (pow(roughness,2.0) * pow(dot(N, H),2.0))));
	  // calculate Geometric Attenuation
	  	 float G = min(min(2.0*dot(N, H)*dot(N, V)/dot(V, H), 2.0*dot(N, H)*dot(N, L)/dot(V, H)) , 1.0);
	  	 
	  	 finalColor += ((getSpecularColor(fUV)*Fbeta*Dtheta*G)/(3.14159*dot(N, V)*dot(N, L))+getDiffuseColor(fUV))*max(dot(N, L),0.0)*vec4(lightIntensity[i], 0.0)/(r*r);            
	 }
	 gl_FragColor = (finalColor + getSpecularColor(fUV)* vec4(ambientLightIntensity, 0.0)) * exposure; 	 
	  	 
}