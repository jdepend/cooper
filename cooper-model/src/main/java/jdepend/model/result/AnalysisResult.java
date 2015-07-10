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
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.JavaPackage;
import jdepend.metadata.Method;
import jdepend.metadata.tree.JavaPackageNode;
import jdepend.metadata.tree.JavaPackageTreeCreator;
import jdepend.metadata.util.JavaClassCollection;
import jdepend.metadata.util.JavaClassUtil;
import jdepend.model.AreaComponent;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClassUnit;
import jdepend.model.Relation;
import jdepend.model.area.AreaCreatorChain;
import jdepend.model.component.MemoryComponent;
import jdepend.model.util.CopyUtil;
import jdepend.model.util.JavaClassUnitUtil;
import jdepend.model.util.RelationCreator;

public class AnalysisResult extends AnalysisResultScored implements Serializable {

	private static final long serialVersionUID = -8130705071313304937L;

	private List<Component> components;

	private AnalysisRunningContext runningContext;

	private transient AnalysisResultSummary summary;

	private transient Collection<Relation> relations;

	private transient Collection<JavaClassUnit> javaClasses;

	private transient Collection<JavaPackage> javaPackages;// 包含在Component中的包集合

	private transient Float tableRelationScale = null;// tableRelation比例

	private transient byte[] data = null;// result字节数据

	private transient Map<String, Component> componentForNames;

	private transient Map<String, JavaClassUnit> javaClassForIds;

	private transient List<AreaComponent> areaComponents;

	private transient boolean isExecuteResult = false;

	private transient JavaPackageNode javaPackageTree;

	private transient Collection<Method> methods;

	private transient Map<String, JavaClassRelationItem> javaClassRelationItems;

	public static final String Metrics_D = "Result_Metrics_D";
	public static final String Metrics_Balance = "Result_Metrics_Balance";
	public static final String Metrics_RelationRationality = "Result_Metrics_RelationRationality";
	public static final String Metrics_Encapsulation = "Result_Metrics_Encapsulation";

	public static final String Metrics_TotalScore = "Result_Metrics_TotalScore";

	public static final String Metrics_LC = "Result_Metrics_LC";
	public static final String Metrics_CN = "Result_Metrics_CN";
	public static final String Metrics_ComponentCount = "Result_Metrics_ComponentCount";
	public static final String Metrics_RelationCount = "Result_Metrics_RelationCount";
	public static final String Metrics_RelationComponentScale = "Result_Metrics_RelationComponentScale";

	public static final String Metrics_Coupling = "Result_Metrics_Coupling";
	public static final String Metrics_Cohesion = "Result_Metrics_Cohesion";

	public AnalysisResult(List<Component> components, AnalysisRunningContext runningContext) {
		super();
		this.runningContext = runningContext;
		this.components = components;
		this.init();
	}

	public AnalysisResult(AnalysisResult result) {

		this.runningContext = result.runningContext;
		this.components = result.components;

		for (Component component : this.components) {
			component.setResult(this);
		}
		this.areaComponents = result.areaComponents;
		this.isExecuteResult = result.isExecuteResult;

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
		for (Component component : this.components) {
			component.setResult(this);
		}
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

	public synchronized Collection<JavaClassUnit> getClasses() {
		if (javaClasses == null) {
			javaClasses = JavaClassUnitUtil.getClasses(components);
		}
		return javaClasses;
	}

	public synchronized Collection<JavaPackage> getJavaPackages() {
		if (javaPackages == null) {
			javaPackages = JavaClassUnitUtil.getJavaPackages(components);
		}
		return javaPackages;
	}

	public synchronized JavaPackageNode getJavaPackageTree() {
		if (this.javaPackageTree == null) {
			this.javaPackageTree = (new JavaPackageTreeCreator()).createTree(getJavaPackages());
		}
		return javaPackageTree;
	}

	public synchronized JavaClassUnit getTheClass(String id) {
		if (javaClassForIds == null) {
			javaClassForIds = new HashMap<String, JavaClassUnit>();
			for (JavaClassUnit javaClass : getClasses()) {
				javaClassForIds.put(javaClass.getId(), javaClass);
				for (JavaClassUnit innerClassUnit : javaClass.getInnerClassUnits()) {
					javaClassForIds.put(innerClassUnit.getId(), innerClassUnit);
				}
			}
		}
		return javaClassForIds.get(id);
	}

	public synchronized Collection<Method> getMethods() {
		if (this.methods == null) {
			this.methods = new HashSet<Method>();
			for (JavaClassUnit javaClass : this.getClasses()) {
				for (Method method : javaClass.getJavaClass().getSelfMethods()) {
					this.methods.add(method);
				}
			}
		}
		return this.methods;
	}

	public synchronized JavaClassRelationItem getTheJavaClassRelationItem(String id) {
		if (this.javaClassRelationItems == null) {
			this.javaClassRelationItems = new HashMap<String, JavaClassRelationItem>();
			for (JavaClassUnit javaClass : this.getClasses()) {
				for (JavaClassRelationItem item : javaClass.getJavaClass().getRelationItems()) {
					this.javaClassRelationItems.put(item.getId(), item);
				}
			}
		}
		return this.javaClassRelationItems.get(id);
	}

	public boolean isEmpty() {
		return this.components == null || this.components.size() == 0;
	}

	protected boolean hasRelation() {
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
			for (JavaClassUnit javaClass : deleteComponent.getClasses()) {
				for (JavaClassUnit dependClass : javaClass.getCaList()) {
					it = dependClass.getJavaClass().getCeItems().iterator();
					while (it.hasNext()) {
						if (it.next().getTarget().equals(javaClass)) {
							it.remove();
						}
					}
				}
				for (JavaClassUnit dependClass : javaClass.getCeList()) {
					it = dependClass.getJavaClass().getCaItems().iterator();
					while (it.hasNext()) {
						if (it.next().getSource().equals(javaClass)) {
							it.remove();
						}
					}
				}
			}
		}

		return deleteComponent;
	}

