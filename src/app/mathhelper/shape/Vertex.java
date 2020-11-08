package app.mathhelper.shape;

public class Vertex {
	public String name;
	public double x,y,z;
	
	public Vertex() {
		this.name = "?";
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Vertex(String name,double x, double y, double z) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static double dist(Vertex a, Vertex b) {
		return Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y) + (a.z-b.z)*(a.z-b.z));
	}
	
	public boolean equals(Vertex v) {
		return ((this.x == v.x) && (this.y == v.y) && (this.z == v.z));
	}
	
	public Vertex getOpositeVector() {
		Vertex t = new Vertex();
		
		t.x = -this.x;
		t.y = -this.y;
		t.z = -this.z;
		
		return t;
	}
	
	public Vertex add(Vertex v) {
		Vertex t = new Vertex();
		
		t.x = this.x+v.x;
		t.y = this.y+v.y;
		t.z = this.z+v.z;
		
		return t;
	}
}
