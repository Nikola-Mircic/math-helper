package app.mathhelper.screen;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Screen extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int WIDTH,HEIGHT;
	
	private Render render;
	
	public Screen(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.render = new Render(width, height);
		this.add(render);
	}
	
	public void paint(Graphics g) {
		render.draw();
		g.drawImage(render.img, 0, 0, null);
	}
}
