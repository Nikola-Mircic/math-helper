package app.mathhelper.screen.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.mathhelper.shape.Edge;
import app.mathhelper.shape.GeometryObject;
import app.mathhelper.shape.Object3D;
import app.mathhelper.shape.Shape;
import app.mathhelper.shape.Triangle;
import app.mathhelper.shape.Vertex;

public class Camera3D{
	public static int id = 0;
	public int currentId;
	
	private int width;
	private int height;
	public BufferedImage context;
	private double[][] zBuffer;
	
	public Vertex postition;
	public Vertex light;
	
	private int object;
	private List<Object3D> objectSet;
	private Vertex clickedVertex;
	
	public boolean renderingCenter;
	public int renderMode;
	
	public boolean LIGHT_EFFECT = false;
	private double lightPeriod = 5;
	private double angle = 0;
	
	public Camera3D(int width,int height,Object3D object) {
		this(width, height, 0, 0, 0,object);
	}
	
	public Camera3D(int width, int height, double x, double y, double z, Object3D object) {
		Camera3D.id++;
		this.currentId = id;
		
		this.width = width;
		this.height = height;
		this.context = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.zBuffer = new double[width][height];
		
		this.postition = new Vertex("camera"+id, x, y, z);
		this.light = new Vertex("light"+id,2,1.5,2);
		
		this.objectSet = new ArrayList<>();
		objectSet.add(object);
		this.object = 0;
		
		renderingCenter = false;
		renderMode = 0;
		
		drawContext();
	}
	
	public void drawContext() {
		if(this.objectSet.isEmpty()) {
			return;
		}
		
		List<Edge> filled = new ArrayList<>();
		
		Graphics g = context.getGraphics();
		
		g.setColor(new Color(70, 70, 70));
		g.fillRect(0, 0, width, height);
		for(int i=0;i<width;++i) {
			for(int j=0;j<height;++j) {
				zBuffer[i][j] = 0;
			}
		}
		
		if(LIGHT_EFFECT) {
			angle = (System.currentTimeMillis()%(lightPeriod*1000))/1000.0*(2*Math.PI/lightPeriod);
			this.light = new Vertex("light"+id, 5*Math.cos(angle), this.postition.y, 5+5*Math.sin(angle));
		}
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
		
		if(renderingCenter)
			renderObjectCenter(objectSet.get(object), g);
		
		if(renderMode == 0) {
			for(Object3D object : this.objectSet) {
				for(Shape s : object.getSides()) {
					fill3DShape(s, g);
				}
			}
		}else if(renderMode == 1) {
			for(Object3D object : this.objectSet) {
				for(Shape s : object.getSides()) {
					draw3DEdges(s, g, filled);
				}
			}
		}else {
			for(Object3D object : this.objectSet) {
				for(Shape s : object.getSides()) {
					fill3DShape(s, g);
				}
			}
			for(Object3D object : this.objectSet) {
				for(Shape s : object.getSides()) {
					draw3DEdges(s, g, filled);
				}
			}
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("mono", Font.PLAIN, 15));
		g.drawString("Camera id: "+id, 10, this.height-50);
	}
	
	public BufferedImage getToDrawContex(int width, int height, int xOffset, int yOffset) {
		if(LIGHT_EFFECT)
			this.drawContext();
		context.getGraphics().setColor(Color.BLACK);
		context.getGraphics().drawRect(0, 0, width, height);
		BufferedImage temp = context.getSubimage((this.width-width)/2+xOffset, (this.height-height)/2+yOffset, width, height);
		
		printObjectData(this.objectSet.get(object), temp.getGraphics());
		
		return temp;
	}
	
	public void fill3DShape(Shape s, Graphics g) {
		Edge normal = s.getNormal();
		Vertex v1 = normal.a;
		Vertex v2 = normal.b;
		
		double visible = Vertex.getDotProduct(v1.add(postition), v2);
		
		if(visible < 0) {
			double dotProduct = Vertex.getDotProduct(v1.add(light.getOpositeVector()), v2);
			double cos = dotProduct/(v1.add(light.getOpositeVector()).getLenght()*v2.getLenght());
			
			int c = (int)(130-cos*90);
			c=Math.min(255, c);
			c=Math.max(0, c);
			int color = (new Color(c, c, c)).getRGB();
			for(Triangle t : s.triangles) {
				fillTriangle(t.getVerticies().get(0), t.getVerticies().get(1), t.getVerticies().get(2), color);
			}
		}
	}
	
