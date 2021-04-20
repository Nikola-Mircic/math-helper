package app.mathhelper.screen.render;

import java.awt.image.BufferedImage;

import app.mathhelper.shape.shape3d.Object3D;

public abstract class Camera {
	public static int id = 0;
	public int currentId;
	
	protected int width;
	protected int height;
	public BufferedImage context;
	
	public Camera(int width, int height) {
		this.currentId = id++;
		this.width = width;
		this.height = height;
		
		this.context = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	public abstract void drawContext();
	
	public abstract BufferedImage getToDrawContex(int width, int height, int xOffset, int yOffset);
	
	public abstract void mouseClick(int x, int y);
	
	public abstract void mouseScroll(int d);
	
	public abstract void mouseDragged(int dx, int dy);

	protected abstract void update(int width, int height);

	public abstract void moveY(double d);

	public abstract void moveX(double d);
}
