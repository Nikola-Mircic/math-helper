package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

public class Shape extends GeometryObject{	
	public List<Triangle> triangles;
	
	public Shape(Triangle[] triangles) {
		super();
		
		this.triangles = new ArrayList<>();
				
		for(Triangle temp : triangles) {
			this.triangles.add(temp);
			addEdgeFromTriangle(temp);
		}
		
		this.area = getArea();
		this.scope = getScope();
	}
	
	public Shape(Vertex[] verticies){
		this.triangles = new ArrayList<>();
		for(int i=2;i<verticies.length;++i) {
			triangles.add(new Triangle(verticies[i-2], verticies[i-1], verticies[i]));
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
				if(edge.equals(temp)) {
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
}
