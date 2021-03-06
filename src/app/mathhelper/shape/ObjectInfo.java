package app.mathhelper.shape;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import app.mathhelper.shape.shape3d.Object3D;

public class ObjectInfo {
	private GeometryObject object;
	private Map<String, String> info;
	
	public ObjectInfo(GeometryObject object) {
		this.object = object;
		this.info = new HashMap<>();
	}
	
	public ObjectInfo(GeometryObject object, HashMap<String, String> info) {
		this(object);
		this.info = info;
	}
	
	public void forEach(ForEachInterface forEachFunction) {
		for(Entry<String, String> e : info.entrySet()) {
			forEachFunction.doForEach(e.getKey(), e.getValue());
		}
	}
	
	@Override
	public String toString() {
		String rez = "Object of type : ["+object.getClass().getSimpleName()+"] ";
		rez+=(object instanceof Object3D)?((Object3D)object).getCenter()+" \n" : "\n";		 
		rez+="Info : \n";
		for(Entry<String, String> e : info.entrySet()) {
			rez += "  -"+e.getKey()+": "+e.getValue()+"\n";
		}
		return rez;
	}
	
	public interface ForEachInterface{
		public void doForEach(String key, String value);
	}
}