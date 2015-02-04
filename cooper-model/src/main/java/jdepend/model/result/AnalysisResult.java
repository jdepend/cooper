package jdepend.model.result;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MathUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.AreaComponent;
import jdepend.model.CalculateMetricsTool;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.JavaPackage;
import jdepend.model.Method;
import jdepend.model.Relation;
import jdepend.model.area.AreaCreatorChain;
import jdepend.model.component.MemoryComponent;
import jdepend.model.tree.JavaPackageNode;
import jdepend.model.tree.JavaPackageTreeCreator;
import jdepend.model.util.CopyUtil;
import jdepend.model.util.JavaClassCollection;
import jdepend.model.util.JavaClassUtil;
import jdepend.model.util.RelationCreator;

public class AnalysisResult extends AnalysisResultScored implements Serializable {

	private static final long serialVersionUID = -8130705071313304937L;

	private List<Component> components;

	private AnalysisRunningContext runningContext;

	private transient AnalysisResultSummary summary;

	private transient Collection<Relation> relations;

	private transient Collection<JavaClass> javaClasses;

	private transient Collection<JavaPackage> javaPackages;// 包含在Component中的包集合

	private transient Float tableRelationScale = null;// tableRelation比例

	private transient byte[] data = null;// result字节数据

	private transient Map<String, Component> componentForNames;

	private transient Map<String, JavaClass> javaClassForIds;

	private transient List<AreaComponent> areaComponents;

	private transient boolean isExecuteResult = false;

	private transient JavaPackageNode javaPackageTree;

	private transient Collection<Method> methods;

	public static final String Metrics_D = "Result_Metrics_D";
	public static final String Metrics_Balance = "Result_Metrics_Balance";
	public static final String Metrics_RelationRationality = "Result_Metrics_RelationRationality";
	public static final String Metrics_Encapsulation = "Result_Metrics_Encapsulation";

	public static final String Metrics_OO = "Result_Metrics_OO";
	public static final String Metrics_TotalScore = "Result_Metrics_TotalScore";

	public static final String Metrics_LC = "Result_Metrics_LC";
	public static final String Metrics_CN = "Result_Metrics_CN";
	public static final String Metrics_ComponentCount = "Result_Metrics_ComponentCount";
	public static final String Metrics_RelationCount = "Result_Metrics_RelationCount";
	public static final String Metrics_RelationComponentScale = "Result_Metrics_RelationComponentScale";

	public static final String Metrics_Coupling = "Result_Metrics_Coupling";
	public static final String Metrics_Cohesion = "Result_Metrics_Cohesion";

	public AnalysisResult(List<Component> components) {
		super();
		this.components = components;
		this.init();
	}

	public AnalysisResult(List<Component> components, AnalysisRunningContext runningContext) {
		this(components);
		this.runningContext = runningContext;
	}

	public AnalysisResult(AnalysisResult result) {

		this.components = result.components;
		for (Component component : this.components) {
			component.setResult(this);
		}
		this.areaComponents = result.areaComponents;
		this.isExecuteResult = result.isExecuteResult;
		this.runningContext = result.runningContext;

		this.relations = result.relations;
		this.summary = result.summary;
		this.data = result.data;
		this.tableRelationScale = result.tableRelationScale;
		this.componentForNames = result.componentForNames;
		this.javaClassForIds = result.javaClassForIds;
		this.methods = result.methods;
		this.javaClasses = result.javaClasses;
		this.javaPackages = result.javaPackages;
		this.javaPackageTree = result.javaPackageTree;
	}

	private void init() {
		// 填充Result
		for (Component component : this.components) {
			component.setResult(this);
		}
		// 计算关系
		this.calRelations();
		// 重新计算组件区域
		this.calAreaComponents();
		// 初始化Relations
		this.initRelations();
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
		this.componentForNames = null;
		this.javaClasses = null;
		this.javaPackages = null;
		this.javaClassForIds = null;
	}

	public AnalysisRunningContext getRunningContext() {
		return runningContext;
	}

	public void setRunningContext(AnalysisRunningContext runningContext) {
		this.runningContext = runningContext;
	}

	public synchronized Collection<JavaClass> getClasses() {
		if (javaClasses == null) {
			javaClasses = JavaClassUtil.getClasses(components);
		}
		return javaClasses;
	}

	public synchronized Collection<JavaPackage> getJavaPackages() {
		if (javaPackages == null) {
			javaPackages = JavaClassUtil.getJavaPackages(components);
		}
		return javaPackages;
	}

	public synchronized JavaPackageNode getJavaPackageTree() {
		if (this.javaPackageTree == null) {
			this.javaPackageTree = (new JavaPackageTreeCreator()).createTree(getJavaPackages());
		}
		return javaPackageTree;
	}

