package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

public class Shape {	
	public List<Triangle> triangles;
	public List<Edge> edges;
	
	private double area;
	
	public Shape(Triangle[] triangles) {
		this.triangles = new ArrayList<>();
		for(Triangle temp : triangles) {
			this.triangles.add(temp);
			addEdgeFromTriangle(temp);
		}
		this.area = -1;
	}
	
	public Shape(Vertex[] verticies){
		this.triangles = new ArrayList<>();
		for(int i=2;i<verticies.length;++i) {
			triangles.add(new Triangle(verticies[i-2], verticies[i-1], verticies[i]));
		}
		this.area = -1;
	}
	
	private void addEdgeFromTriangle(Triangle t) {
		A:for(Edge temp : t.getEdges()) {
			for(Edge e : this.edges) {
				if(temp.equals(e))
					continue A;
			}
			this.edges.add(temp);
		}
	}
	
	public double getArea() {
		if(this.area > 0)
			return this.area;
		
		double temp = 0;
		for(Triangle t : triangles) {
			temp += t.getArea();
		}
		return temp;
	}
}
