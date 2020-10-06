package app.mathhelper.screen;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import app.mathhelper.shape.Shape;
import app.mathhelper.shape.Vertex;

public class Render extends Canvas {
	private static final long serialVersionUID = 1L;
	
	private int WIDTH,HEIGHT;
	
	public BufferedImage img;
	
	public double rotation = 0;
	
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
		
		double d = (System.currentTimeMillis()/25.0);
		double sine = Math.sin(d/180.0*Math.PI)*1.2;
		double cosine = Math.cos(d/180.0*Math.PI)*1.2;
		rotation = d/180.0*Math.PI;
		
		Vertex center = s.getCenterCords();
		
		double[] angleHoriz = getHorizontalAngle(s);
		double[] angleVert = getVerticalAngle(s);
		
		for(int i=0;i<4;++i) {
			temp[i].z = center.z + Math.sin(angleHoriz[i]+rotation)*getDistHorizontal(center, s.v[i]);
			//temp[i].z = center.z + Math.sin(angleVert[i]+rotation)*getDistHorizontal(center, s.v[i]);
			temp[i].z = (temp[i].z-zNear+cosine)/zRatio;
		}
		
		for(int i=0;i<4;++i) {
			temp[i].x = center.x + Math.cos(angleHoriz[i]+rotation)*getDistHorizontal(center, s.v[i]);
			temp[i].y = center.y - Math.sin(angleVert[i]+rotation)*getDistVertical(center, s.v[i]);
			temp[i].x = a*fov*(temp[i].x+cosine)/temp[i].z*WIDTH+WIDTH/2;
			temp[i].y = -fov*(s.v[i].y+sine)/temp[i].z*HEIGHT+HEIGHT/2;
			
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
	
	private double[] getHorizontalAngle(Shape s) {
		double[] temp = new double[4];
		Vertex center = s.getCenterCords();
		
		for(int i=0;i<4;++i) {
			double dist = getDistHorizontal(center, s.v[i]);
			double sin = (s.v[i].z-center.z)/dist;
			double cos = (s.v[i].x-center.x)/dist;
			double asin = Math.asin(sin);
			
			if(cos>=0) {
				temp[i] = asin;
			}else {
				temp[i] = Math.PI-asin;
			}
		}
		
		return temp;
	}
	
	private double[] getVerticalAngle(Shape s) {
		double[] temp = new double[4];
		Vertex center = s.getCenterCords();
		
		for(int i=0;i<4;++i) {
			double dist = getDistHorizontal(center, s.v[i]);
			double cos = (s.v[i].z-center.z)/dist;
			double sin = (s.v[i].y-center.y)/dist;
			double asin = Math.asin(sin);
			
			if(cos>=0) {
				temp[i] = asin;
			}else {
				temp[i] = Math.PI-asin;
			}
		}
		
		return temp;
	}
	
	private double getDistHorizontal(Vertex a,Vertex b) {
		double dx = a.x-b.x;
		double dz = a.z-b.z;
		return Math.sqrt(dx*dx+dz*dz);
	}
	
	private double getDistVertical(Vertex a,Vertex b) {
		double dy = a.y-b.y;
		double dz = a.z-b.z;
		return Math.sqrt(dy*dy+dz*dz);
	}
	
}
