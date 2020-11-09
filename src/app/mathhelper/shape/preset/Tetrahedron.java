package app.mathhelper.shape.preset;

import app.mathhelper.shape.Edge;
import app.mathhelper.shape.Object3D;
import app.mathhelper.shape.Shape;
import app.mathhelper.shape.Vertex;

public class Tetrahedron extends Object3D {

	public Tetrahedron(int x, int y,int z) {
		super(x, y, z);
	}
	
	@Override
	protected void createVerticies(int x, int y, int z) {
		v.add(new Vertex("A", x-0.5, y-0.5, z-0.5));
		v.add(new Vertex("B", x+0.5, y-0.5, z-0.5));
		v.add(new Vertex("C", x-0.5, y-0.5, z+0.5));
		v.add(new Vertex("D", x-0.5, y+0.5, z-0.5));
	}
	
	@Override
	protected void createEdges() {
		e.add(new Edge(v.get(0), v.get(1)));//0 1
		e.add(new Edge(v.get(1), v.get(2)));//1 2
		e.add(new Edge(v.get(2), v.get(0)));//2 0
		e.add(new Edge(v.get(0), v.get(3)));//0 3
		e.add(new Edge(v.get(1), v.get(3)));//1 3
		e.add(new Edge(v.get(2), v.get(3)));//2 3
	}
	
	@Override
	protected void createSides() {
		Vertex[] side1 = {v.get(2), v.get(1), v.get(0)};
		Vertex[] side2 = {v.get(0), v.get(1), v.get(3)};
		Vertex[] side3 = {v.get(1), v.get(2), v.get(3)};
		Vertex[] side4 = {v.get(2), v.get(0), v.get(3)};
		
		s.add(new Shape(side1));
		s.add(new Shape(side2));
		s.add(new Shape(side3));
		s.add(new Shape(side4));
	}
}
