package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

import app.mathhelper.shape.shape3d.Vertex3D;

public abstract class GeometryObject {
	protected double area;
	protected double scope;
	protected List<Vertex3D> v;
	protected List<Edge> e;
	protected ObjectInfo info;
	
	protected Vertex3D center;
	
	protected GeometryObject() {	
		this.area = -1;
		this.scope = -1;
		this.v = new ArrayList<>();
		this.e = new ArrayList<>();
		this.info = new ObjectInfo(this);
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
	
	protected Vertex3D getCenterCords() {
		Vertex3D temp;
		if(v.size()==0) {
			return null;
		}
		
		double xsum = 0;
		double ysum = 0;
		double zsum = 0;
		
		for(Vertex3D vertex : this.v) {
			xsum += vertex.x/v.size();
			ysum += vertex.y/v.size();
			zsum += vertex.z/v.size();
		}
		
		temp = new Vertex3D("center",xsum, ysum, zsum);
		
		return temp;
	}
	
	protected void addVertex(int idx,String[] values) {
		idx--;
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String name = ""+alphabet.charAt(idx%26);
		for(int i=0;i<idx/26;++i) name+="\'";
		
		this.v.add(new Vertex3D(name,Double.parseDouble(values[1]),Double.parseDouble(values[2]),Double.parseDouble(values[3])));
		this.center = getCenterCords();
	}
	
	public List<Vertex3D> getVertices(){
		return this.v;
	}
	
	public List<Edge> getEdges() {
		return this.e;
	}

	public Vertex3D getCenter() {
		return center;
	}

	public void setCenter(Vertex3D center) {
		this.center = center;
	}
}