	public synchronized JavaClass getTheClass(String id) {
		if (javaClassForIds == null) {
			javaClassForIds = new HashMap<String, JavaClass>();
			for (JavaClass javaClass : getClasses()) {
				javaClassForIds.put(javaClass.getId(), javaClass);
			}
		}
		return javaClassForIds.get(id);
	}

	public synchronized Collection<Method> getMethods() {
		if (this.methods == null) {
			this.methods = new HashSet<Method>();
			for (JavaClass javaClass : this.getClasses()) {
				for (Method method : javaClass.getSelfMethods()) {
					this.methods.add(method);
				}
			}
		}
		return this.methods;
	}

	public boolean isEmpty() {
		return this.components == null || this.components.size() == 0;
	}

	public boolean hasRelation() {
		return this.getRelations() != null && this.getRelations().size() > 0;
	}

	public Component getTheComponent(String componentName) {
		return this.getComponentForNames().get(componentName);
	}

	public synchronized Component deleteTheComponent(String name) {
		Component deleteComponent = null;
		for (Component component : this.components) {
			if (component.getName().equals(name)) {
				deleteComponent = component;
				this.components.remove(component);
				break;
			}
		}
		// 删除JavaClass间的关系
		if (deleteComponent != null) {
			if (this.componentForNames != null) {
				this.componentForNames.remove(deleteComponent.getName());
			}
			Iterator<JavaClassRelationItem> it;
			for (JavaClass javaClass : deleteComponent.getClasses()) {
				for (JavaClass dependClass : javaClass.getCaList()) {
					it = dependClass.getSelfCeItems().iterator();
					while (it.hasNext()) {
						if (it.next().getDepend().equals(javaClass)) {
							it.remove();
						}
					}
				}
				for (JavaClass dependClass : javaClass.getCeList()) {
					it = dependClass.getSelfCaItems().iterator();
					while (it.hasNext()) {
						if (it.next().getDepend().equals(javaClass)) {
							it.remove();
						}
					}
				}
			}
		}

		return deleteComponent;
	}

	public synchronized void addComponent(String componentName, int componentLayer) {
		MemoryComponent newComponent = new MemoryComponent(componentName);
		newComponent.setLayer(componentLayer);
		this.components.add(newComponent);
		if (this.componentForNames != null) {
			this.componentForNames.put(newComponent.getName(), newComponent);
		}

	}

	public synchronized AnalysisResultSummary getSummary() {

		if (this.summary == null) {
			this.summary = AnalysisResultSummary.calSummary(this);
		}
		return this.summary;
	}

	/**
	 * 计算存在问题的关系的权重
	 * 
	 * @return
	 */
	public float calAttentionRelation() {

		float scale = 0F;

		if (this.getRelations().size() == 0) {
			scale = 0F;
		} else {

			float attentions = 0F;
			for (Relation relation : this.getRelations()) {
				if (relation.isAttention()) {
					// 根据关系性质计算存在问题的关系比例
					if (relation.getAttentionType() == Relation.MutualDependAttentionType) {
						attentions += 1.0F;// 彼此依赖一次增加1，两次增加到2（存在彼此依赖的关系，两条线全部记录为有问题的关系）
					} else if (relation.getAttentionType() == Relation.ComponentLayerAttentionType) {
						attentions += 0.8F;
					} else if (relation.getAttentionType() == Relation.SDPAttentionType) {
						attentions += 0.5F;
					}
					if (relation.getAttentionType() == Relation.CycleDependAttentionType) {
						attentions += 0.3F;
					}
				}
			}
			scale = attentions * 1F / this.getRelations().size();
		}

		return scale;
	}

	/**
	 * 计算存在问题的关系的比例
	 * 
	 * @return
	 */
	public float getAttentionRelationScale() {

		float scale = 0F;

		if (this.getRelations().size() == 0) {
			scale = 0F;
		} else {
			int isAttention = 0;
			for (Relation relation : this.getRelations()) {
				if (relation.isAttention()) {
					isAttention++;
				}
			}
			scale = isAttention * 1F / this.getRelations().size();
		}

		return scale;
	}

	/**
	 * 基于数据库表实现组件间通讯的比例
	 * 
	 * @return
	 */
	public synchronized float calTableRelationScale() {
		if (this.tableRelationScale == null) {
			this.tableRelationScale = MetricsFormat.toFormattedMetrics(CalculateMetricsTool
					.tableRelationScale(getClasses()));
		}
		return this.tableRelationScale;
	}

	/**
	 * 返回关系个数和组件个数的比值
	 * 
	 * @return
	 */
	public float calRelationComponentScale() {
		if (this.getComponents().size() > 0) {
			return MetricsFormat.toFormattedMetrics(this.getRelations().size() * 1F / this.getComponents().size());
		} else {
			return 0F;
		}
	}

