package app.mathhelper.screen;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

import app.mathhelper.shape.*;
import app.mathhelper.shape.Shape;

public class Render extends Canvas{
	private static final long serialVersionUID = 1L;
	
	private int WIDTH,HEIGHT;
	
	private BufferedImage img;
	
	private Vertex clickedVertex;
	
	private double rotationX = 0;
	private double rotationY = 0;
	
	public boolean transparent;
	
	public Map<String,Vertex> onScreenVertex;
	
	public Render(int w,int h) {
		this.WIDTH = w;
		this.HEIGHT = h;
		this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		this.onScreenVertex = new HashMap<String, Vertex>();
		this.clickedVertex = null;
		
		this.transparent = true;
	}
	
	public void renderObject(Object3D object) {
		BufferStrategy bs = null;
		do {
			createBufferStrategy(3);
			bs = this.getBufferStrategy();
		}while(bs==null);
		
		List<Edge> filled = new ArrayList<>();
		
		Graphics gDraw = bs.getDrawGraphics();
		Graphics g = img.getGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for(Shape s : object.getSides()) {
			draw3DEdges(s, bs, g, filled);
		}
		
		printObjectData(object, g);
		
		gDraw.drawImage(img, 0, 0, null);
		
		bs.show();
		g.dispose();
	}

	public void draw3DEdges(GeometryObject object, BufferStrategy bs, Graphics g, List<Edge> filled) {
		Edge normal = ((Shape) object).getNormal();
		Vertex v1 = normal.a;
		Vertex v2 = normal.b;
		
		if((v1.x*v2.x + v1.y*v2.y + v1.z*v2.z > 0) || transparent) {
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
			
			for(int i=0;i<temp.length;++i) {
				temp[i].a.z = (object.getEdges().get(i).a.z-zNear)/zRatio;
				temp[i].b.z = (object.getEdges().get(i).b.z-zNear)/zRatio;
			}
			
			for(int i=0;i<temp.length;++i) {
				temp[i].a.x = a*fov*(object.getEdges().get(i).a.x)/temp[i].a.z*WIDTH+WIDTH/2;
				temp[i].a.y = -fov*(object.getEdges().get(i).a.y)/temp[i].a.z*HEIGHT+HEIGHT/2;
				onScreenVertex.put(object.getEdges().get(i).a.name, new Vertex("", temp[i].a.x, temp[i].a.y, temp[i].a.z));
				
				temp[i].b.x = a*fov*(object.getEdges().get(i).b.x)/temp[i].b.z*WIDTH+WIDTH/2;
				temp[i].b.y = -fov*(object.getEdges().get(i).b.y)/temp[i].b.z*HEIGHT+HEIGHT/2;
				onScreenVertex.put(object.getEdges().get(i).b.name, new Vertex("", temp[i].b.x, temp[i].b.y, temp[i].b.z));
			}
			
			g.setColor(Color.BLACK);
			
			if((v1.x*v2.x + v1.y*v2.y + v1.z*v2.z < 0)) {
				Graphics2D g2d = (Graphics2D) g.create();
				Stroke s = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10,20}, 0);
				g2d.setStroke(s);
				
				A:for(int i=0;i<temp.length;++i) {
					for(Edge e : filled) {
						if(e.equals(temp[i])) {
							continue A;
						}
					}					
					g2d.drawLine((int)temp[i].a.x, (int)temp[i].a.y, (int)temp[i].b.x, (int)temp[i].b.y);
				}
			} else {
				g.setColor(Color.BLACK);
				for(int i=0;i<temp.length;++i) {
					g.drawLine((int)temp[i].a.x, (int)temp[i].a.y, (int)temp[i].b.x, (int)temp[i].b.y);
					filled.add(temp[i]);
				}
			}
			
			g.setFont(new Font("Serif", Font.PLAIN, 20));
			for(int i=0;i<temp.length;++i) {
				g.fillOval((int)temp[i].a.x-3, (int)temp[i].a.y-3, 6, 6);
				g.drawString((object.getEdges().get(i).a.name), (int)temp[i].a.x-15, (int)temp[i].a.y-3);	
				
				g.fillOval((int)temp[i].b.x-3, (int)temp[i].b.y-3, 6, 6);
				g.drawString((object.getEdges().get(i).b.name), (int)temp[i].b.x-15, (int)temp[i].b.y-3);
			}
			
			if(clickedVertex!=null) {
				String data = clickedVertex.name+"( "+Math.round(clickedVertex.x*100)/100.0+", "+Math.round(clickedVertex.y*100)/100.0+")";
				g.drawString("Clicked on: ", 30, 220);
				g.drawString("- "+data, 30, 250);
			}
		}
	}
	
	private void printObjectData(GeometryObject object, Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Serif", Font.PLAIN, 25));
		g.drawString("Object data: ", 30, 25);
		g.drawString("- Object area: "+Math.round(object.getArea()*100)/100.0, 35, 55);
		g.drawString("- Object scope: "+Math.round(object.getScope()*100)/100.0, 35, 85);
		g.drawString("- Verticies: "+object.getVerticies().size(), 35, 115);
		g.drawString("- Edges: "+object.getEdges().size(), 35, 145);
		if(object instanceof Object3D) {
			g.drawString("- Sides: "+((Object3D) object).getSides().size(), 35, 175);
		}
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

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}
	
}
