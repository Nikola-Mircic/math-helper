package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import app.mathhelper.shape.shape3d.*;
import app.mathhelper.shape.shape2d.*;

public class ObjectInfoCalculator {
	public static ObjectInfo getObjectInfo(GeometryObject object) {
		if(object instanceof Edge3D) {
			return getEdge3DInfo((Edge3D) object);
		}
		if(object instanceof Shape3D) {
			return getShape3DInfo((Shape3D) object);
		}
		if(object instanceof Object3D) {
			return getObject3Dinfo((Object3D) object);
		}
		if(object instanceof Edge2D) {
			return getEdge2DInfo((Edge2D) object);
		}
		if(object instanceof Shape2D) {
			return getShape2DInfo((Shape2D) object);
		}
		return null;
	}
	
	private static ObjectInfo getEdge2DInfo(Edge2D e) {
		HashMap<String, String> info = new LinkedHashMap<>();
		
		info.put("vertex 1", ""+e.a);
		info.put("vertex 2", ""+e.b);
		info.put("weight", ""+e.weight);
		
		return new ObjectInfo(e, info);
	}

	private static ObjectInfo getShape2DInfo(Shape2D s) {
		HashMap<String, String> info = new LinkedHashMap<>();
		
		info.put("vertices", ""+s.getVertices().size());
		info.put("edges", ""+s.getEdges().size());
		info.put("triangles", ""+s.getTriangles().size());
		info.put("scope", ""+s.getScope());
		info.put("surface", ""+s.getArea());
		
		return new ObjectInfo(s, info);
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
		for(Edge3D edge : o.e){
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
		
		List<Vertex3D> vertices = new ArrayList<>();
		
		List<List<Integer>> connections = new ArrayList<>();
		Map<Vertex3D, Integer> indices = new HashMap<>();
		
		for(int i=0; i<o.v.size(); ++i) {
			vertices.add(o.v.get(i).getCopy());
			indices.put(o.v.get(i), i);
		}
		
		for(int i=0;i<o.v.size();++i) {
			connections.add(new ArrayList<>());
		}
		
		for(Edge3D e : o.e) {
			int i = indices.get(e.a);
			int j = indices.get(e.b);
			connections.get(i).add(j);
			connections.get(j).add(i);
		}
		
		List<ArrayList<Integer>> sides = new ArrayList<>();
		
		for(int i=0;i<o.getSides().size();++i) {
			sides.add(new ArrayList<>());
			for(Vertex3D vertex : o.getSides().get(i).getVertices()) {
				sides.get(i).add(indices.get(vertex));
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
	
	private static double getObjectVolume(List<List<Integer>> connections, List<Vertex3D> vertexOrder, List<ArrayList<Integer>> sides) {
		double volume = 0;
		for(int vertexIdx = 0; vertexIdx < vertexOrder.size(); vertexIdx++) {
			ArrayList<Integer> newSide = new ArrayList<>();
			
			if(connections.get(0).size()==3) {
				//Volume of tetrahedron
				Vertex3D v0 = vertexOrder.get(connections.get(0).get(0));
				Vertex3D v1 = vertexOrder.get(connections.get(0).get(1));
				Vertex3D v2 = vertexOrder.get(connections.get(0).get(2));
				
				v0 = v0.add(vertexOrder.get(vertexIdx).getOpositeVector());
				v1 = v1.add(vertexOrder.get(vertexIdx).getOpositeVector());
				v2 = v2.add(vertexOrder.get(vertexIdx).getOpositeVector());
				
				volume += Math.abs(v0.getDotProduct(v1.getCrossProduct(v2)))/6.0;
				
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
						sidesToCheck.add(new ArrayList<>());
						sidesToCheck.set(sidesToCheck.size()-1, s);
					}
				}
								
				for(int i=0;i<sidesToCheck.size();++i) {
					if(sidesToCheck.get(i).contains(start)) {
						Collections.swap(sidesToCheck, i, 0);
						
						break;
					}
				}
				
				for(int i=1;i<sidesToCheck.size();++i) {
					for(int j=i+1;j<sidesToCheck.size();++j) {
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
								
								Vertex3D v0 = (Vertex3D) vertexOrder.get(a);
								Vertex3D v1 = (Vertex3D) vertexOrder.get(b);
								Vertex3D v2 = (Vertex3D) vertexOrder.get(start);
								
								v0 = (Vertex3D) v0.add(vertexOrder.get(vertexIdx).getOpositeVector());
								v1 = (Vertex3D) v1.add(vertexOrder.get(vertexIdx).getOpositeVector());
								v2 = (Vertex3D) v2.add(vertexOrder.get(vertexIdx).getOpositeVector());
								
								volume += Math.abs(v0.getDotProduct(v1.getCrossProduct(v2)))/6.0;
								
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
		
		return volume;
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
	
	private static ObjectInfo getShape3DInfo(Shape3D s) {
		HashMap<String, String> info = new LinkedHashMap<>();
		
		info.put("vertices", ""+s.getVertices().size());
		info.put("edges", ""+s.getEdges().size());
		info.put("triangles", ""+s.getTriangles().size());
		info.put("scope", ""+s.getScope());
		info.put("surface", ""+s.getArea());
		
		return new ObjectInfo(s, info);
	}

	private static ObjectInfo getEdge3DInfo(Edge3D e) {
		HashMap<String, String> info = new LinkedHashMap<>();
		
		info.put("vertex 1", ""+e.a);
		info.put("vertex 2", ""+e.b);
		info.put("weight", ""+e.weight);
		
		return new ObjectInfo(e, info);
	}
}
