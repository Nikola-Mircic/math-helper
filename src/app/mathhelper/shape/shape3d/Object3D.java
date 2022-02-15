package app.mathhelper.shape.shape3d;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import app.mathhelper.shape.*;
import app.mathhelper.shape.preset.Cube;

public class Object3D extends GeometryObject{
	public double area;
	public double scope;
	
	public Vertex3D center;
	
	public List<Vertex3D> v;
	public List<Edge3D> e;
	public List<Shape3D> s;
	public double volume;
	
	public Object3D() {
		this.v = new ArrayList<>();
		this.e = new ArrayList<>();
		this.s = new ArrayList<>();
		this.center = new Vertex3D("center", 0, 0, 7);

		this.info = ObjectInfoCalculator.getObjectInfo(this);
	}
	
	public Object3D(int x, int y, int z) {
		this();
		
		this.createVerticies(x, y, z);
		this.createEdges();
		this.createSides();
		
		this.center = getCenterCords();
		

		this.info = ObjectInfoCalculator.getObjectInfo(this);
	}
	
	protected void createVerticies(int x, int y, int z) {}
	protected void createSides(){};
	protected void createEdges(){};
	
	protected Vertex3D getCenterCords() {
		Vertex3D temp;
		if(v.size()==0) {
			return null;
		}
		
		double xsum = 0;
		double ysum = 0;
		double zsum = 0;
		
		for(Vertex3D vertex : this.v) {
			Vertex3D v2 =  vertex;
			xsum += v2.x/v.size();
			ysum += v2.y/v.size();
			zsum += v2.z/v.size();
		}
		
		temp = new Vertex3D("center",xsum, ysum, zsum);
		
		return temp;
	}
	
	public void moveTo(Vertex3D destination) {
		this.setCenter(destination);
	}
	
	public void moveFor(double dx, double dy, double dz) {
		this.setCenter(this.center.add(new Vertex3D("?", dx, dy, dz)));
	}
	
	//Rotation 53 - 116 
	public void rotateVertical(double rotation) {
		double[] angleVert = getVerticalAngle();
		
		double dist;
		
		for(int i=0;i<v.size();++i) {
			dist = getDistVertical(center, v.get(i));
			(v.get(i)).y = center.y + Math.sin(angleVert[i]+rotation)*dist;
			( v.get(i)).z = center.z + Math.cos(angleVert[i]+rotation)*dist;
		}
	}
	
	public void rotateHorizontal(double rotation) {
		double[] angleHoriz = getHorizontalAngle();
		
		double dist;
		
		for(int i=0;i<v.size();++i) {
			dist = getDistHorizontal(center, v.get(i));
			(v.get(i)).x = center.x + Math.cos(angleHoriz[i]+rotation)*dist;
			(v.get(i)).z = center.z + Math.sin(angleHoriz[i]+rotation)*dist;
		}
		
	}
	
	private double[] getHorizontalAngle() {
		double[] temp = new double[v.size()];
		
		for(int i=0;i<v.size();++i) {
			double dist = getDistHorizontal(center, v.get(i));
			double sin = ((v.get(i)).z-center.z)/dist;
			double cos = ((v.get(i)).x-center.x)/dist;
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
			double cos = ((v.get(i)).z-center.z)/dist;
			double sin = ((v.get(i)).y-center.y)/dist;
			double asin = Math.asin(sin);
			
			if(cos>=0) {
				temp[i] = asin;
			}else {
				temp[i] = Math.PI-asin;
			}
		}
		
		return temp;
	}
	
	private double getDistHorizontal(Vertex3D a,Vertex3D b) {
		double dx = a.x-b.x;
		double dz = a.z-b.z;
		return Math.sqrt(dx*dx+dz*dz);
	}
	
	private double getDistVertical(Vertex3D a,Vertex3D b) {
		double dy = a.y-b.y;
		double dz = a.z-b.z;
		return Math.sqrt(dy*dy+dz*dz);
	}
	
