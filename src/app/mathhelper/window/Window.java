package app.mathhelper.window;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.JavaBean;

import javax.swing.JFrame;
import javax.swing.UIManager;

import app.mathhelper.screen.Screen;
import app.mathhelper.screen.gui.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Window extends Stage{
	private final String TITLE = "Math-helper";
	
	/*private Stage stage;
	private Screen screen;*/
	
	FXMLLoader loader = null;
	Parent root = null;
	
	public Window(Class<?> mainClass) {
		this.setTitle(TITLE);
		this.setMaximized(true);
		
		try {
			this.loader = new FXMLLoader(mainClass.getResource("/app/mathhelper/screen/gui/main.fxml"));
			
			this.root = loader.load();
			
			Screen screen = new Screen(root, loader.getController());
			
			screen.getStylesheets().add(getClass().getResource("/app/mathhelper/screen/gui/main.css").toExternalForm());
			
			this.setScene(screen);
			
			this.show();
			
			screen.setCameraView(((Controller)this.loader.getController()).createCVObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
