package app.mathhelper.screen.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import app.mathhelper.shape.Edge;
import app.mathhelper.shape.Vertex;
import app.mathhelper.shape.shape2d.Shape2D;
import app.mathhelper.shape.shape2d.Vertex2D;

public class Camera2D extends Camera{
	private Vertex2D position;
	
	private Shape2D shape;
	
	private double scale;
	
	public Camera2D(int width, int height, Shape2D shape) {
		this(width, height, 0, 0, shape);
	}
	
	public Camera2D(int width, int height, int x, int y, Shape2D shape) {
		super(width, height);
		
		this.shape = shape;
		this.position = new Vertex2D("camera"+id, x, y);
		
		findScale();
		
		drawContext();
	}
	
	private void findScale() {
		this.scale = 1;
		double scaleMin = 0, scaleMax = 1, s;
		
		for(Vertex v : shape.getVertices()) {
			Vertex2D temp = (Vertex2D) v.add(position.getOpositeVector());
			if((Math.abs(temp.x)*scale >= this.width/2.0) || (Math.abs(temp.y)*scale >= this.height/2.0)) {
				while(scaleMin <= scaleMax) {
					s = (scaleMax+scaleMin)/2.0;
					if((Math.abs(temp.x)*s >= this.width/2.0) || (Math.abs(temp.y)*s >= this.height/2.0)) {
						scaleMax = s;
					}else {
						this.scale = s;
						scaleMin = s;
					}
				}
			}
			scaleMax = this.scale;
			scaleMin = 0;
		}
	}
	
	@Override
	public void drawContext() {
		System.out.println("Drawing with a scale "+ scale);
		Graphics g = context.getGraphics();
		g.setColor(new Color(70, 70, 70));
		g.fillRect(0, 0, width, height);
		
		for(Vertex v : shape.getVertices()) {
			Vertex2D temp = (Vertex2D) v.add(position.getOpositeVector());
			
			g.setColor(Color.BLACK);
			g.fillOval((int)(temp.x*scale)-3+this.width/2, (int)(temp.y*scale)-3+this.height/2, 6, 6);
		}
		
		for(Edge e : shape.getEdges()) {
			Vertex2D tempA = (Vertex2D) e.a.add(position.getOpositeVector());
			Vertex2D tempB = (Vertex2D) e.b.add(position.getOpositeVector());
			
			g.setColor(Color.BLACK);
			g.drawLine((int)(tempA.x*scale)+this.width/2, (int)(tempA.y*scale)+this.height/2, (int)(tempB.x*scale)+this.width/2, (int)(tempB.y*scale)+this.height/2);
		}
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("mono", Font.PLAIN, 15));
		g.drawString("Camera id: "+id, 10, this.height-50);
	}

	@Override
	public BufferedImage getToDrawContex(int width, int height, int xOffset, int yOffset) {
		BufferedImage temp = context.getSubimage((this.width-width)/2+xOffset, (this.height-height)/2+yOffset, width, height);
		
		return temp;
	}

	@Override
	public void mouseClick(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseScroll(int d) {
		this.scale -= d/20.0;
		if(scale < 0)
			scale = 0;
		drawContext();
	}

	@Override
	public void mouseDragged(int dx, int dy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(int width, int height) {
		this.width = width;
		this.height = height;
		
		findScale();
		this.context = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.drawContext();
	}

	public Shape2D getShape() {
		return shape;
	}

	public void setShape(Shape2D shape) {
		this.shape = shape;
	}

	@Override
	public void moveY(double d) {
		this.position = (Vertex2D)this.position.add(new Vertex2D("", 0, d*100));
		drawContext();
	}

	@Override
	public void moveX(double d) {
		this.position = (Vertex2D)this.position.add(new Vertex2D("", -d*100, 0));
		drawContext();
	}

}
