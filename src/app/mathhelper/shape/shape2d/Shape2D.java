package app.mathhelper.shape.shape2d;

import java.util.ArrayList;

import app.mathhelper.shape.Edge;
import app.mathhelper.shape.Shape;
import app.mathhelper.shape.Triangle;
import app.mathhelper.shape.Vertex;
import app.mathhelper.shape.shape3d.Edge3D;

public class Shape2D extends Shape {

	public Shape2D() {
		super();
	}
	
	public Shape2D(Triangle2D[] triangles) {
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
	
	private void addVertexFromTriangle(Triangle t) {
		A : for(Vertex vertex : t.v) {
			for(Vertex test : this.v) {
				if(test.equals(vertex)) {
					continue A;
				}
			}
			this.v.add(vertex);
		}
	}
	
	private void addEdgeFromTriangle(Triangle t) {
		Edge3D toDelete = null;
		
		A:for(Edge temp : t.e) {
			if(toDelete!=null) {
				e.remove(toDelete);
				toDelete = null;
			}
			for(Edge edge : this.e) {
				if(edge.equalsByName(temp)) {
					toDelete = (Edge3D)edge;
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

	@Override
	protected void calculateArea() {
		this.area = 0;
		for(Triangle t : this.triangles) {
			area += t.getArea();
		}
	}

	@Override
	protected void calculateScope() {
		this.scope = 0;
		for(Edge edge : this.e) {
			this.scope += edge.weight;
		}
	}

	@Override
	public void addVertex(int idx, String[] values) {
		idx--;
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String name = ""+alphabet.charAt(idx%26);
		for(int i=0;i<idx/26;++i) name+="\'";
		
		this.v.add(new Vertex2D(name,Double.parseDouble(values[1]),Double.parseDouble(values[2])));
	}
	
	public void addTriangle(Triangle t) {
		this.triangles.add(t);
		addEdgeFromTriangle(t);
		addVertexFromTriangle(t);
	}

}
