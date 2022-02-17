package app.mathhelper.shape.shape3d;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import app.mathhelper.shape.*;
import app.mathhelper.shape.ObjectInfo.FieldInfo;
import app.mathhelper.shape.preset.Cube;

public class Object3D extends GeometryObject{
	public Vertex3D center;
	
	public List<Vertex3D> v;
	public List<Edge3D> e;
	public List<Shape3D> s;
	
	public double area;
	public double scope;
	public double volume;
	
	public Object3D() {
		this.v = new ArrayList<>();
		this.e = new ArrayList<>();
		this.s = new ArrayList<>();
		this.center = new Vertex3D("center", 0, 0, 7);

		this.info = this.getInfo();
	}
	
	public Object3D(int x, int y, int z) {
		this();
		
		this.createVerticies(x, y, z);
		this.createEdges();
		this.createSides();
		
		this.center = getCenterCords();
		

		this.info = this.getInfo();
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

	public static Object3D loadObjectFromFile(String filename) {
		return loadObjectFromString(loadDataFromFile(filename));
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
		
		temp.setInfo(temp.getInfo());
		
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
	
	protected void calculateVolume() {
		this.volume = 0;
		
		int V = this.v.size();
		int S = this.s.size();
		int E = this.e.size();
		
		if(!(V + S == E + 2))
			return;
		
		List<Vertex3D> vertices = new ArrayList<>();//Contains all vertices of the object
		
		List<List<Integer>> connections = new ArrayList<>(); //A graph representing edges
		Map<Vertex3D, Integer> indices = new HashMap<>(); // Map every vertex with specific index
		
		for(int i=0; i<this.v.size(); ++i) {
			vertices.add(this.v.get(i).getCopy());
			indices.put(this.v.get(i), i);
		}
		
		for(int i=0;i<this.v.size();++i) {
			connections.add(new ArrayList<>());
		}
		
		for(Edge3D e : this.e) {
			int i = indices.get(e.a);
			int j = indices.get(e.b);
			connections.get(i).add(j);
			connections.get(j).add(i);
		}
		
		List<ArrayList<Integer>> sides = new ArrayList<>();// A representation of all object's side as matrix
		
		for(int i=0;i<this.s.size();++i) {
			sides.add(new ArrayList<>());
			for(Vertex3D vertex : this.s.get(i).getVertices()) {
				sides.get(i).add(indices.get(vertex));
			}
		}
		
		
		for(int vertexIdx = 0; vertexIdx < vertices.size(); vertexIdx++) {
			ArrayList<Integer> newSide = new ArrayList<>();
			
			//Divide the objects into smaller pyramids and then sum their volumes
			//If a vertex is connected to three other vertices only cut that tetrahedron from the object and continue the calculation
			//else calculate the volume of some complex pyramid and cut it off from the object
			if(connections.get(0).size()==3) {
				//Volume of tetrahedron
				Vertex3D v0 = vertices.get(connections.get(0).get(0));
				Vertex3D v1 = vertices.get(connections.get(0).get(1));
				Vertex3D v2 = vertices.get(connections.get(0).get(2));
				
				v0 = v0.add(vertices.get(vertexIdx).getOpositeVector());
				v1 = v1.add(vertices.get(vertexIdx).getOpositeVector());
				v2 = v2.add(vertices.get(vertexIdx).getOpositeVector());
				
				this.volume += Math.abs(v0.getDotProduct(v1.getCrossProduct(v2)))/6.0;
				
				newSide.add(connections.get(0).get(0));
				newSide.add(connections.get(0).get(1));
				newSide.add(connections.get(0).get(2));
				
				for(int i=0;i<connections.get(0).size();++i) {
					int idx1 = connections.get(0).get(i);
					for(int j=0;j<connections.get(0).size();++j) {
						if(i==j) continue;
						int idx2 = connections.get(0).get(j);
						
						if(!connections.get(idx1-vertexIdx).contains(idx2)){
							connections.get(idx1-vertexIdx).add(idx2);
						}
						
						if(!connections.get(idx2-vertexIdx).contains(idx1)){
							connections.get(idx2-vertexIdx).add(idx1);
						}
					}
				}
				
				for(int i=1;i<connections.size();++i) {
					connections.get(i).remove((Integer)vertexIdx);
				}
				
				for(ArrayList<Integer> side : sides) {
					side.remove((Integer)vertexIdx);
				}
			}else if(connections.get(0).size()>3) {
				int start = connections.get(0).remove(0);
				int a = -1, b = -1;
				
				newSide.add(start);
								
				//Create list of sides connected to the corner
				List<ArrayList<Integer>> sidesToCheck = new ArrayList<>();
				
				for(ArrayList<Integer> s : sides) {
					if(s.contains(vertexIdx)) {
						sidesToCheck.add(s);
						//sidesToCheck.set(sidesToCheck.size()-1, s);
					}
				}
								
				for(int i=0;i<sidesToCheck.size();++i) {
					if(sidesToCheck.get(i).contains(start)) {
						Collections.swap(sidesToCheck, i, 0);
						
						break;
					}
				}
				
				
				//Put all connected sides ( where both sides have in common start index and one other index ) next to each other
				for(int i=1;i<sidesToCheck.size();++i) {
					for(int j=i;j<sidesToCheck.size();++j) {
						for(int v : sidesToCheck.get(i-1)) {
							if(v == vertexIdx || v == start)
								continue;
							
							if(sidesToCheck.get(j).contains(v)) {
								Collections.swap(sidesToCheck, i, j);
							}
						}
					}
				}
				
				
				A:for(int i=0;i<sidesToCheck.size();++i) {
					for(int vertex : sidesToCheck.get(i)) {
						if(vertex == vertexIdx || vertex == start)
							continue;
						if(connections.get(0).contains(vertex)){
							if(a == -1) {
								a = vertex;
								addConnection(connections, a, start, vertexIdx);
							}else {
								b = vertex;
								
								Vertex3D v0 = (Vertex3D) vertices.get(a);
								Vertex3D v1 = (Vertex3D) vertices.get(b);
								Vertex3D v2 = (Vertex3D) vertices.get(start);
								
								v0 = (Vertex3D) v0.add(vertices.get(vertexIdx).getOpositeVector());
								v1 = (Vertex3D) v1.add(vertices.get(vertexIdx).getOpositeVector());
								v2 = (Vertex3D) v2.add(vertices.get(vertexIdx).getOpositeVector());
								
								this.volume += Math.abs(v0.getDotProduct(v1.getCrossProduct(v2)))/6.0;
								
								addConnection(connections, a, b, vertexIdx);
								a = b;
							}
							
							connections.get(0).remove((Integer)vertex);
							newSide.add(vertex);
							continue A;
						}
					}
				}
				
				addConnection(connections, a, start, vertexIdx);
			}
			
			sides.add(newSide);

			for(int i=1;i<connections.size();++i) {
				connections.get(i).remove((Integer)vertexIdx);
			}
			
			for(ArrayList<Integer> side : sides) {
				side.remove((Integer)vertexIdx);
			}
			
			Predicate<ArrayList<Integer>> valid = toCheck -> toCheck.size()>2;
			
			List<ArrayList<Integer>> temp = sides.stream().filter(valid).collect(Collectors.toList());
						
			sides = temp;
			
			connections.remove(0);
		}
		
		//this.volume /= 2.0;
	}
	
	private static void addConnection(List<List<Integer>> connections, int a, int b, int vertexIdx) {
		if(a < 0 || b < 0)
			return;
		
		boolean conn1 = connections.get(b-vertexIdx).contains(a);
		boolean conn2 = connections.get(a-vertexIdx).contains(b);
		
		if(!conn1 && !conn2) {
			connections.get(b-vertexIdx).add(a);
			connections.get(a-vertexIdx).add(b);
		}else if(conn1 != conn2){
			System.out.println("THERE IS AN PROBLEM WITH CONNECTIONS!");
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
	
	public double getVolume() {
		calculateVolume();
		return this.volume;
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
	
	@Override
	public ObjectInfo getInfo() {
		Vertex3D v = this.getCenter();
		this.info = new ObjectInfo(this, "Object: ", "3D object");
		
		Field field;
		try {
			// Center of the object
			field = getClass().getField("center");
			FieldInfo<Object3D, String> fieldInfo = info.new FieldInfo<>(field, this, "", "Center", v.toString());
			info.fields.add(fieldInfo);
			
			// Vertices
			field = getClass().getField("v");
			fieldInfo = info.new FieldInfo<>(field, this, "", "Vertices", "[List of vertices]");
			info.fields.add(fieldInfo);
			
			// Edges
			field = getClass().getField("e");
			fieldInfo = info.new FieldInfo<>(field, this, "", "Edges", "[List of edges]");
			info.fields.add(fieldInfo);
			
			// Sides
			field = getClass().getField("s");
			fieldInfo = info.new FieldInfo<>(field, this, "", "Sides", "[List of sides]");
			info.fields.add(fieldInfo);
			
			// Scope
			field = getClass().getField("scope");
			fieldInfo = info.new FieldInfo<>(field, this, "", "Scope", ""+this.getScope());
			info.fields.add(fieldInfo);
			
			// Area
			field = getClass().getField("area");
			fieldInfo = info.new FieldInfo<>(field, this, "", "Area", ""+this.getArea());
			info.fields.add(fieldInfo);
			
			// Scope
			field = getClass().getField("volume");
			fieldInfo = info.new FieldInfo<>(field, this, "", "Volume", ""+this.getVolume());
			info.fields.add(fieldInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return info;
	}
}
