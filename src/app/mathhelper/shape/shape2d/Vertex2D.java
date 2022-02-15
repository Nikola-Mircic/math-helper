package app.mathhelper.shape.shape2d;

import app.mathhelper.shape.ObjectInfo;
import app.mathhelper.shape.ObjectInfoCalculator;
import app.mathhelper.shape.Vertex;
import app.mathhelper.shape.shape3d.Vertex3D;

public class Vertex2D extends Vertex<Vertex2D> {
	public double x;
	public double y;
	
	public Vertex2D(String name, double x, double y) {
		this.name = name;
		this.x = x;
		this.y = y;

		this.info = ObjectInfoCalculator.getObjectInfo(this);
	}
	
	public Vertex2D(String name, double x, double y, ObjectInfo info) {
		this.name = name;
		this.x = x;
		this.y = y;

		this.info = info;
	}

	@Override
	public double getDotProduct(Vertex2D v) {
		return this.x * ((Vertex2D)v).x + this.y * ((Vertex2D)v).y;
	}

	@Override
	public double getCrossProduct(Vertex2D v) {
		return (this.x*v.y - this.y*v.x);
	}

	@Override
	public double getLenght() {
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}

	@Override
	public Vertex2D getCopy() {
		return new Vertex2D(""+this.name, this.x, this.y, info);
	}
	
	public static double dist(Vertex2D a, Vertex2D b) {
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		
		return Math.sqrt(dx*dx + dy*dy);
	}

	@Override
	public Vertex3D getCrossProduct(Vertex3D v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getCordsArr() {
		double[] t = {this.x, this.y};
		return t;
	}

	@Override
	public void setCordsByArr(double[] arr) {
		this.x = arr[0];
		this.y = arr[1];
	}

}
