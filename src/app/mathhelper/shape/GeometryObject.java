package app.mathhelper.shape;

import java.util.ArrayList;
import java.util.List;

public class GeometryObject {
	protected double area;
	protected double scope;
	protected List<Vertex> v;
	protected List<Edge> e;
	
	protected GeometryObject() {	
		this.area = -1;
		this.scope = -1;
		this.v = new ArrayList<>();
		this.e = new ArrayList<>();
	}
	
	protected void calculateArea(){}
	protected void calculateScope() {}
	
	public double getArea() {
		if(this.area==-1)
			calculateArea();
		return this.area;
	}
	
	public double getScope() {
		if(this.scope==-1)
			calculateScope();
		return this.scope;
	}
}
