package app.mathhelper.shape.shape2d;

import app.mathhelper.shape.Edge;
import app.mathhelper.shape.ObjectInfoCalculator;

public class Edge2D extends Edge<Vertex2D> {
	
	public Edge2D() {
		this(new Vertex2D("a", 0, 0), new Vertex2D("b", 0, 0));
	}
	
	public Edge2D(Vertex2D a, Vertex2D b) {
		this.a = a;
		this.b = b;
		this.weight = Vertex2D.dist(a, b);

		this.info = ObjectInfoCalculator.getObjectInfo(this);
	}

}
