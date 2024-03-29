package app.mathhelper.shape;

public abstract class GeometryObject {
	protected ObjectInfo info;
	
	public static interface InfoHandler{
		public void handle(ObjectInfo info);
	}
	
	public void handleInfo(InfoHandler handler) {
		handler.handle(this.info);
	}

	public ObjectInfo getInfo() {
		return info;
	}

	public void setInfo(ObjectInfo info) {
		this.info = info;
	}
}
