package app.mathhelper.screen;

import java.awt.Graphics;

import javax.swing.JPanel;

import app.mathhelper.input.InputListener;
import app.mathhelper.screen.render.Render;
import app.mathhelper.shape.*;

public class Screen extends JPanel {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private int WIDTH,HEIGHT;
	
	private Render render;
	
	private InputListener il;
	
	public Screen(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.render = new Render(width, height);
		
		this.il = new InputListener(this);
		
		this.add(render);
		this.addMouseListener(il);
		this.addMouseMotionListener(il);
		this.addMouseWheelListener(il);
	}
	
	@Override
	public void paint(Graphics g) {
		render.renderCameras();
		g.drawImage(render.getImg(), 0, 0, null);
	}
	
	public void update(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.render.update(width, height);
	}

	public Render getRender() {
		return render;
	}

	public void setRender(Render render) {
		this.render = render;
	}
	
	public InputListener getInputListener() {
		return il;
	}

	public void setInputListener(InputListener il) {
		this.il = il;
	}
	
	public Object3D getObject() {
		return render.getCamera().getObject();
	}
	
	public void setObject(Object3D object) {
		render.setObject(object);
	}

	public void mousePressed(int x, int y) {
		render.mousePressed(x, y);
	}
}
