package cs4620.common.texture;

import egl.math.Color;
import egl.math.Colord;
import egl.math.Vector2i;
import egl.math.Vector3d;

public class TexGenSphereNormalMap extends ACTextureGenerator {
	// 0.5f means that the discs are tangent to each other
	// For greater values discs intersect
	// For smaller values there is a "planar" area between the discs
	private float bumpRadius;
	// The number of rows and columns
	// There should be one disc in each row and column
	private int resolution;
	
	public TexGenSphereNormalMap() {
		this.bumpRadius = 0.5f;
		this.resolution = 10;
		this.setSize(new Vector2i(256));
	}
	
	public void setBumpRadius(float bumpRadius) {
		this.bumpRadius = bumpRadius;
	}
	
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
	
	@Override
	public void getColor(float u, float v, Color outColor) {
		// TODO A4
		//calculate the TBN matrix
		double Phi = 2 * Math.PI * u;
		double Theta = Math.PI * (1.0f-v);
		double x = Math.cos(Phi) * Math.sin(Theta);
		double y = Math.sin(Phi) * Math.sin(Theta);
		double z = Math.cos(Theta);
		double a = -Math.sin(Phi);
		double b = Math.cos(Phi);
		double c = Math.cos(Phi) * Math.cos(Theta);
		double d = Math.sin(Phi) * Math.cos(Theta);
		double e = -Math.sin(Theta);
		Vector3d T = new Vector3d(a, b, 0);
		Vector3d B = new Vector3d(c, d, e);
		Vector3d N = new Vector3d(x, y, z);
		Vector3d objectnormal = new Vector3d(x, y, z);
		//judge whether uv belongs to disc, if so, use center of disc as normal;if not, just use uv as normal   
		float step = 1.0f / resolution;
		float mindistance = 2.0f;
		float udisccenter = 0.0f;
		float vdisccenter = 0.0f;
		for(float i = 0.0f; i <= 1.0f; i += step){
			for(float j = 0.0f; j <= 1.0f; j += step){
				float distance = (float)Math.sqrt((u-i)*(u-i) + (v-j)*(v-j));
				if(distance <= mindistance){
					mindistance = distance;
					udisccenter = i;
					vdisccenter = j;
				}
			}
		}
		if(mindistance <= step*bumpRadius){
			double CPhi = 2 * Math.PI * udisccenter;
			double CTheta = Math.PI * (1.0f-vdisccenter);
			double nx = Math.cos(CPhi) * Math.sin(CTheta);
			double ny = Math.sin(CPhi) * Math.sin(CTheta);
			double nz = Math.cos(CTheta);
			objectnormal.set(nx, ny, nz);
		}
		//transform normal to world space
		Colord tnormal = new Colord(T.dot(objectnormal), B.dot(objectnormal), N.dot(objectnormal));
		tnormal.add(1).mul(1.0/2.0);
		outColor.set(tnormal);
	}
}
