package app.mathhelper.screen.render;

import java.awt.*;
import java.awt.image.*;

import app.mathhelper.shape.preset.*;
import app.mathhelper.shape.shape2d.Edge2D;
import app.mathhelper.shape.shape2d.Shape2D;
import app.mathhelper.shape.shape2d.Triangle2D;
import app.mathhelper.shape.shape2d.Vertex2D;
import app.mathhelper.shape.shape3d.Object3D;

import java.util.*;
import java.util.List;

public class CameraView{
	public int WIDTH,HEIGHT;
	
	private List<Camera> cameras;
	private int activeCamera;
	public int cameraCount;
	
	private BufferedImage img;
	
	public static boolean MULTIPLE_CAMERA_ENABLED = true;
	
	public CameraView(int w,int h) {
		this.WIDTH = w;
		this.HEIGHT = h;
		
		this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		this.cameras = new ArrayList<>();
		this.activeCamera = 0;
		this.cameraCount = 2;
		/*for(int i=0;i<cameraCount;++i) {
			cameras.add(new Camera3D(this.WIDTH/cameraCount, this.HEIGHT, Preset.CUBE.getObject()));
		}*/
		cameras.add(new Camera3D(this.WIDTH/cameraCount, this.HEIGHT, Preset.CUBE.getObject()));
		cameras.add(new Camera2D(this.WIDTH/cameraCount, this.HEIGHT, new Test2D()));
	}
	
	public void renderCameras() {
		Graphics g = img.getGraphics();
		
		for(int i=0;i<cameras.size();++i) {
			g.drawImage(cameras.get(i).getToDrawContex(WIDTH/cameraCount, HEIGHT, 0, 0), i*WIDTH/cameraCount, 0, null);
			if(i==activeCamera) {
				g.setColor(Color.GREEN);
				g.fillOval((i+1)*WIDTH/cameraCount-30, HEIGHT-55, 10, 10);
			}
		}
		
		g.dispose();
	}
	
	public void update(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		for(Camera camera : cameras) {
			camera.update(width/cameraCount, height);
		}
	}
	
	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public Camera getCamera() {
		return this.cameras.get(activeCamera);
	}

	public void setCamera(Camera3D camera) {
		this.cameras.set(activeCamera, camera);
	}

	public void setObject(Object3D object) {
		this.cameras.set(activeCamera, new Camera3D(this.WIDTH/cameraCount, this.HEIGHT, object));
	}

	public void mousePressed(int x, int y) {
		int temp = x/(this.WIDTH/cameraCount);
		if(temp == this.activeCamera) {
			cameras.get(activeCamera).mouseClick(x-(WIDTH/cameraCount)*activeCamera, y);
		}
		this.activeCamera = temp;
	}
	
	public void addCamera() {
		if(cameraCount<5 && MULTIPLE_CAMERA_ENABLED) {
			for(int i=0;i<cameraCount;++i) {
				if(cameras.get(i) instanceof Camera2D) {
					cameras.set(i, new Camera2D(this.WIDTH/(cameraCount+1), this.HEIGHT, ((Camera2D)cameras.get(i)).getShape()));
				}else {
					cameras.set(i, new Camera3D(this.WIDTH/(cameraCount+1), this.HEIGHT, ((Camera3D)cameras.get(i)).getObject()));
				}
			}
			this.cameraCount++;
			cameras.add(new Camera3D(this.WIDTH/cameraCount, this.HEIGHT, Preset.CUBE.getObject()));
			activeCamera = cameraCount-1;
			System.out.println(cameraCount);
		}
	}
	
	public void removeCamera() {
		if(cameraCount>1) {
			for(int i=0;i<cameraCount;++i) {
				if(cameras.get(i) instanceof Camera2D) {
					cameras.set(i, new Camera2D(this.WIDTH/(cameraCount-1), this.HEIGHT, ((Camera2D)cameras.get(i)).getShape()));
				}else {
					cameras.set(i, new Camera3D(this.WIDTH/(cameraCount-1), this.HEIGHT, ((Camera3D)cameras.get(i)).getObject()));
				}
			}
			this.cameraCount--;
			cameras.remove(activeCamera);
			activeCamera = cameraCount-1;
			System.out.println(cameraCount);
		}
	}
	
}

class Test2D extends Shape2D {
	public Test2D() {
		super();
		
		loadVertices();
		loadEdges();
		loadTriangles();
	}
	
	private void loadTriangles() {
		this.triangles.add(new Triangle2D((Vertex2D)v.get(0), (Vertex2D)v.get(3), (Vertex2D)v.get(1)));
		this.triangles.add(new Triangle2D((Vertex2D)v.get(1), (Vertex2D)v.get(3), (Vertex2D)v.get(2)));
	}

	private void loadEdges() {
		this.e.add(new Edge2D((Vertex2D)v.get(0), (Vertex2D)v.get(1)));
		this.e.add(new Edge2D((Vertex2D)v.get(1), (Vertex2D)v.get(2)));
		this.e.add(new Edge2D((Vertex2D)v.get(2), (Vertex2D)v.get(3)));
		this.e.add(new Edge2D((Vertex2D)v.get(3), (Vertex2D)v.get(0)));
	}

	private void loadVertices() {
		this.v.add(new Vertex2D("A", -100, 100));
		this.v.add(new Vertex2D("A",  100, 100));
		this.v.add(new Vertex2D("A",  100, -100));
		this.v.add(new Vertex2D("A", -100, -100));
	}
}


