package jdepend.model.component.judge;

import java.util.ArrayList;
import java.util.List;

public final class JudgeConfigure {

	public boolean applyChildren;

	public List<String> childrenKeys = new ArrayList<String>();

	public boolean applyLayer;

	public Integer layer;

	public String getChildrenKeys() {
		StringBuilder keys = new StringBuilder(20);
		for (String key : childrenKeys) {
			if (keys.length() != 0) {
				keys.append(",");
			}
			keys.append(key);
		}
		return keys.toString();
	}

}
