package app.mathhelper.screen;

import java.awt.Graphics;

import javax.swing.JPanel;

import app.mathhelper.input.InputListener;
import app.mathhelper.screen.gui.Controller;
import app.mathhelper.screen.render.Camera3D;
import app.mathhelper.screen.render.CameraView;
import app.mathhelper.shape.shape3d.Object3D;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Screen extends Scene{
	private CameraView camView;
	
	private InputListener il;
	
	public Screen(Parent root, Controller controller) {
		super(root);
	}
	
	public void update(int width, int height) {
		this.camView.update(width, height);
	}

	public CameraView getCameraView() {
		return camView;
	}

	public void setCameraView(CameraView camView) {
		this.camView = camView;
	}
	
	public InputListener getInputListener() {
		return il;
	}

	public void setInputListener(InputListener il) {
		this.il = il;
	}
	
	public Object3D getObject() {
		try {
			return ((Camera3D) camView.getCamera()).getObject();
		} catch (Exception e) {
			return null;
		}
		//return camView.getCamera().getObject();
	}
	
	public void setObject(Object3D object) {
		camView.setObject(object);
	}

	public void mousePressed(int x, int y) {
		camView.mousePressed(x, y);
	}
}
