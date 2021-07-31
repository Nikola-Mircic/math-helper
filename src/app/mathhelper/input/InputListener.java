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

public class InputListener implements KeyListener{
	private Screen screen;
	
	private Preset object;
	
	private static boolean disabledRotation[] = {false,false,false};
	
	public InputListener(Screen screen) {
		this.screen = screen;
		
		this.object = Preset.CUBE;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		
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
