package tbrugz.graphml.model;

public class NodeXYWH extends NodeXY {
	
	public NodeXYWH() {
		setStereotype("xynode.whnode");
	}
	
	Float width, height;

	public Float getWidth() {
		return width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	@Override
	public String getStereotypeParam(int i) {
		switch (i) {
			case 3:
				return width==null?null:String.valueOf(width);
			case 4:
				return height==null?null:String.valueOf(height);
		}
		return super.getStereotypeParam(i);
	}
	
	@Override
	public int getStereotypeParamCount() {
		return 5;
	}
	
}
