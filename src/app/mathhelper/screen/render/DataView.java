package app.mathhelper.screen.render;

import app.mathhelper.screen.Screen;
import app.mathhelper.shape.ObjectInfo;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class DataView{
	private Screen screen;
	private VBox box;
	private ObjectInfo info;
	
	public DataView(VBox box, Screen screen) {
		this.screen = screen;
		this.setBox(box);
		
		this.setInfo(screen.getCameraView().getCameraPane().getInfo());
		
		//Object change -> calculation and refresh
		
		System.out.println("Added DataView");
	}

	public ObjectInfo getInfo() {
		return info;
	}

	public void setInfo(ObjectInfo info) {
		this.info = info;
		
		box.getChildren().clear();
		this.info.forEach((name, value)->{
			Label label = new Label(name + " : " + value);
			box.getChildren().add(label);
		});
	}

	public VBox getBox() {
		return box;
	}

	public void setBox(VBox box) {
		this.box = box;
	}

}
