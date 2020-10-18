package app.mathhelper.window;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

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
		this.MIN_WIDTH = 600;
		this.MIN_HEIGHT = 400;
		
		this.TITLE = "Math helper";
		
		this.setTitle(TITLE);
		this.setMinimumSize(new Dimension(MIN_WIDTH,MIN_HEIGHT));
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		this.WIDTH = this.getWidth();
		this.HEIGHT = this.getHeight();
		
		this.screen = new Screen(WIDTH,HEIGHT);
		
		this.getContentPane().addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				WIDTH = getWidth();
				HEIGHT = getHeight();
				
				screen.update(WIDTH, HEIGHT);
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		this.add(screen);
		
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		Window mathHelper = new Window();
		
		Thread t = new Thread(mathHelper);
		t.start();
	}

	@Override
	public void run() {
		while (true) {
			screen.repaint();
		}
	}

}
