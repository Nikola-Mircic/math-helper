package app.mathhelper.shape.preset;

import app.mathhelper.shape.shape3d.Edge3D;
import app.mathhelper.shape.shape3d.Object3D;
import app.mathhelper.shape.shape3d.Shape3D;
import app.mathhelper.shape.shape3d.Vertex3D;

public class Tetrahedron extends Object3D {

	public Tetrahedron(int x, int y,int z) {
		super(x, y, z);
	}
	
	@Override
	protected void createVerticies(int x, int y, int z) {
		v.add(new Vertex3D("A", x-0.5, y-0.5, z-0.5));
		v.add(new Vertex3D("B", x+0.5, y-0.5, z-0.5));
		v.add(new Vertex3D("C", x-0.5, y-0.5, z+0.5));
		v.add(new Vertex3D("D", x-0.5, y+0.5, z-0.5));
	}
	
	@Override
	protected void createEdges() {
		e.add(new Edge3D( v.get(0),  v.get(1)));//0 1
		e.add(new Edge3D( v.get(1),  v.get(2)));//1 2
		e.add(new Edge3D( v.get(2),  v.get(0)));//2 0
		e.add(new Edge3D( v.get(0),  v.get(3)));//0 3
		e.add(new Edge3D( v.get(1),  v.get(3)));//1 3
		e.add(new Edge3D( v.get(2),  v.get(3)));//2 3
	}
	
	@Override
	protected void createSides() {
		Vertex3D[] side1 = { v.get(2),  v.get(1),  v.get(0)};
		Vertex3D[] side2 = { v.get(0),  v.get(1),  v.get(3)};
		Vertex3D[] side3 = { v.get(1),  v.get(2),  v.get(3)};
		Vertex3D[] side4 = { v.get(2),  v.get(0),  v.get(3)};
		
		s.add(new Shape3D(side1));
		s.add(new Shape3D(side2));
		s.add(new Shape3D(side3));
		s.add(new Shape3D(side4));
	}
}
