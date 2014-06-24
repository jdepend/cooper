package jdepend.util.refactor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MathUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.Measurable;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisResultSummary;

public class AdjustHistory {

	private List<Memento> mementos = new ArrayList<Memento>();

	private List<String> actions;

	private AnalysisResult current;// 移动后的当前结果

	private static AdjustHistory inst = new AdjustHistory();

	public static final String CompareType_Component = "CompareType_Component";

	public static final String CompareType_Class = "CompareType_Class";

	private AdjustHistory() {
	}

	public static AdjustHistory getInstance() {
		return inst;
	}

	public void addMemento() {
		AnalysisResult result = JDependUnitMgr.getInstance().getResult().clone();
		this.addMemento(CreateMemento(result));
	}

	private void addMemento(Memento memento) {
		this.mementos.add(memento);
	}

	private Memento CreateMemento(AnalysisResult result) {
		return new Memento(result, actions);
	}

	public Memento getOriginality() {
		if (this.mementos.size() == 0) {
			return null;
		} else {
			return this.mementos.get(0);
		}
	}

	public List<String> getActions() {
		return actions;
	}

	public void setActions(List<String> actions) {
		this.actions = actions;
	}

	public Memento getPrior() {
		return this.mementos.get(this.mementos.size() - 1);
	}

	public List<Memento> getMementos() {
		return this.mementos;
	}

	public Memento getTheMemento(Date date) {
		for (Memento memento : this.mementos) {
			if (memento.getCreateDate().equals(date)) {
				return memento;
			}
		}
		return null;
	}

	public void clear() {
		this.mementos = new ArrayList<Memento>();
		this.actions = null;
		this.current = null;
	}

	public boolean empty() {
		return this.mementos.size() == 0;
	}

	public void setCurrent(AnalysisResult current) {
		this.current = current;
	}

	public AnalysisResult getCurrent() {
		return current;
	}

	public CompareInfo compare(Object value, String type, String objectMeasuredName, String metrics)
			throws JDependException {
		if (this.getOriginality() != null) {
			AnalysisResult result = this.getOriginality().getResult();
			Measurable measurable;
			if (type.equals(CompareType_Component)) {
				if (objectMeasuredName.equals(AnalysisResultSummary.Name)) {
					measurable = result.getSummary();
				} else {
					measurable = result.getTheComponent(objectMeasuredName);
				}
			} else if (type.equals(CompareType_Class)) {
				measurable = result.getTheClass(objectMeasuredName);
			} else {
				throw new JDependException("compare type is error");
			}

			if (measurable != null) {
				// 获取比较的数值
				CompareInfo info = new CompareInfo();
				info.setMetrics(metrics);
				Object originality = measurable.getValue(metrics);
				info.setValue(value);
				info.setOriginality(originality);
				// 进行比较
				if (value instanceof Float) {
					Float newValue = (Float) value;
					Float oldValue = (Float) originality;
					info.setResult(MathUtil.compare(newValue, oldValue));
				} else if (value instanceof String) {
					String newValue = (String) value;
					String oldValue = (String) originality;
					info.setResult(MathUtil.compare(newValue, oldValue));
				} else if (value instanceof Integer) {
					Integer newValue = (Integer) value;
					Integer oldValue = (Integer) originality;
					info.setResult(MathUtil.compare(newValue, oldValue));
				} else {
					info.setResult(0);
				}
				return info;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
