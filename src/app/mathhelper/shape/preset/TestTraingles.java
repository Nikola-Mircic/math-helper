package app.mathhelper.shape.preset;

import app.mathhelper.shape.Edge;
import app.mathhelper.shape.Object3D;
import app.mathhelper.shape.Shape;
import app.mathhelper.shape.Vertex;

//This class has been used for testing purpose only!!!

public class TestTraingles extends Object3D {

	public TestTraingles() {
		this(0, 0, 0);
	}

	public TestTraingles(int x, int y, int z) {
		super(x, y, z);
	}
	
	@Override
	protected void createVerticies(int x, int y, int z) {
		v.add(new Vertex("A", x, y+1, z+4));
		v.add(new Vertex("B",x+1,y,z+4));
		v.add(new Vertex("C",x-1,y-1,z+5));
		
		v.add(new Vertex("A1", x-0.5, y, z+1));
		v.add(new Vertex("B1",x+0.5,y-1,z+2));
		v.add(new Vertex("C1",x-1.5,y-2,z+3.5));
	}
	@Override
	protected void createSides(){
		s.add(new Shape(new Vertex[]{v.get(0), v.get(2), v.get(1)}));
		s.add(new Shape(new Vertex[]{v.get(3), v.get(5), v.get(4)}));
		
		s.add(new Shape(new Vertex[]{v.get(0), v.get(1), v.get(2)}));
		s.add(new Shape(new Vertex[]{v.get(3), v.get(4), v.get(5)}));
	};
	@Override
	protected void createEdges(){
		e.add(new Edge(v.get(0), v.get(1)));
		e.add(new Edge(v.get(1), v.get(2)));
		e.add(new Edge(v.get(2), v.get(0)));
		
		e.add(new Edge(v.get(3), v.get(4)));
		e.add(new Edge(v.get(4), v.get(5)));
		e.add(new Edge(v.get(5), v.get(3)));
	};

}
