package jdepend.model;

import java.io.Serializable;

import jdepend.model.util.JavaClassCollection;

public abstract class InvokeItem implements Serializable {

	protected transient Method caller;
	protected transient Method callee;

	public static final String Ca = "Ca";
	public static final String Ce = "Ce";

	public Method getCallee() {
		return callee;
	}

	protected void setCallee(Method callee) {
		this.callee = callee;
		this.callee.addInvokedItem(this);
	}

	public Method getCaller() {
		return caller;
	}

	public void setCaller(Method caller) {
		this.caller = caller;
	}

	/**
	 * 补充callee信息
	 * 
	 * @param javaClasses
	 * @return
	 */
	public abstract boolean supplyCallee(JavaClassCollection javaClasses);

	/**
	 * 调用的method是否是传入的method
	 * 
	 * @param method
	 * @return
	 */
	public abstract boolean math2(Method method);

	/**
	 * 该调用类型需要转化为其他调用的类型
	 * 
	 * @return
	 */
	public InvokeItem transform() {
		return null;
	}
}
