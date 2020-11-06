package app.mathhelper.shape;

public class Object3D {
	public Vertex[] v;
	public Shape[] s;
	public Edge[] e;
	
	private double area;
	private double volume;
	
	public Object3D() {
		this.v = new Vertex[8];
		this.s = new Shape[6];
		this.e = new Edge[12];
		
		this.area = -1;
		this.volume = -1;
		
		createCubeVerticies();
		createCubeSides();
		createCubeEdges();
	}
	
	private void createCubeVerticies() {
		v[0] = new Vertex("A", -0.5, -0.5, 4);
		v[1] = new Vertex("B", 0.5, -0.5, 4);
		v[2] = new Vertex("C", 0.5, -0.5, 5);
		v[3] = new Vertex("D", -0.5, -0.5, 5);
		v[4] = new Vertex("A'", -0.5, 0.5, 4);
		v[5] = new Vertex("B'", 0.5, 0.5, 4);
		v[6] = new Vertex("C'", 0.5, 0.5, 5);
		v[7] = new Vertex("D'", -0.5, 0.5, 5);
	}
	
	private void createCubeSides() {
		Vertex[] nearSide = {v[0], v[1], v[4], v[5]};
		Vertex[] farSide = {v[2], v[3], v[6], v[7]};
		Vertex[] topSide = {v[4], v[5], v[7], v[6]};
		Vertex[] bottomSide = {v[3], v[2], v[0], v[1]};
		Vertex[] leftSide = {v[3], v[0], v[7], v[4]};
		Vertex[] rightSide = {v[1], v[2], v[5], v[6]};
		
		s[0] = new Shape(nearSide);
		s[1] = new Shape(farSide);
		s[2] = new Shape(topSide);
		s[3] = new Shape(bottomSide);
		s[4] = new Shape(leftSide);
		s[5] = new Shape(rightSide);
	}
	
	private void createCubeEdges() {
		e[0] = new Edge(v[0], v[1]);//0 1
		e[1] = new Edge(v[1], v[2]);//1 2
		e[2] = new Edge(v[2], v[3]);//2 3
		e[3] = new Edge(v[3], v[0]);//3 0 
		e[4] = new Edge(v[0], v[4]);//0 4 
		e[5] = new Edge(v[1], v[5]);//1 5 
		e[6] = new Edge(v[2], v[6]);//2 6 
		e[7] = new Edge(v[3], v[7]);//3 7
		e[8] = new Edge(v[4], v[5]);//4 5 
		e[9] = new Edge(v[5], v[6]);//5 6 
		e[10] = new Edge(v[6], v[7]);//6 7 
		e[11] = new Edge(v[7], v[4]);//7 4 
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
			if(vertex.x < minX)
				minX = vertex.x;
			if(vertex.y < minY)
				minY = vertex.y;
			if(vertex.z < minZ)
				minZ = vertex.z;
		}
		
		temp = new Vertex("center",minX+(maxX-minX)/2, minY+(maxY-minY)/2, minZ+(maxZ-minZ)/2);
		
		return temp;
	}
	
	public void rotateVertical(double rotation) {
		Vertex center = this.getCenterCords();
		
		double[] angleVert = getVerticalAngle();
		
		double dist;
		
		for(int i=0;i<v.length;++i) {
			dist = getDistVertical(center, v[i]);
			v[i].y = center.y + Math.sin(angleVert[i]+rotation)*dist;
			v[i].z = center.z + Math.cos(angleVert[i]+rotation)*dist;
		}
	}
	
	public void rotateHorizontal(double rotation) {
		Vertex center = this.getCenterCords();
		
		double[] angleHoriz = getHorizontalAngle();
		
		double dist;
		
		for(int i=0;i<v.length;++i) {
			dist = getDistHorizontal(center, v[i]);
			v[i].x = center.x + Math.cos(angleHoriz[i]+rotation)*getDistHorizontal(center, v[i]);
			v[i].z = center.z + Math.sin(angleHoriz[i]+rotation)*dist;
		}
		
	}
	
	private double[] getHorizontalAngle() {
		double[] temp = new double[v.length];
		Vertex center = getCenterCords();
		
		for(int i=0;i<v.length;++i) {
			double dist = getDistHorizontal(center, v[i]);
			double sin = (v[i].z-center.z)/dist;
			double cos = (v[i].x-center.x)/dist;
			double asin = Math.asin(sin);
			
			if(cos>=0) {
				temp[i] = asin;
			}else {
				temp[i] = Math.PI-asin;
			}
		}
		
		return temp;
	}
	
	private double[] getVerticalAngle() {
		double[] temp = new double[v.length];
		Vertex center = getCenterCords();
		
		for(int i=0;i<v.length;++i) {
			double dist = getDistVertical(center, v[i]);
			double cos = (v[i].z-center.z)/dist;
			double sin = (v[i].y-center.y)/dist;
			double asin = Math.asin(sin);
			
			if(cos>=0) {
				temp[i] = asin;
			}else {
				temp[i] = Math.PI-asin;
			}
		}
		
		return temp;
	}
	
	private double getDistHorizontal(Vertex a,Vertex b) {
		double dx = a.x-b.x;
		double dz = a.z-b.z;
		return Math.sqrt(dx*dx+dz*dz);
	}
	
	private double getDistVertical(Vertex a,Vertex b) {
		double dy = a.y-b.y;
		double dz = a.z-b.z;
		return Math.sqrt(dy*dy+dz*dz);
	}
	
	public double getArea() {
		if(area>0)
			return area;
		
		double temp = 0;
		for(Shape shape : s) {
			temp += shape.getArea();
		}
		return temp;
	}
}
