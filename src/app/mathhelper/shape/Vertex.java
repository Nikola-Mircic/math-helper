package app.mathhelper.shape;

import app.mathhelper.shape.shape2d.Vertex2D;
import app.mathhelper.shape.shape3d.Vertex3D;

public abstract class Vertex<dvT extends Vertex> extends GeometryObject{
	public String name;
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Vertex))
			return false;
		
		dvT t = (dvT) obj;
		
		if(this.name.equals(t.name))
			return true;
		
		return false;
	}
	
	public dvT add(dvT v) {
		double[] vCords1 = this.getCordsArr();
		double[] vCords2 = v.getCordsArr();
		
		double[] output = new double[vCords1.length];
		
		for(int i=0;i<vCords1.length;++i) {
			output[i] = vCords1[i]+vCords2[i];
		}
		
		dvT result = this.getCopy();
		
		result.setCordsByArr(output);
		
		return result;
	}
	
	public dvT getOpositeVector() {
		double[] vCords = this.getCordsArr();
		
		for(int i=0;i<vCords.length;++i) {
			vCords[i] = -vCords[i];
		}
		
		dvT copy = (dvT) this.getCopy();
		
		copy.setCordsByArr(vCords);
		
		return copy;
	}
	
	public double getLenght() {
		double[] vCords = this.getCordsArr();
		
		double length = 0;
		
		for(double d : vCords) {
			length += d*d;
		}
		
		return Math.sqrt(length);
	}
	
	public double getDotProduct(dvT v) {
		double[] vCords1 = this.getCordsArr();
		double[] vCords2 = v.getCordsArr();
		
		return (vCords1[0]*vCords2[0]+vCords1[1]*vCords2[1]+vCords1[2]*vCords2[2]);
	}
	
	public abstract double getCrossProduct(Vertex2D v);
	
	public abstract Vertex3D getCrossProduct(Vertex3D v);
	
	public abstract double[] getCordsArr();
	
	public abstract void setCordsByArr(double[] arr);
	
	public abstract dvT getCopy();
	
	@Override
	public ObjectInfo getInfo() {
		String data = this.toString();
		data = data.substring(data.indexOf('('));
		data = data.substring(0, data.indexOf(')'));
		
		ObjectInfo info = new ObjectInfo(this, "Vertex "+this.name, data );
		
		return info;
	}
}
