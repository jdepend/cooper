package jdepend.model.profile.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AreaComponentProfile implements Serializable {

	private static final long serialVersionUID = 1619476873489927050L;

	private boolean create;

	private List<String> accordings;

	public final static String AccordingComponentLayer = "按人工指定的组件层次创建组件区域";
	public final static String AccordingInstability = "按组件的稳定性创建组件区域";
	public final static String AccordingPathInfo = "按着组件路径创建组件区域";
	

	public static List<String> getAllAccordings() {
		List<String> allAccordings = new ArrayList<String>();
		allAccordings.add(AccordingComponentLayer);
		allAccordings.add(AccordingInstability);

		return allAccordings;
	}

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
	
	public String getExplain() {
		StringBuilder info = new StringBuilder();

		info.append("系统是由多个组件组成的，当系统比较大的时候，组件的数量会比较大（大于20），这时候通过将组件划分到不同的区域（或者叫子系统）内，可以更好的管理这些组件。\n\n");
		info.append("系统可以通过组件的一些指标来识别组件所属的区域，并计算区域的一些指标，如稳定性。\n\n");
		info.append("携带了区域信息的组件关系应该有一定的要求，应该避免稳定区域中的组件依赖不稳定区域中的组件。\n\n");
		info.append("识别组件区域是可选的。\n\n");
		info.append("用户可以通过选择识别组件区域的算法来识别待分析系统的组件区域。\n\n");

		return info.toString();
	}
}
