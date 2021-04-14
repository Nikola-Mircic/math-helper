package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import app.mathhelper.shape.shape3d.Edge3D;
import app.mathhelper.shape.shape3d.Object3D;
import app.mathhelper.shape.shape3d.Shape3D;
import app.mathhelper.shape.shape3d.Vertex3D;

public class ObjectInfoCalculator {
	public static ObjectInfo getObjectInfo(GeometryObject object) {
		if(object instanceof Edge3D) {
			return getEdgeInfo((Edge3D) object);
		}
		if(object instanceof Shape3D) {
			return getShapeInfo((Shape3D) object);
		}
		if(object instanceof Object3D) {
			return getObject3Dinfo((Object3D) object);
		}
		return null;
	}
	
	//Represents a group of vertices of an side of the object and edges between it 
	
	/**
	 * Function for calculating data of Object3D <br>
	 * 	@param o  - Object which data will be calculated
	 * 	@return Calculated data as <b> ObjectInfo </b> object that contains:
	 * 	<ol>
	 * 		<li> Scope </li>
	 * 		<li> Surface area </li>
	 * 		<li> Volume </li>
	 * 	</ol>
	 * */
	private static ObjectInfo getObject3Dinfo(Object3D o) {
		HashMap<String, String> info = new LinkedHashMap<>();
		
		info.put("vertices", ""+o.getVertices().size());
		info.put("edges", ""+o.getEdges().size());
		info.put("sides", ""+o.getSides().size());
		info.put("scope", ""+getObjectScope(o));
		info.put("surface", ""+getObjectSurfaceArea(o));
		info.put("volume", ""+getObjectVolume(o));
		
		return new ObjectInfo(o, info);
	}
	
	private static double getObjectScope(Object3D o) {
		double scope = 0;
		for(Edge edge : o.e){
			scope += edge.weight;
		}
		return Math.round(scope*1000)/1000.0;
	}
	
	private static double getObjectSurfaceArea(Object3D o) {
		double area = 0;
		for(Shape3D s : o.s) {
			area += s.getArea();
		}
		return Math.round(area*1000)/1000.0;
	}
	
