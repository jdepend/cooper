package jdepend.parse.impl;

import java.util.Collection;
import java.util.HashSet;

import org.apache.bcel.Const;

import jdepend.metadata.HttpInvokeItem;
import jdepend.metadata.Method;
import jdepend.parse.ParseConfigurator;

/**
 * 识别Http调用的方法读取器
 * 
 * @author user
 * 
 */
public class HttpInvokeMethodReader extends MethodReader {

	private String url;

	private String constantClassName;

	private String constantAttributeName;

	private Collection<String> invokeClassNames = new HashSet<String>();

	public HttpInvokeMethodReader(Method method, ParseConfigurator parseConfigurator) {
		super(method);

		if (parseConfigurator.getHttpInvokeClassNames() != null) {
			for (String invokeClassName : parseConfigurator.getHttpInvokeClassNames()) {
				this.invokeClassNames.add(invokeClassName);
			}
		}
	}

	public void addInvokeClassName(String invokeClassName) {
		this.invokeClassNames.add(invokeClassName);
	}

	@Override
	protected void readInfo(int opcode, String info) {

		if (invokeClassNames.size() == 0) {
			return;
		}

		String[] infos;
		int pos;
		String calledName;

		if (opcode == Const.INVOKEVIRTUAL || opcode == Const.INVOKESPECIAL || opcode == Const.INVOKESTATIC) {
			infos = info.split("\\s+");
			if (infos.length > 1) {
				pos = infos[1].lastIndexOf('.');
				if (pos != -1) {
					calledName = infos[1].substring(0, pos);
					if (invokeClassNames.contains(calledName)) {
						if (url != null || constantClassName != null) {
							HttpInvokeItem item = new HttpInvokeItem(url, constantClassName, constantAttributeName);
							method.addInvokeItem(item);
							url = null;
							constantClassName = null;
							constantAttributeName = null;
						}
					}
				}
			}
		} else if (opcode == Const.LDC) {
			infos = info.split("\\s+");
			if (infos.length > 0 && infos[1].startsWith("\"/")) {
				url = infos[1];
			}
		} else if (opcode == Const.GETSTATIC) {
			infos = info.split("\\s+");
			if (infos.length > 0) {
				pos = infos[1].lastIndexOf('.');
				if (pos != -1) {
					constantClassName = infos[1].substring(0, pos);
					constantAttributeName = infos[1].substring(pos + 1);
				}
			}
		}
	}
}
