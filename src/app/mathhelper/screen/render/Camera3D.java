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

import app.mathhelper.shape.*;
import app.mathhelper.shape.preset.Cube;
import app.mathhelper.shape.preset.Preset;
import app.mathhelper.shape.shape3d.*;

public class Camera3D extends Camera{
	private double[][] zBuffer;
	
	public Vertex3D position;
	public Vertex3D light;
	
	private GeometryObject selected = null;
	private int object;
	private List<Object3D> objectSet;
	private Vertex3D clickedVertex;
	
	public boolean renderingCenter;
	public int renderMode;
	
	public boolean LIGHT_EFFECT = false;
	private double lightPeriod = 5;
	private double angle = 0;
	
	public Camera3D(int width,int height,Object3D object) {
		this(width, height, 0, 0, -7,object);
	}
	
	public Camera3D(int width, int height, double x, double y, double z, Object3D object) {
		super(width, height);
		
		this.zBuffer = new double[width][height];
		
		this.position = new Vertex3D("camera"+id, x, y, z);
		this.light = new Vertex3D("light"+id,2,1.5,-5);
		
		this.objectSet = new ArrayList<>();
		objectSet.add(object);
		this.object = 0;
		
		renderingCenter = false;
		renderMode = 0;
		
		drawContext();
	}
	
	@Override
	public void drawContext() {
		if(this.objectSet.isEmpty()) {
			return;
		}
		
		List<Edge3D> filled = new ArrayList<>();
		
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
			this.light = new Vertex3D("light"+id, 5*Math.cos(angle), this.position.y, 5*Math.sin(angle));
		}
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
				
		if(renderingCenter)
			for(Object3D object3d : objectSet)
				renderObjectCenter(object3d, g);
		
