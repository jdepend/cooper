package jdepend.parse.impl;

import jdepend.metadata.Method;

/**
 * 方法內容读取器
 * 
 * @author user
 * 
 */
public abstract class MethodReader {

	protected Method method;

	public MethodReader(Method method) {
		super();
		this.method = method;
	}

	protected abstract void readInfo(int opcode, String info);

}
