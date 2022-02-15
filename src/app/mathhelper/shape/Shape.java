package app.mathhelper.shape;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class Shape<VT extends Vertex, ET extends Edge, TT extends Triangle> extends GeometryObject{
	public double area;
	public double scope;
	
	public List<VT> v;
	public List<ET> e;
	public List<TT> triangles;
	
	protected abstract void calculateArea();
	protected abstract void calculateScope();
	
	public Shape() {
		this.area = 0;
		this.scope = 0;
		
		this.v = new ArrayList<>();
		this.e = new ArrayList<>();
		this.triangles = new ArrayList<>();
	}
	
	public double getArea() {
		calculateArea();
		return Math.round(area*100.0)/100.0;
	}
	
	public double getScope() {
		calculateScope();
		return this.scope;
	}
	
	public abstract void addVertex(int idx,String[] values);
	
	public List<VT> getVertices(){
		return this.v;
	}
	
	public List<ET> getEdges() {
		return this.e;
	}
	
	public List<TT> getTriangles() {
		return triangles;
	}

	public void setTriangles(List<TT> triangles) {
		this.triangles = triangles;
	}
	
	@Override
	public ObjectInfo getInfo() {
		String label = "Shape";
		String value = this.toString();
		ObjectInfo info = new ObjectInfo(this, label, value);
		
		Field field;
		try {
			//Vertices
			field = this.getClass().getField("v");
			ObjectInfo.FieldInfo<Shape, String> fieldInfo = info.new FieldInfo<>(field, this, "", "Vertices", "[list of vertices]");
			info.fields.add(fieldInfo);
			
			//Edges
			field = this.getClass().getField("e");
			fieldInfo = info.new FieldInfo<>(field, this, "", "Edges", "[List of edges]");
			info.fields.add(fieldInfo);
			
			//Triangles
			field = this.getClass().getField("triangles");
			fieldInfo = info.new FieldInfo<>(field, this, "", "Triangles", "[List of triangles]");
			info.fields.add(fieldInfo);
			
			//Scope
			field = this.getClass().getField("scope");
			fieldInfo = info.new FieldInfo<>(field, this, "", "Scope", ""+this.getScope());
			info.fields.add(fieldInfo);
			
			//Area (Surface)
			field = this.getClass().getField("area");
			fieldInfo = info.new FieldInfo<>(field, this, "", "Area", ""+this.getArea());
			info.fields.add(fieldInfo);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return info;
	}
}
