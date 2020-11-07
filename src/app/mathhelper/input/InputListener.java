package app.mathhelper.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Map.Entry;

import app.mathhelper.screen.Render;
import app.mathhelper.screen.Screen;
import app.mathhelper.shape.Vertex;

public class InputListener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	private Screen screen;
	private Render render;
	
	private int lastX,lastY;
	
	public InputListener(Screen screen) {
		this.screen = screen;
		this.render = screen.getRender();
		
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
			render.setRotationX(render.getRotationX() + (x/180.0*Math.PI));
			render.setRotationY(render.getRotationY() + (y/180.0*Math.PI));
			
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
		
		for(Entry<String, Vertex> entry : render.onScreenVertex.entrySet()) {
			if(entry.getValue().x-3<=x && entry.getValue().x+3>=x &&
				entry.getValue().y-3<=y && entry.getValue().y+3>=y) {
				render.setClickedVertex(new Vertex(entry.getKey(), entry.getValue().x, entry.getValue().y, entry.getValue().z));
				return;
			}
		}
		
		render.setClickedVertex(null);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.lastX = -1;
		this.lastY = -1;
	}
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_R:
			screen.resetShape();
			break;
		default:
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
