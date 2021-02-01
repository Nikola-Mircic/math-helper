package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

public class GeometryObject {
	protected double area;
	protected double scope;
	protected List<Vertex> v;
	protected List<Edge> e;
	
	protected Vertex center;
	
	protected GeometryObject() {	
		this.area = -1;
		this.scope = -1;
		this.v = new ArrayList<>();
		this.e = new ArrayList<>();
	}
	
	protected void calculateArea(){}
	protected void calculateScope() {}
	
	public double getArea() {
		calculateArea();
		return this.area;
	}
	
	public double getScope() {
		calculateScope();
		return this.scope;
	}
	
	protected Vertex getCenterCords() {
		Vertex temp;
		if(v.size()==0) {
			return null;
		}
		
		double xsum = 0;
		double ysum = 0;
		double zsum = 0;
		
		for(Vertex vertex : this.v) {
			xsum += vertex.x;
			ysum += vertex.y;
			zsum += vertex.z;
		}
		
		temp = new Vertex("center",xsum/v.size(), ysum/v.size(), zsum/v.size());
		
		return temp;
	}
	
	protected void addVertex(int idx,String[] values) {
		this.v.add(new Vertex("V"+idx,Double.parseDouble(values[1]),Double.parseDouble(values[2]),Double.parseDouble(values[3])+7.0));
		this.center = getCenterCords();
	}
	
	public List<Vertex> getVerticies(){
		return this.v;
	}
	
	public List<Edge> getEdges() {
		return this.e;
	}

	public Vertex getCenter() {
		return center;
	}

	public void setCenter(Vertex center) {
		this.center = center;
	}
}
