package app.mathhelper.shape;

public class Vertex {
	public String name;
	public double x,y,z;
	
	public Vertex() {
		this.name = "";
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
}