	private static double getObjectVolume(Object3D o) {
		int V = o.getVertices().size();
		int S = o.getSides().size();
		int E = o.getEdges().size();
		
		if(!(V + S == E + 2))
			return 0;
		
		List<List<Vertex>> connections = new ArrayList<>();
		
		for(int i=0;i<o.getVertices().size();++i) {
			connections.add(new ArrayList<>());
		}
		
		for(Edge e : o.e) {
			int i = o.getVertices().indexOf(e.a);
			int j = o.getVertices().indexOf(e.b);
			connections.get(i).add(e.b.getCopy());
			connections.get(j).add(e.a.getCopy());
		}
		
		List<Vertex> vertices = new ArrayList<>();
		
		for(Vertex v : o.v) {
			vertices.add(v.getCopy());
		}
		
		List<ArrayList<Vertex>> sides = new ArrayList<>();
		
		for(int i=0;i<o.getSides().size();++i) {
			sides.add(new ArrayList<>());
			for(Vertex vertex : o.getSides().get(i).getVertices()) {
				sides.get(i).add(vertex.getCopy());
			}
		}
		
		double volume = 0;
		
		try {
			volume = Math.round(getObjectVolume(connections, vertices, sides)*1000)/1000.0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
		return volume;
	}
	
	private static double getObjectVolume(List<List<Vertex>> connections, List<Vertex> vertexOrder, List<ArrayList<Vertex>> sides) {
		double volume = 0;
		while(connections.size()>0) {
			System.out.println("------------------------------------------");
			System.out.println("Processing object: ");
			System.out.println("Current volume :"+Math.round(volume*100)/100.0);
			System.out.println("Current vertex :"+vertexOrder.get(0));
			for(List<Vertex> list : connections) {
				System.out.print(vertexOrder.get(connections.indexOf(list)).name+": ");
				for(Vertex v : list) {
					System.out.print(v.name + " ");
				}
				System.out.print("\n");
			}
			
			ArrayList<Vertex> newSide = new ArrayList<>();
			
			if(connections.get(0).size()==3) {
				//Volume of tetrahedron
				Vertex3D v0 = (Vertex3D) connections.get(0).get(0).add(vertexOrder.get(0).getOpositeVector());
				Vertex3D v1 = (Vertex3D) connections.get(0).get(1).add(vertexOrder.get(0).getOpositeVector());
				Vertex3D v2 = (Vertex3D) connections.get(0).get(2).add(vertexOrder.get(0).getOpositeVector());
				
				volume += Math.abs(v0.getDotProduct(v1.getCrossProduct(v2)))/6.0;
				
				newSide.add(connections.get(0).get(0));
				newSide.add(connections.get(0).get(1));
				newSide.add(connections.get(0).get(2));
				
				for(int i=0;i<connections.get(0).size();++i) {
					int idx1 = vertexOrder.indexOf(connections.get(0).get(i));
					for(int j=0;j<connections.get(0).size();++j) {
						if(i==j) continue;
						
						int idx2 = vertexOrder.indexOf(connections.get(0).get(j));
						
						if(!connections.get(idx1).contains(connections.get(0).get(j))){
							connections.get(idx1).add(connections.get(0).get(j));
						}
						
						if(!connections.get(idx2).contains(connections.get(0).get(i))){
							connections.get(idx2).add(connections.get(0).get(i));
						}
					}
				}
				
				int conRemoved = 0;
				for(int i=1;i<connections.size();++i) {
					conRemoved += connections.get(i).remove(vertexOrder.get(0)) ? 1 : 0;
				}
				System.out.println("Removed "+conRemoved+" connections from object");
				
				for(ArrayList<Vertex> side : sides) {
					side.remove(vertexOrder.get(0));
				}
			}else if(connections.get(0).size()>3) {
				Vertex start = connections.get(0).remove(0);
				Vertex a = null, b = null;
				
				newSide.add(start);
				
				System.out.println(vertexOrder.get(0).name);
				
				//Create list of sides connected to the corner
				List<ArrayList<Vertex>> sidesToCheck = new ArrayList<>();
				
				for(ArrayList<Vertex> s : sides) {
					if(s.contains(vertexOrder.get(0))) {
						sidesToCheck.add(new ArrayList<>());
						sidesToCheck.set(sidesToCheck.size()-1, s);
					}
				}
								
				for(int i=0;i<sidesToCheck.size();++i) {
					if(sidesToCheck.get(i).contains(start)) {
						System.out.print("Start ["+start.name+"] ");
						for(Vertex v : sidesToCheck.get(i)) {
							System.out.print(v.name+" ");
						}
						System.out.println("");
						
						Collections.swap(sidesToCheck, i, 0);
						
						break;
					}
				}
				
				for(int i=1;i<sidesToCheck.size();++i) {
					for(int j=i+1;j<sidesToCheck.size();++j) {
						for(Vertex v : sidesToCheck.get(i-1)) {
							if(v.equals(vertexOrder.get(0)) || v.equals(start))
								continue;
							
							if(sidesToCheck.get(j).contains(v)) {
								Collections.swap(sidesToCheck, i, j);
							}
						}
					}
				}
				
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
				for(int i=0;i<sidesToCheck.size();++i) {
					for(Vertex v : sidesToCheck.get(i)) {
						System.out.print(v.name+" ");
					}
					System.out.println("");
				}
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
				System.out.println("There are "+sidesToCheck.size()+" sides around "+vertexOrder.get(0).name);
				
				A:for(int i=0;i<sidesToCheck.size();++i) {
					for(Vertex vertex : sidesToCheck.get(i)) {
						if(vertex.equals(vertexOrder.get(0)) || vertex.equals(start))
							continue;
						if(connections.get(0).contains(vertex)){
							if(a == null) {
								a = vertex;
								addConnection(connections, vertexOrder, a, start);
								System.out.println("Found A : "+a.name);
							}else {
								b = vertex;
								
								Vertex3D v0 = (Vertex3D) a.add(vertexOrder.get(0).getOpositeVector());
								Vertex3D v1 = (Vertex3D) b.add(vertexOrder.get(0).getOpositeVector());
								Vertex3D v2 = (Vertex3D) start.add(vertexOrder.get(0).getOpositeVector());
								
								volume += Math.abs(v0.getDotProduct(v1.getCrossProduct(v2)))/6.0;
								
								addConnection(connections, vertexOrder, a, b);
								a = b;
							}
							
							connections.get(0).remove(vertex);
							newSide.add(vertex);
							continue A;
						}
					}
				}
				
				addConnection(connections, vertexOrder, a, start);
			}
			
			sides.add(newSide);
			
			System.out.print("New side [");
			for(Vertex v : newSide) {
				System.out.print(v.name+", ");
			}
			System.out.println("]");
			
			int conRemoved = 0;
			for(int i=1;i<connections.size();++i) {
				conRemoved += connections.get(i).remove(vertexOrder.get(0)) ? 1 : 0;
			}
			System.out.println("Removed "+conRemoved+" connections from object");
			
			for(ArrayList<Vertex> side : sides) {
				side.remove(vertexOrder.get(0));
			}
			
			Predicate<ArrayList<Vertex>> valid = toCheck -> toCheck.size()>2;
			
			List<ArrayList<Vertex>> temp = sides.stream().filter(valid).collect(Collectors.toList());
			
			System.out.println("There were "+(sides.size()-temp.size())+" sides with 2 corners");
			
			sides = temp;
			
			for(List<Vertex> list: connections) {
				list.remove(vertexOrder.get(0));
			}
			
			connections.remove(0);
			vertexOrder.remove(0);
		}
		
		return volume;
	}
	
	private static void addConnection(List<List<Vertex>> connections, List<Vertex> vertexOrder, Vertex a, Vertex b) {
		int aIdx = vertexOrder.indexOf(a);
		int bIdx = vertexOrder.indexOf(b);
		
		if(aIdx < 0 || bIdx < 0)
			return;
		
		boolean conn1 = connections.get(bIdx).contains(a);
		boolean conn2 = connections.get(aIdx).contains(b);
		
		if(!conn1 && !conn2) {
			connections.get(bIdx).add(a);
			connections.get(aIdx).add(b);
			System.out.println("ADDED NEW CONNECTION TO OBJECT!!!!");
		}else if(conn1 && conn2) {
			System.out.println("CONNECTION ALREADY EXISTS!");
		}else {
			System.out.println("THERE IS AN PROBLEM WITH CONNECTIONS!");
		}
	}
	
	private static ObjectInfo getShapeInfo(Shape3D s) {
		HashMap<String, String> info = new LinkedHashMap<>();
		
		info.put("vertices", ""+s.getVertices().size());
		info.put("edges", ""+s.getEdges().size());
		info.put("triangles", ""+s.getTriangles().size());
		info.put("scope", ""+s.getScope());
		info.put("surface", ""+s.getArea());
		
		return new ObjectInfo(s, info);
	}

	private static ObjectInfo getEdgeInfo(Edge3D e) {
		HashMap<String, String> info = new LinkedHashMap<>();
		
		info.put("vertex 1", ""+e.a);
		info.put("vertex 2", ""+e.b);
		info.put("weight", ""+e.weight);
		
		return new ObjectInfo(e, info);
	}
}
