package tbrugz.graphml.model;

public class NodeXY extends Node implements Stereotyped {
	
	public NodeXY() {
		setStereotype("xynode");
	}
	
	Float x,y;
	
	public Float getX() {
		return x;
	}

	public void setX(Float x) {
		this.x = x;
	}

	public Float getY() {
		return y;
	}

	public void setY(Float y) {
		this.y = y;
	}

	@Override
	public String getStereotypeParam(int i) {
		switch (i) {
			case 0:
				return getLabel();
			case 1:
				return x==null?null:String.valueOf(x);
			case 2:
				return y==null?null:String.valueOf(y);
		}
		return null;
	}
	
	@Override
	public int getStereotypeParamCount() {
		return 3;
	}

}