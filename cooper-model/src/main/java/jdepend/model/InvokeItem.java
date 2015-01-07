package jdepend.model;

import java.io.Serializable;

import jdepend.model.util.JavaClassCollection;

public abstract class InvokeItem implements Serializable {

	protected transient Method self;
	protected transient Method method;

	public Method getMethod() {
		return method;
	}

	protected void setMethod(Method method) {
		this.method = method;
		this.method.addInvokedMethod(self);
	}

	public Method getSelf() {
		return self;
	}

	public void setSelf(Method self) {
		this.self = self;
	}

	/**
	 * 补充method信息
	 * 
	 * @param javaClasses
	 * @return
	 */
	public abstract boolean supplyMethod(JavaClassCollection javaClasses);

	/**
	 * 调用的method是否是传入的method
	 * 
	 * @param method
	 * @return
	 */
	public abstract boolean math2(Method method);

	/**
	 * 该调用类型需要转化的其他调用类型
	 * 
	 * @return
	 */
	public InvokeItem transform() {
		return null;
	};

}