		if(renderMode == 0) {
			for(Object3D object : this.objectSet) {
				for(Shape3D s : object.getSides()) {
					if(object.equals(selected))
						fill3DShape(s, g, true, true);
					else
						fill3DShape(s, g, true, false);
				}
			}
		}else if(renderMode == 1) {
			for(Object3D object : this.objectSet) {
				for(Shape3D s : object.getSides()) {
					draw3DEdges(s, g, filled);
				}
			}
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("mono", Font.PLAIN, 15));
		g.drawString("Camera id: "+id, 10, this.height-50);
	}
	
	@Override
	public BufferedImage getToDrawContex(int width, int height, int xOffset, int yOffset) {
		if(LIGHT_EFFECT)
			this.drawContext();
		BufferedImage temp = context.getSubimage((this.width-width)/2+xOffset, (this.height-height)/2+yOffset, width, height);
		
		return temp;
	}
	
	public void fill3DShape(Shape3D s, Graphics g, boolean doZbuffer, boolean isSelected) {
		Edge3D normal = s.getNormal();
		Vertex3D v1 = (Vertex3D) normal.a;
		Vertex3D v2 = (Vertex3D) normal.b;
		
		double dotProduct = v2.getDotProduct((Vertex3D) v1.add(light));
		double cos = dotProduct/(v1.add(light).getLenght()*v2.getLenght());
			
		int colorComp = (int)(150-cos*95);
		colorComp=Math.min(255, colorComp);
		colorComp=Math.max(0, colorComp);
		int color = (new Color(colorComp, colorComp, colorComp)).getRGB();
		
		if(s.equals(selected) || isSelected) {
			color = (new Color(colorComp/255.0f, colorComp/255.0f*0.6f, colorComp/255.0f*0.6f)).getRGB();
		}
			
		for(Triangle3D t : s.getTriangles()) {
			fillTriangle((Vertex3D) t.getVertices().get(0), (Vertex3D) t.getVertices().get(1),(Vertex3D)  t.getVertices().get(2), color, doZbuffer);
		}
	}
	
	public void draw3DEdges(Shape3D shape, Graphics g, List<Edge3D> filled) {
		Edge3D normal = shape.getNormal();
		Vertex3D v1 = (Vertex3D) normal.a;
		Vertex3D v2 = (Vertex3D) normal.b;
		
		double dotProduct = v2.getDotProduct((Vertex3D) v1.add(position));

		Edge3D temp[] = new Edge3D[shape.getEdges().size()];
		Edge3D toConvert;
		for(int i=0;i<temp.length;++i) {
			toConvert = (Edge3D) shape.getEdges().get(i);
			temp[i] = new Edge3D(convertTo2D((Vertex3D) toConvert.a), convertTo2D((Vertex3D) toConvert.b));
		}
		
		g.setColor(Color.BLACK);
		
		if((dotProduct > 0) && renderMode==1) {
			Graphics2D g2d = (Graphics2D) g.create();
			Stroke s = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10,20}, 0);
			g2d.setStroke(s);
			
			A:for(int i=0;i<temp.length;++i) {
				if(filled.contains((Edge3D) temp[i])) {
					continue A;
				}
				g2d.drawLine((int)((Vertex3D) temp[i].a).x, (int)((Vertex3D) temp[i].a).y, (int)((Vertex3D) temp[i].b).x, (int)((Vertex3D) temp[i].b).y);
			}
		}else{
			for(int i=0;i<temp.length;++i) {
				drawLine((int)((Vertex3D) temp[i].a).x, (int)((Vertex3D) temp[i].a).y, ((Vertex3D) temp[i].a).z, (int)((Vertex3D) temp[i].b).x, (int)((Vertex3D) temp[i].b).y, ((Vertex3D) temp[i].b).z, 0);
				filled.add(temp[i]);
			}
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Serif", Font.PLAIN, 20));
		for(int i=0;i<temp.length;++i) {
			if(dotProduct<0 || renderMode == 1) {
				g.fillOval((int)((Vertex3D) temp[i].a).x-3, (int)((Vertex3D) temp[i].a).y-3, 6, 6);
				g.drawString((shape.getEdges().get(i).a.name), (int)((Vertex3D) temp[i].a).x-15, (int)((Vertex3D) temp[i].a).y-3);	
				
				g.fillOval((int)((Vertex3D) temp[i].b).x-3, (int)((Vertex3D) temp[i].b).y-3, 6, 6);
				g.drawString((shape.getEdges().get(i).b.name), (int)((Vertex3D) temp[i].b).x-15, (int)((Vertex3D) temp[i].b).y-3);
			}
		}
		
		if(clickedVertex!=null) {
			String data = clickedVertex.name+"( "+Math.round(clickedVertex.x*100)/100.0+", "+Math.round(clickedVertex.y*100)/100.0+")";
			g.drawString("Clicked on: ", 30, 220);
			g.drawString("- "+data, 30, 250);
		}
	}
	
	private Vertex3D convertTo2D(Vertex3D v) {
		Vertex3D temp = new Vertex3D(v.name+"_c", 0, 0, 0);
		
		double zFar = 10000;
		double zNear = 0.1;
		double angle = Math.PI/2;
		double fov = 1/Math.tan(angle/2);
		double a = height/(double)width;
		
		double zRatio = zFar/(zFar-zNear);
		
		temp.z = (v.z+this.position.z-zNear)/zRatio;
	
		temp.x = -a*fov*(v.x+this.position.x)/temp.z*width+width/2;
		temp.y = fov*(v.y+this.position.y)/temp.z*height+height/2;
		
		return temp;
	}
	
	private void renderObjectCenter(Object3D object, Graphics g) {
		Vertex3D center = new Vertex3D();
		
		g.setColor(Color.GREEN);
		
		double zFar = 10000;
		double zNear = 0.1;
		double angle = Math.PI/2;
		double fov = 1/Math.tan(angle/2);
		double a = height/(double)width;
		
		double zRatio = zFar/(zFar-zNear);
		
		center.z = (object.getCenter().z+position.z-zNear)/zRatio;
		center.x = -a*fov*(object.getCenter().x+position.x)/center.z*width+width/2;
		center.y = fov*(object.getCenter().y+position.y)/center.z*height+height/2;
		
		g.fillOval((int)center.x, (int)center.y, 5, 5);
	}
	
	private void drawLine(int x1, int y1,double z1, int x2, int y2,double z2, int color) {
		Vertex3D a = new Vertex3D("a", (double)x1, (double)y1, z1);
		Vertex3D b = new Vertex3D("b", (double)x2, (double)y2, z2);
		double zValue;
		double zStep;
		
		if(x1==x2) {
			a = new Vertex3D("a", x1, y1, z1);
			b = new Vertex3D("b", x2, y2, z2);
			
			if(b.y<a.y) {
				a = a.add(b);
				b = a.add(b.getOpositeVector());
				a = a.add(b.getOpositeVector());
			}
			
			zValue = a.z;
			zStep = (b.z-a.z)/(b.y-a.y);
			for(int y=(int)a.y;y<b.y;++y) {
				if(x1<0 || x1+1>=width)
					return;
				
				if(y<0 || y+1>=height)
					continue;
				
				if(zBuffer[x1][y]<zValue || zBuffer[x1][y]==0) {
					context.setRGB(x1, y, color);
					zBuffer[x1][y]=zValue;	
				}
				
				if(zBuffer[x1+1][y]<zValue || zBuffer[x1+1][y]==0) {
					context.setRGB(x1+1, y, color);
					zBuffer[x1+1][y]=zValue;
				}
				zValue+=zStep;
			}
		}else if(y1 == y2) {
			a = new Vertex3D("a", x1, y1, z1);
			b = new Vertex3D("b", x2, y2, z2);

			if(b.x<a.x) {
				a = a.add(b);
				b = a.add(b.getOpositeVector());
				a = a.add(b.getOpositeVector());
			}
			
			zValue = a.z;
			zStep = (b.z-a.z)/(b.x-a.x);
			for(int x=(int)a.x;x<b.x;++x) {
				if(x<0 || x>=width)
					continue;
				
				if(y1<0 || y1>=height-1)
					return;
				
				if(zBuffer[x][y1]<zValue || zBuffer[x][y1]==0) {
					context.setRGB(x, y1, color);
					zBuffer[x][y1]=zValue;	
				}
				if(zBuffer[x][y1+1]<zValue || zBuffer[x][y1+1]==0) {
					context.setRGB(x, y1+1, color);
					zBuffer[x][y1+1]=zValue;
				}
				zValue+=zStep;
			}
		} else {
			if(x1>x2) {
				x1 = x1+x2;
				x2 = x1-x2;
				x1 = x1-x2;
				
				y1 = y1+y2;
				y2 = y1-y2;
				y1 = y1-y2;
				
				z1 = z1+z2;
				z2 = z1-z2;
				z1 = z1-z2;
			}
			
			double k = (y2-y1)/(double)(x2-x1);
			double n = y1*1.0-k*x1;
			zValue = z1;
			//zStep = (z2-z1)/(Math.max(x2-x1, Math.abs(y2-y1)));
			zStep = (z2-z1)/(x2-x1);
			for(int x=x1;x<x2;++x) {
				double ys = x*k+n;
				double ye = (x+1)*k+n;
				if(ye<ys) {
					ye = ys+ye;
					ys = ye-ys;
					ye = ye-ys;
				}
				for(double y=ys;y<ye;y+=1) {
					if(x>0 && x<width-1 && y>0 && y<height-1) {
						if(zBuffer[x][(int)y]<zValue || zBuffer[x][(int)y]==0)
							context.setRGB(x, (int)y, color);
						if(zBuffer[x+1][(int)y]<zValue || zBuffer[x+1][(int)y]==0)
							context.setRGB(x+1, (int)y, color);
						if(zBuffer[x][(int)y+1]<zValue || zBuffer[x][(int)y+1]==0)
							context.setRGB(x, (int)y+1, color);
					
						zValue+=zStep;
					}
				}
			}
		}
	}
	
	private void fillTriangle(Vertex3D a, Vertex3D b, Vertex3D c, int color, boolean doZbuffer) {
		List<Vertex3D> temp = new ArrayList<>();
		temp.add(convertTo2D(a));
		temp.add(convertTo2D(b));
		temp.add(convertTo2D(c));
		temp.sort(new Comparator<Vertex3D>() {
			@Override
			public int compare(Vertex3D o1, Vertex3D o2) {
				//return (int)(o1.y-o2.y);
				
				if((o1.y - o2.y) < 0)
					return -1;
				else
					return 1;
			}
		});
		
		double dy02 = (temp.get(2).y-temp.get(0).y);
		double dy01 = (temp.get(1).y-temp.get(0).y);
		
		if(Math.abs(dy02)<=1) {
			drawLine((int)temp.get(0).x, (int)temp.get(0).y, temp.get(0).z, (int)temp.get(2).x, (int)temp.get(2).y, temp.get(2).z, color);
			return;
		}
		
		double scale = dy01/dy02;
		double middleX = temp.get(0).x+(temp.get(2).x-temp.get(0).x)*scale;
		double middleZ = temp.get(0).z+(temp.get(2).z-temp.get(0).z)*scale;
		Vertex3D middle = new Vertex3D("middle", middleX, temp.get(1).y, middleZ);
		
		fillBottomFlatTriangle(temp.get(0), temp.get(1), middle, color, doZbuffer);
		fillTopFlatTriangle(temp.get(1), middle, temp.get(2), color, doZbuffer);
		
		System.out.print("");
	}
	
	//a - vertex on the top of the triangle
	//b,c - vertices on the bottom edge of the triangle
	private void fillBottomFlatTriangle(Vertex3D a, Vertex3D b,Vertex3D c, int color, boolean doZbuffer) {
		Vertex3D ab = new Vertex3D("ab", a.x, a.y, a.z);
		Vertex3D ac = new Vertex3D("ac", a.x, a.y, a.z);
		
		double dy = b.y-a.y;
		
		if(dy<=1) {
			drawLine((int)b.x, (int)b.y, b.z, (int)c.x, (int)c.y, c.z, color);
			return;
		}
		
		double dxAB = (b.x-a.x)/dy;
		double dxAC = (c.x-a.x)/dy;
		
		double dzAB = (b.z-a.z)/dy;
		double dzAC = (c.z-a.z)/dy;
		
		Vertex3D stepAC = new Vertex3D("step", dxAC, 0, dzAC);
		Vertex3D stepAB = new Vertex3D("step", dxAB, 0, dzAB);
		
		double zValue;
		for(int y=(int)a.y; y<(int)(b.y); ++y) {
			for(int x=(int)Math.min(ab.x, ac.x);x<(int)Math.max(ab.x, ac.x);++x) {
				if(y>0 && y<this.height-1 && x>0 && x<this.width-1) {
					zValue = calculateZ(ac, ab, x);
					if(zBuffer[x][y]<zValue || zBuffer[x][y]==0) {
						context.setRGB(x, y, color);
						if(doZbuffer)
							zBuffer[x][y] = zValue;
					}
					zValue = calculateZ((Vertex3D) ac.add(stepAC), (Vertex3D) ab.add(stepAB), x);
					if(zBuffer[x][y+1]<zValue || zBuffer[x][y+1]==0) {
						context.setRGB(x, y+1, color);
						if(doZbuffer)
							zBuffer[x][y+1] = zValue;
					}
				}
			}
			ab = (Vertex3D) ab.add(stepAB);
			ac = (Vertex3D) ac.add(stepAC);
			
			if(Vertex3D.dist(a, ab) > Vertex3D.dist(a, b)) {
				ab.x = b.x;
				ab.z = b.z;
			}
			
			if(Vertex3D.dist(a, ac) > Vertex3D.dist(a, c)) {
				ac.x = c.x;
				ac.z = c.z;
			}
		}
	}
	
	//a,b - vertices on the top edge of the triangle
	//c - vertex on the bottom  of the triangle
	private void fillTopFlatTriangle(Vertex3D a, Vertex3D b,Vertex3D c, int color, boolean doZbuffer) {
		Vertex3D ac = new Vertex3D("ac", a.x, a.y, a.z);
		Vertex3D bc = new Vertex3D("bc", b.x, b.y, b.z);
		
		double dy = c.y-b.y;
		if(dy<=1) {
			drawLine((int)a.x, (int)a.y, a.z, (int)b.x, (int)b.y, b.z, color);
			return;
		}
		double dxAC = (c.x-a.x)/dy;
		double dxBC = (c.x-b.x)/dy;
		
		double dzBC = (c.z-b.z)/dy;
		double dzAC = (c.z-a.z)/dy;
		
		Vertex3D stepAC = new Vertex3D("step", dxAC, 0, dzAC);
		Vertex3D stepBC = new Vertex3D("step", dxBC, 0, dzBC);
		
		double zValue;
		for(int y=(int)b.y; y<(int)(c.y);++y) {
			for(int x=(int)Math.min(ac.x, bc.x);x<(int)Math.max(ac.x, bc.x);++x) {
				if(y>0 && y<this.height-1 && x>0 && x<this.width-1) {
					zValue = calculateZ(ac, bc, x);
					if(zBuffer[x][y]<zValue || zBuffer[x][y]==0) {
						context.setRGB(x, y, color);
						if(doZbuffer)
							zBuffer[x][y] = zValue;
					}
					zValue = calculateZ((Vertex3D) ac.add(stepAC), (Vertex3D) bc.add(stepBC), x);
					if(zBuffer[x][y+1]<zValue || zBuffer[x][y+1]==0) {
						context.setRGB(x, y+1, color);
						if(doZbuffer)
							zBuffer[x][y+1] = zValue;
					}
				}
			}
			ac = ac.add(stepAC);
			bc = bc.add(stepBC);
			
			if(Vertex3D.dist(a, ac) > Vertex3D.dist(a, c)) {
				ac.x = c.x;
				ac.z = c.z;
			}
			
			if(Vertex3D.dist(b, bc) > Vertex3D.dist(b, c)) {
				bc.x = c.x;
				bc.z = c.z;
			}
		}
	}
	
	private double calculateZ(Vertex3D a,Vertex3D b,int x) {
		Vertex3D temp1 = a.getCopy();
		Vertex3D temp2 = b.getCopy();
		if(a.x>b.x) {
			temp1 = b.getCopy();
			temp2 = a.getCopy();
		}
		double xDif = temp2.x-temp1.x;
		x-=(int)temp1.x;
		double scale = x/xDif;
		return temp1.z+(temp2.z-temp1.z)*scale;
	}
	
	@Override
	public GeometryObject mouseClick(int x, int y) {
		for(Vertex vertex : objectSet.get(object).v) {
			Vertex3D temp = convertTo2D((Vertex3D)vertex);
			if(Math.abs(x-temp.x)<=3 && Math.abs(y-temp.y)<=3) {
				System.out.println(vertex.name +" "+ ((Vertex3D)vertex).x + " "+((Vertex3D)vertex).y+" "+ ((Vertex3D)vertex).z);
				return null;
			}
		}
		if(renderMode == 1) {
			for(Edge3D e : objectSet.get(object).e) {
				Vertex3D p1 = convertTo2D(e.a);
				Vertex3D p2 = convertTo2D(e.b);
				double a,b,c;
				
				if(p1.x == p2.x) {
					a = 1;
					b = 0;
					c = -p1.x;
					if(Math.min(p1.y, p2.y) < y && y < Math.max(p1.y, p2.y)) {
						if((a*x+b*y+c)/Math.sqrt(a*a+b*b)<3) {
							System.out.println(e.a.name + " - " + e.b.name +" : "+Math.round(e.weight*1000)/1000.0);
							return e;
						}
					}
				}else {
					if(p2.x < p1.x) {
						Vertex3D temp = p1;
						p1 = p2;
						p2 = temp;
					}
					double k = (p2.y - p1.y)/(p2.x - p1.x);
					double n = p1.y-k*p1.x;
					a = -k;
					b = 1;
					c = -n;
					if(Math.min(p1.y, p2.y) < y && y < Math.max(p1.y, p2.y) && Math.min(p1.x, p2.x) < x && x < Math.max(p1.x, p2.x)) {
						if((a*x+b*y+c)/Math.sqrt(a*a+b*b)<3) {
							System.out.println(e.a.name + " - " + e.b.name +" : "+Math.round(e.weight*1000)/1000.0);
							return e;
						}
					}
				}
			}
		}else {
			for(int i=0;i<objectSet.size();++i) {
				for(Shape3D s: objectSet.get(i).s) {
					Edge3D normal = s.getNormal();
					Vertex3D v1 = (Vertex3D) normal.a;
					Vertex3D v2 = (Vertex3D) normal.b;
					
					double visible = v2.getDotProduct((Vertex3D) v1.add(position));
					
					if(visible < 0) {
						for(Triangle3D t : s.getTriangles()) {
							if(isInTriangle(x, y, (Triangle3D)t)) {
								System.out.println(s);
								if(i != this.object || selected == null) {
									this.object = i;
									this.selected = objectSet.get(i);
									drawContext();
									return objectSet.get(i);
								}
								this.selected = s;
								drawContext();
								return s;
							}
						}
					}
				}
			}
		}
		
		this.selected = null;
		drawContext();
		return null;
	}

	@Override
	public void mouseScroll(int d) {
		this.moveZ(d/10.0);
	}
	
	@Override
	public void mouseDragged(int dx, int dy) {
		double rotationX = (-dx/180.0*Math.PI);
		double rotationY = (-dy/180.0*Math.PI);
		
		this.getObject().rotateHorizontal(rotationX/4);
		this.getObject().rotateVertical(rotationY/4);
		this.drawContext();
	}

	private boolean isInTriangle(int x, int y, Triangle3D t) {
		Vertex3D v = new Vertex3D("click", x, y, 0);
		double d1, d2, d3;
	    boolean has_neg, has_pos;
	    
	    Vertex3D v1 = convertTo2D((Vertex3D) t.getVertices().get(0));
	    Vertex3D v2 = convertTo2D((Vertex3D) t.getVertices().get(1));
	    Vertex3D v3 = convertTo2D((Vertex3D) t.getVertices().get(2));

	    d1 = test(v, v1, v2);
	    d2 = test(v, v2, v3);
	    d3 = test(v, v3, v1);

	    has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
	    has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

	    return !(has_neg && has_pos);
	}
	
	private double test(Vertex3D p1, Vertex3D p2, Vertex3D p3){
	    return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}
	
	public Object3D getObject() {
		return objectSet.get(object);
	}

	public void setObject(Object3D newObject) {
		newObject.setCenter(objectSet.get(object).getCenter());
		objectSet.set(this.object, newObject);
		drawContext();
	}
	
	public void addObject(Object3D toAdd) {
		objectSet.add(toAdd);
		this.object = objectSet.size()-1;
		drawContext();
	}
	
	public void removeObject() {
		if(objectSet.size()==1)
			return;
		
		objectSet.remove(object);
		this.object = objectSet.size()-1;
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
		this.position.x+=d;
		drawContext();
	}
	
	public void moveY(double d) {
		this.position.y+=d;
		drawContext();
	}
	
	public void moveZ(double d) {
		this.position.z+=d;
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
