package app.mathhelper.screen.render;

import app.mathhelper.screen.Screen;
import app.mathhelper.shape.ObjectInfo;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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
		
		double prefWidth = screen.getController().dataContainer.getPrefWidth();
		
		box.setPrefWidth(prefWidth);
		
		box.widthProperty().addListener((obs, oldValue, newValue)->{
			this.creatInfoView();
		});
		
		box.heightProperty().addListener((obs, oldValue, newValue)->{
			this.creatInfoView();
		});
		
		System.out.println("Added DataView");
	}

	public ObjectInfo getInfo() {
		return info;
	}

	public void setInfo(ObjectInfo info) {
		this.info = info;
		
		creatInfoView();
	}
	
	public void creatInfoView() {
		if(box == null || info == null || screen.getController() == null)
			return;
		double prefWidth = screen.getController().dataContainer.getWidth();
		box.getChildren().clear();
		box.setPrefWidth(prefWidth);
		this.info.forEach((name, value)->{
			Label label = new Label(name + " : ");
			//label.setMaxWidth(box.getWidth()/2);
			TextField tf = new TextField(value);
			tf.setEditable(false);
			//tf.setMaxWidth(box.getWidth()/2);
			//HBox item = new HBox(label, tf);
			FlowPane item = new FlowPane(Orientation.HORIZONTAL);
			item.setMaxWidth(box.getWidth());
			item.setPrefWrapLength(box.getWidth());
			
			item.setVgap(5);
			item.setHgap(10);
			
			item.getChildren().add(label);
			item.getChildren().add(tf);
			
			box.getChildren().add(item);
		});
	}

	public VBox getBox() {
		return box;
	}

	public void setBox(VBox box) {
		this.box = box;
	}

}
