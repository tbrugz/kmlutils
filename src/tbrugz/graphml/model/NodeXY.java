package tbrugz.graphml.model;

public class NodeXY extends Node implements Stereotyped {
	
	public NodeXY() {
		setStereotype("xynode");
	}
	
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
	public String getStereotypeParam(int i) {
		switch (i) {
			case 0:
				return getLabel();
			case 1:
				return String.valueOf(x);
			case 2:
				return String.valueOf(y);
		}
		return null;
	}
	
	@Override
	public int getStereotypeParamCount() {
		return 3;
	}

}