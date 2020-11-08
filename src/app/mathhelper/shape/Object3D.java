package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

public class Object3D extends GeometryObject{
	public List<Shape> s;
	private double volume;
	
	public Object3D() {
		this(0, 0, 0);
	}
	
	public Object3D(int x, int y, int z) {
		super();
		
		this.s = new ArrayList<>();
		this.volume = -1;
		
		this.createVerticies(x, y, z);
		this.createEdges();
		this.createSides();
	}
	
	protected void createVerticies(int x, int y, int z) {}
	protected void createSides(){};
	protected void createEdges(){};
	
	public Vertex getCenterCords() {
		Vertex temp;
		double maxX=v.get(0).x,maxY=v.get(0).y,maxZ=v.get(0).z;
		double minX=v.get(0).x,minY=v.get(0).y,minZ=v.get(0).z;
		
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
		
		for(int i=0;i<v.size();++i) {
			dist = getDistVertical(center, v.get(i));
			v.get(i).y = center.y + Math.sin(angleVert[i]+rotation)*dist;
			v.get(i).z = center.z + Math.cos(angleVert[i]+rotation)*dist;
		}
	}
	
	public void rotateHorizontal(double rotation) {
		Vertex center = this.getCenterCords();
		
		double[] angleHoriz = getHorizontalAngle();
		
		double dist;
		
		for(int i=0;i<v.size();++i) {
			dist = getDistHorizontal(center, v.get(i));
			v.get(i).x = center.x + Math.cos(angleHoriz[i]+rotation)*getDistHorizontal(center, v.get(i));
			v.get(i).z = center.z + Math.sin(angleHoriz[i]+rotation)*dist;
		}
		
	}
	
	private double[] getHorizontalAngle() {
		double[] temp = new double[v.size()];
		Vertex center = getCenterCords();
		
		for(int i=0;i<v.size();++i) {
			double dist = getDistHorizontal(center, v.get(i));
			double sin = (v.get(i).z-center.z)/dist;
			double cos = (v.get(i).x-center.x)/dist;
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
		double[] temp = new double[v.size()];
		Vertex center = getCenterCords();
		
		for(int i=0;i<v.size();++i) {
			double dist = getDistVertical(center, v.get(i));
			double cos = (v.get(i).z-center.z)/dist;
			double sin = (v.get(i).y-center.y)/dist;
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
	
	@Override
	protected void calculateArea(){
		this.area = 0;
		for(Shape s : this.s) {
			area += s.getArea();
		}
	}
	
	@Override
	protected void calculateScope(){
		this.scope = 0;
		
		for(Edge edge : this.e) {
			scope += edge.weight;
		}
	}
	
	public List<Vertex> getVerticies(){
		return this.v;
	}
	
	public List<Edge> getEdges() {
		return this.e;
	}
	
	public List<Shape> getSides(){
		return this.s;
	}
}