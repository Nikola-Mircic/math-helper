package app.mathhelper.shape.preset;

import app.mathhelper.shape.Object3D;

public enum Preset {
	CUBE("presets/cube.obj"),
	TETRAHEDRON("presets/tetrahedron.obj"),
	TEAPOT("presets/utah.obj"),
	CONE("presets/cone.obj"),
	ICOSPHERE("presets/icosphere.obj"),
	CYLINDER("presets/cylinder.obj"),
	BALL("presets/ball.obj");
	
	public Object3D model;
	private String path;
	
	private Preset(String path) {
		this.model = Object3D.loadFromFile(path);
		this.path = path;
	}
	
	public void recreateObject() {
		this.model = Object3D.loadFromFile(path);
	}
}
