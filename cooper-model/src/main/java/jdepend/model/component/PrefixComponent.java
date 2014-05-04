package jdepend.model.component;

import jdepend.model.Component;
import jdepend.model.JavaPackage;

public abstract class PrefixComponent extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7684368639173862094L;

	public PrefixComponent() {
		super();
	}

	public PrefixComponent(String name) {
		super(name);
	}

	/**
	 * 判断javaPackage是否隶属于本组件
	 * 
	 * @param javaPackage
	 * @return
	 */
	public boolean isMember(JavaPackage javaPackage) {
		return javaPackage.getName().startsWith(this.getName());
	}

}
