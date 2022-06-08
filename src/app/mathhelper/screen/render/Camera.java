package app.mathhelper.screen.render;

import java.awt.image.BufferedImage;

import app.mathhelper.shape.GeometryObject;
import app.mathhelper.shape.preset.Preset;

public abstract class Camera {
	private static int LAST_CAMERA_ID = 0;
	public int id;
	
	protected int width;
	protected int height;
	public BufferedImage context;
	
	public Preset objectType = Preset.CUBE;
	
	public Camera(int width, int height) {
		this.id = LAST_CAMERA_ID++;
		this.width = width;
		this.height = height;
		
		this.context = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public abstract void drawContext();
	
	public abstract BufferedImage getToDrawContex(int width, int height, int xOffset, int yOffset);
	
	public abstract GeometryObject mouseClick(int x, int y);
	
	public abstract void mouseScroll(int d);
	
	public abstract void mouseDragged(int dx, int dy);

	protected abstract void update(int width, int height);

	public abstract void moveY(double d);

	public abstract void moveX(double d);
}
