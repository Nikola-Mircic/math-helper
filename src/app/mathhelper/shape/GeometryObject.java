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
		if(this.area==-1)
			calculateArea();
		return this.area;
	}
	
	public double getScope() {
		if(this.scope==-1)
			calculateScope();
		return this.scope;
	}
	
	protected Vertex getCenterCords() {
		Vertex temp;
		if(v.size()==0) {
			return null;
		}
		
		double maxX=v.get(0).x,maxY=v.get(0).y,maxZ=v.get(0).z;
		double minX=v.get(0).x,minY=v.get(0).y,minZ=v.get(0).z;
		
		for(Vertex vertex : this.v) {
			if(vertex.x > maxX)
				maxX = vertex.x;
			if(vertex.y > maxY)
				maxY = vertex.y;
			if(vertex.z > maxZ)
				maxZ = vertex.z;
			if(vertex.x < minX)
				minX = vertex.x;
			if(vertex.y < minY)
				minY = vertex.y;
			if(vertex.z < minZ)
				minZ = vertex.z;
		}
		
		temp = new Vertex("center",minX+(maxX-minX)/2, minY+(maxY-minY)/2, minZ+(maxZ-minZ)/2);
		
		return temp;
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
