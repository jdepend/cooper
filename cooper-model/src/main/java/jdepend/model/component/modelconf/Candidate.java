package jdepend.model.component.modelconf;

import java.util.Collection;

import jdepend.model.JavaClass;
import jdepend.model.Named;

/**
 * 创建组件模型时的备选元素接口
 * 
 * @author user
 * 
 */
public interface Candidate extends Named {

	public boolean isInner();

	public int size();

	public Collection<JavaClass> getClasses();

}