	public void draw3DEdges(GeometryObject object, Graphics g, List<Edge> filled) {
		Edge normal = ((Shape) object).getNormal();
		Vertex v1 = normal.a;
		Vertex v2 = normal.b;
		
		double dotProduct = Vertex.getDotProduct(v1.add(postition.getOpositeVector()), v2);

		Edge temp[] = new Edge[object.getEdges().size()];
		Edge toConvert;
		for(int i=0;i<temp.length;++i) {
			toConvert = object.getEdges().get(i);
			temp[i] = new Edge(convertTo2D(toConvert.a), convertTo2D(toConvert.b));
		}
		
		g.setColor(Color.BLACK);
		
		if((dotProduct > 0) && renderMode==1) {
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
		} else if(dotProduct < 0){
			g.setColor(Color.BLACK);
			for(int i=0;i<temp.length;++i) {
				drawLine((int)temp[i].a.x, (int)temp[i].a.y, temp[i].a.z, (int)temp[i].b.x, (int)temp[i].b.y, temp[i].b.z, 0);
				filled.add(temp[i]);
			}
		}
		
		g.setFont(new Font("Serif", Font.PLAIN, 20));
		for(int i=0;i<temp.length;++i) {
			if(dotProduct<0 || renderMode == 1) {
				g.fillOval((int)temp[i].a.x-3, (int)temp[i].a.y-3, 6, 6);
				g.drawString((object.getEdges().get(i).a.name), (int)temp[i].a.x-15, (int)temp[i].a.y-3);	
				
				g.fillOval((int)temp[i].b.x-3, (int)temp[i].b.y-3, 6, 6);
				g.drawString((object.getEdges().get(i).b.name), (int)temp[i].b.x-15, (int)temp[i].b.y-3);
			}
		}
		
		if(clickedVertex!=null) {
			String data = clickedVertex.name+"( "+Math.round(clickedVertex.x*100)/100.0+", "+Math.round(clickedVertex.y*100)/100.0+")";
			g.drawString("Clicked on: ", 30, 220);
			g.drawString("- "+data, 30, 250);
		}
	}
	
	private Vertex convertTo2D(Vertex v) {
		Vertex temp = new Vertex(v.name+"_c", 0, 0, 0);
		
		double zFar = 100000;
		double zNear = 0.1;
		double angle = Math.PI/2;
		double fov = 1/Math.tan(angle/2);
		double a = height/(double)width;
		
		double zRatio = zFar/(zFar-zNear);
		
		temp.z = (v.z+this.postition.z-zNear)/zRatio;
	
		temp.x = a*fov*(v.x+this.postition.x)/temp.z*width+width/2;
		temp.y = -fov*(v.y+this.postition.y)/temp.z*height+height/2;
		
		//temp.z = v.z;
		
		return temp;
	}
	
	private void renderObjectCenter(GeometryObject object, Graphics g) {
		Vertex center = new Vertex();
		
		g.setColor(Color.GREEN);
		
		double zFar = 100000;
		double zNear = 0.1;
		double angle = Math.PI/2;
		double fov = 1/Math.tan(angle/2);
		double a = height/(double)width;
		
		double zRatio = zFar/(zFar-zNear);
		
		center.z = (object.getCenter().z+postition.z-zNear)/zRatio;
		center.x = a*fov*(object.getCenter().x+postition.x)/center.z*width+width/2;
		center.y = -fov*(object.getCenter().y+postition.y)/center.z*height+height/2;
		
		g.fillOval((int)center.x, (int)center.y, 5, 5);
	}
	
