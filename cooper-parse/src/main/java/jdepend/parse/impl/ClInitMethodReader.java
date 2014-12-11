package jdepend.parse.impl;

import jdepend.model.Attribute;
import jdepend.model.Method;

public class ClInitMethodReader extends MethodReader {

	private String staticValue;

	public ClInitMethodReader(Method method) {
		super(method);
	}

	@Override
	protected void readInfo(String info) {

		String[] infos;
		int pos;
		String staticAtributeName;

		if (info.startsWith("putstatic")) {
			infos = info.split("\\s+");
			if (infos.length > 1) {
				pos = infos[1].lastIndexOf('.');
				if (pos != -1) {
					staticAtributeName = infos[1].substring(pos + 1);
					L: for (Attribute attribute : this.method.getJavaClass().getAttributes()) {
						if (attribute.getName().equals(staticAtributeName)) {
							if (attribute.getStaticValue() == null && staticValue != null) {
								attribute.setStaticValue(staticValue);
								staticValue = null;
							}
							break L;
						}
					}

				}
			}
		} else if (info.startsWith("ldc")) {
			infos = info.split("\\s+");
			if (infos.length > 0) {
				staticValue = infos[1];
			}
		}

	}

}
