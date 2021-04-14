package app.mathhelper.shape;

import app.mathhelper.shape.shape3d.Vertex3D;

public abstract class Vertex {
	public String name;
	
	public boolean equals(Vertex v) {
		if(v == null)
			return false;
		return this.name.contentEquals(v.name);
	}
	
	public abstract Vertex add(Vertex v);
	
	public abstract double getDotProduct(Vertex v);
	
	public abstract Vertex3D getCrossProduct(Vertex v);
	
	public abstract double getLenght();
	
	public abstract Vertex3D getCopy();

	public abstract Vertex getOpositeVector();
}
