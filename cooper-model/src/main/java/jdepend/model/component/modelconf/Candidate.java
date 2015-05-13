package jdepend.model.component.modelconf;

import java.util.Collection;

import jdepend.model.Identifyer;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassUnit;
import jdepend.model.Named;

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
