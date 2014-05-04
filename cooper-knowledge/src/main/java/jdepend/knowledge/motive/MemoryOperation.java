package jdepend.knowledge.motive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jdepend.framework.util.DateUtil;

public class MemoryOperation {

	private Map<String, Map<String, String>> problemOPerations = new HashMap<String, Map<String, String>>();

	private Map<String, Map<String, String>> stableOPerations = new HashMap<String, Map<String, String>>();

	private Map<String, String> areaComponentOperations = new HashMap<String, String>();

	private Map<String, Map<String, String>> componentOperations = new HashMap<String, Map<String, String>>();

	public final static String Add = "Add";

	public final static String Delete = "Delete";

	public final static String isImportant = "isImportant";

	public final static String reason = "reason";

	public final static String Mutability = "Mutability";

	public final static String Stable = "Stable";

	public final static String Middle = "Middle";

	public void addMemoryOperationP(String problemName, String itemName, String itemValue) {

		Map<String, String> item = problemOPerations.get(problemName);
		if (item == null) {
			item = new HashMap<String, String>();
			problemOPerations.put(problemName, item);
		}
		item.put(itemName, itemValue);

	}

	public void addMemoryOperationS(String componentName, String from, String to) {

		Map<String, String> direction = stableOPerations.get(componentName);
		if (direction == null) {
			direction = new HashMap<String, String>();
			stableOPerations.put(componentName, direction);
			direction.put(from, to);
		} else {// 当存在时即是反向操作，删除原来的操作痕迹即可
			stableOPerations.remove(componentName);
		}

	}

	public void addMemoryOperationA(String areaComponent, String componentName, String operation) {
		if (componentName == null) {// 对areaComponent的操作质量Add和Delete两种
			String oldOperation = this.areaComponentOperations.get(areaComponent);
			if (oldOperation == null) {
				this.areaComponentOperations.put(areaComponent, operation);
			} else {
				// 需要刪除增加到该区域里的组件动作
				if (Delete.equals(operation)) {
					L: for (String componentName1 : this.componentOperations.keySet()) {
						Map<String, String> op = this.componentOperations.get(componentName1);
						Iterator<String> opAreaIterator = op.keySet().iterator();
						while (opAreaIterator.hasNext()) {
							String opArea = opAreaIterator.next();
							if (opArea.equals(areaComponent) && op.get(opArea).equals(Add)) {
								opAreaIterator.remove();
								break L;
							}
						}
					}
				}
				// 当存在时即是反向操作，删除原来的操作痕迹
				this.areaComponentOperations.remove(areaComponent);
			}
		} else {
			Map<String, String> info = this.componentOperations.get(componentName);
			if (info == null) {
				info = new HashMap<String, String>();
				info.put(areaComponent, operation);
				this.componentOperations.put(componentName, info);
			} else {
				if (info.get(areaComponent) == null) {
					info.put(areaComponent, operation);
				} else {// 当存在时即是反向操作，删除原来的操作痕迹
					info.remove(areaComponent);
				}

			}
		}
	}

	public Collection<MotiveOperation> create() {
		Collection<MotiveOperation> operations = new ArrayList<MotiveOperation>();
		MotiveOperation operation;
		StringBuilder desc;
		Date sysDate = DateUtil.getSysDate();
		for (String problemName : problemOPerations.keySet()) {
			Map<String, String> items = problemOPerations.get(problemName);
			desc = null;
			for (String itemName : items.keySet()) {
				String itemValue = items.get(itemName);
				if (itemValue != null && itemValue.length() > 0) {
					desc = new StringBuilder();
					if (itemName.equals(isImportant)) {
						desc.append("你认为问题[");
						desc.append(problemName);
						desc.append("]");
						desc.append(itemValue);
					} else if (itemName.equals(reason)) {
						desc.append("你认为问题[");
						desc.append(problemName);
						desc.append("]");
						Map<String, String> items1 = problemOPerations.get(problemName);
						L: for (String itemName1 : items1.keySet()) {
							String itemValue1 = items1.get(itemName1);
							if (itemName1.equals(isImportant)) {
								desc.append(itemValue1);
								break L;
							}
						}
						desc.append("的理由是");
						desc.append(itemValue);
					}
				}
				if (desc != null) {
					operation = new MotiveOperation(desc.toString(), sysDate);
					operations.add(operation);
				}
			}
		}
		for (String componentName : stableOPerations.keySet()) {
			Map<String, String> items = stableOPerations.get(componentName);
			desc = new StringBuilder();
			desc.append("你将组件[");
			desc.append(componentName);
			desc.append("]从[");
			L: for (String from : items.keySet()) {
				desc.append(from);
				desc.append("]移动到了[");
				String to = items.get(from);
				desc.append(to);
				desc.append("]中");
				break L;
			}
			operation = new MotiveOperation(desc.toString(), sysDate);
			operations.add(operation);
		}
		for (String areaComponentName : areaComponentOperations.keySet()) {
			desc = new StringBuilder();
			desc.append("你");
			desc.append(areaComponentOperations.get(areaComponentName));
			desc.append("了组件区域[");
			desc.append(areaComponentName);
			desc.append("]");

			operation = new MotiveOperation(desc.toString(), sysDate);
			operations.add(operation);
		}
		for (String componentName : componentOperations.keySet()) {
			Map<String, String> items = componentOperations.get(componentName);

			for (String areaComponentName : items.keySet()) {
				desc = new StringBuilder();
				desc.append("你将组件[");
				desc.append(componentName);
				desc.append("]");
				String op = items.get(areaComponentName);
				if (op.equals(Add)) {
					desc.append("加入到了组件区域[");
					desc.append(areaComponentName);
					desc.append("]中");
				} else {
					desc.append("从组件区域[");
					desc.append(areaComponentName);
					desc.append("]中刪除了");
				}
				operation = new MotiveOperation(desc.toString(), sysDate);
				operations.add(operation);
			}

		}

		return operations;
	}

	public void reset() {
		problemOPerations = new HashMap<String, Map<String, String>>();
		stableOPerations = new HashMap<String, Map<String, String>>();
		areaComponentOperations = new HashMap<String, String>();
		componentOperations = new HashMap<String, Map<String, String>>();
	}
}
