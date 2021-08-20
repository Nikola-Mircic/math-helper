package app.mathhelper.screen.gui;

import app.mathhelper.screen.Screen;
import app.mathhelper.screen.render.CameraView;
import app.mathhelper.screen.render.DataView;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller{
	
	@FXML
	public AnchorPane dataContainer;
	
	@FXML
    private HBox cameras;
	
	@FXML
	public VBox dataView;
	
	@FXML
	public HBox objectTypes;
	
	public CameraView createCVObject(Screen screen) {
		if(cameras == null)
			System.out.println("Can't find HBox");
		return new CameraView(cameras, screen);
	}
	
	public DataView createDVObject(Screen screen) {
		if(dataView == null)
			System.out.println("Can't find VBox");
		return new DataView(dataView, screen);
	}
	
}
