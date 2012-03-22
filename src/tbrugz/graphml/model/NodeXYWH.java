package tbrugz.graphml.model;

public class NodeXYWH extends NodeXY {
	
	public NodeXYWH() {
		setStereotype("xynode.whnode");
	}
	
	float width, height;

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public String getStereotypeParam(int i) {
		switch (i) {
			case 3:
				return String.valueOf(width);
			case 4:
				return String.valueOf(height);
		}
		return super.getStereotypeParam(i);
	}
	
	@Override
	public int getStereotypeParamCount() {
		return 5;
	}
	
}
