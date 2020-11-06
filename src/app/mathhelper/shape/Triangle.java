package app.mathhelper.shape;

public class Triangle {
	private Vertex[] verticies;
	private Edge[] edges;
	
	private double area;
	
	public Triangle(Vertex a, Vertex b, Vertex c) {
		this.verticies = new Vertex[3];
		this.edges = new Edge[3];
		verticies[0] = a;
		verticies[1] = b;
		verticies[2] = c;
		edges[0] = new Edge(a, b);
		edges[1] = new Edge(b, c);
		edges[2] = new Edge(c, a);
		
		this.area = getArea();
	}
	
	public Triangle(Vertex[] verticies) {
		this(verticies[0], verticies[1], verticies[2]);
	}
	
	public double getArea() {
		double a = edges[0].weight;
		double b = edges[1].weight;
		double c = edges[2].weight;
		
		double s = (a+b+c)/2;
		
		return Math.sqrt(s*(s-a)*(s-b)*(s-c));
	}

	public Edge[] getEdges() {
		return edges;
	}

	public void setEdges(Edge[] edges) {
		this.edges = edges;
	}
}
