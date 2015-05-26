package jdepend.metadata;

import java.io.Serializable;
import java.util.UUID;

import jdepend.metadata.util.JavaClassCollection;

public abstract class InvokeItem implements Serializable {

	private String id;

	protected transient Method caller;
	protected transient Method callee;

	public static final String Ca = "Ca";
	public static final String Ce = "Ce";

	public InvokeItem() {
		super();
		this.id = UUID.randomUUID().toString();
	}

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

	public abstract String getName();

	/**
	 * 该调用类型需要转化为其他调用的类型
	 * 
	 * @return
	 */
	public InvokeItem transform() {
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvokeItem other = (InvokeItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
