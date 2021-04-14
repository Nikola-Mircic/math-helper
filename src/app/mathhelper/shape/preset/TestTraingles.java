package app.mathhelper.shape.preset;

import app.mathhelper.shape.shape3d.Edge3D;
import app.mathhelper.shape.shape3d.Object3D;
import app.mathhelper.shape.shape3d.Shape3D;
import app.mathhelper.shape.shape3d.Vertex3D;

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
		v.add(new Vertex3D("A", x, y+1, z+4));
		v.add(new Vertex3D("B",x+1,y,z+4));
		v.add(new Vertex3D("C",x-1,y-1,z+5));
		
		v.add(new Vertex3D("A1", x-0.5, y, z+1));
		v.add(new Vertex3D("B1",x+0.5,y-1,z+2));
		v.add(new Vertex3D("C1",x-1.5,y-2,z+3.5));
	}
	@Override
	protected void createSides(){
		s.add(new Shape3D(new Vertex3D[]{(Vertex3D) v.get(0), (Vertex3D) v.get(2), (Vertex3D) v.get(1)}));
		s.add(new Shape3D(new Vertex3D[]{(Vertex3D) v.get(3), (Vertex3D) v.get(5), (Vertex3D) v.get(4)}));
		
		s.add(new Shape3D(new Vertex3D[]{(Vertex3D) v.get(0), (Vertex3D) v.get(1), (Vertex3D) v.get(2)}));
		s.add(new Shape3D(new Vertex3D[]{(Vertex3D) v.get(3), (Vertex3D) v.get(4), (Vertex3D) v.get(5)}));
	};
	@Override
	protected void createEdges(){
		e.add(new Edge3D((Vertex3D) v.get(0), (Vertex3D) v.get(1)));
		e.add(new Edge3D((Vertex3D) v.get(1), (Vertex3D) v.get(2)));
		e.add(new Edge3D((Vertex3D) v.get(2), (Vertex3D) v.get(0)));
		
		e.add(new Edge3D((Vertex3D) v.get(3), (Vertex3D) v.get(4)));
		e.add(new Edge3D((Vertex3D) v.get(4), (Vertex3D) v.get(5)));
		e.add(new Edge3D((Vertex3D) v.get(5), (Vertex3D) v.get(3)));
	};

}
