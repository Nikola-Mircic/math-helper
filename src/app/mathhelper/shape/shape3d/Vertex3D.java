package app.mathhelper.shape.shape3d;

public class Vertex3D{
	public String name;
	public double x,y,z;
	public int numOfCon;
	
	public Vertex3D() {
		this("?", 0 ,0 ,0);
	}
	
	public Vertex3D(String name,double x, double y, double z) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.numOfCon = 0;
	}
	
	public static double dist(Vertex3D a, Vertex3D b) {
		return Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y) + (a.z-b.z)*(a.z-b.z));
	}
	
	public boolean equals(Vertex3D v) {
		return ((this.x == v.x) && (this.y == v.y) && (this.z == v.z));
	}
	
	public Vertex3D getOpositeVector() {
		Vertex3D t = new Vertex3D();
		
		t.x = -this.x;
		t.y = -this.y;
		t.z = -this.z;
		
		return t;
	}
	
	public Vertex3D add(Vertex3D v) {
		Vertex3D t = new Vertex3D();
		
		t.x = this.x+v.x;
		t.y = this.y+v.y;
		t.z = this.z+v.z;
		
		return t;
	}
	
	public double getDotProduct(Vertex3D v) {
		return this.x*v.x + this.y*v.y + this.z*v.z;
	}
	
	public static double getDotProduct(Vertex3D v1, Vertex3D v2) {
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	
	public Vertex3D getCrossProduct(Vertex3D v) {
		Vertex3D temp = new Vertex3D("Normal", 0, 0, 0);
		
		temp.x = this.y*v.z - this.z*v.y;
		temp.y = this.z*v.x - this.x*v.z;
		temp.z = this.x*v.y - this.y*v.x;
		
		return temp;
	}
	
	public static Vertex3D getCrossProduct(Vertex3D v1, Vertex3D v2) {
		Vertex3D temp = new Vertex3D("Normal", 0, 0, 0);
		
		temp.x = v1.y*v2.z - v1.z*v2.y;
		temp.y = v1.z*v2.x - v1.x*v2.z;
		temp.z = v1.x*v2.y - v1.y*v2.x;
		
		return temp;
	}
	
	public double getLenght() {
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	@Override
	public String toString() {
		return this.name+" ("+this.x+", "+this.y+", "+this.z+")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Vertex3D))
			return false;
		boolean flag1 = this.name.contentEquals(((Vertex3D)obj).name);
		boolean flag2 = this.x == ((Vertex3D)obj).x;
		boolean flag3 = this.y == ((Vertex3D)obj).y;
		boolean flag4 = this.z == ((Vertex3D)obj).z;
		return (flag1 && flag2 && flag3 && flag4);
	}
	
	public Vertex3D getCopy() {
		return new Vertex3D(""+this.name, this.x, this.y, this.z);
	}
}
