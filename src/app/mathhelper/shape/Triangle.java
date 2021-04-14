package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

public abstract class Triangle extends GeometryObject {
	protected double area;
	protected double scope;
	
	public List<Vertex> v;
	public List<Edge> e;
	
	public Triangle() {
		this.v = new ArrayList<>();
		this.e = new ArrayList<>();
		
		this.area = 0;
		this.scope = 0;
	}
	
	protected abstract void calculateArea();
	protected abstract void calculateScope();
	
	public double getArea() {
		calculateArea();
		return this.area;
	}
	
	public double getScope() {
		calculateScope();
		return this.scope;
	}
	
	public List<Vertex> getVertices(){
		return this.v;
	}
	
	public List<Edge> getEdges() {
		return this.e;
	}
}