	/**
	 * 计算平均类大小
	 * 
	 * @return
	 */
	public int calClassSize() {
		int classCount = 0;
		for (JavaClass JavaClass : this.getClasses()) {
			if (JavaClass.getLineCount() != 0) {
				classCount += 1;
			}
		}
		if (classCount != 0) {
			return this.getSummary().getLineCount() / classCount;
		} else {
			return 0;
		}
	}

	public synchronized Collection<Relation> getRelations() {
		if (this.relations == null) {
			this.calRelations();
		}
		return this.relations;
	}

	public synchronized List<AreaComponent> getAreaComponents() {
		if (this.areaComponents == null) {
			this.calAreaComponents();
		}
		return this.areaComponents;
	}

	private void calRelations() {
		this.relations = new RelationCreator().create(components);
	}

	private void calAreaComponents() {
		this.areaComponents = new AreaCreatorChain().create(this);
	}

	private void initRelations() {
		for (final Relation relation : this.getRelations()) {
			relation.init();
		}
	}

	public Relation getTheRelation(String current, String depend) {
		for (Relation r : this.getRelations()) {
			if (r.equals(current, depend)) {
				return r;
			}
		}
		return null;
	}

	private synchronized Map<String, Component> getComponentForNames() {
		if (this.componentForNames == null) {
			this.componentForNames = new HashMap<String, Component>();
			for (Component unit : this.components) {
				this.componentForNames.put(unit.getName(), unit);
			}
		}
		return this.componentForNames;
	}

	/**
	 * 得到与配置信息存在差异的elements
	 * 
	 * @return
	 */
	public Map<String, String> getDiffElements() {
		if (this.isExecuteResult) {
			return this.runningContext.getDiffElements();
		} else {
			return null;
		}
	}

	public void clearCache() {
		// 清空自身内容
		this.relations = null;
		this.summary = null;
		this.data = null;
		this.tableRelationScale = null;
		this.componentForNames = null;
		this.javaClassForIds = null;
		this.methods = null;

		this.clearScore();

		// 清空Component的缓存信息
		for (JDependUnit unit : this.getComponents()) {
			unit.clear();
		}
		// 清空JavaClass的缓存信息
		for (JavaClass javaClass : getClasses()) {
			javaClass.clear();
		}

		this.javaClasses = null;
		this.javaPackages = null;
		this.javaPackageTree = null;

		// 重新计算relations
		this.calRelations();
		// 重新计算组件区域
		this.calAreaComponents();
	}

	public void clearRelationCache() {
		for (Relation relation : this.relations) {
			relation.clear();
		}
	}

	public boolean isExecuteResult() {
		return isExecuteResult;
	}

	public void setExecuteResult(boolean isExecuteResult) {
		this.isExecuteResult = isExecuteResult;
	}

	public AnalysisResult clone() {
		return new AnalysisResult((new CopyUtil()).copy(this.components), this.runningContext);
	}

