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
	
	public boolean equals(Edge edge) {
		return (this.a.equals(edge.a) && this.b.equals(edge.b));
	}
}
