package app.mathhelper.shape;

public class Edge extends GeometryObject {
	public Vertex a;
	public Vertex b;
	public double weight;
	
	public boolean equalsByName(Edge edge) {
		boolean flag1 = a.name.equals(edge.a.name) && b.name.equals(edge.b.name);
		boolean flag2 = a.name.equals(edge.b.name) && b.name.equals(edge.a.name);
		return (flag1 || flag2);
	}
}
