package app.mathhelper.shape.shape2d;

import app.mathhelper.shape.Vertex;
import app.mathhelper.shape.shape3d.Vertex3D;

public class Vertex2D extends Vertex {
	public double x;
	public double y;
	
	public Vertex2D(String name, double x, double y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Vertex v) {
		if(!(v instanceof Vertex2D))
			return false;
		
		if(this.name.contentEquals(v.name))
			return true;
		
		if(this.x == ((Vertex2D)v).x && this.y == ((Vertex2D)v).y)
			return true;
		
		return false;
	}

	@Override
	public Vertex add(Vertex v) {
		Vertex2D temp = new Vertex2D(this.name, 0, 0);
		
		temp.x = this.x + ((Vertex2D)v).x;
		temp.y = this.y + ((Vertex2D)v).y;
		
		return temp;
	}

	@Override
	public double getDotProduct(Vertex v) {
		return this.x * ((Vertex2D)v).x + this.y * ((Vertex2D)v).y;
	}

	@Override
	public Vertex3D getCrossProduct(Vertex v) {
		Vertex3D v1 = new Vertex3D("", this.x, this.y, 0);
		Vertex3D v2 = new Vertex3D("", ((Vertex2D)v).x, ((Vertex2D)v).y, 0);
		
		return v1.getCrossProduct(v2);
	}

	@Override
	public double getLenght() {
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}

	@Override
	public Vertex2D getCopy() {
		return new Vertex2D(""+this.name, this.x, this.y);
	}

	@Override
	public Vertex getOpositeVector() {
		return new Vertex2D("", -this.x, -this.y);
	}
	
	public static double dist(Vertex2D a, Vertex2D b) {
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		
		return Math.sqrt(dx*dx + dy*dy);
	}

}
