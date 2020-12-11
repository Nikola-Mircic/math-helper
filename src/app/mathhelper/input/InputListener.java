package app.mathhelper.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import app.mathhelper.screen.Screen;
import app.mathhelper.screen.render.Camera3D;
import app.mathhelper.shape.Object3D;
import app.mathhelper.shape.preset.Cube;
import app.mathhelper.shape.preset.Tetrahedron;

public class InputListener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	private Screen screen;
	
	private int lastX,lastY;
	
	private int objectIdx = 1;
	
	private List<Object3D> objects;
	
	private static boolean disabledRotation[] = {false,false,false};
	
	public InputListener(Screen screen) {
		this.screen = screen;
		
		this.lastX = -1;
		this.lastY = -1;
		
		makeNewObjects();
	}
	
	private void makeNewObjects() {
		this.objects = new ArrayList<>();
		objects.add(new Cube(0, 0, 0));
		objects.add(new Tetrahedron(0, 0, 0));
		objects.add(Object3D.loadFromFile("utah.obj"));
		objects.add(Object3D.loadFromFile("cone.obj"));
		objects.add(Object3D.loadFromFile("icosphere.obj"));
		objects.add(Object3D.loadFromFile("cylinder.obj"));
		objects.add(Object3D.loadFromFile("ball.obj"));
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
				makeNewObjects();
				screen.getRender().setCamera(new Camera3D(screen.getRender().WIDTH, screen.getRender().HEIGHT, objects.get(objectIdx-1)));
				screen.setObject(objects.get(objectIdx-1));
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
				objectIdx = 1;
				screen.setObject(objects.get(objectIdx-1));
			}
			break;
		case KeyEvent.VK_2:
			if(e.isShiftDown()) {
				disabledRotation[2] = !disabledRotation[2];
				disabledRotation[1] = false;
			}else {
				objectIdx = 2;
				screen.setObject(objects.get(objectIdx-1));
			}
			break;
		case KeyEvent.VK_3:
			objectIdx = 3;
			screen.setObject(objects.get(objectIdx-1));
			break;
		case KeyEvent.VK_4:
			objectIdx = 4;
			screen.setObject(objects.get(objectIdx-1));
			break;
		case KeyEvent.VK_5:
			objectIdx = 5;
			screen.setObject(objects.get(objectIdx-1));
			break;
		case KeyEvent.VK_6:
			objectIdx = 6;
			screen.setObject(objects.get(objectIdx-1));
			break;
		case KeyEvent.VK_7:
			objectIdx = 7;
			screen.setObject(objects.get(objectIdx-1));
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
