package app.mathhelper.shape;

public class Edge {
	public Vertex a,b;
	public double weight;
	
	public Edge() {
		this.a = new Vertex();
		this.b = new Vertex();
		this.weight = 0;
	}
	
	public Edge(Vertex a, Vertex b) {
		this.a = a;
		this.b = b;
		this.weight = Vertex.dist(a, b);
	}
	
	public boolean equalsByName(Edge edge) {
		boolean flag1 = a.name.equals(edge.a.name) && b.name.equals(edge.b.name);
		boolean flag2 = a.name.equals(edge.b.name) && b.name.equals(edge.a.name);
		return (flag1 || flag2);
	}
}
