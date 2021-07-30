package app.mathhelper.screen.render;

import java.awt.*;
import java.awt.image.BufferedImage;

import app.mathhelper.shape.preset.*;
import app.mathhelper.shape.shape2d.Edge2D;
import app.mathhelper.shape.shape2d.Shape2D;
import app.mathhelper.shape.shape2d.Triangle2D;
import app.mathhelper.shape.shape2d.Vertex2D;
import app.mathhelper.shape.shape3d.Object3D;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.*;
import java.util.List;

public class CameraView{
	private HBox box;
	
	private List<Camera> cameras;
	private int activeCamera;
	public int cameraCount;
	
	private BufferedImage img;
	
	public static boolean MULTIPLE_CAMERA_ENABLED = true;
	
	public CameraView(HBox box) {
		this((int)box.getWidth(), (int)box.getHeight());
		
		this.box = box;
		
		this.renderCameras();
		
		CameraPane temp;
		
		for(Camera camera : cameras) {
			temp = new CameraPane(getWidth()/cameraCount, getHeight(), camera, "Frame "+camera.currentId);
			box.getChildren().add(temp);
		}
		
		box.widthProperty().addListener((obs, oldValue, newValue)->{
			this.update(getWidth(), getHeight());
		});
		
		box.heightProperty().addListener((obs, oldValue, newValue)->{
			this.update(getWidth(), getHeight());
		});
	}
	
	public CameraView(int w,int h) {
		System.out.println("Creating CameraView: W:"+w+" H:"+h);
		
		this.img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		
		this.cameras = new ArrayList<>();
		this.activeCamera = 0;
		this.cameraCount = 2;
		for(int i=0;i<cameraCount;++i) {
			cameras.add(new Camera3D(w/cameraCount, h, Preset.CUBE.getObject()));
		}
	}
	
	public void renderCameras() {
		Graphics g = img.getGraphics();
		
		for(int i=0;i<cameras.size();++i) {
			g.drawImage(cameras.get(i).getToDrawContex(getWidth()/cameraCount, getHeight(), 0, 0), i*getWidth()/cameraCount, 0, null);
			if(i==activeCamera) {
				g.setColor(Color.GREEN);
				g.fillOval((i+1)*getWidth()/cameraCount-30, getHeight()-55, 10, 10);
			}
		}
		
		g.dispose();
	}
	
	public void update(int width, int height) {
		this.img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		
		box.getChildren().clear();
		
		CameraPane temp;
		
		for(Camera camera : cameras) {
			camera.update(width/cameraCount, height);
			temp = new CameraPane(getWidth()/cameraCount, getHeight(), camera, "Frame "+camera.currentId);
			box.getChildren().add(temp);
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
		this.cameras.set(activeCamera, new Camera3D(getWidth()/cameraCount, this.getHeight(), object));
	}

	public void mousePressed(int x, int y) {
		int temp = x/(getWidth()/cameraCount);
		if(temp == this.activeCamera) {
			cameras.get(activeCamera).mouseClick(x-(int)(getWidth()/cameraCount)*activeCamera, y);
		}
		this.activeCamera = temp;
	}
	
	public void addCamera() {
		if(cameraCount<5 && MULTIPLE_CAMERA_ENABLED) {
			for(int i=0;i<cameraCount;++i) {
				if(cameras.get(i) instanceof Camera2D) {
					cameras.set(i, new Camera2D(getWidth()/(cameraCount+1), this.getHeight(), ((Camera2D)cameras.get(i)).getShape()));
				}else {
					cameras.set(i, new Camera3D(getWidth()/(cameraCount+1), this.getHeight(), ((Camera3D)cameras.get(i)).getObject()));
				}
			}
			this.cameraCount++;
			cameras.add(new Camera3D(getWidth()/cameraCount, this.getHeight(), Preset.CUBE.getObject()));
			activeCamera = cameraCount-1;
			System.out.println(cameraCount);
		}
	}
	
	public void removeCamera() {
		if(cameraCount>1) {
			for(int i=0;i<cameraCount;++i) {
				if(cameras.get(i) instanceof Camera2D) {
					cameras.set(i, new Camera2D(getWidth()/(cameraCount-1), this.getHeight(), ((Camera2D)cameras.get(i)).getShape()));
				}else {
					cameras.set(i, new Camera3D(getWidth()/(cameraCount-1), this.getHeight(), ((Camera3D)cameras.get(i)).getObject()));
				}
			}
			this.cameraCount--;
			cameras.remove(activeCamera);
			activeCamera = cameraCount-1;
			System.out.println(cameraCount);
		}
	}
	
	public int getWidth() {
		return (int)this.box.getWidth();
	}
	
	public int getHeight() {
		return (int)this.box.getHeight();
	}
}

class CameraPane extends Pane{
	Camera camera;
	String frameName;
	
