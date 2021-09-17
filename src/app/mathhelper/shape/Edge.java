package app.mathhelper.shape;

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
}
