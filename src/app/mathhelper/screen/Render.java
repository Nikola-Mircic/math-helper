package app.mathhelper.screen;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import app.mathhelper.shape.*;

public class Render extends Canvas{
	private static final long serialVersionUID = 1L;
	
	private int WIDTH,HEIGHT;
	
	private BufferedImage img;
	
	private Vertex clickedVertex;
	
	private double rotationX = 0;
	private double rotationY = 0;
	
	public Map<String,Vertex> onScreenVertex;
	
	public Render(int w,int h) {
		this.WIDTH = w;
		this.HEIGHT = h;
		this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		this.onScreenVertex = new HashMap<String, Vertex>();
		this.clickedVertex = null;
		
		this.rotationX = 0;
		this.rotationY = 0;
	}

	public void draw3DEdges(Object3D object) {
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
		
		Edge temp[] = new Edge[object.getEdges().size()];
		
		for(int i=0;i<temp.length;++i) {
			temp[i] = new Edge();
		}
		
		object.rotateHorizontal(this.rotationX/2);
		object.rotateVertical(this.rotationY/2);
		this.rotationX=0;
		this.rotationY=0;
		
		for(int i=0;i<temp.length;++i) {
			temp[i].a.z = (object.getEdges().get(i).a.z-zNear)/zRatio;
			temp[i].b.z = (object.getEdges().get(i).b.z-zNear)/zRatio;
		}
		
		for(int i=0;i<temp.length;++i) {
			temp[i].a.x = a*fov*(object.getEdges().get(i).a.x)/temp[i].a.z*WIDTH+WIDTH/2;
			temp[i].a.y = -fov*(object.getEdges().get(i).a.y)/temp[i].a.z*HEIGHT+HEIGHT/2;
			
			temp[i].b.x = a*fov*(object.getEdges().get(i).b.x)/temp[i].b.z*WIDTH+WIDTH/2;
			temp[i].b.y = -fov*(object.getEdges().get(i).b.y)/temp[i].b.z*HEIGHT+HEIGHT/2;
		}
		
		g.setColor(Color.BLACK);
		for(int i=0;i<temp.length;++i) {
			g.drawLine((int)temp[i].a.x, (int)temp[i].a.y, (int)temp[i].b.x, (int)temp[i].b.y);
		}
		
		g.setFont(new Font("Serif", Font.PLAIN, 20));
		for(int i=0;i<temp.length;++i) {
			g.fillOval((int)temp[i].a.x-3, (int)temp[i].a.y-3, 6, 6);
			g.drawString((object.getEdges().get(i).a.name), (int)temp[i].a.x-15, (int)temp[i].a.y-3);
			//onScreenVertex.add(new Vertex(object.getEdges().get(i).a.name, temp[i].a.x, temp[i].a.y, temp[i].a.z));
			onScreenVertex.put(object.getEdges().get(i).a.name, new Vertex("", temp[i].a.x, temp[i].a.y, temp[i].a.z));
			
			g.fillOval((int)temp[i].b.x-3, (int)temp[i].b.y-3, 6, 6);
			g.drawString((object.getEdges().get(i).b.name), (int)temp[i].b.x-15, (int)temp[i].b.y-3);
			onScreenVertex.put(object.getEdges().get(i).b.name, new Vertex("", temp[i].b.x, temp[i].b.y, temp[i].b.z));
		}
		
		g.setFont(new Font("Serif", Font.PLAIN, 25));
		g.drawString("Object data: ", 30, 25);
		g.drawString("- Object area: "+Math.round(object.getArea()*100)/100.0, 35, 55);
		g.drawString("- Object scope: "+Math.round(object.getScope()*100)/100.0, 35, 85);
		g.drawString("- Verticies: "+object.getVerticies().size(), 35, 115);
		g.drawString("- Sides: "+object.getSides().size(), 35, 145);
		g.drawString("- Edges: "+object.getEdges().size(), 35, 175);
		
		if(clickedVertex!=null) {
			String data = clickedVertex.name+"( "+Math.round(clickedVertex.x*100)/100.0+", "+Math.round(clickedVertex.y*100)/100.0+")";
			g.drawString("Clicked on: ", 30, 220);
			g.drawString("- "+data, 30, 250);
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

	public Vertex getClickedVertex() {
		return clickedVertex;
	}

	public void setClickedVertex(Vertex clickedVertex) {
		this.clickedVertex = clickedVertex;
	}
	
}
