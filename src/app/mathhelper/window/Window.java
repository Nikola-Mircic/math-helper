package app.mathhelper.window;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.JavaBean;

import javax.swing.JFrame;

import app.mathhelper.screen.Screen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Window extends Application implements Runnable{
	private final String TITLE = "Math-helper";
	
	private Screen screen;
	
	public Window() {
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void run() {
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle(TITLE);
		stage.setMaximized(true);
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/app/mathhelper/screen/gui/main.fxml"));
			Scene s = new Scene(root);
			s.getStylesheets().add(getClass().getResource("/app/mathhelper/screen/gui/main.css").toExternalForm());
			stage.setScene(s);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
