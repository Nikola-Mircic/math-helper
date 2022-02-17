package app.mathhelper.shape;

import java.lang.reflect.Field;
import java.util.*;

import app.mathhelper.shape.shape3d.Object3D;

public class ObjectInfo {
	public class FieldInfo<objectT, dataT>{
		private Field field;
		private dataT data;
		private objectT object;
		
		public boolean editable;
		
		public String label;
		public String value;
		
		public FieldInfo(Field field, objectT object, dataT data, String label, String value) {
			this.field = field;
			this.data = data;
			this.object = object;
			this.label = label;
			this.value = value;
		}
	}
	
	private GeometryObject object;
	public String label;
	public String value;
	
	public boolean extensible;
	public boolean editable;
	
	public LinkedList<ObjectInfo> objects;
	public LinkedList<LinkedList<ObjectInfo>> lists;
	public LinkedList<FieldInfo> fields;
	
	public ObjectInfo(GeometryObject object, String label, String value) {
		this.object = object;
		
		this.objects = new LinkedList<>();
		this.fields = new LinkedList<>();
	}
	
	@Override
	public String toString() {	 
		String rez = "Info : \n";
		rez+=printObjectInfo(this, "");
		return rez;
	}
	
	public String printObjectInfo(ObjectInfo info, String intent) {
		String rez = intent + "Object of type : ["+object.getClass().getSimpleName()+"] ";
		rez+=(object instanceof Object3D)?((Object3D)object).getCenter()+" \n" : "\n";	
		System.out.println(intent+label+" : "+value);
		
		if(info.objects != null) {
			for(ObjectInfo oi : info.objects) {
				printObjectInfo(oi, intent+"\t");
			}
		}
		
		if(info.fields != null) {
			for(FieldInfo fi : fields) {
				System.out.println(intent + fi.label + " : "+fi.value);
			}
		}
		
		return rez;
	}
}