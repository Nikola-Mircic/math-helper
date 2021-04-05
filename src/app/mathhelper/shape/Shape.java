package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

import app.mathhelper.shape.shape3d.Vertex3D;

public class Shape extends GeometryObject{	
	public List<Triangle> triangles;
	
	public Shape(Triangle[] triangles) {
		super();
		
		this.triangles = new ArrayList<>();
				
		for(Triangle temp : triangles) {
			this.triangles.add(temp);
			addEdgeFromTriangle(temp);
			addVertexFromTriangle(temp);
		}
		
		this.area = getArea();
		this.scope = getScope();
	}
	
	public Shape(Vertex3D[] verticies){
		super();
		for(Vertex3D vertex : verticies) {
			this.v.add(vertex);
		}
		this.triangles = new ArrayList<>();
		for(int i=2;i<verticies.length;++i) {
			triangles.add(new Triangle(verticies[i], verticies[i-1], verticies[i-2]));
			addEdgeFromTriangle(triangles.get(triangles.size()-1));
		}
		this.area = -1;
	}
	
	private void addEdgeFromTriangle(Triangle t) {
		Edge toDelete = null;
		
		A:for(Edge temp : t.e) {
			if(toDelete!=null) {
				e.remove(toDelete);
				toDelete = null;
			}
			for(Edge edge : this.e) {
				if(edge.equalsByName(temp)) {
					toDelete = edge;
					continue A;
				}
			}
			
			this.e.add(temp);
		}
		if(toDelete!=null) {
			e.remove(toDelete);
			toDelete = null;
		}
	}
	
	private void addVertexFromTriangle(Triangle t) {
		A : for(Vertex3D vertex : t.v) {
			for(Vertex3D test : this.v) {
				if(test.equals(vertex)) {
					continue A;
				}
			}
			this.v.add(vertex);
		}
	}
	
	public void addTriangle(Triangle t) {
		this.triangles.add(t);
		addEdgeFromTriangle(t);
		addVertexFromTriangle(t);
	}
	
	@Override
	protected void calculateArea() {
		this.area = 0;
		for(Triangle t : triangles) {
			area += t.getArea();
		}
	}
	
	@Override
	protected void calculateScope() {
		this.scope = 0;
		
		for(Edge edge : this.e) {
			scope += edge.weight;
		}
	}
	
	public Edge getNormal() {
		return new Edge(triangles.get(0).v.get(0), triangles.get(0).getCrossProduct());
	}

	public List<Triangle> getTriangles() {
		return triangles;
	}

	public void setTriangles(List<Triangle> triangles) {
		this.triangles = triangles;
	}
	
	@Override
	public String toString() {
		String msg = "Shape : [ ";
		for(Vertex3D vertex: this.v) {
			msg += vertex.name + " ";
		}
		msg += "]";
		return msg;
	}
}
