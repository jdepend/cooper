package jdepend.parse;

import jdepend.framework.exception.JDependException;

public class ParseJDependException extends JDependException {

	private String errorJavaClass;

	public ParseJDependException(String errorJavaClass, Exception e) {
		super(e);
		this.errorJavaClass = errorJavaClass;
	}

	public ParseJDependException(Exception e) {
		super(e);
	}

	@Override
	public String getMessage() {
		StringBuilder msg = new StringBuilder();
		if (this.errorJavaClass != null && this.errorJavaClass.length() > 0) {
			msg.append("解析JavaClass[" + this.errorJavaClass + "]出错.\n");
		}
		msg.append("错误信息：\n");
		msg.append(super.getMessage());
		return msg.toString();
	}

}
