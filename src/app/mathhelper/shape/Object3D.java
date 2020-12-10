package app.mathhelper.shape;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import app.mathhelper.shape.preset.Cube;

public class Object3D extends GeometryObject{
	public List<Shape> s;
	//private double volume;
	
	public Object3D() {
		this.v = new ArrayList<>();
		this.e = new ArrayList<>();
		this.s = new ArrayList<>();
		this.center = new Vertex("center", 0, 0, 7);
	}
	
	public Object3D(int x, int y, int z) {
		super();
		
		this.s = new ArrayList<>();
		//this.volume = -1;
		
		this.createVerticies(x, y, z+5);
		this.createEdges();
		this.createSides();
		
		this.center = getCenterCords();
	}
	
	protected void createVerticies(int x, int y, int z) {}
	protected void createSides(){};
	protected void createEdges(){};
	
	public void rotateVertical(double rotation) {
		double[] angleVert = getVerticalAngle();
		
		double dist;
		
		for(int i=0;i<v.size();++i) {
			dist = getDistVertical(center, v.get(i));
			v.get(i).y = center.y + Math.sin(angleVert[i]+rotation)*dist;
			v.get(i).z = center.z + Math.cos(angleVert[i]+rotation)*dist;
		}
	}
	
	public void rotateHorizontal(double rotation) {
		double[] angleHoriz = getHorizontalAngle();
		
		double dist;
		
		for(int i=0;i<v.size();++i) {
			dist = getDistHorizontal(center, v.get(i));
			v.get(i).x = center.x + Math.cos(angleHoriz[i]+rotation)*dist;
			v.get(i).z = center.z + Math.sin(angleHoriz[i]+rotation)*dist;
		}
		
	}
	
	private double[] getHorizontalAngle() {
		double[] temp = new double[v.size()];
		
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
	
	public static Object3D loadFromFile(String filename) {
		Object3D temp = new Object3D();
		
		try {
			FileInputStream fis = new FileInputStream(new File(filename));
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader bf = new BufferedReader(isr);
			
			int idx = 1;
			String line=bf.readLine();
			if(line==null)
				return new Cube(0, 0, 0);
			while(line!=null) {
				if(line.equals(""))
					break;
				if(line.charAt(0)=='v') {
					String[] values = line.split(" ");
					if(!values[0].equals("v")) {
						line = bf.readLine();
						continue;
					}
					
					temp.addVertex(idx, values);
					idx++;
				}else if(line.charAt(0)=='f') {
					String[] values = line.split(" ");
					if(!values[0].equals("f")){
						line = bf.readLine();
						continue;
					}
					
					int end = values.length-1;
					Vertex v1,v2,v3;
					for(int i=1;i<end-1;++i) {
						if(values[i].indexOf('/')!=-1) {
							v1 = temp.v.get(Integer.parseInt(values[i].substring(0,values[i].indexOf('/')))-1);
							v2 = temp.v.get(Integer.parseInt(values[i+1].substring(0,values[i+1].indexOf('/')))-1);
							v3 = temp.v.get(Integer.parseInt(values[end].substring(0,values[end].indexOf('/')))-1);
						}else {
							v1 = temp.v.get(Integer.parseInt(values[i])-1);
							v2 = temp.v.get(Integer.parseInt(values[i+1])-1);
							v3 = temp.v.get(Integer.parseInt(values[end])-1);
						}
						
						Triangle t = new Triangle(v1, v2, v3);
						temp.addTriangle(t);
					}
				}
				line = bf.readLine();
			}
			bf.close();
			isr.close();
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temp;
	}
	
	private void addTriangle(Triangle t) {
		double diff = 5*Math.pow(10, -10);
		Vertex test = t.getCrossProduct();
		Vertex temp;
		for(Shape side : this.s) {
			temp = side.getNormal().b;
			if(Math.abs(temp.getDotProduct(test)-temp.getLenght()*test.getLenght())<diff) {
				side.addTriangle(t);
				return;
			}
		}
		this.s.add(new Shape(new Triangle[] {t}));
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
