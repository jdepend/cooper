package jdepend.model;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.result.AnalysisResult;

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

	private transient List<? extends JDependUnit> cycles = null;

	private transient Float coupling = null;
	private transient Float encapsulation = null;

	private transient Float abstractness = null;
	private transient Float stability = null;
	private transient Float distance = null;
	private transient Float volatility = null;

	public final static int Cycle = 2;
	public final static int LocalCycle = 1;
	public final static int NoCycle = 0;
	public final static int StopCheckCycle = -1;

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

	public abstract AnalysisResult getResult();

	/**
	 * @return Instability (0-1).
	 */
	public synchronized float getStability() {
		if (stability == null) {
			stability = new CalculateMetricsTool(this).stability();
		}
		return stability;
	}

	/**
	 * @return The package's abstractness (0-1).
	 */
	public synchronized float getAbstractness() {
		if (abstractness == null) {
			abstractness = new CalculateMetricsTool(this).abstractness();
		}
		return abstractness;
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
	public synchronized float getDistance() {
		if (distance == null) {
			distance = new CalculateMetricsTool(this).distance();
		}
		return distance;
	}

	@Override
	public synchronized float getVolatility() {
		if (volatility == null) {
			volatility = new CalculateMetricsTool(this).volatility();
		}
		return volatility;
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
		return this.collectCycle().size() > 1;
	}

	public synchronized List<? extends JDependUnit> collectCycle() {
		if (this.cycles == null) {
			List<List<? extends JDependUnit>> cycleses = new CalculateMetricsTool(this).collectCycle();
			if (cycleses != null) {
				this.cycles = cycleses.get(0);
			} else {
				this.cycles = new ArrayList<JDependUnit>();
			}
		}
		return this.cycles;
	}

	public synchronized void setCycles(List<? extends JDependUnit> cycles) {
		// 计算循环依赖的顺序
		List<JDependUnit> sortCycles = new ArrayList<JDependUnit>();
		if (cycles.get(0).equals(this)) {
			sortCycles = (List<JDependUnit>) cycles;
		} else {
			int pos;
			JDependUnit unit;
			L: for (pos = 1; pos < cycles.size(); pos++) {
				unit = cycles.get(pos);
				if (unit.equals(this)) {
					break L;
				}
			}
			for (int index = pos; index < cycles.size(); index++) {
				sortCycles.add(cycles.get(index));
			}
			for (int index = 0; index < pos; index++) {
				sortCycles.add(cycles.get(index));
			}
		}
		// 赋值
		this.cycles = sortCycles;
	}

	public synchronized List<? extends JDependUnit> getCycles() {
		return cycles;
	}

	public synchronized int getLineCount() {
		if (this.lineCount == -1) {
			this.lineCount = 0;
			for (JavaClass jClass : this.getClasses()) {
				this.lineCount += jClass.getLineCount();
			}
		}
		return this.lineCount;
	}

	public float coupling(JDependUnit jdependUnit) {
		return this.caCouplingDetail(jdependUnit).getIntensity() + this.ceCouplingDetail(jdependUnit).getIntensity();
	}

	public synchronized float getCoupling() {
		if (coupling == null) {
			coupling = caCoupling() + this.ceCoupling();
		}
		return coupling;
	}

	public synchronized Float getEncapsulation() {
		if (this.encapsulation == null) {
			this.encapsulation = new CalculateMetricsTool(this).encapsulation();
		}
		return this.encapsulation;
	}

	@Override
	public synchronized void clear() {
		this.cycles = null;
		
		this.coupling = null;
		this.encapsulation = null;

		this.abstractness = null;
		this.distance = null;
		this.stability = null;
		this.volatility = null;
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
		if (getClass() != obj.getClass())
			return false;

		AbstractJDependUnit other = (AbstractJDependUnit) obj;
		if (!name.equals(other.name))
			return false;

		return true;
	}

	public int compareTo(JDependUnit obj) {
		return this.getName().compareTo(obj.getName());
	}
}
