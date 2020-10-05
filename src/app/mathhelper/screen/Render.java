package app.mathhelper.screen;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import app.mathhelper.shape.Shape;
import app.mathhelper.shape.Vertex;

public class Render extends Canvas {
	private static final long serialVersionUID = 1L;
	
	private int WIDTH,HEIGHT;
	
	public BufferedImage img;
	
	public Render(int w,int h) {
		this.WIDTH = w;
		this.HEIGHT = h;
		this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	}

	public void draw() {
		BufferStrategy bs = null;
		do {
			createBufferStrategy(3);
			bs = this.getBufferStrategy();
		}while(bs==null);
		
		Graphics gDraw = bs.getDrawGraphics();
		
		Graphics g = img.getGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		double zFar = 1000;
		double zNear = 0.1;
		double angle = Math.PI/2;
		double fov = 1/Math.tan(angle/2);
		double a = HEIGHT/(double)WIDTH;
		
		double zRatio = zFar/(zFar-zNear);
		
		Shape s = new Shape();
		Vertex temp[] = new Vertex[4];
		
		for(int i=0;i<4;++i) {
			temp[i] = new Vertex();
		}
		
		for(int i=0;i<4;++i) {
			temp[i].z = (s.v[i].z-zNear+5)/zRatio;
		}
		
		double d = (System.currentTimeMillis()/25.0);
		double sine = Math.sin(d/180.0*Math.PI)*2;
		double cosine = Math.cos(d/180.0*Math.PI)*2;
		for(int i=0;i<4;++i) {
			temp[i].x = a*fov*(s.v[i].x+cosine)/temp[i].z*WIDTH+WIDTH/2;
			temp[i].y = -fov*(s.v[i].y+sine)/temp[i].z*HEIGHT+HEIGHT/2;
		}
		
		g.setColor(Color.BLACK);
		
		for(int i=0;i<4;++i) {
			for(int j=0;j<4;++j) {
				if(s.e[i][j]) {
					g.drawLine((int)temp[i].x, (int)temp[i].y, (int)temp[j].x, (int)temp[j].y);
				}
			}
		}
		
		gDraw.drawImage(img, 0, 0, null);
		bs.show();
		g.dispose();
	}
	
}
