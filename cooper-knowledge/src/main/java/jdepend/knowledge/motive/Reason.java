package jdepend.knowledge.motive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Reason implements Serializable {

	private String name;
	private String desc;

	public final static String TheSameAreaReason = "彼此依赖或循环依赖不重要的理由是因为这些组件处于一个区块中，不存在单独使用的情况";
	public final static String MutabilityToStableReason = "不稳定的组件被认为成稳定的组件";
	public final static String StableToMutabilityReason = "稳定的组件被认为成不稳定的组件";
	private final static List<String> reasons = new ArrayList<String>();
	static {
		reasons.add(TheSameAreaReason);
		reasons.add(MutabilityToStableReason);
		reasons.add(StableToMutabilityReason);
	}

	public Reason() {
		super();
	}

	public Reason(String name, String desc) {
		super();
		this.name = name;
		this.desc = desc;
	}

	public static List<String> getReasons() {
		return reasons;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static Collection<Reason> findAll() {
		Collection<Reason> reasons = null;

		return reasons;
	}
}
