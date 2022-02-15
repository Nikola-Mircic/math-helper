package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class Shape<VT extends Vertex, ET extends Edge, TT extends Triangle> extends GeometryObject{
	public double area;
	public double scope;
	
	public List<VT> v;
	public List<ET> e;
	public List<TT> triangles;
	
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
		return Math.round(area*100.0)/100.0;
	}
	
	public double getScope() {
		calculateScope();
		return this.scope;
	}
	
	public abstract void addVertex(int idx,String[] values);
	
	public List<VT> getVertices(){
		return this.v;
	}
	
	public List<ET> getEdges() {
		return this.e;
	}
	
	public List<TT> getTriangles() {
		return triangles;
	}

	public void setTriangles(List<TT> triangles) {
		this.triangles = triangles;
	}
}
