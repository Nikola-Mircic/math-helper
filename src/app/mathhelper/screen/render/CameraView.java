package app.mathhelper.screen.render;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import app.mathhelper.screen.Screen;
import app.mathhelper.shape.GeometryObject;
import app.mathhelper.shape.ObjectInfo;
import app.mathhelper.shape.ObjectInfoCalculator;
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
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;

public class CameraView{
	private Screen screen;
	private HBox box;
	
	private List<Camera> cameras;
	private int activeCamera;
	public int cameraCount;
	
	private BufferedImage img;
	
	public static boolean MULTIPLE_CAMERA_ENABLED = true;
	
	public CameraView(HBox box, Screen screen) {
		this((int)box.getWidth(), (int)box.getHeight());
		
		this.screen = screen;
		this.box = box;
		
		this.renderCameras();
		
		CameraPane temp;
		
		for(Camera camera : cameras) {
			temp = new CameraPane(getWidth()/cameraCount, getHeight(), camera, "Frame "+camera.currentId, this);
			box.getChildren().add(temp);
		}
		
		box.widthProperty().addListener((obs, oldValue, newValue)->{
			this.update(getWidth(), getHeight());
		});
		
		box.heightProperty().addListener((obs, oldValue, newValue)->{
			this.update(getWidth(), getHeight());
		});
		
		box.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				int temp = x/(getWidth()/cameraCount);
				
				if(activeCamera != temp)
					screen.getDataView().setInfo(((CameraPane) box.getChildren().get(activeCamera)).getInfo());
				
				activeCamera = temp;
			}
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
			temp = new CameraPane(getWidth()/cameraCount, getHeight(), camera, "Frame "+camera.currentId, this);
			box.getChildren().add(temp);
		}
	}
	
	public void onKeyEvent(EventType<KeyEvent> type, KeyEvent event) {
		if(type == KeyEvent.KEY_PRESSED) {
			switch (event.getCode()) {
			case F1:
				addCamera();
				System.out.println(cameraCount);
				break;
			case F2:
				removeCamera();
				System.out.println(cameraCount);
				break;
			case F3:
				//TODO saveScreenshot();
				break;
			default:
				((CameraPane) box.getChildren().get(activeCamera)).handleKeyPressed(event);
				break;
			}
		}
			
		if(type == KeyEvent.KEY_RELEASED)
			((CameraPane) box.getChildren().get(activeCamera)).handleKeyReleased(event);
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

	public CameraPane getCameraPane() {
		return (CameraPane)this.box.getChildren().get(activeCamera);
	}
	
	public void setObject(Object3D object) {
		this.cameras.set(activeCamera, new Camera3D(getWidth()/cameraCount, this.getHeight(), object));
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
			
			box.getChildren().clear();
			
			CameraPane temp;
			
			for(Camera camera : cameras) {
				temp = new CameraPane(getWidth()/cameraCount, getHeight(), camera, "Frame "+camera.currentId, this);
				box.getChildren().add(temp);
			}
			
			screen.getDataView().setInfo(((CameraPane) box.getChildren().get(activeCamera)).getInfo());
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
			
			box.getChildren().clear();
			
			CameraPane temp;
			
			for(Camera camera : cameras) {
				temp = new CameraPane(getWidth()/cameraCount, getHeight(), camera, "Frame "+camera.currentId, this);
				box.getChildren().add(temp);
			}
			
			screen.getDataView().setInfo(((CameraPane) box.getChildren().get(activeCamera)).getInfo());
		}
	}
	
	public int getWidth() {
		return (int)this.box.getWidth();
	}
	
	public int getHeight() {
		return (int)this.box.getHeight();
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}
}

class CameraPane extends Pane{
	Camera camera;
	ObjectInfo info;
	String frameName;
	
	Label frameNameLabel;
	Button exit;
	ImageView view;
	
	CameraView cameraView;
	
	private double lastMouseX = -1, lastMouseY = -1;
	private boolean disabledRotation[] = {false, false, false};
	
