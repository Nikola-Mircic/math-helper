package app.mathhelper.screen.render;

import java.awt.*;
import java.awt.image.*;

import app.mathhelper.shape.*;
import app.mathhelper.shape.preset.*;

import java.util.*;
import java.util.List;

public class Render extends Canvas{
	private static final long serialVersionUID = 1L;
	
	public int WIDTH,HEIGHT;
	
	private List<Camera3D> cameras;
	private int activeCamera;
	public int cameraCount;
	
	private BufferedImage img;
	
	public Render(int w,int h) {
		this.WIDTH = w;
		this.HEIGHT = h;
		
		this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		this.cameras = new ArrayList<>();
		this.activeCamera = 0;
		this.cameraCount = 2;
		for(int i=0;i<cameraCount;++i) {
			cameras.add(new Camera3D(this.WIDTH/cameraCount, this.HEIGHT, Preset.CUBE.model));
		}
	}
	
	public void renderCameras() {
		BufferStrategy bs = getBufferStrategy();
		do {
			createBufferStrategy(3);
			bs=getBufferStrategy();
		}while(bs==null);
		
		Graphics g = img.getGraphics();
		
		for(int i=0;i<cameraCount;++i) {
			g.drawImage(cameras.get(i).getToDrawContex(WIDTH/cameraCount, HEIGHT, 0, 0), i*WIDTH/cameraCount, 0, null);
			if(i==activeCamera) {
				g.setColor(Color.GREEN);
				g.fillOval((i+1)*WIDTH/cameraCount-30, HEIGHT-55, 10, 10);
			}
		}
	}

	
	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public Camera3D getCamera() {
		return this.cameras.get(activeCamera);
	}

	public void setCamera(Camera3D camera) {
		this.cameras.set(activeCamera, camera);
	}

	public void setObject(Object3D object) {
		this.cameras.set(activeCamera, new Camera3D(this.WIDTH/cameraCount, this.HEIGHT, object));
	}

	public void mousePressed(int x, int y) {
		this.activeCamera = x/(this.WIDTH/cameraCount);
	}
	
}
