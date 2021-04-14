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
}
