package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

public abstract class Shape extends GeometryObject{
	protected double area;
	protected double scope;
	
	protected List<Vertex> v;
	protected List<Edge> e;
	protected List<Triangle> triangles;
	
	protected abstract void calculateArea();
	protected abstract void calculateScope();
	
	public Shape() {
		this.area = 0;
		this.scope = 0;
		
		this.v = new ArrayList<>();
		this.e = new ArrayList<>();
		this.triangles = new ArrayList<>();
	}
	
	public double getArea() {
		calculateArea();
		return this.area;
	}
	
	public double getScope() {
		calculateScope();
		return this.scope;
	}
	
	public abstract void addVertex(int idx,String[] values);
	
	public List<Vertex> getVertices(){
		return this.v;
	}
	
	public List<Edge> getEdges() {
		return this.e;
	}
}
