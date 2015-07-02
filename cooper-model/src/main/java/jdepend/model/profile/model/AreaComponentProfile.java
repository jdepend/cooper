package jdepend.model.profile.model;

import java.io.Serializable;
import java.util.List;

public class AreaComponentProfile implements Serializable{

	private static final long serialVersionUID = 1619476873489927050L;
	
	private boolean create;
	
	private List<String> accordings;
	
	public final static String AccordingComponentLayer = "按人工指定的组件层次创建组件区域";
	public final static String AccordingInstability = "按组件的稳定性创建组件区域";

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}

	public List<String> getAccordings() {
		return accordings;
	}

	public void setAccordings(List<String> accordings) {
		this.accordings = accordings;
	}
}
