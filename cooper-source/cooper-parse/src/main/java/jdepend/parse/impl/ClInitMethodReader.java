package jdepend.parse.impl;

import org.apache.bcel.Const;

import jdepend.metadata.Attribute;
import jdepend.metadata.Method;

/**
 * 通過讀取ClInit方法，填充static屬性的值
 * 
 * @author user
 * 
 */
public class ClInitMethodReader extends MethodReader {

	private String staticValue = "";

	public ClInitMethodReader(Method method) {
		super(method);
	}

	@Override
	protected void readInfo(int opcode, String info) {

		String[] infos;
		int pos;
		String staticAtributeName;

		if (opcode == Const.PUTSTATIC) {
			infos = info.split("\\s+");
			if (infos.length > 1) {
				pos = infos[1].lastIndexOf('.');
				if (pos != -1) {
					staticAtributeName = infos[1].substring(pos + 1);
					L: for (Attribute attribute : this.method.getJavaClass().getAttributes()) {
						if (attribute.getName().equals(staticAtributeName)) {
							if (attribute.getStaticValue() == null && staticValue.length() > 0) {
								attribute.setStaticValue("\"" + staticValue + "\"");
								staticValue = "";
							}
							break L;
						}
					}

				}
			}
		} else if (opcode == Const.LDC) {
			infos = info.split("\\s+");
			if (infos.length > 1 && infos[1].length() > 2) {
				staticValue += infos[1].substring(1, infos[1].length() - 1);
			}
		}

	}
}
