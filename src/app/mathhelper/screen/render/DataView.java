package app.mathhelper.screen.render;

import app.mathhelper.shape.ObjectInfo;
import javafx.scene.layout.VBox;

public class DataView extends VBox {
	private ObjectInfo info;
	
	public DataView(VBox box) {
		super(box);
	}

	public ObjectInfo getInfo() {
		return info;
	}

	public void setInfo(ObjectInfo info) {
		this.info = info;
	}

}
