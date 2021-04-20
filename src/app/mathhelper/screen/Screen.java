package app.mathhelper.screen;

import java.awt.Graphics;

import javax.swing.JPanel;

import app.mathhelper.input.InputListener;
import app.mathhelper.screen.render.Camera3D;
import app.mathhelper.screen.render.CameraView;
import app.mathhelper.shape.shape3d.Object3D;

public class Screen extends JPanel{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private int WIDTH,HEIGHT;
	
	private CameraView camView;
	
	private InputListener il;
	
	public Screen(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.camView = new CameraView(width, height);
		
		this.il = new InputListener(this);
		
		this.addMouseListener(il);
		this.addMouseMotionListener(il);
		this.addMouseWheelListener(il);
	}
	
	@Override
	public void paint(Graphics g) {
		camView.renderCameras();
		g.drawImage(camView.getImg(), 0, 0, null);
	}
	
	public void update(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
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
