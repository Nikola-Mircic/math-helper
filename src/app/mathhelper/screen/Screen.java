package app.mathhelper.screen;

import java.awt.Graphics;

import javax.swing.JPanel;

import app.mathhelper.input.InputListener;

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
		
		this.il = new InputListener(render);
		
		this.add(render);
		this.addMouseListener(il);
		this.addMouseMotionListener(il);
	}
	
	@Override
	public void paint(Graphics g) {
		render.draw();
		g.drawImage(render.getImg(), 0, 0, null);
	}
	
	public void update(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.remove(render);
		this.render = new Render(width, height);
		this.il = new InputListener(render);
		
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
}
