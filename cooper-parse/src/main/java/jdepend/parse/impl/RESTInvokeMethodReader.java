package jdepend.parse.impl;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.Method;
import jdepend.model.RESTInvokeItem;
import jdepend.parse.ParseConfigurator;

/**
 * 识别REST调用的方法读取器
 * 
 * @author user
 * 
 */
public class RESTInvokeMethodReader extends MethodReader {

	private String url;

	private String constantClassName;

	private String constantAttributeName;

	private List<String> invokeClassNames = new ArrayList<String>();

	public RESTInvokeMethodReader(Method method, ParseConfigurator parseConfigurator) {
		super(method);

		for (String invokeClassName : parseConfigurator.getRestInvokeClassNames()) {
			this.invokeClassNames.add(invokeClassName);
		}
	}

	public void addInvokeClassName(String invokeClassName) {
		this.invokeClassNames.add(invokeClassName);
	}

	@Override
	protected void readInfo(String info) {

		if (invokeClassNames.size() == 0) {
			return;
		}

		String[] infos;
		int pos;
		String calledName;

		if (info.startsWith("invokevirtual")) {
			infos = info.split("\\s+");
			if (infos.length > 1) {
				pos = infos[1].lastIndexOf('.');
				if (pos != -1) {
					calledName = infos[1].substring(0, pos);
					if (invokeClassNames.contains(calledName)) {
						RESTInvokeItem item = new RESTInvokeItem(url, constantClassName, constantAttributeName);
						method.addInvokeItem(item);
					}
				}
			}
		} else if (info.startsWith("ldc")) {
			infos = info.split("\\s+");
			if (infos.length > 0 && infos[1].startsWith("\"")) {
				url = infos[1];
			}
		} else if (info.startsWith("getstatic")) {
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
