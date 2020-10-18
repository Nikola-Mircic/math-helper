package app.mathhelper.screen;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import app.mathhelper.shape.Shape;
import app.mathhelper.shape.Vertex;

public class Render extends Canvas{
	private static final long serialVersionUID = 1L;
	
	private int WIDTH,HEIGHT;
	
	private BufferedImage img;
	
	private String clickedVertex;
	
	Shape s;
	
	private double rotationX = 0;
	private double rotationY = 0;
	
	private List<Vertex> onScreenVertex;
	
	public Render(int w,int h) {
		this.WIDTH = w;
		this.HEIGHT = h;
		this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		this.onScreenVertex = new ArrayList<>();
		this.clickedVertex = "";
		
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
		
		double zFar = 100000;
		double zNear = 0.1;
		double angle = Math.PI/2;
		double fov = 1/Math.tan(angle/2);
		double a = HEIGHT/(double)WIDTH;
		
		double zRatio = zFar/(zFar-zNear);
		
		s = new Shape();
		Vertex temp[] = new Vertex[s.v.length];
		
		
		for(int i=0;i<temp.length;++i) {
			temp[i] = new Vertex();
		}
		
		s.rotateHorizontal(this.rotationX/2);
		s.rotateVertical(this.rotationY/2);
		
		for(int i=0;i<temp.length;++i) {
			temp[i].z = (s.v[i].z-zNear)/zRatio;
		}
		
		for(int i=0;i<temp.length;++i) {
			temp[i].x = a*fov*(s.v[i].x)/temp[i].z*WIDTH+WIDTH/2;
			temp[i].y = -fov*(s.v[i].y)/temp[i].z*HEIGHT+HEIGHT/2;
		}
		
		for(int i=0;i<temp.length;++i) {
			for(int j=0;j<temp.length;++j) {
				if(s.e[i][j]) {
					g.setColor(new Color(0));
					g.drawLine((int)temp[i].x, (int)temp[i].y, (int)temp[j].x, (int)temp[j].y);
				}
			}
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Serif", Font.PLAIN, 20));
		for(int i=0;i<temp.length;++i) {
			g.fillOval((int)temp[i].x-3, (int)temp[i].y-3, 6, 6);
			g.drawString((s.v[i].name), (int)temp[i].x-15, (int)temp[i].y-3);
			onScreenVertex.add(new Vertex(s.v[i].name, temp[i].x, temp[i].y, temp[i].z));
		}
		
		if(!clickedVertex.equals("")) {
			g.setFont(new Font("Serif", Font.PLAIN, 35));
			g.drawString("Clicked: "+clickedVertex, 5, 40);
		}
		
		gDraw.drawImage(img, 0, 0, null);
		bs.show();
		g.dispose();
	}
	
	public void findSelectedVertex(int x,int y) {
		//for(int i=0;)
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

	public String getClickedVertex() {
		return clickedVertex;
	}

	public void setClickedVertex(String clickedVertex) {
		this.clickedVertex = clickedVertex;
	}
	
}
