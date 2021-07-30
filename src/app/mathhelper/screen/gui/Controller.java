package app.mathhelper.screen.gui;

import app.mathhelper.screen.render.CameraView;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller{
	
	@FXML
    private HBox cameras;
	
	@FXML
	public VBox dataView;
	
	@FXML
	public HBox objectTypes;
	
	public CameraView createCVObject() {
		if(cameras == null)
			System.out.println("Can't find HBox");
		return new CameraView(cameras);
	}
	
}