	private void printObjectData(GeometryObject object, Graphics g) {
		g.setColor(Color.WHITE);
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
	
	private void drawLine(int x1, int y1,double z1, int x2, int y2,double z2, int color) {
		Vertex a = new Vertex("a", (double)x1, (double)y1, z1);
		Vertex b = new Vertex("b", (double)x2, (double)y2, z2);
		double zValue;
		double zStep;
		
		if(x1==x2) {
			a = new Vertex("a", x1, y1, z1);
			b = new Vertex("b", x2, y2, z2);
			if(a.y<b.y) {
				zValue = a.z;
				zStep = (b.z-a.z)/(b.y-a.y);
				for(int y=(int)a.y;y<b.y;++y) {
					if(x1<0 || x1>=width || y<0 || y>=height)
						return;
					if(zBuffer[x1][y]>zValue || zBuffer[x1][y]==0) {
						context.setRGB(x1, y, color);
						zBuffer[x1][y]=zValue;	
					}
					if(zBuffer[x1+1][y]<zValue || zBuffer[x1+1][y]==0) {
						context.setRGB(x1+1, y, color);
						zBuffer[x1+1][y]=zValue;
					}
					zValue+=zStep;
				}
			}else {
				zValue = b.z;
				zStep = (a.z-b.z)/(a.y-b.y);
				for(int y=(int)b.y;y<a.y;++y) {
					if(x1<0 || x1>=width || y<0 || y>=height)
						return;
					if(zBuffer[x1][y]>zValue || zBuffer[x1][y]==0) {
						context.setRGB(x1, y, color);
						zBuffer[x1][y]=zValue;	
					}
					if(zBuffer[x1+1][y]<zValue || zBuffer[x1+1][y]==0) {
						context.setRGB(x1+1, y, color);
						zBuffer[x1+1][y]=zValue;
					}
					zValue+=zStep;
				}
			}
		}else if(x1<x2) {
			double k = (y2-y1)/(double)(x2-x1);
			double n = y1-k*x1;
			zValue = z1;
			zStep = (z2-z1)/(Math.max(x2-x1, Math.abs(y2-y1)));
			for(int x=x1;x<x2;++x) {
				double ys = x*k+n;
				double ye = (x+1)*k+n;
				if(ye<ys) {
					ye = ys+ye;
					ys = ye-ys;
					ye = ye-ys;
				}
				for(double y=ys;y<=ye;y+=1) {
					if(x>0 && x<width-1 && y>0 && y<height-1) {
						if(zBuffer[x][(int)y]>zValue || zBuffer[x][(int)y]==0)
							context.setRGB(x, (int)y, color);
						if(zBuffer[x+1][(int)y]>zValue || zBuffer[x+1][(int)y]==0)
							context.setRGB(x+1, (int)y, color);
						if(zBuffer[x][(int)y+1]>zValue || zBuffer[x][(int)y+1]==0)
							context.setRGB(x, (int)y+1, color);
					
						zValue+=zStep;
					}
				}
			}
		}else {
			double k = (y1-y2)/(double)(x1-x2);
			double n = y2-k*x2;
			zValue = z2;
			zStep = (z1-z2)/(Math.max(x1-x2, Math.abs(y1-y2)));
			for(int x=x2;x<x1;++x) {
				double ys = x*k+n;
				double ye = (x+1)*k+n;
				if(ye<ys) {
					ye = ys+ye;
					ys = ye-ys;
					ye = ye-ys;
				}
				for(double y=ys;y<=ye;y+=1) {
					if(x>0 && x<width-1 && y>0 && y<height-1) 
						if(zBuffer[x][(int)y]>zValue || zBuffer[x][(int)y]==0) {
							context.setRGB(x, (int)y, color);
						if(zBuffer[x+1][(int)y]>zValue || zBuffer[x+1][(int)y]==0)
							context.setRGB(x+1, (int)y, color);
						if(zBuffer[x][(int)y+1]>zValue || zBuffer[x][(int)y+1]==0)
							context.setRGB(x, (int)y+1, color);
						
						zValue+=zStep;
					}
				}
			}
		}
	}
	
	private void fillTriangle(Vertex a, Vertex b, Vertex c, int color) {
		List<Vertex> temp = new ArrayList<>();
		temp.add(convertTo2D(a));
		temp.add(convertTo2D(b));
		temp.add(convertTo2D(c));
		temp.sort(new Comparator<Vertex>() {
			@Override
			public int compare(Vertex o1, Vertex o2) {
				return (int)(o1.y-o2.y);
			}
		});
		
		double dy20 = (temp.get(2).y-temp.get(0).y);
		double dy10 = (temp.get(1).y-temp.get(0).y);
		
		if(Math.abs(dy20)<=1) {
			drawLine((int)temp.get(0).x, (int)temp.get(0).y, temp.get(0).z, (int)temp.get(2).x, (int)temp.get(2).y, temp.get(2).z, color);
			return;
		}
		double scale = dy10/dy20;
		double middleX = temp.get(0).x+(temp.get(2).x-temp.get(0).x)*scale;
		double middleZ = temp.get(0).z+(temp.get(2).z-temp.get(0).z)*scale;
		Vertex middle = new Vertex("middle", middleX, temp.get(1).y, middleZ);
		
		fillBottomFlatTriangle(temp.get(0), temp.get(1), middle, color);
		fillTopFlatTriangle(temp.get(1), middle, temp.get(2), color);
	}
	
	//a - vertex on the top of the triangle
	//b,c - vertices on the bottom edge of the triangle
	private void fillBottomFlatTriangle(Vertex a, Vertex b,Vertex c, int color) {
		Vertex ab = new Vertex("ab", a.x, a.y, a.z);
		Vertex ac = new Vertex("ac", a.x, a.y, a.z);
		double dy = b.y-a.y;
		if(dy<=1) {
			return;
		}
		double dxAB = (b.x-a.x)/dy;
		double dxAC = (c.x-a.x)/dy;
		
		double dzAB = (b.z-a.z)/dy;
		double dzAC = (c.z-a.z)/dy;
		
		Vertex stepAC = new Vertex("step", dxAC, 0, dzAC);
		Vertex stepAB = new Vertex("step", dxAB, 0, dzAB);
		
		double zValue;
		for(int y=(int)a.y; y<(int)(a.y+dy); ++y) {
			for(int x=(int)Math.min(ab.x, ac.x);x<Math.max(ab.x, ac.x);++x) {
				if(y>0 && y<this.height-1 && x>0 && x<this.width-1) {
					zValue = calculateZ(ac, ab, x);
					if(zBuffer[x][y]>zValue || zBuffer[x][y]==0) {
						context.setRGB(x, y, color);
						zBuffer[x][y] = zValue;
					}
					/*zValue = calculateZ(ac, ab, x+1);
					if(zBuffer[x+1][y]>zValue || zBuffer[x+1][y]==0) {
						context.setRGB(x+1, y, color);
						zBuffer[x+1][y] = zValue;
					}*/
					zValue = calculateZ(ac.add(stepAC), ab.add(stepAB), x);
					if(zBuffer[x][y+1]>zValue || zBuffer[x][y+1]==0) {
						context.setRGB(x, y+1, color);
						zBuffer[x][y+1] = zValue;
					}
				}
			}
			ab = ab.add(stepAB);
			ac = ac.add(stepAC);
		}
	}
	
	//a,b - vertices on the top edge of the triangle
	//c - vertex on the bottom  of the triangle
	private void fillTopFlatTriangle(Vertex a, Vertex b,Vertex c, int color) {
		Vertex ac = new Vertex("ac", a.x, a.y, a.z);
		Vertex bc = new Vertex("bc", b.x, b.y, b.z);
		double dy = c.y-a.y;
		if(dy<=1) {
			return;
		}
		double dxAC = (c.x-a.x)/dy;
		double dxBC = (c.x-b.x)/dy;
		
		double dzBC = (c.z-b.z)/dy;
		double dzAC = (c.z-a.z)/dy;
		
		Vertex stepAC = new Vertex("step", dxAC, 0, dzAC);
		Vertex stepBC = new Vertex("step", dxBC, 0, dzBC);
		
		double zValue;
		for(int y=(int)a.y; y<(int)(a.y+dy);++y) {
			for(int x=(int)Math.min(ac.x, bc.x);x<Math.max(ac.x, bc.x);++x) {
				if(y>0 && y<this.height-1 && x>0 && x<this.width-1) {
					zValue = calculateZ(ac, bc, x);
					if(zBuffer[x][y]>zValue || zBuffer[x][y]==0) {
						context.setRGB(x, y, color);
						zBuffer[x][y] = zValue;
					}
					/*zValue = calculateZ(ac, bc, x+1);
					if(zBuffer[x+1][y]>zValue || zBuffer[x+1][y]==0) {
						context.setRGB(x+1, y, color);
						zBuffer[x+1][y] = zValue;
					}*/
					zValue = calculateZ(ac.add(stepAC), bc.add(stepBC), x);
					if(zBuffer[x][y+1]>zValue || zBuffer[x][y+1]==0) {
						context.setRGB(x, y+1, color);
						zBuffer[x][y+1] = zValue;
					}
				}
			}
			ac = ac.add(stepAC);
			bc = bc.add(stepBC);
		}
	}
	
	private double calculateZ(Vertex a,Vertex b,int x) {
		Vertex temp1 = a;
		Vertex temp2 = b;
		if(a.x>b.x) {
			temp1 = b;
			temp2 = a;
		}
		double xDif = temp2.x-temp1.x;
		x-=(int)temp1.x;
		double scale = x/xDif;
		return temp1.z+(temp2.z-temp1.z)*scale;
	}
	
	public void checkOnClick(int x, int y) {
		for(Vertex vertex : objectSet.get(object).getVerticies()) {
			Vertex temp = convertTo2D(vertex);
			if(Math.abs(x-temp.x)<=3 && Math.abs(y-temp.y)<=3) {
				System.out.println(vertex.name +" "+ vertex.x + " "+vertex.y+" "+vertex.z);
				return;
			}
		}
		
		for(Edge e : objectSet.get(object).getEdges()) {
			Vertex p1 = convertTo2D(e.a);
			Vertex p2 = convertTo2D(e.b);
			double a,b,c;
			
			if(p1.x == p2.x) {
				a = 1;
				b = 0;
				c = -p1.x;
				if(Math.min(p1.y, p2.y) < y && y < Math.max(p1.y, p2.y)) {
					if(a*x+b*y+c<3) {
						System.out.println(e.a.name + " - " + e.b.name);
						return;
					}
				}
			}else {
				if(p2.x < p1.x) {
					Vertex temp = p1;
					p1 = p2;
					p2 = temp;
				}
				double k = (p2.y - p1.y)/(p2.x - p1.x);
				double n = p1.y-k*p1.x;
				a = -k;
				b = 1;
				c = -n;
				if(Math.min(p1.y, p2.y) < y && y < Math.max(p1.y, p2.y) && Math.min(p1.x, p2.x) < x && x < Math.max(p1.x, p2.x)) {
					if(a*x+b*y+c<3) {
						System.out.println(e.a.name + " - " + e.b.name);
						return;
					}
				}
			}
		}
		
		for(Shape s: objectSet.get(object).s) {
			Edge normal = s.getNormal();
			Vertex v1 = normal.a;
			Vertex v2 = normal.b;
			
			double visible = Vertex.getDotProduct(v1.add(postition), v2);
			
			if(visible < 0) {
				for(Triangle t : s.triangles) {
					if(isInTriangle(x, y, t)) {
						System.out.println(s);
					}
				}
			}
		}
	}
	
	private boolean isInTriangle(int x, int y, Triangle t) {
		Vertex v = new Vertex("click", x, y, 0);
		double d1, d2, d3;
	    boolean has_neg, has_pos;
	    
	    Vertex v1 = convertTo2D(t.getVerticies().get(0));
	    Vertex v2 = convertTo2D(t.getVerticies().get(1));
	    Vertex v3 = convertTo2D(t.getVerticies().get(2));

	    d1 = test(v, v1, v2);
	    d2 = test(v, v2, v3);
	    d3 = test(v, v3, v1);

	    has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
	    has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

	    return !(has_neg && has_pos);
	}
	
	private double test(Vertex p1, Vertex p2, Vertex p3)
	{
	    return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}
	
	public Object3D getObject() {
		return objectSet.get(object);
	}

	public void setObject(Object3D object) {
		int idx = objectSet.indexOf(object);
		if(idx!=-1)
			this.object = idx;
		drawContext();
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void moveX(double d) {
		this.postition.x+=d;
		drawContext();
	}
	
	public void moveY(double d) {
		this.postition.y+=d;
		drawContext();
	}
	
	public void moveZ(double d) {
		this.postition.z+=d;
		drawContext();
	}

	public void switchLightEffect() {
		this.LIGHT_EFFECT = !this.LIGHT_EFFECT;	
	}

	public void update(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
		
		this.context = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.zBuffer = new double[width][height];
		this.drawContext();
	}
}
