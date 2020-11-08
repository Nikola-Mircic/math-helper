package app.mathhelper.screen;

import java.awt.Graphics;

import javax.swing.JPanel;

import app.mathhelper.input.InputListener;
import app.mathhelper.shape.Object3D;
import app.mathhelper.shape.preset.Cube;
import app.mathhelper.shape.preset.Tetrahedron;

public class Screen extends JPanel {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private int WIDTH,HEIGHT;
	
	private Render render;
	private Object3D object;
	
	private InputListener il;
	
	public Screen(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.render = new Render(width, height);
		object = new Tetrahedron(0, 0, 0);
		
		this.il = new InputListener(this);
		
		this.add(render);
		this.addMouseListener(il);
		this.addMouseMotionListener(il);
	}
	
	@Override
	public void paint(Graphics g) {
		render.renderObject(object);
		g.drawImage(render.getImg(), 0, 0, null);
	}
	
	public void update(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.remove(render);
		this.render = new Render(width, height);
		this.il = new InputListener(this);
		
		this.add(render);
		this.addMouseListener(il);
		this.addMouseMotionListener(il);
	}

	public Render getRender() {
		return render;
	}

	public void setRender(Render render) {
		this.render = render;
	}
	
	public void resetShape() {
		this.object = new Object3D();
	}

	public InputListener getInputListener() {
		return il;
	}

	public void setInputListener(InputListener il) {
		this.il = il;
	}
	
	public Object3D getObject() {
		return this.object;
	}
	
	public void setObject(Object3D object) {
		this.object = object;
	}
}
