package app.mathhelper.shape.preset;

import app.mathhelper.shape.shape3d.Object3D;

public enum Preset {
	CUBE("app/presets/cube.obj"),
	TETRAHEDRON("app/presets/tetrahedron.obj"),
	TEAPOT("app/presets/utah.obj"),
	CONE("app/presets/cone.obj"),
	ICOSPHERE("app/presets/icosphere.obj"),
	CYLINDER("app/presets/cylinder.obj"),
	BALL("app/presets/ball.obj");
	
	public String modelData;
	
	private Preset(String path) {
		this.modelData = Object3D.loadDataFromFile(path);
	}
	
	public Object3D getObject() {
		return Object3D.loadObjectFromString(modelData);
	}
}
