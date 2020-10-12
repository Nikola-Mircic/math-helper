package app.mathhelper.screen;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import app.mathhelper.shape.Shape;
import app.mathhelper.shape.Vertex;

public class Render extends Canvas{
	private static final long serialVersionUID = 1L;
	
	private int WIDTH,HEIGHT;
	
	private BufferedImage img;
	
	private double rotationX;
	private double rotationY;
	
	
	public Render(int w,int h) {
		this.WIDTH = w;
		this.HEIGHT = h;
		this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		this.rotationX = 0;
		this.rotationY = 0;
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
		
		s.rotateVertical(rotationY/2);
		s.rotateHorizontal(rotationX/2);
		
		for(int i=0;i<4;++i) {
			temp[i].z = (s.v[i].z-zNear)/zRatio;
		}
		
		for(int i=0;i<4;++i) {
			temp[i].x = a*fov*(s.v[i].x)/temp[i].z*WIDTH+WIDTH/2;
			temp[i].y = -fov*(s.v[i].y)/temp[i].z*HEIGHT+HEIGHT/2;
			
		}
		
		for(int i=0;i<4;++i) {
			for(int j=0;j<4;++j) {
				if(s.e[i][j]) {
					g.setColor(new Color(15<<(i*10)));
					g.drawLine((int)temp[i].x, (int)temp[i].y, (int)temp[j].x, (int)temp[j].y);
				}
			}
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Serig", Font.PLAIN, 20));
		for(int i=0;i<4;++i) {
			g.fillOval((int)temp[i].x-3, (int)temp[i].y-3, 6, 6);
			g.drawString((""+(i+1)), (int)temp[i].x-15, (int)temp[i].y-3);
		}
		
		gDraw.drawImage(img, 0, 0, null);
		bs.show();
		g.dispose();
	}
	
	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public double getRotationX() {
		return rotationX;
	}

	public void setRotationX(double rotationX) {
		this.rotationX = rotationX;
	}

	public double getRotationY() {
		return rotationY;
	}

	public void setRotationY(double rotationY) {
		this.rotationY = rotationY;
	}
	
}
