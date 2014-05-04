package jdepend.model.util;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitType;

/**
 * JDependUnit类型管理器
 * 
 * @author <b>Abner</b>
 * 
 */
public abstract class JDependUnitTypeMgr {

	private List<JDependUnitType> types = null;

	public JDependUnitTypeMgr() {
		this.types = this.createJDependUnitTypes();
	}

	/**
	 * 设置制定包的类型
	 * 
	 * @param unit
	 */
	public void setType(JDependUnit unit) {

		JDependUnitType type = null;

		for (int j = 0; j < types.size(); j++) {
			type = types.get(j);
			if (type.isMember(unit)) {
				unit.setType(type);
			}
		}
	}

	/**
	 * 返回符合特定类型的包集合
	 * 
	 * @param units
	 * @param type
	 * @return
	 */
	public List<JDependUnit> getJDependUnits(List<JDependUnit> units, JDependUnitType type) {

		List<JDependUnit> rtn = new ArrayList<JDependUnit>();

		for (JDependUnit unit : units) {
			if (type.isMember(unit)) {
				rtn.add(unit);
			}
		}

		return rtn;
	}

	public List<JDependUnitType> getTypes() {
		return types;
	}

	public void setTypes(List<JDependUnitType> types) {
		this.types = types;
	}

	/**
	 * 返回类型列表
	 * 
	 * @return
	 */
	protected abstract List<JDependUnitType> createJDependUnitTypes();

}
