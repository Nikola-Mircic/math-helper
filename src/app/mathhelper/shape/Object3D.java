package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

public class Object3D extends GeometryObject{
	public List<Shape> s;
	private double volume;
	
	public Object3D() {
		super();
		
		this.s = new ArrayList<>();
		this.volume = -1;
		
		createCubeVerticies();
		createCubeSides();
		createCubeEdges();
	}
	
	private void createCubeVerticies() {
		v.add( new Vertex("A", -0.5, -0.5, 4));
		v.add( new Vertex("B", 0.5, -0.5, 4));
		v.add( new Vertex("C", 0.5, -0.5, 5));
		v.add( new Vertex("D", -0.5, -0.5, 5));
		v.add( new Vertex("A'", -0.5, 0.5, 4));
		v.add( new Vertex("B'", 0.5, 0.5, 4));
		v.add( new Vertex("C'", 0.5, 0.5, 5));
		v.add( new Vertex("D'", -0.5, 0.5, 5));
	}
	
	private void createCubeSides() {
		Vertex[] nearSide = {v.get(0), v.get(1), v.get(4), v.get(5)};
		Vertex[] farSide = {v.get(2), v.get(3), v.get(6), v.get(7)};
		Vertex[] topSide = {v.get(4), v.get(5), v.get(7), v.get(6)};
		Vertex[] bottomSide = {v.get(3), v.get(2), v.get(0), v.get(1)};
		Vertex[] leftSide = {v.get(3), v.get(0), v.get(7), v.get(4)};
		Vertex[] rightSide = {v.get(1), v.get(2), v.get(5), v.get(6)};
		
		s.add( new Shape(nearSide));
		s.add( new Shape(farSide));
		s.add( new Shape(topSide));
		s.add( new Shape(bottomSide));
		s.add( new Shape(leftSide));
		s.add( new Shape(rightSide));
	}
	
	private void createCubeEdges() {
		e.add( new Edge(v.get(0), v.get(1)));//0 1
		e.add( new Edge(v.get(1), v.get(2)));//1 2
		e.add( new Edge(v.get(2), v.get(3)));//2 3
		e.add( new Edge(v.get(3), v.get(0)));//3 0 
		e.add( new Edge(v.get(0), v.get(4)));//0 4 
		e.add( new Edge(v.get(1), v.get(5)));//1 5 
		e.add( new Edge(v.get(2), v.get(6)));//2 6 
		e.add( new Edge(v.get(3), v.get(7)));//3 7
		e.add( new Edge(v.get(4), v.get(5)));//4 5 
		e.add( new Edge(v.get(5), v.get(6)));//5 6 
		e.add( new Edge(v.get(6), v.get(7)));//6 7 
		e.add( new Edge(v.get(7), v.get(4)));//7 4 
	}
	
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
