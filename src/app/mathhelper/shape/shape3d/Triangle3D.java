package app.mathhelper.shape.shape3d;

import java.util.List;

import app.mathhelper.shape.ObjectInfoCalculator;
import app.mathhelper.shape.Triangle;

public class Triangle3D extends Triangle<Vertex3D, Edge3D>{
	public Triangle3D(Vertex3D a, Vertex3D b, Vertex3D c) {
		super();
		v.add(a);
		v.add(b);
		v.add(c);
		e.add(new Edge3D(a, b));
		e.add(new Edge3D(b, c));
		e.add(new Edge3D(c, a));
		
		this.area = getArea();
		this.scope = getScope();
		
		this.info = ObjectInfoCalculator.getObjectInfo(this);
	}
	
	public Triangle3D(Vertex3D[] verticies) {
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

	public List<Edge3D> getEdges() {
		return this.e;
	}
	
	public Vertex3D getCrossProduct() {
		Vertex3D temp = new Vertex3D("Normal", 0, 0, 0);
		Vertex3D A = (Vertex3D) v.get(1).add(v.get(0).getOpositeVector());
		Vertex3D B = (Vertex3D) v.get(2).add(v.get(0).getOpositeVector());
		
		temp.x = A.y*B.z - A.z*B.y;
		temp.y = A.z*B.x - A.x*B.z;
		temp.z = A.x*B.y - A.y*B.x;
		
		return temp;
	}
	
	public Edge3D getNormalEdge() {
		return new Edge3D((Vertex3D) this.v.get(0), this.getCrossProduct());
	}
}
