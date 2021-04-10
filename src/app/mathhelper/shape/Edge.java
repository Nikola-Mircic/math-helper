package app.mathhelper.shape;

import java.util.ArrayList;

import app.mathhelper.shape.shape3d.Vertex3D;

public class Edge extends GeometryObject{
	public Vertex3D a,b;
	public double weight;
	
	public Edge() {
		this(new Vertex3D("a", 0, 0, 0), new Vertex3D("b", 0, 0, 0));
	}
	
	public Edge(Vertex3D a, Vertex3D b) {
		this.a = a;
		this.b = b;
		this.v = new ArrayList<>();
		this.e = new ArrayList<>();
		v.add(a);
		v.add(b);
		e.add(this);
		this.weight = Vertex3D.dist(a, b);
	}
	
	public boolean equalsByName(Edge edge) {
		boolean flag1 = a.name.equals(edge.a.name) && b.name.equals(edge.b.name);
		boolean flag2 = a.name.equals(edge.b.name) && b.name.equals(edge.a.name);
		return (flag1 || flag2);
	}

	@Override
	protected void calculateArea() {}

	@Override
	protected void calculateScope() {}
}
