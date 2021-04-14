package app.mathhelper.shape;

import app.mathhelper.shape.shape3d.Vertex3D;

public abstract class GeometryObject {
	protected ObjectInfo info;
	
	protected Vertex3D center;
	
	protected GeometryObject() {	
		this.info = new ObjectInfo(this);
	}
	
	public static interface InfoHandler{
		public void handle(ObjectInfo info);
	}
	
	public void handleInfo(InfoHandler handler) {
		handler.handle(this.info);
	}
	/*protected abstract void calculateArea();
	protected abstract void calculateScope();*/
	
	/*public double getArea() {
		calculateArea();
		return this.area;
	}
	
	public double getScope() {
		calculateScope();
		return this.scope;
	}
	
	
	
	protected void addVertex(int idx,String[] values) {
		idx--;
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String name = ""+alphabet.charAt(idx%26);
		for(int i=0;i<idx/26;++i) name+="\'";
		
		this.v.add(new Vertex3D(name,Double.parseDouble(values[1]),Double.parseDouble(values[2]),Double.parseDouble(values[3])));
		this.center = getCenterCords();
	}
	
	public List<Vertex3D> getVertices(){
		return this.v;
	}
	
	public List<Edge> getEdges() {
		return this.e;
	}

	public void setCenter(Vertex3D center) {
		this.center = center;
	}*/
}
