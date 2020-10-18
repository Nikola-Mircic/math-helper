package app.mathhelper.shape;

public class Shape {	
	public Vertex[] v;
	public boolean[][] e;
	
	public Shape() {
		this.v = new Vertex[8];
		this.e = new boolean[8][8];
		
		v[0] = new Vertex("A", -0.5, -0.5, 4);
		v[1] = new Vertex("B", 0.5, -0.5, 4);
		v[2] = new Vertex("C", 0.5, -0.5, 5);
		v[3] = new Vertex("D", -0.5, -0.5, 5);
		v[4] = new Vertex("A'", -0.5, 0.5, 4);
		v[5] = new Vertex("B'", 0.5, 0.5, 4);
		v[6] = new Vertex("C'", 0.5, 0.5, 5);
		v[7] = new Vertex("D'", -0.5, 0.5, 5);
		
		e[0][1] = true;//0 1
		e[1][2] = true;//1 2
		e[2][3] = true;//2 3
		e[3][0] = true;//3 0
		e[0][4] = true;//0 4
		e[1][5] = true;//1 5
		e[2][6] = true;//2 6
		e[3][7] = true;//3 7
		e[4][5] = true;//4 5
		e[5][6] = true;//5 6
		e[6][7] = true;//6 7
		e[7][4] = true;//7 4
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
}
