package app.mathhelper.shape.shape3d;

import app.mathhelper.shape.ObjectInfo;
import app.mathhelper.shape.ObjectInfoCalculator;
import app.mathhelper.shape.Vertex;
import app.mathhelper.shape.shape2d.Vertex2D;

public class Vertex3D extends Vertex<Vertex3D>{
	public double x,y,z;
	public int numOfCon;
	
	public Vertex3D() {
		this("", 0 ,0 ,0);
	}
	
	public Vertex3D(String name,double x, double y, double z) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.numOfCon = 0;

		this.info = ObjectInfoCalculator.getObjectInfo(this);
	}
	
	public Vertex3D(String name,double x, double y, double z, ObjectInfo info) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.numOfCon = 0;

		this.info = info;
	}
	
	public static double dist(Vertex3D a, Vertex3D b) {
		return Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y) + (a.z-b.z)*(a.z-b.z));
	}
	
	@Override
	public double getDotProduct(Vertex3D v) {
		if(!(v instanceof Vertex3D))
				return 0;
		
		Vertex3D v3d = (Vertex3D) v;
		
		Vertex3D t = new Vertex3D(this.name, v3d.x, v3d.y, v3d.z);
		
		return this.x*t.x + this.y*t.y + this.z*t.z;
	}
	
	@Override
	public Vertex3D getCrossProduct(Vertex3D v) {
		if(!(v instanceof Vertex3D))
			return new Vertex3D("", 0, 0, 0);
	
		Vertex3D v3d = (Vertex3D) v;
		Vertex3D t = new Vertex3D(this.name, v3d.x, v3d.y, v3d.z);
		Vertex3D temp = new Vertex3D("Normal", 0, 0, 0);
		
		temp.x = this.y*t.z - this.z*t.y;
		temp.y = this.z*t.x - this.x*t.z;
		temp.z = this.x*t.y - this.y*t.x;
		
		return temp;
	}
	
	@Override
	public String toString() {
		return this.name+" ("+Math.round(this.x*1000)/1000.0+", "+Math.round(this.y*1000)/1000.0+", "+Math.round(this.z*1000)/1000.0+")";
	}
	
	@Override
	public Vertex3D getCopy() {
		return new Vertex3D(""+this.name, this.x, this.y, this.z, info);
	}

	@Override
	public double getCrossProduct(Vertex2D v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] getCordsArr() {
		return new double[] {x, y, z};
	}

	@Override
	public void setCordsByArr(double[] arr) {
		this.x = arr[0];
		this.y = arr[1];
		this.z = arr[2];
	}
}
