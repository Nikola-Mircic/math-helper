package app.mathhelper.shape;

import java.util.List;

public class Triangle extends GeometryObject{
	public Triangle(Vertex a, Vertex b, Vertex c) {
		super();
		v.add(a);
		v.add(b);
		v.add(c);
		e.add(new Edge(a, b));
		e.add(new Edge(b, c));
		e.add(new Edge(c, a));
		
		this.area = getArea();
		this.scope = getScope();
	}
	
	public Triangle(Vertex[] verticies) {
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
		double a = e.get(0).weight;
		double b = e.get(1).weight;
		double c = e.get(2).weight;
		
		this.scope = (a+b+c);
	}

	public List<Edge> getEdges() {
		return this.e;
		
	}
	
	public Vertex getCrossProduct() {
		Vertex temp = new Vertex("Normal", 0, 0, 0);
		Vertex A = v.get(1).add(v.get(0).getOpositeVector());
		Vertex B = v.get(2).add(v.get(0).getOpositeVector());
		
		temp.x = A.y*B.z - A.z*B.y;
		temp.y = A.z*B.x - A.x*B.z;
		temp.z = A.x*B.y - A.y*B.x;
		
		return temp;
	}
}