	public synchronized boolean addComponent(String componentName, int componentLayer) {
		MemoryComponent newComponent = new MemoryComponent(componentName);
		newComponent.setLayer(componentLayer);

		return addComponent(newComponent);
	}

	public synchronized boolean addComponent(Component component) {
		if (!this.components.contains(component)) {
			this.components.add(component);
			component.setResult(this);
			if (this.componentForNames != null) {
				this.componentForNames.put(component.getName(), component);
			}
			return true;
		} else {
			return false;
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
	protected float calAttentionRelation() {
		return new AnalysisResultUtil(this).calAttentionRelation();
	}

	/**
	 * 计算存在问题的关系的比例
	 * 
	 * @return
	 */
	public float getAttentionRelationScale() {
		return new AnalysisResultUtil(this).getAttentionRelationScale();
	}

	/**
	 * 基于数据库表实现组件间通讯的比例
	 * 
	 * @return
	 */
	public synchronized float calTableRelationScale() {
		if (this.tableRelationScale == null) {
			this.tableRelationScale = new AnalysisResultUtil(this).tableRelationScale();
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
		return new AnalysisResultUtil(this).calClassSize();
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
		if (this.runningContext.getProfileFacade().getAreaComponentProfile().isCreate()) {
			this.areaComponents = new AreaCreatorChain(this).create();
		} else {
			this.areaComponents = new ArrayList<AreaComponent>();
		}
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
		this.javaClassRelationItems = null;

		this.clearScore();

		// 清空Component的缓存信息
		for (JDependUnit unit : this.getComponents()) {
			unit.clear();
		}
		// 清空JavaClass的缓存信息
		for (JavaClassUnit javaClass : getClasses()) {
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
		List<Component> newComponents = (new CopyUtil()).copy(runningContext, this.components);
		return new AnalysisResult(newComponents, this.runningContext);
	}

	public byte[] sequence() throws IOException {
		return getBytes();
	}

	private byte[] getBytes() throws IOException {
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
			} catch (Exception e) {
				e.printStackTrace();
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
		JavaClassCollection javaClasses = new JavaClassCollection(this.getRunningContext().getProfileFacade()
				.getJavaClassRelationItemProfile().getJavaClassRelationTypes(),
				JavaClassUnitUtil.getAllClasses(components));
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
		scoreInfo.append(this.getDistance());
		scoreInfo.append("【满分：" + this.getDistanceScale() + "】 内聚性得分：");
		scoreInfo.append(this.getBalance());
		scoreInfo.append("【满分：" + this.getBalanceScale() + "】 内聚性得分：】封装性得分：");
		scoreInfo.append(this.getEncapsulation());
		scoreInfo.append("【满分：" + this.getEncapsulationScale() + "】 关系合理性得分：");
		scoreInfo.append(this.getRelationRationality());
		scoreInfo.append("【满分：" + this.getRelationRationalityScale() + "】)\n");

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnalysisResult other = (AnalysisResult) obj;

		if (!runningContext.equals(other.runningContext))
			return false;

		if (!this.getSummary().equals(other.getSummary()))
			return false;

		return true;
	}

	public StringBuilder compare(AnalysisResult result) throws JDependException {
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
		itemDiffs.add(new ItemDiff("抽象程度合理性得分", this.getDistance(), result.getDistance()));
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

	@Override
	public float getDistanceScale() {
		return this.runningContext.getProfileFacade().getAnalysisResultProfile().getDistance();
	}

	@Override
	public float getBalanceScale() {
		return this.runningContext.getProfileFacade().getAnalysisResultProfile().getBalance();
	}

	@Override
	public float getEncapsulationScale() {
		return this.runningContext.getProfileFacade().getAnalysisResultProfile().getEncapsulation();
	}

	@Override
	public float getRelationRationalityScale() {
		return this.runningContext.getProfileFacade().getAnalysisResultProfile().getRelationRationality();
	}
}
