package app.mathhelper.shape;

import java.lang.reflect.Field;

public class Edge<VT extends Vertex> extends GeometryObject {
	public VT a;
	public VT b;
	public double weight;
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Edge)
			return equalsByName((Edge<VT>) obj);
		return false;
	}
	
	public <T extends Vertex> boolean equalsByName(Edge<T> edge) {
		boolean flag1 = a.name.equals(edge.a.name) && b.name.equals(edge.b.name);
		boolean flag2 = a.name.equals(edge.b.name) && b.name.equals(edge.a.name);
		return (flag1 || flag2);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return (a.name+" - "+b.name);
	}
	
	@Override
	public ObjectInfo getInfo() {
		String edgeName = this.toString();
		String edgeWeight = ""+this.weight;
		ObjectInfo info = new ObjectInfo(this, edgeName, edgeWeight);
		info.editable = false;
		info.extensible = true;
		
		/*info.objects.add(e.a.info);
		info.objects.add(e.b.info);*/
		
		Field weightf;
		try {
			weightf = this.getClass().getField("weight");
			
			ObjectInfo.FieldInfo<Edge<VT>, Double> weightInfo = info.new FieldInfo<Edge<VT>, Double>(weightf, this, this.weight, "Weight", ""+this.weight);
			
			info.fields.add(weightInfo);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return info;
	}
}
