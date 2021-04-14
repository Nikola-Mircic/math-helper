package app.mathhelper.shape;

import app.mathhelper.shape.shape3d.Vertex3D;

public abstract class Vertex {
	public String name;
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Vertex))
			return false;
		return this.equals((Vertex)obj);
	}
	
	public abstract boolean equals(Vertex v);
	
	public abstract Vertex add(Vertex v);
	
	public abstract double getDotProduct(Vertex v);
	
	public abstract Vertex3D getCrossProduct(Vertex v);
	
	public abstract double getLenght();
	
	public abstract Vertex3D getCopy();

	public abstract Vertex getOpositeVector();
}
