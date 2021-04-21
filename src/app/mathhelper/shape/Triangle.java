package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class Triangle<TT extends Vertex, ET extends Edge> extends GeometryObject {
	protected double area;
	protected double scope;
	
	public List<TT> v;
	public List<ET> e;
	
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
	
	public List<TT> getVertices(){
		return this.v;
	}
	
	public List<ET> getEdges() {
		return this.e;
	}
}
