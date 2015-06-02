package jdepend.parse.impl;

import jdepend.parse.ParseException;

public class ParseClassException extends ParseException {

	private String errorJavaClass;

	public ParseClassException(String errorJavaClass, Exception e) {
		super(e);
		this.errorJavaClass = errorJavaClass;
	}

	public ParseClassException(Exception e) {
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
