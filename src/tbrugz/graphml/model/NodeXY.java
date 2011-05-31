package tbrugz.graphml.model;

public class NodeXY extends Node implements Stereotyped {
	
	float x,y;
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public String getStereotype() {
		return "xynode";
	}
	
	public String getStereotypeParam(int i) {
		switch (i) {
			case 0:
				return String.valueOf(x);
			case 1:
				return String.valueOf(y);
			case 2:
				return getLabel();
		}
		return null;
	}
	
	public int getStereotypeParamCount() {
		return 3;
	}

}