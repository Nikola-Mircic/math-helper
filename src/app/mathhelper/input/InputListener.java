package app.mathhelper.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import app.mathhelper.screen.Screen;
import app.mathhelper.screen.render.Camera;
import app.mathhelper.screen.render.Camera3D;
import app.mathhelper.shape.preset.Preset;

public class InputListener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	private Screen screen;
	
	private int lastX,lastY;
	
	private Preset object;
	
	private static boolean disabledRotation[] = {false,false,false};
	
	public InputListener(Screen screen) {
		this.screen = screen;
		
		this.object = Preset.CUBE;
		
		this.lastX = -1;
		this.lastY = -1;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(lastX == -1 || lastY==-1) {
			lastX = e.getX();
			lastY = e.getY();
		}else {
			int x = e.getX()-lastX;
			int y = e.getY()-lastY;
			
			if(disabledRotation[2]) {
				screen.getCameraView().getCamera().mouseDragged(x, 0);
			}else if(disabledRotation[1]) {
				screen.getCameraView().getCamera().mouseDragged(0, y);
			}else {
				screen.getCameraView().getCamera().mouseDragged(x, y);
			}
			
			lastX = e.getX();
			lastY = e.getY();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		screen.mousePressed(x, y);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.lastX = -1;
		this.lastY = -1;
	}
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		//screen.getCameraView().getCamera().moveZ(e.getWheelRotation()/10.0);
		screen.getCameraView().getCamera().mouseScroll(e.getWheelRotation());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Camera camera = screen.getCameraView().getCamera();
		switch (e.getKeyCode()) {
		case KeyEvent.VK_R:
			if(e.isControlDown() && e.isShiftDown()) {
				int mode = ((Camera3D) screen.getCameraView().getCamera()).renderMode;
				Camera3D newCamera = new Camera3D(screen.getCameraView().getWidth()/screen.getCameraView().cameraCount, screen.getCameraView().getHeight(), object.getObject());
				newCamera.renderMode = mode;
				newCamera.drawContext();
				screen.getCameraView().setCamera(newCamera);
			}
			break;
		case KeyEvent.VK_T:
			if(screen.getCameraView().getCamera() instanceof Camera3D) {
				((Camera3D) camera).renderMode++;
				((Camera3D) camera).renderMode%=2;
				camera.drawContext();
			}
			break;
		case KeyEvent.VK_C:
			if(screen.getCameraView().getCamera() instanceof Camera3D)
				((Camera3D) screen.getCameraView().getCamera()).switchLightEffect();
			break;
		case KeyEvent.VK_1:
			if(e.isShiftDown()) {
				disabledRotation[1] = !disabledRotation[1];
				disabledRotation[2] = false;
			}else {
				object = Preset.CUBE;
				screen.setObject(object.getObject());
			}
			break;
		case KeyEvent.VK_2:
			if(e.isShiftDown()) {
				disabledRotation[2] = !disabledRotation[2];
				disabledRotation[1] = false;
			}else {
				object = Preset.TETRAHEDRON;
				screen.setObject(object.getObject());
			}
			break;
		case KeyEvent.VK_3:
			object = Preset.TEAPOT;
			screen.setObject(object.getObject());
			break;
		case KeyEvent.VK_4:
			object = Preset.CONE;
			screen.setObject(object.getObject());
			break;
		case KeyEvent.VK_5:
			object = Preset.ICOSPHERE;
			screen.setObject(object.getObject());
			break;
		case KeyEvent.VK_6:
			object = Preset.CYLINDER;
			screen.setObject(object.getObject());
			break;
		case KeyEvent.VK_7:
			object = Preset.BALL;
			screen.setObject(object.getObject());
			break;
		case KeyEvent.VK_CONTROL:
			if(screen.getCameraView().getCamera() instanceof Camera3D)
				((Camera3D)camera).renderingCenter = true;
			break;
		case KeyEvent.VK_UP:
			camera.moveY(0.1);
			break;
		case KeyEvent.VK_DOWN:
			camera.moveY(-0.1);
			break;
		case KeyEvent.VK_LEFT:
			camera.moveX(-0.1);
			break;
		case KeyEvent.VK_RIGHT:
			camera.moveX(0.1);
			break;
		case KeyEvent.VK_F1:
			screen.getCameraView().addCamera();
			System.out.println(screen.getCameraView().cameraCount);
			break;
		case KeyEvent.VK_F2:
			screen.getCameraView().removeCamera();
			System.out.println(screen.getCameraView().cameraCount);
			break;
		case KeyEvent.VK_F3:
			saveScreenshot();
			break;
		default:
			break;
		}
		
		//screen.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_CONTROL:
			if(screen.getCameraView().getCamera() instanceof Camera3D)
				((Camera3D)screen.getCameraView().getCamera()).renderingCenter = false;
			break;

		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	private void saveScreenshot() {
		BufferedImage img = screen.getCameraView().getImg();
		try {
			File saved = new File("saved.jpg");
			if(!saved.exists()){
				saved.createNewFile();
			}
			ImageIO.write(img, "png", saved);
		} catch (Exception e) {
			System.out.println("Failed");
			e.printStackTrace();
		}
	}

}
