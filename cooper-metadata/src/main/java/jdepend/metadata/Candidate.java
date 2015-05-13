package jdepend.metadata;

import java.util.Collection;

/**
 * 创建组件模型时的备选元素接口
 * 
 * @author user
 * 
 */
public interface Candidate extends Named, Identifyer {
	
	public String getPlace();

	public boolean isInner();

	public int size();

	public Collection<JavaClass> getClasses();

}
