package jdepend.knowledge.domainanalysis;

import jdepend.knowledge.AdviseInfo;

public class DAdviseInfo extends AdviseInfo {

	@Override
	protected String calInfo() {
		StringBuilder info = new StringBuilder();
		info.append(this.getComponentNameInfo());
		info.append(this.getDesc());
		return info.toString();
	}

}
