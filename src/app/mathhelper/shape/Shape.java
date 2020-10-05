package app.mathhelper.shape;

public class Shape {
	private Vertex center;
	
	public Vertex[] v;
	public boolean[][] e;
	
	public Shape() {
		this.v = new Vertex[4];
		this.e = new boolean[4][4];
		
		v[0] = new Vertex(0,0,1);
		v[1] = new Vertex(1,0,1);
		v[2] = new Vertex(0.1,0,2);
		v[3] = new Vertex(0,1,1.5);
		
		e[0][1] = true;
		e[1][2] = true;
		e[2][0] = true;
		e[0][3] = true;
		e[1][3] = true;
		e[2][3] = true;
		
		this.center = getCenterCords();
	}
	
	public Vertex getCenterCords() {
		Vertex temp;
		double maxX=v[0].x,maxY=v[0].y,maxZ=v[0].z;
		double minX=v[0].x,minY=v[0].y,minZ=v[0].z;
		
		for(Vertex vertex : this.v) {
			if(vertex.x > maxX)
				maxX = vertex.x;
			if(vertex.y > maxY)
				maxY = vertex.y;
			if(vertex.z > maxZ)
				maxZ = vertex.z;
			if(vertex.x > minX)
				minX = vertex.x;
			if(vertex.y > minY)
				minY = vertex.y;
			if(vertex.y > minY)
				minY = vertex.y;
		}
		
		temp = new Vertex(minX+(maxX-minX)/2, minY+(maxY-minY)/2, minZ+(maxZ-minZ)/2);
		
		return temp;
	}
}
