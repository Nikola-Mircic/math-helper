package app.mathhelper.shape.preset;

import java.util.ArrayList;

import app.mathhelper.shape.Vertex;
import app.mathhelper.shape.shape3d.Edge3D;
import app.mathhelper.shape.shape3d.Object3D;
import app.mathhelper.shape.shape3d.Shape3D;
import app.mathhelper.shape.shape3d.Vertex3D;

public class Cube extends Object3D {
	
	private double edgeLenght;
	
	public Cube(int x, int y, int z) {
		this(x, y, z, 0.5);
	}
	
	public Cube(int x, int y, int z, double edgeLenght) {
		super(x, y, z);
		
		this.s = new ArrayList<>();
		this.v = new ArrayList<>();
		
		this.edgeLenght = edgeLenght;
		this.createVerticies(x, y, z);
		this.createEdges();
		this.createSides();
		
		this.center = getCenterCords();
	}
	
	@Override
	protected void createVerticies(int x, int y, int z) {
		v.add( new Vertex3D("A", x-edgeLenght, y-edgeLenght, z-edgeLenght));
		v.add( new Vertex3D("B", x+edgeLenght, y-edgeLenght, z-edgeLenght));
		v.add( new Vertex3D("C", x+edgeLenght, y-edgeLenght, z+edgeLenght));
		v.add( new Vertex3D("D", x-edgeLenght, y-edgeLenght, z+edgeLenght));
		v.add( new Vertex3D("A'", x-edgeLenght, y+edgeLenght, z-edgeLenght));
		v.add( new Vertex3D("B'", x+edgeLenght, y+edgeLenght, z-edgeLenght));
		v.add( new Vertex3D("C'", x+edgeLenght, y+edgeLenght, z+edgeLenght));
		v.add( new Vertex3D("D'", x-edgeLenght, y+edgeLenght, z+edgeLenght));
	}
	
	@Override
	protected void createSides() {
		Vertex3D[] nearSide = {v.get(5), v.get(4), v.get(1), v.get(0)};
		Vertex3D[] farSide = {v.get(7), v.get(6), v.get(3), v.get(2)};
		Vertex3D[] topSide = {v.get(6), v.get(7), v.get(5), v.get(4)};
		Vertex3D[] bottomSide = {v.get(1), v.get(0), v.get(2), v.get(3)};
		Vertex3D[] leftSide = {v.get(4), v.get(7), v.get(0), v.get(3)};
		Vertex3D[] rightSide = {v.get(6), v.get(5), v.get(2), v.get(1)};
		
		s.add( new Shape3D(nearSide));
		s.add( new Shape3D(farSide));
		s.add( new Shape3D(topSide));
		s.add( new Shape3D(bottomSide));
		s.add( new Shape3D(leftSide));
		s.add( new Shape3D(rightSide));
	}
	
	@Override
	protected void createEdges() {
		e.add( new Edge3D( v.get(0),  v.get(1)));//0 1
		e.add( new Edge3D( v.get(1),  v.get(2)));//1 2
		e.add( new Edge3D( v.get(2),  v.get(3)));//2 3
		e.add( new Edge3D( v.get(3),  v.get(0)));//3 0 
		e.add( new Edge3D( v.get(0),  v.get(4)));//0 4 
		e.add( new Edge3D( v.get(1),  v.get(5)));//1 5 
		e.add( new Edge3D( v.get(2),  v.get(6)));//2 6 
		e.add( new Edge3D( v.get(3),  v.get(7)));//3 7
		e.add( new Edge3D( v.get(4),  v.get(5)));//4 5 
		e.add( new Edge3D( v.get(5),  v.get(6)));//5 6 
		e.add( new Edge3D( v.get(6),  v.get(7)));//6 7 
		e.add( new Edge3D( v.get(7),  v.get(4)));//7 4 
	}
	
}
