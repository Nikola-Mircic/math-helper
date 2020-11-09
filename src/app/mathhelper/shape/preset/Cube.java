package app.mathhelper.shape.preset;

import app.mathhelper.shape.Edge;
import app.mathhelper.shape.Object3D;
import app.mathhelper.shape.Shape;
import app.mathhelper.shape.Vertex;

public class Cube extends Object3D {

	public Cube(int x, int y, int z) {
		super(x, y, z);
	}
	
	@Override
	protected void createVerticies(int x, int y, int z) {
		v.add( new Vertex("A", x-0.5, y-0.5, z-0.5));
		v.add( new Vertex("B", x+0.5, y-0.5, z-0.5));
		v.add( new Vertex("C", x+0.5, y-0.5, z+0.5));
		v.add( new Vertex("D", x-0.5, y-0.5, z+0.5));
		v.add( new Vertex("A'", x-0.5, y+0.5, z-0.5));
		v.add( new Vertex("B'", x+0.5, y+0.5, z-0.5));
		v.add( new Vertex("C'", x+0.5, y+0.5, z+0.5));
		v.add( new Vertex("D'", x-0.5, y+0.5, z+0.5));
	}
	
	@Override
	protected void createSides() {
		Vertex[] nearSide = {v.get(0), v.get(1), v.get(4), v.get(5)};
		Vertex[] farSide = {v.get(2), v.get(3), v.get(6), v.get(7)};
		Vertex[] topSide = {v.get(4), v.get(5), v.get(7), v.get(6)};
		Vertex[] bottomSide = {v.get(3), v.get(2), v.get(0), v.get(1)};
		Vertex[] leftSide = {v.get(3), v.get(0), v.get(7), v.get(4)};
		Vertex[] rightSide = {v.get(1), v.get(2), v.get(5), v.get(6)};
		
		s.add( new Shape(nearSide));
		s.add( new Shape(farSide));
		s.add( new Shape(topSide));
		s.add( new Shape(bottomSide));
		s.add( new Shape(leftSide));
		s.add( new Shape(rightSide));
	}
	
	@Override
	protected void createEdges() {
		e.add( new Edge(v.get(0), v.get(1)));//0 1
		e.add( new Edge(v.get(1), v.get(2)));//1 2
		e.add( new Edge(v.get(2), v.get(3)));//2 3
		e.add( new Edge(v.get(3), v.get(0)));//3 0 
		e.add( new Edge(v.get(0), v.get(4)));//0 4 
		e.add( new Edge(v.get(1), v.get(5)));//1 5 
		e.add( new Edge(v.get(2), v.get(6)));//2 6 
		e.add( new Edge(v.get(3), v.get(7)));//3 7
		e.add( new Edge(v.get(4), v.get(5)));//4 5 
		e.add( new Edge(v.get(5), v.get(6)));//5 6 
		e.add( new Edge(v.get(6), v.get(7)));//6 7 
		e.add( new Edge(v.get(7), v.get(4)));//7 4 
	}
	
}