	public byte[] getBytes() throws IOException {
		if (this.data == null) {
			ByteArrayOutputStream outstream = null;
			GZIPOutputStream gzip = null;
			ObjectOutputStream out = null;
			try {
				outstream = new ByteArrayOutputStream();
				gzip = new GZIPOutputStream(outstream);
				out = new ObjectOutputStream(gzip);
				out.writeObject(this);
				out.flush();
				gzip.flush();
				gzip.finish();
				outstream.flush();
				this.data = outstream.toByteArray();
			} finally {
				if (outstream != null) {
					try {
						outstream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (gzip != null) {
					try {
						gzip.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return this.data;

	}

	public static AnalysisResult create(byte[] data) throws IOException, ClassNotFoundException {
		return create(data, null);
	}

	public static AnalysisResult create(byte[] data, AnalysisResultUnSequenceListener listener) throws IOException,
			ClassNotFoundException {
		InputStream inputstream = null;
		GZIPInputStream gzip = null;
		ObjectInputStream in = null;
		try {
			inputstream = new ByteArrayInputStream(data);
			gzip = new GZIPInputStream(inputstream);
			in = new ObjectInputStream(gzip);
			AnalysisResult result = (AnalysisResult) in.readObject();
			result.unSequence(listener);
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (gzip != null) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputstream != null) {
				try {
					inputstream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void unSequence() {
		this.unSequence(null);
	}

	public void unSequence(AnalysisResultUnSequenceListener listener) {
		if (listener != null) {
			listener.onUnSequence("正在创建类集合");
		}
		JavaClassCollection javaClasses = new JavaClassCollection(JavaClassUtil.getAllClasses(components));
		// 填充JavaClassRelationItem
		if (listener != null) {
			listener.onUnSequence("正在填充类关系");
		}
		JavaClassUtil.supplyJavaClassRelationItem(javaClasses);
		// 填充Method中的InvokeItem中的Method
		if (listener != null) {
			listener.onUnSequence("正在填充类细节");
		}
		JavaClassUtil.supplyJavaClassDetail(javaClasses);
		// 初始化
		this.init();
	}

	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();

		info.append(this.getRunningContext());
		info.append("\n");
		info.append(this.getSummary());
		info.append("\n");
		info.append(this.getScoreInfo());
		info.append("\n");
		info.append("基于数据库表实现组件间通讯的比例：");
		info.append(MetricsFormat.toFormattedPercent(this.calTableRelationScale()));
		info.append("关系个数与组件个数的比值：");
		info.append(this.calRelationComponentScale());
		info.append("\n");

		return info.toString();
	}

	private StringBuilder getScoreInfo() {

		StringBuilder scoreInfo = new StringBuilder();

		scoreInfo.append("最终得分：");
		scoreInfo.append(this.getScore());
		scoreInfo.append("(抽象程度合理性得分：");
		scoreInfo.append(this.getD());
		scoreInfo.append("【满分：" + AnalysisResultScored.D + "】 内聚性得分：");
		scoreInfo.append(this.getBalance());
		scoreInfo.append("【满分：" + AnalysisResultScored.Balance + "】 内聚性得分：】封装性得分：");
		scoreInfo.append(this.getEncapsulation());
		scoreInfo.append("【满分：" + AnalysisResultScored.Encapsulation + "】 关系合理性得分：");
		scoreInfo.append(this.getRelationRationality());
		scoreInfo.append("【满分：" + AnalysisResultScored.RelationRationality + "】)\n");

		return scoreInfo;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.getRunningContext().getCommand() == null) ? 0 : this.getRunningContext().getCommand()
						.hashCode());
		result = prime * result
				+ ((this.getRunningContext().getGroup() == null) ? 0 : this.getRunningContext().getGroup().hashCode());
		result = prime * result + new Integer(this.getSummary().getLineCount()).hashCode();
		result = prime * result + new Float(this.getScore()).hashCode();
		return result;
	}

	public StringBuilder equals(AnalysisResult result) throws JDependException {
		if (result == null) {
			throw new JDependException("参数不能为空");
		}
		if (!this.getRunningContext().getGroup().equals(result.getRunningContext().getGroup())
				|| !this.getRunningContext().getCommand().equals(result.getRunningContext().getCommand())) {
			throw new JDependException("不同命令的分析结果不能比较");
		}
		StringBuilder diff = new StringBuilder();

		List<ItemDiff> itemDiffs = new ArrayList<ItemDiff>();
		itemDiffs.add(new ItemDiff("组件个数", this.getComponents().size(), result.getComponents().size()));
		itemDiffs.add(new ItemDiff("关系个数", this.getRelations().size(), result.getRelations().size()));
		itemDiffs
				.add(new ItemDiff("关系个数与组件个数的比值", this.calRelationComponentScale(), result.calRelationComponentScale()));
		itemDiffs.add(new ItemDiff("总分", this.getScore(), result.getScore()));
		itemDiffs.add(new ItemDiff("抽象程度合理性得分", this.getD(), result.getD()));
		itemDiffs.add(new ItemDiff("内聚性得分", this.getBalance(), result.getBalance()));
		itemDiffs.add(new ItemDiff("封装性得分", this.getEncapsulation(), result.getEncapsulation()));
		itemDiffs.add(new ItemDiff("关系合理性得分", this.getRelationRationality(), result.getRelationRationality()));
		itemDiffs.add(new ItemDiff("耦合值", this.getSummary().getCoupling(), result.getSummary().getCoupling()));
		itemDiffs.add(new ItemDiff("内聚值", this.getSummary().getCohesion(), result.getSummary().getCohesion()));
		String itemDiffDesc;
		for (ItemDiff itemDiff : itemDiffs) {
			itemDiffDesc = itemDiff.toString();
			if (itemDiffDesc != null) {
				diff.append(itemDiffDesc);
			}
		}

		if (diff.length() == 0) {
			return null;
		} else {
			return diff;
		}
	}

	class ItemDiff {
		private String item;
		private Comparable o1;
		private Comparable o2;

		public ItemDiff(String item, Comparable o1, Comparable o2) {
			super();
			this.item = item;
			this.o1 = o1;
			this.o2 = o2;
		}

		@Override
		public String toString() {
			if (!MathUtil.isEquals(o1, o2)) {
				StringBuilder diff = new StringBuilder();
				diff.append(item);
				if (o1.compareTo(o2) == -1) {
					diff.append("下降");
				} else {
					diff.append("升高");
				}
				diff.append("，当前为[");
				diff.append(o1);
				diff.append("]，之前为[");
				diff.append(o2);
				diff.append("]。\n");

				return diff.toString();
			} else {
				return null;
			}
		}

	}
}
