package app.mathhelper.shape;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.lang.model.element.VariableElement;
import javax.print.attribute.Size2DSyntax;

import app.mathhelper.shape.shape3d.Object3D;
import app.mathhelper.shape.shape3d.Vertex3D;

public class ObjectInfoCalculator {
	public static ObjectInfo getObjectInfo(GeometryObject object) {
		if(object instanceof Edge) {
			return getEdgeInfo((Edge) object);
		}
		if(object instanceof Shape) {
			return getShapeInfo((Shape) object);
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
		HashMap<String, String> info = new HashMap<>();
		
		info.put("vertices", ""+o.getVertices().size());
		info.put("edges", ""+o.getEdges().size());
		info.put("sides", ""+o.getSides().size());
		info.put("scope", ""+getObjectScope(o));
		info.put("surface", ""+getObjectSurfaceArea(o));
		try {
			info.put("volume", ""+getObjectVolume(o));
		} catch (Exception e) {
			e.printStackTrace();
			info.put("volume", "-"+1);
		}
		
		
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
		for(Shape s : o.s) {
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
		
		List<List<Vertex3D>> connections = new ArrayList<>();
		
		for(int i=0;i<o.getVertices().size();++i) {
			connections.add(new ArrayList<>());
		}
		
		for(Edge e : o.getEdges()) {
			int i = o.getVertices().indexOf(e.a);
			int j = o.getVertices().indexOf(e.b);
			connections.get(i).add(e.b.getCopy());
			connections.get(j).add(e.a.getCopy());
		}
		
		List<Vertex3D> vertices = new ArrayList<>();
		
		for(Vertex3D v : o.getVertices()) {
			vertices.add(v.getCopy());
		}
		
		List<ArrayList<Vertex3D>> sides = new ArrayList<>();
		
		for(int i=0;i<o.getSides().size();++i) {
			sides.add(new ArrayList<>());
			for(Vertex3D vertex : o.getSides().get(i).getVertices()) {
				sides.get(i).add(vertex.getCopy());
			}
		}
		double volume = 0;
		
		try {
			volume = Math.round(getObjectVolume(connections, vertices, sides)*1000)/1000.0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return volume;
	}
	
	private static double getObjectVolume(List<List<Vertex3D>> connections, List<Vertex3D> vertexOrder, List<ArrayList<Vertex3D>> sides) {
		double volume = 0;
		while(connections.size()>0) {
			ArrayList<Vertex3D> newSide = new ArrayList<>();
			System.out.println("------------------------------------------");
			System.out.println("Processing object: ");
			System.out.println("Current volume :"+Math.round(volume*100)/100.0);
			System.out.println("Current vertex :"+vertexOrder.get(0));
			for(List<Vertex3D> list : connections) {
				System.out.print(vertexOrder.get(connections.indexOf(list)).name+": ");
				for(Vertex3D v : list) {
					System.out.print(v.name + " ");
				}
				System.out.print("\n");
			}
			if(connections.get(0).size()==3) {
				//Volume of tetrahedron
				Vertex3D v0 = connections.get(0).get(0).add(vertexOrder.get(0).getOpositeVector());
				Vertex3D v1 = connections.get(0).get(1).add(vertexOrder.get(0).getOpositeVector());
				Vertex3D v2 = connections.get(0).get(2).add(vertexOrder.get(0).getOpositeVector());
				
				volume += Math.abs(v0.getDotProduct(v1.getCrossProduct(v2)))/6.0;
				
				newSide.add(connections.get(0).get(0));
				newSide.add(connections.get(0).get(1));
				newSide.add(connections.get(0).get(2));
				
				//Remove vertex from object
				int listIdx = -1,//index of list new connection will be added in
					temp = -1;//variable to check if connection already exists
				for(int k=0;k<connections.get(0).size();++k) {
					listIdx = vertexOrder.indexOf(connections.get(0).get(k));
					
					//Add new edges
					for(int t=0;t<connections.get(0).size();++t) {
						if(k==t) continue;
						
						if(vertexOrder.get(listIdx)==connections.get(0).get(t)) continue;
						
						temp = connections.get(listIdx).indexOf(connections.get(0).get(t));
						
						if(temp == -1) {
							connections.get(listIdx).add(connections.get(0).get(t));
							temp = vertexOrder.indexOf(connections.get(0).get(t));
							connections.get(vertexOrder.indexOf(connections.get(0).get(t))).add(vertexOrder.get(listIdx));
						}
					}
					
					//Remove old edges
					connections.get(listIdx).remove(vertexOrder.get(0));
				}
				
				for(int i=0;i<sides.size();) {
					if(sides.get(i).contains(vertexOrder.get(0))) {
						if(sides.get(i).size() == 3) {
							System.out.print("Side [");
							for(Vertex3D v : sides.get(i)) {
								System.out.print(v.name+", ");
							}
							System.out.println("] is"+(sides.remove(sides.get(i))?"":"n't")+" removed!");
						}else {
							sides.get(i).remove(vertexOrder.get(0));
							i++;
						}
					}else {
						i++;
					}
				}
			}else if(connections.get(0).size()>3) {
				Vertex3D start = connections.get(0).remove(0);
				Vertex3D a = null, b = null;
				
				newSide.add(start);
				
				System.out.println(vertexOrder.get(0).name);
				
				//Create list of sides connected to the corner
				List<ArrayList<Vertex3D>> sidesToCheck = new ArrayList<>();
				
				for(ArrayList<Vertex3D> s : sides) {
					if(s.contains(vertexOrder.get(0))) {
						sidesToCheck.add(new ArrayList<>());
						sidesToCheck.set(sidesToCheck.size()-1, s);
					}
				}
								
				for(int i=0;i<sidesToCheck.size();++i) {
					if(sidesToCheck.get(i).contains(start)) {
						System.out.print("Start ["+start.name+"] ");
						for(Vertex3D v : sidesToCheck.get(i)) {
							System.out.print(v.name+" ");
						}
						System.out.println("");
						
						Collections.swap(sidesToCheck, i, 0);
						
						break;
					}
				}
				
				for(int i=1;i<sidesToCheck.size();++i) {
					for(int j=i+1;j<sidesToCheck.size();++j) {
						for(Vertex3D v : sidesToCheck.get(i-1)) {
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
					for(Vertex3D v : sidesToCheck.get(i)) {
						System.out.print(v.name+" ");
					}
					System.out.println("");
				}
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
				System.out.println("There are "+sidesToCheck.size()+" sides around "+vertexOrder.get(0).name);
				
				for(int i=0;i<sidesToCheck.size();++i) {
					for(Vertex3D vertex : sidesToCheck.get(i)) {
						if(vertex.equals(vertexOrder.get(0)) || vertex.equals(start))
							continue;
						if(connections.get(0).contains(vertex)){
							if(a == null) {
								a = vertex;
								System.out.println("Found A : "+a.name);
							}else {
								b = vertex;
								
								Vertex3D v0 = a.add(vertexOrder.get(0).getOpositeVector());
								Vertex3D v1 = b.add(vertexOrder.get(0).getOpositeVector());
								Vertex3D v2 = start.add(vertexOrder.get(0).getOpositeVector());
								
								volume += Math.abs(v0.getDotProduct(v1.getCrossProduct(v2)))/6.0;
								
								a = b;
							}
							
							connections.get(0).remove(vertex);
							newSide.add(vertex);
							
							
							int vertexIdx = vertexOrder.indexOf(vertex);
							connections.get(vertexIdx).remove(vertexOrder.get(0));
						}
					}
				}
				
				addConnection(connections, vertexOrder, a, start);
				int startIdx = vertexOrder.indexOf(start);
				connections.get(startIdx).remove(vertexOrder.get(0));
			}
			
			sides.add(newSide);
			
			System.out.print("New side [");
			for(Vertex3D v : newSide) {
				System.out.print(v.name+", ");
			}
			System.out.println("]");
			
			for(ArrayList<Vertex3D> side : sides) {
				side.remove(vertexOrder.get(0));
			}
			
			Predicate<ArrayList<Vertex3D>> valid = toCheck -> toCheck.size()>2;
			
			List<ArrayList<Vertex3D>> temp = sides.stream().filter(valid).collect(Collectors.toList());
			
			sides = temp;
			
			System.out.println("There were "+(sides.size()-temp.size())+" sides with 2 corners");
			
			connections.remove(0);
			vertexOrder.remove(0);
		}
		
		return volume;
	}
	
	private static void addConnection(List<List<Vertex3D>> connections, List<Vertex3D> vertexOrder, Vertex3D a, Vertex3D b) {
		int aIdx = vertexOrder.indexOf(a);
		int bIdx = vertexOrder.indexOf(b);
		
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
	
	private static ObjectInfo getShapeInfo(Shape s) {
		return null;
	}

	private static ObjectInfo getEdgeInfo(Edge e) {
		return null;
	}
}
