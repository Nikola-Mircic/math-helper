package app.mathhelper.shape.shape2d;

import java.util.ArrayList;

import app.mathhelper.shape.Shape;

public class Shape2D extends Shape<Vertex2D, Edge2D, Triangle2D> {

	public Shape2D() {
		super();
	}
	
	public Shape2D(Triangle2D[] triangles) {
		super();
		
		this.triangles = new ArrayList<>();
				
		for(Triangle2D temp : triangles) {
			this.triangles.add(temp);
			addEdgeFromTriangle(temp);
			addVertexFromTriangle(temp);
		}
		
		this.area = getArea();
		this.scope = getScope();
	}
	
	private void addVertexFromTriangle(Triangle2D t) {
		A : for(Vertex2D vertex : t.v) {
			for(Vertex2D test : this.v) {
				if(test.equals(vertex)) {
					continue A;
				}
			}
			this.v.add(vertex);
		}
	}
	
	private void addEdgeFromTriangle(Triangle2D t) {
		Edge2D toDelete = null;
		
		A:for(Edge2D temp : t.e) {
			if(toDelete!=null) {
				e.remove(toDelete);
				toDelete = null;
			}
			for(Edge2D edge : this.e) {
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

	@Override
	protected void calculateArea() {
		this.area = 0;
		for(Triangle2D t : this.triangles) {
			area += t.getArea();
		}
	}

	@Override
	protected void calculateScope() {
		this.scope = 0;
		for(Edge2D edge : this.e) {
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
	
	public void addTriangle(Triangle2D t) {
		this.triangles.add(t);
		addEdgeFromTriangle(t);
		addVertexFromTriangle(t);
	}

}
