package app.mathhelper.shape.shape3d;

import java.util.ArrayList;

import app.mathhelper.shape.Shape;

public class Shape3D extends Shape<Vertex3D, Edge3D, Triangle3D>{
	public Shape3D(Triangle3D[] triangles) {
		super();
		
		this.triangles = new ArrayList<>();
				
		for(Triangle3D temp : triangles) {
			this.triangles.add(temp);
			addEdgeFromTriangle(temp);
			addVertexFromTriangle(temp);
		}
		
		this.area = getArea();
		this.scope = getScope();

		this.info = this.getInfo();
	}
	
	public Shape3D(Vertex3D... verticies){
		super();
		for(Vertex3D vertex : verticies) {
			this.v.add(vertex);
		}
		this.triangles = new ArrayList<>();
		for(int i=2;i<verticies.length;++i) {
			triangles.add(new Triangle3D(verticies[0], verticies[i-1], verticies[i]));
			addEdgeFromTriangle(triangles.get(triangles.size()-1));
		}
		this.area = -1;

		this.info = this.getInfo();
	}
	
	private void addEdgeFromTriangle(Triangle3D t) {
		Edge3D toDelete = null;
		
		A:for(Edge3D temp : t.e) {
			if(toDelete!=null) {
				e.remove(toDelete);
				toDelete = null;
			}
			for(Edge3D edge : this.e) {
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
	
	private void addVertexFromTriangle(Triangle3D t) {
		A : for(Vertex3D vertex : t.v) {
			for(Vertex3D test : this.v) {
				if(test.equals(vertex)) {
					continue A;
				}
			}
			this.v.add(vertex);
		}
	}
	
	public void addTriangle(Triangle3D t) {
		this.triangles.add(t);
		addEdgeFromTriangle(t);
		addVertexFromTriangle(t);
		calculateArea();
		calculateScope();
		this.info = this.getInfo();
	}
	
	@Override
	protected void calculateArea() {
		this.area = 0;
		for(Triangle3D t : triangles) {
			this.area += t.getArea();
		}
	}
	
	@Override
	protected void calculateScope() {
		this.scope = 0;
		
		for(Edge3D edge : this.e) {
			scope += edge.weight;
		}
	}
	
	public Edge3D getNormal() {
		return new Edge3D(triangles.get(0).v.get(0), (triangles.get(0)).getCrossProduct());
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

	@Override
	public void addVertex(int idx,String[] values) {
		idx--;
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String name = ""+alphabet.charAt(idx%26);
		for(int i=0;i<idx/26;++i) name+="\'";
		
		this.v.add(new Vertex3D(name,Double.parseDouble(values[1]),Double.parseDouble(values[2]),Double.parseDouble(values[3])));
	}
}
