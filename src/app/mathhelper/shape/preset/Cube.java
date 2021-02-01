package app.mathhelper.shape.preset;

import java.util.ArrayList;

import app.mathhelper.shape.Edge;
import app.mathhelper.shape.Object3D;
import app.mathhelper.shape.Shape;
import app.mathhelper.shape.Vertex;

public class Cube extends Object3D {
	
	private double edgeLenght;
	
	public Cube(int x, int y, int z) {
		super(x, y, z);
		this.edgeLenght = 0.5;
	}
	
	public Cube(int x, int y, int z, double edgeLenght) {
		super();
		
		this.s = new ArrayList<>();
		//this.volume = -1;
		
		this.edgeLenght = edgeLenght;
		this.createVerticies(x, y, z+5);
		this.createEdges();
		this.createSides();
		
		this.center = getCenterCords();
	}
	
	@Override
	protected void createVerticies(int x, int y, int z) {
		v.add( new Vertex("A", x-edgeLenght, y-edgeLenght, z-edgeLenght));
		v.add( new Vertex("B", x+edgeLenght, y-edgeLenght, z-edgeLenght));
		v.add( new Vertex("C", x+edgeLenght, y-edgeLenght, z+edgeLenght));
		v.add( new Vertex("D", x-edgeLenght, y-edgeLenght, z+edgeLenght));
		v.add( new Vertex("A'", x-edgeLenght, y+edgeLenght, z-edgeLenght));
		v.add( new Vertex("B'", x+edgeLenght, y+edgeLenght, z-edgeLenght));
		v.add( new Vertex("C'", x+edgeLenght, y+edgeLenght, z+edgeLenght));
		v.add( new Vertex("D'", x-edgeLenght, y+edgeLenght, z+edgeLenght));
	}
	
	@Override
	protected void createSides() {
		Vertex[] nearSide = {v.get(5), v.get(4), v.get(1), v.get(0)};
		Vertex[] farSide = {v.get(7), v.get(6), v.get(3), v.get(2)};
		Vertex[] topSide = {v.get(6), v.get(7), v.get(5), v.get(4)};
		Vertex[] bottomSide = {v.get(1), v.get(0), v.get(2), v.get(3)};
		Vertex[] leftSide = {v.get(4), v.get(7), v.get(0), v.get(3)};
		Vertex[] rightSide = {v.get(6), v.get(5), v.get(2), v.get(1)};
		
		s.add( new Shape(nearSide));
		s.add( new Shape(farSide));
		s.add( new Shape(topSide));
		s.add( new Shape(bottomSide));
		s.add( new Shape(leftSide));
		s.add( new Shape(rightSide));
	}
	
	@Override
	protected void createEdges() {
		e.add( new Edge(v.get(0), v.get(1)));//0 1
		e.add( new Edge(v.get(1), v.get(2)));//1 2
		e.add( new Edge(v.get(2), v.get(3)));//2 3
		e.add( new Edge(v.get(3), v.get(0)));//3 0 
		e.add( new Edge(v.get(0), v.get(4)));//0 4 
		e.add( new Edge(v.get(1), v.get(5)));//1 5 
		e.add( new Edge(v.get(2), v.get(6)));//2 6 
		e.add( new Edge(v.get(3), v.get(7)));//3 7
		e.add( new Edge(v.get(4), v.get(5)));//4 5 
		e.add( new Edge(v.get(5), v.get(6)));//5 6 
		e.add( new Edge(v.get(6), v.get(7)));//6 7 
		e.add( new Edge(v.get(7), v.get(4)));//7 4 
	}
	
}
