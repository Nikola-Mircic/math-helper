package app.mathhelper;

import javax.swing.UIManager;

import app.mathhelper.window.Window;
import javafx.application.Application;
import javafx.stage.Stage;

public class MathHelper extends Application{
	
	public MathHelper() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		setUserAgentStylesheet(STYLESHEET_CASPIAN);
		
		Window w = new Window(MathHelper.class);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
