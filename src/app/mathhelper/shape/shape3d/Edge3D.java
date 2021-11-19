package app.mathhelper.shape.shape3d;

import app.mathhelper.shape.Edge;
import app.mathhelper.shape.ObjectInfoCalculator;

public class Edge3D extends Edge<Vertex3D>{
	
	public Edge3D() {
		this(new Vertex3D("a", 0, 0, 0), new Vertex3D("b", 0, 0, 0));
	}
	
	public Edge3D(Vertex3D a, Vertex3D b) {
		this.a = a;
		this.b = b;
		this.weight = Vertex3D.dist(a, b);

		this.info = ObjectInfoCalculator.getObjectInfo(this);
	}
}