	Label frameNameLabel;
	Button exit;
	ImageView view;
	
	private double lastMouseX = -1, lastMouseY = -1;
	
	public CameraPane(int width, int height, Camera camera, String frameName) { 
		this.setWidth(width);
		this.setHeight(height);
		this.setFrameName(frameName);
		this.setCamera(camera);
		
		this.frameNameLabel = new Label(frameName);
		frameNameLabel.setLayoutX(5.0);
		frameNameLabel.setLayoutY(5.0);
		frameNameLabel.setFont(new Font("Arial", 15));
		
		this.exit = new Button("X");
		exit.setLayoutX(width-55.0);
		exit.setLayoutY(5.0);
		exit.setPrefWidth(15.0);
		exit.setPrefHeight(15.0);
		
		int camW = camera.context.getWidth();
		int camH = camera.context.getHeight();
		int imageLayoutY = 30;
		
		int xOffset = (camW - width) / 2;
		int yOffset = (camH - (height-imageLayoutY)) / 2;
		
		Image img = SwingFXUtils.toFXImage(camera.getToDrawContex(width, height-imageLayoutY, xOffset, yOffset), null);
		this.view = new ImageView(img);
		view.setLayoutY(imageLayoutY);
		
		System.out.println(getHeight()+" = "+img.getHeight()+" + "+imageLayoutY);
		
		this.getChildren().add(frameNameLabel);
		this.getChildren().add(exit);
		this.getChildren().add(view);
		
		addListeners();
	}

	private void addListeners() {
		EventHandler<MouseEvent> draggMouse = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(lastMouseX == -1 && lastMouseY == -1) {
					lastMouseX = event.getX();
					lastMouseY = event.getY();
				}else {
					int dx = (int)(event.getX()-lastMouseX);
					int dy = (int)(event.getY()-lastMouseY);
					
					camera.mouseDragged(dx, dy);
					
					lastMouseX = event.getX();
					lastMouseY = event.getY();
					
					updateImage();
				}
				
			}
		};
		
		EventHandler<MouseEvent> clickMouse = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				camera.mouseClick((int)event.getX(), (int)event.getY());
			}
		};
		
		EventHandler<MouseEvent> releaseMouse = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				lastMouseX = -1;
				lastMouseY = -1;
			}
		};
		
		this.addEventFilter(MouseEvent.MOUSE_DRAGGED, draggMouse);
		this.addEventFilter(MouseEvent.MOUSE_CLICKED, clickMouse);
		this.addEventFilter(MouseEvent.MOUSE_RELEASED, releaseMouse);
	}
	
	private void updateImage() {
		int camW = camera.context.getWidth();
		int camH = camera.context.getHeight();
		int imageLayoutY = 30;
		
		int xOffset = (int)(camW - getWidth()) / 2;
		int yOffset = (int)(camH - (getHeight()-imageLayoutY)) / 2;
		
		Image img = SwingFXUtils.toFXImage(camera.getToDrawContex((int)getWidth(), (int)getHeight()-imageLayoutY, xOffset, yOffset), null);
		this.view.setImage(img);
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public String getFrameName() {
		return frameName;
	}

	public void setFrameName(String frameName) {
		this.frameName = frameName;
	}
}