	//Object loading 119 - 308
	public static Object3D loadObjectFromFile(String filename) {
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
					Vertex3D v1,v2,v3;
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
						
						Triangle3D t = new Triangle3D(v1, v2, v3);
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
		
		temp.loadEdgesfromSides();
		temp.center = temp.getCenterCords();
		temp.calculateArea();
		temp.calculateScope();
		
		temp.setInfo(ObjectInfoCalculator.getObjectInfo(temp));
		
		return temp;
	}
	
	public static Object3D loadObjectFromString(String data) {
		Object3D temp = new Object3D();
		
		try {
			StringReader reader = new StringReader(data);
			BufferedReader bf = new BufferedReader(reader);
			
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
					Vertex3D v1,v2,v3;
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
						
						Triangle3D t = new Triangle3D(v1, v2, v3);
						temp.addTriangle(t);
					}
				}
				line = bf.readLine();
			}
			bf.close();
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		temp.loadEdgesfromSides();
		temp.center = temp.getCenterCords();
		temp.calculateArea();
		temp.calculateScope();
		
		temp.setInfo(ObjectInfoCalculator.getObjectInfo(temp));
		
		return temp;
	}
	
	public static String loadDataFromFile(String filename) {
		String temp = "";
		
		try {
			FileInputStream fis = new FileInputStream(new File(filename));
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader bf = new BufferedReader(isr);
		
			String line=bf.readLine();
			if(line==null)
				return "";
			while(line!=null) {
				if(line.equals(""))
					break;
				
				temp+=line+"\n";
				
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
	
	private void addVertex(int idx,String[] values) {
		idx--;
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String name = ""+alphabet.charAt(idx%26);
		for(int i=0;i<idx/26;++i) name+="\'";
		
		this.v.add(new Vertex3D(name,Double.parseDouble(values[1]),Double.parseDouble(values[2]),Double.parseDouble(values[3])));
	}
	
	private void addTriangle(Triangle3D t) {
		double diff = 5*Math.pow(10, -13);
		Vertex3D test = t.getCrossProduct();
		Vertex3D temp;
		for(Shape3D side : this.s) {
			temp = side.getNormal().b;
			if(Math.abs(temp.getDotProduct(test)-temp.getLenght()*test.getLenght())<diff) {
				side.addTriangle(t);
				return;
			}
		}
		Shape3D shape = new Shape3D(new Triangle3D[] {t});
		this.s.add(shape);
	}
	
	private void loadEdgesfromSides() {
		for(Shape3D s : this.s) {
			addEdgeFromShape(s);
		}
	}
	
	private void addEdgeFromShape(Shape3D s) {	
		A:for(int i=0; i<s.getEdges().size(); ++i) {
			for(Edge3D edge : this.e) {
				if(edge.equalsByName(s.e.get(i))) {
					s.e.set(i, edge);
					continue A;
				}
			}
			this.e.add(s.e.get(i));
		}
	}
	
	//Object data ( area, scope & volume ) 311 - 339
	protected void calculateArea(){
		this.area = 0;
		for(Shape3D s : this.s) {
			area += s.getArea();
		}
	}
	
	protected void calculateScope(){
		this.scope = 0;
		
		for(Edge3D edge : this.e) {
			scope += edge.weight;
		}
	}
	
	public double getArea() {
		calculateArea();
		return this.area;
	}
	
	public double getScope() {
		calculateScope();
		return this.scope;
	}
	
	public List<Vertex3D> getVertices(){
		return this.v;
	}
	
	public List<Edge3D> getEdges() {
		return this.e;
	}
	
	public List<Shape3D> getSides(){
		return this.s;
	}

	public Vertex3D getCenter() {
		return center;
	}
	
	public void setCenter(Vertex3D center) {
		for(Vertex3D vertex : this.v) {
			vertex.x += center.x - this.center.x;
			vertex.y += center.y - this.center.y;
			vertex.z += center.z - this.center.z;
		}
		
		this.center = getCenterCords();
	}
	
	public double getVolume() {
		return volume;
	}
}
