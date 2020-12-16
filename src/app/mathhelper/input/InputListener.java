package app.mathhelper.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import app.mathhelper.screen.Screen;
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
			/*render.setRotationX(render.getRotationX() + (x/180.0*Math.PI));
			render.setRotationY(render.getRotationY() + (y/180.0*Math.PI));*/
			
			double rotationX = (x/180.0*Math.PI);
			double rotationY = (y/180.0*Math.PI);
			
			if(!disabledRotation[1]) {
				screen.getObject().rotateHorizontal(rotationX/4);
				screen.getRender().getCamera().drawContext();
			}
				
			if(!disabledRotation[2]) {
				screen.getObject().rotateVertical(rotationY/4);
				screen.getRender().getCamera().drawContext();
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
		screen.getRender().getCamera().moveZ(e.getWheelRotation()/10.0);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Camera3D camera = screen.getRender().getCamera();
		switch (e.getKeyCode()) {
		case KeyEvent.VK_R:
			if(e.isControlDown() && e.isShiftDown()) {
				object.recreateObject();
				int mode = screen.getRender().getCamera().renderMode;
				Camera3D newCamera = new Camera3D(screen.getRender().WIDTH/screen.getRender().cameraCount, screen.getRender().HEIGHT, object.model);
				newCamera.renderMode = mode;
				newCamera.drawContext();
				screen.getRender().setCamera(newCamera);
			}
			break;
		case KeyEvent.VK_T:
			camera.renderMode++;
			camera.renderMode%=3;
			camera.drawContext();
			break;
		case KeyEvent.VK_1:
			if(e.isShiftDown()) {
				disabledRotation[1] = !disabledRotation[1];
				disabledRotation[2] = false;
			}else {
				object = Preset.CUBE;
				screen.setObject(object.model);
			}
			break;
		case KeyEvent.VK_2:
			if(e.isShiftDown()) {
				disabledRotation[2] = !disabledRotation[2];
				disabledRotation[1] = false;
			}else {
				object = Preset.TETRAHEDRON;
				screen.setObject(object.model);
			}
			break;
		case KeyEvent.VK_3:
			object = Preset.TEAPOT;
			screen.setObject(object.model);
			break;
		case KeyEvent.VK_4:
			object = Preset.CONE;
			screen.setObject(object.model);
			break;
		case KeyEvent.VK_5:
			object = Preset.ICOSPHERE;
			screen.setObject(object.model);
			break;
		case KeyEvent.VK_6:
			object = Preset.CYLINDER;
			screen.setObject(object.model);
			break;
		case KeyEvent.VK_7:
			object = Preset.BALL;
			screen.setObject(object.model);
			break;
		case KeyEvent.VK_CONTROL:
			camera.renderingCenter = true;
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
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_CONTROL:
			screen.getRender().getCamera().renderingCenter = false;
			break;

		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
