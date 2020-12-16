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
	
	public Object3D object;
	private Vertex clickedVertex;
	
	public boolean renderingCenter;
	public int renderMode;
	
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
		
		this.object = object;
		
		renderingCenter = false;
		renderMode = 0;
		
		drawContext();
	}
	
	public void drawContext() {
		if(this.object == null) {
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
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
		
		if(renderingCenter)
			renderObjectCenter(object, g);
		
		for(Shape s : object.getSides()) {
			if(renderMode == 0) {
				fill3DShape(s, g);
			}else if(renderMode == 1){
				draw3DEdges(s, g, filled);
			}else {
				fill3DShape(s, g);
				draw3DEdges(s, g, filled);
			}
		}
		
		printObjectData(object, g);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("mono", Font.PLAIN, 15));
		g.drawString("Camera id: "+id, 10, this.height-50);
	}
	
	public BufferedImage getToDrawContex(int width, int height, int xOffset, int yOffset) {
		BufferedImage temp = context.getSubimage(xOffset, yOffset, width, height);
		return temp;
	}
	
	public void fill3DShape(Shape s, Graphics g) {
		Edge normal = s.getNormal();
		Vertex v1 = normal.a;
		Vertex v2 = normal.b;
		
		double dotProduct = Vertex.getDotProduct(v1.add(postition), v2);
		
		if(dotProduct < 0) {
			double dotProduct2 = Vertex.getDotProduct(v1.add(light.getOpositeVector()), v2);
			double cos = dotProduct2/(v1.add(light.getOpositeVector()).getLenght()*v2.getLenght());
			int c = (int)(150-cos*95);
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
		
		double dotProduct = Vertex.getDotProduct(v1.add(postition), v2);
		
		if((dotProduct < 0) || renderMode==1) {
			
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
			} else {
				g.setColor(Color.BLACK);
				for(int i=0;i<temp.length;++i) {
					drawLine((int)temp[i].a.x, (int)temp[i].a.y, temp[i].a.z, (int)temp[i].b.x, (int)temp[i].b.y, temp[i].b.z, 0);
					filled.add(temp[i]);
				}
			}
			
			/*g.setFont(new Font("Serif", Font.PLAIN, 20));
			for(int i=0;i<temp.length;++i) {
				g.fillOval((int)temp[i].a.x-3, (int)temp[i].a.y-3, 6, 6);
				g.drawString((object.getEdges().get(i).a.name), (int)temp[i].a.x-15, (int)temp[i].a.y-3);	
				
				g.fillOval((int)temp[i].b.x-3, (int)temp[i].b.y-3, 6, 6);
				g.drawString((object.getEdges().get(i).b.name), (int)temp[i].b.x-15, (int)temp[i].b.y-3);
			}*/
			
			if(clickedVertex!=null) {
				String data = clickedVertex.name+"( "+Math.round(clickedVertex.x*100)/100.0+", "+Math.round(clickedVertex.y*100)/100.0+")";
				g.drawString("Clicked on: ", 30, 220);
				g.drawString("- "+data, 30, 250);
			}
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
		
		temp.z = v.z;
		
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
		
		center.z = (object.getCenter().z-zNear)/zRatio;
		center.x = a*fov*(object.getCenter().x)/center.z*width+width/2;
		center.y = -fov*(object.getCenter().y)/center.z*height+height/2;
		
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
		if(x1==x2) {
			a = new Vertex("a", (double)y1, (double)x1, z1);
			b = new Vertex("b", (double)y2, (double)x2, z2);
			for(int y=Math.min(y1, y2);y<=Math.max(y1, y2);++y) {
				zValue = calculateZ(a, b, y);
				if(x1>0 && x1<width && y>0 && y<height) {
					if(zBuffer[x1][y]>zValue || zBuffer[x1][y]==0) {
						context.setRGB(x1, y, color);
						zBuffer[x1][y] = zValue;
					}
					if(zBuffer[x1+1][y]>zValue || zBuffer[x1+1][y]==0) {
						context.setRGB(x1+1, y, color);
						zBuffer[x1+1][y] = zValue;
					}
				}
			}
		}else if(x1<x2) {
			double k = (y2-y1)/(double)(x2-x1);
			double n = y1-k*x1;
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
						zValue = calculateZ(a, b, x);
						if(zBuffer[x][(int)y]>zValue || zBuffer[x][(int)y]==0) {
							context.setRGB(x, (int)y, color);
							zBuffer[x][(int)y] = zValue;
						}
						if(zBuffer[x+1][(int)y]>zValue || zBuffer[x+1][(int)y]==0) {
							context.setRGB(x+1, (int)y, color);
							zBuffer[x+1][(int)y] = zValue;
						}
						if(zBuffer[x][(int)y+1]>zValue || zBuffer[x][(int)y+1]==0) {
							context.setRGB(x, (int)y+1, color);
							zBuffer[x][(int)y+1] = zValue;
						}
					}
				}
			}
		}else {
			double k = (y1-y2)/(double)(x1-x2);
			double n = y2-k*x2;
			for(int x=x2;x<x1;++x) {
				double ys = x*k+n;
				double ye = (x+1)*k+n;
				if(ye<ys) {
					ye = ys+ye;
					ys = ye-ys;
					ye = ye-ys;
				}
				for(double y=ys;y<=ye;y+=1) {
					if(x>0 && x<width && y>0 && y<height-1) {
						zValue = calculateZ(a, b, x);
						if(zBuffer[x][(int)y]>zValue || zBuffer[x][(int)y]==0) {
							context.setRGB(x, (int)y, color);
							zBuffer[x][(int)y] = zValue;
						}
						if(zBuffer[x+1][(int)y]>zValue || zBuffer[x+1][(int)y]==0) {
							context.setRGB(x+1, (int)y, color);
							zBuffer[x+1][(int)y] = zValue;
						}
						if(zBuffer[x][(int)y+1]>zValue || zBuffer[x][(int)y+1]==0) {
							context.setRGB(x, (int)y+1, color);
							zBuffer[x][(int)y+1] = zValue;
						}
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
		if(dy20<=1) {
			Vertex min = temp.get(0);
			Vertex max = temp.get(0);
			if(temp.get(1).x > max.x)
				max = temp.get(1);
			if(temp.get(1).x < min.x)
				min = temp.get(1);
			if(temp.get(2).x > max.x)
				max = temp.get(2);
			if(temp.get(2).x < min.x)
				min = temp.get(2);
			drawLine((int)min.x, (int)temp.get(0).y, min.z, (int)max.x, (int)temp.get(0).y, max.z, color);
			return;
		}
		double scale = dy10/dy20;
		double middleX = temp.get(0).x+(temp.get(2).x-temp.get(0).x)*scale;
		double middleZ = temp.get(2).z-(temp.get(2).z-temp.get(0).z)*scale;
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
		
		double zValue;
		for(int y=(int)a.y; y<(int)(a.y+dy); ++y) {
			for(int x=(int)Math.min(ab.x, ac.x);x<Math.max(ab.x, ac.x);++x) {
				if(y>0 && y<this.height-1 && x>0 && x<this.width-1) {
					zValue = calculateZ(ac, ab, x);
					if(zBuffer[x][y]>zValue || zBuffer[x][y]==0) {
						context.setRGB(x, y, color);
						zBuffer[x][y] = zValue;
					}
					if(zBuffer[x+1][y]>zValue || zBuffer[x+1][y]==0) {
						context.setRGB(x+1, y, color);
						zBuffer[x+1][y] = zValue;
					}
					if(zBuffer[x][y+1]>zValue || zBuffer[x][y+1]==0) {
						context.setRGB(x, y+1, color);
						zBuffer[x][y+1] = zValue;
					}
				}
			}
			ab.x+=dxAB;
			ac.x+=dxAC;
			
			ab.z+=dzAB;
			ac.z+=dzAC;
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
		
		double zValue;
		for(int y=(int)a.y; y<(int)(a.y+dy);++y) {
			for(int x=(int)Math.min(ac.x, bc.x);x<Math.max(ac.x, bc.x);++x) {
				if(y>0 && y<this.height-1 && x>0 && x<this.width-1) {
					zValue = calculateZ(ac, bc, x);
					if(zBuffer[x][y]>zValue || zBuffer[x][y]==0) {
						context.setRGB(x, y, color);
						zBuffer[x][y] = zValue;
					}
					if(zBuffer[x+1][y]>zValue || zBuffer[x+1][y]==0) {
						context.setRGB(x+1, y, color);
						zBuffer[x+1][y] = zValue;
					}
					if(zBuffer[x][y+1]>zValue || zBuffer[x][y+1]==0) {
						context.setRGB(x, y+1, color);
						zBuffer[x][y+1] = zValue;
					}
				}
			}
			ac.x+=dxAC;
			bc.x+=dxBC;
			
			bc.z+=dzBC;
			ac.z+=dzAC;
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
	
	public void setObject(Object3D object) {
		this.object = object;
		drawContext();
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
}
