package jdepend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractJDependUnit extends ObjectMeasured implements JDependUnit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4293863673389111622L;

	protected static final String COUPLING = "COUPLING";
	protected static final String CACOUPLING = "CACOUPLING";
	protected static final String CECOUPLING = "CECOUPLING";

	private String name;

	private JDependUnitType type = null;// 类型

	private int lineCount = -1;

	private transient List<JDependUnit> cycles = null;

	private transient Float cohesion = null;
	private transient Float coupling = null;
	private transient Float encapsulation = null;

	public AbstractJDependUnit() {

	}

	public AbstractJDependUnit(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return name;
	}

	public JDependUnitType getType() {
		return type;
	}

	public void setType(JDependUnitType type) {
		this.type = type;
	}

	/**
	 * @return Instability (0-1).
	 */
	public float getStability() {
		return new CalculateMetricsTool(this).stability();
	}

	/**
	 * @return The package's abstractness (0-1).
	 */
	public float getAbstractness() {
		return new CalculateMetricsTool(this).abstractness();
	}

	public int getAfferentCoupling() {
		return this.getAfferents().size();
	}

	public int getEfferentCoupling() {
		return this.getEfferents().size();
	}

	/**
	 * @return The package's distance from the main sequence (D).
	 */
	public float getDistance() {
		return new CalculateMetricsTool(this).distance();
	}

	@Override
	public float getVolatility() {
		return new CalculateMetricsTool(this).volatility();
	}

	/**
	 * 扩展指标
	 */
	public MetricsInfo extendMetrics(String metrics) {
		if (MetricsMgr.getInstance().getMetrics(metrics) == null) {
			return null;
		} else {
			return MetricsMgr.getInstance().getMetrics(metrics).getMetrics(this);
		}
	}

	public boolean getContainsCycle() {
		return this.collectCycle().size() > 0;
	}

	public List<JDependUnit> collectCycle() {
		if (this.cycles == null) {
			List<List<JDependUnit>> cycleses = new CalculateMetricsTool(this).collectCycle();
			if (cycleses != null) {
				this.cycles = cycleses.get(0);
			} else {
				this.cycles = new ArrayList<JDependUnit>();
			}
		}
		return this.cycles;
	}

	public int collectCycle(List<JDependUnit> list, Map<JDependUnit, Integer> knowledge) {
		return new CalculateMetricsTool(this).collectCycle(list, knowledge);
	}

	public int getLineCount() {
		if (this.lineCount == -1) {
			this.lineCount = 0;
			for (JavaClass jClass : this.getClasses()) {
				this.lineCount += jClass.getLineCount();
			}
		}
		return this.lineCount;
	}

	public float coupling(JDependUnit jdependUnit) {
		return this.caCoupling(jdependUnit) + this.ceCoupling(jdependUnit);
	}

	public float getCoupling() {
		if (coupling == null) {
			coupling = caCoupling() + this.ceCoupling();
		}
		return coupling;
	}

	public float getCohesion() {
		if (cohesion == null) {
			float intensity = 0;
			for (JavaClass javaClass : this.getClasses()) {
				// 采用Ce来计算内值值
				for (JavaClassRelationItem relationItem : javaClass.getCeItems()) {
					if (this.containsClass(relationItem.getDepend())) {
						intensity += relationItem.getRelationIntensity();
					}
				}
			}
			cohesion = intensity;
		}
		return cohesion;
	}

	public Float getEncapsulation() {
		if (this.encapsulation == null) {
			this.encapsulation = new CalculateMetricsTool(this).encapsulation();
		}
		return this.encapsulation;
	}

	@Override
	public float calCeCoupling(JDependUnit jdependUnit) {

		if (this.equals(jdependUnit)) {
			return 0;
		}
		float intensity = 0;
		for (JavaClass javaClass : this.getClasses()) {
			for (JavaClassRelationItem relationItem : javaClass.getCeItems()) {
				if (jdependUnit.containsClass(relationItem.getDepend())) {
					intensity += relationItem.getRelationIntensity();
				}
			}
		}
		return intensity;
	}

	@Override
	public Collection<JavaClassRelationItem> calCeCouplingDetail(JDependUnit dependUnit) {

		Collection<JavaClassRelationItem> detail = new ArrayList<JavaClassRelationItem>();
		if (this.equals(dependUnit)) {
			return detail;
		}

		for (JavaClass javaClass : this.getClasses()) {
			for (JavaClassRelationItem relationItem : javaClass.getCeItems()) {
				if (dependUnit.containsClass(relationItem.getDepend())) {
					detail.add(relationItem);
				}
			}
		}
		return detail;
	}

	@Override
	public void clear() {
		this.cycles = null;
		this.cohesion = null;
		this.coupling = null;
		this.encapsulation = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getClass().getName().hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());

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
		final AbstractJDependUnit other = (AbstractJDependUnit) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(JDependUnit obj) {
		return this.getName().compareTo(obj.getName());
	}
	
	@Override
	public int getId() {
		return this.hashCode();
	}
}