	public CameraPane(int width, int height, Camera camera, String frameName, CameraView cameraView) { 
		this.setWidth(width);
		this.setHeight(height);
		this.setFrameName(frameName);
		this.setCamera(camera);
		this.setCameraView(cameraView);
		
		this.setInfo(ObjectInfoCalculator.getObjectInfo(((Camera3D) camera).getObject()));
		
		this.frameNameLabel = new Label(frameName);
		frameNameLabel.setLayoutX(5.0);
		frameNameLabel.setLayoutY(5.0);
		frameNameLabel.setFont(new Font("Arial", 15));
		
		this.exit = new Button("X");
		exit.setLayoutX(width-55.0);
		exit.setLayoutY(5.0);
		exit.setPrefWidth(15.0);
		exit.setPrefHeight(15.0);
		
		Pane p = new Pane(frameNameLabel, exit);
		p.setPrefWidth(width);
		p.setStyle("-fx-border-color: #000; -fx-border-width: 1pt;");
		
		int camW = camera.context.getWidth();
		int camH = camera.context.getHeight();
		int imageLayoutY = 30;
		
		int xOffset = (camW - width) / 2;
		int yOffset = (camH - (height-imageLayoutY)) / 2;
		
		Image img = SwingFXUtils.toFXImage(camera.getToDrawContex(width, height-imageLayoutY, xOffset, yOffset), null);
		this.view = new ImageView(img);
		view.setLayoutY(imageLayoutY);
		
		this.getChildren().add(p);
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
					
					if(disabledRotation[2]) {
						camera.mouseDragged(dx, 0);
					}else if(disabledRotation[1]) {
						camera.mouseDragged(0, dy);
					}else {
						camera.mouseDragged(dx, dy);
					}
					
					lastMouseX = event.getX();
					lastMouseY = event.getY();
					
					updateImage();
				}
				
			}
		};
		
		EventHandler<MouseEvent> clickMouse = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				GeometryObject object = camera.mouseClick((int)event.getX(), (int)event.getY());
				System.out.println("Get: "+object);
				if(object != null)
					cameraView.getScreen().getDataView().setInfo(ObjectInfoCalculator.getObjectInfo(object));
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
	
	
	public void handleKeyPressed(KeyEvent e) {
		System.out.println("Key is Pressed");
		switch (e.getCode()) {
		case R:
			if(e.isControlDown() && e.isShiftDown()) {
				((Camera3D) this.camera).setObject(this.camera.objectType.getObject());
			}
			break;
		case T:
			if(this.camera instanceof Camera3D) {
				((Camera3D) camera).renderMode++;
				((Camera3D) camera).renderMode%=2;
				camera.drawContext();
			}
			break;
		case C:
			if(getCameraView().getCamera() instanceof Camera3D)
				((Camera3D) getCameraView().getCamera()).switchLightEffect();
			break;
		case DIGIT1:
			if(e.isShiftDown()) {
				System.out.println("SHIFT + 1");
				disabledRotation[1] = !disabledRotation[1];
				disabledRotation[2] = false;
			}else {
				System.out.println("1");
				if(this.camera instanceof Camera3D)
					this.camera.objectType = Preset.CUBE;
					((Camera3D) this.camera).setObject(Preset.CUBE.getObject());
					this.info = ObjectInfoCalculator.getObjectInfo(((Camera3D) this.camera).getObject());
					this.cameraView.getScreen().getDataView().setInfo(info);
			}
			break;
		case DIGIT2:
			if(e.isShiftDown()) {
				System.out.println("SHIFT + 2");
				disabledRotation[2] = !disabledRotation[2];
				disabledRotation[1] = false;
			}else {
				System.out.println("2");
				if(this.camera instanceof Camera3D)
					this.camera.objectType = Preset.TETRAHEDRON;
					((Camera3D) this.camera).setObject(Preset.TETRAHEDRON.getObject());
					this.info = ObjectInfoCalculator.getObjectInfo(((Camera3D) this.camera).getObject());
					this.cameraView.getScreen().getDataView().setInfo(info);
			}
			break;
		case DIGIT3:
			if(this.camera instanceof Camera3D)
				this.camera.objectType = Preset.TEAPOT;
				((Camera3D) this.camera).setObject(Preset.TEAPOT.getObject());
				this.info = ObjectInfoCalculator.getObjectInfo(((Camera3D) this.camera).getObject());
				this.cameraView.getScreen().getDataView().setInfo(info);
			break;
		case DIGIT4:
			if(this.camera instanceof Camera3D)
				this.camera.objectType = Preset.CONE;
				((Camera3D) this.camera).setObject(Preset.CONE.getObject());
				this.info = ObjectInfoCalculator.getObjectInfo(((Camera3D) this.camera).getObject());
				this.cameraView.getScreen().getDataView().setInfo(info);
			break;
		case DIGIT5:
			if(this.camera instanceof Camera3D)
				this.camera.objectType = Preset.ICOSPHERE;
				((Camera3D) this.camera).setObject(Preset.ICOSPHERE.getObject());
				this.info = ObjectInfoCalculator.getObjectInfo(((Camera3D) this.camera).getObject());
				this.cameraView.getScreen().getDataView().setInfo(info);
			break;
		case DIGIT6:
			if(this.camera instanceof Camera3D)
				this.camera.objectType = Preset.CYLINDER;
				((Camera3D) this.camera).setObject(Preset.CYLINDER.getObject());
				this.info = ObjectInfoCalculator.getObjectInfo(((Camera3D) this.camera).getObject());
				this.cameraView.getScreen().getDataView().setInfo(info);
			break;
		case DIGIT7:
			if(this.camera instanceof Camera3D)
				this.camera.objectType = Preset.BALL;
				((Camera3D) this.camera).setObject(Preset.BALL.getObject());
				this.info = ObjectInfoCalculator.getObjectInfo(((Camera3D) this.camera).getObject());
				this.cameraView.getScreen().getDataView().setInfo(info);
			break;
		case CONTROL:
			if(getCameraView().getCamera() instanceof Camera3D)
				((Camera3D)camera).renderingCenter = true;
			break;
		case UP:
			camera.moveY(0.1);
			break;
		case DOWN:
			camera.moveY(-0.1);
			break;
		case LEFT:
			camera.moveX(-0.1);
			break;
		case RIGHT:
			camera.moveX(0.1);
			break;
		default:
			break;
		}
		
		updateImage();
	}
	
	public void handleKeyReleased(KeyEvent e) {
		switch (e.getCode()) {
		case CONTROL:
			if(this.camera instanceof Camera3D)
				((Camera3D)this.camera).renderingCenter = false;
			break;

		default:
			break;
		}
	}
	
	private void saveScreenshot() {
		//TODO
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

	public CameraView getCameraView() {
		return cameraView;
	}

	public void setCameraView(CameraView cameraView) {
		this.cameraView = cameraView;
	}

	public ObjectInfo getInfo() {
		return info;
	}

	public void setInfo(ObjectInfo info) {
		this.info = info;
	}
}



