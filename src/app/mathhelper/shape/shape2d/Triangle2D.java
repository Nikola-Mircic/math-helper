package app.mathhelper.shape.shape2d;

import app.mathhelper.shape.Edge;
import app.mathhelper.shape.Triangle;

public class Triangle2D extends Triangle {
	public Triangle2D() {
		super();
	}
	
	public Triangle2D(Vertex2D a, Vertex2D b, Vertex2D c) {
		super();
		v.add(a);
		v.add(b);
		v.add(c);
		e.add(new Edge2D(a, b));
		e.add(new Edge2D(b, c));
		e.add(new Edge2D(c, a));
		
		this.area = getArea();
		this.scope = getScope();
	}
	
	public Triangle2D(Vertex2D[] verticies) {
		this(verticies[0], verticies[1], verticies[2]);
	}
	
	@Override
	protected void calculateArea() {
		double a = e.get(0).weight;
		double b = e.get(1).weight;
		double c = e.get(2).weight;
		
		double s = (a+b+c)/2;
		
		this.area =  Math.sqrt(s*(s-a)*(s-b)*(s-c));
	}

	@Override
	protected void calculateScope() {
		this.scope = 0;
		for(Edge edge : this.e) {
			this.scope += edge.weight;
		}
	}

}
