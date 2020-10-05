package app.mathhelper.window;

import java.awt.Dimension;

import javax.swing.JFrame;

import app.mathhelper.screen.Screen;

public class Window extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private int WIDTH,HEIGHT;
	private final int MIN_WIDTH;
	private final int MIN_HEIGHT;
	
	private String TITLE;
	
	private Screen screen;
	
	public Window() {
		this.MIN_WIDTH = 1000;
		this.MIN_HEIGHT = 800;
		
		this.TITLE = "Math helper";
		
		this.setTitle(TITLE);
		this.setMinimumSize(new Dimension(MIN_WIDTH,MIN_HEIGHT));
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		this.WIDTH = this.getWidth();
		this.HEIGHT = this.getHeight();
		
		this.screen = new Screen(WIDTH,HEIGHT);
		
		this.add(screen);
	}
	
	public static void main(String[] args) {
		Window mathHelper = new Window();
		
		Thread t = new Thread(mathHelper);
		t.run();
	}

	@Override
	public void run() {
		while (true) {
			screen.repaint();
		}
	}

}
