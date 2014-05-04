package jdepend.util.todolist;

import java.util.UUID;

import jdepend.framework.exception.JDependException;

public abstract class TODOItem implements Comparable<TODOItem> {

	private String id;

	private String content;

	private String according;

	private Float order;

	public TODOItem() {
		this.id = UUID.randomUUID().toString();
		order = 0F;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAccording() {
		return according;
	}

	public void setAccording(String according) {
		this.according = according;
	}

	public Float getOrder() {
		return order;
	}

	public void setOrder(Float order) {
		this.order = order;
	}

	public abstract StringBuilder execute() throws JDependException;
	
	public abstract StringBuilder getInfo();

	@Override
	public int compareTo(TODOItem o) {
		return o.order.compareTo(this.order);
	}
}
