package app.mathhelper.shape.preset;

import app.mathhelper.shape.shape3d.Object3D;

public enum Preset {
	CUBE("presets/cube.obj"),
	TETRAHEDRON("presets/tetrahedron.obj"),
	TEAPOT("presets/utah.obj"),
	CONE("presets/cone.obj"),
	ICOSPHERE("presets/icosphere.obj"),
	CYLINDER("presets/cylinder.obj"),
	BALL("presets/ball.obj");
	
	public String modelData;
	
	private Preset(String path) {
		this.modelData = Object3D.loadDataFromFile(path);
	}
	
	public Object3D getObject() {
		return Object3D.loadObjectFromString(modelData);
	}
}
