package jdepend.model.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.DateUtil;
import jdepend.framework.util.VersionUtil;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaPackage;
import jdepend.metadata.util.JavaClassUtil;
import jdepend.model.Component;
import jdepend.model.JavaClassUnit;
import jdepend.model.component.CustomComponent;
import jdepend.model.component.modelconf.ComponentModelConf;

/**
 * 一次解析执行的上下文信息
 * 
 * @author wangdg
 * 
 */
public final class AnalysisRunningContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4744540265380367268L;

	private String group;

	private String command;

	private String path;

	private String client;

	private String userName;

	private Date analyseDate;

	private Component component;

	private List<JavaPackage> javaPackages;

	private boolean analyzeInnerClasses = true;

	private boolean enableAbstractClassCountQualificationConfirmer = true;

	private boolean isCalJavaClassCycle = true;

	private boolean isLocalRunning = true;

	private boolean isSaveResult = true;

	private transient Map<String, String> diffElements;

	private transient Collection<JavaClass> javaClasses;

	private transient Map<String, JavaPackage> javaPackagesForId;

	public AnalysisRunningContext() {
		super();

		this.analyseDate = DateUtil.getSysDate();
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isAnalyzeInnerClasses() {
		return analyzeInnerClasses;
	}

	public void setAnalyzeInnerClasses(boolean analyzeInnerClasses) {
		this.analyzeInnerClasses = analyzeInnerClasses;
	}

	public boolean isEnableAbstractClassCountQualificationConfirmer() {
		return enableAbstractClassCountQualificationConfirmer;
	}

	public void setEnableAbstractClassCountQualificationConfirmer(boolean enable) {
		this.enableAbstractClassCountQualificationConfirmer = enable;
	}

	public boolean isCalJavaClassCycle() {
		return isCalJavaClassCycle;
	}

	public void setCalJavaClassCycle(boolean isCalJavaClassCycle) {
		this.isCalJavaClassCycle = isCalJavaClassCycle;
	}

	public boolean isSaveResult() {
		return isSaveResult;
	}

	public void setSaveResult(boolean isSaveResult) {
		this.isSaveResult = isSaveResult;
	}

	public boolean isLocalRunning() {
		return isLocalRunning;
	}

	public void setLocalRunning(boolean isLocalRunning) {
		this.isLocalRunning = isLocalRunning;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public Date getAnalyseDate() {
		return analyseDate;
	}

	public List<JavaPackage> getJavaPackages() {
		return javaPackages;
	}

	public void setJavaPackages(List<JavaPackage> packages) {
		this.javaPackages = packages;
	}

	public JavaPackage getThePackage(String id) {

		if (javaPackagesForId == null) {
			javaPackagesForId = new HashMap<String, JavaPackage>();
			for (JavaPackage javaPackage : this.getJavaPackages()) {
				javaPackagesForId.put(javaPackage.getId(), javaPackage);
			}
		}
		return this.javaPackagesForId.get(id);

	}

	public Collection<JavaClass> getJavaClasses() {
		if (this.javaClasses == null) {
			this.javaClasses = JavaClassUtil.getClassesForJavaPackages(javaPackages);
		}
		return javaClasses;
	}

	/**
	 * 得到与配置信息存在差异的elements
	 * 
	 * @return
	 */
	public Map<String, String> getDiffElements() {
		if (diffElements == null) {
			diffElements = new HashMap<String, String>();
			if (this.component instanceof CustomComponent) {
				ComponentModelConf componentModelConf = ((CustomComponent) this.component).getComponentModelConf();
				diffElements = componentModelConf.calDiffElements(this.javaPackages);
			}
		}
		return this.diffElements;
	}

	private AnalysisRunningContext clone(Map<String, JavaClass> javaClasses) throws JDependException {

		AnalysisRunningContext obj = new AnalysisRunningContext();
		obj.analyseDate = this.analyseDate;
		obj.client = this.client;
		obj.command = this.command;
		obj.component = this.component;
		obj.diffElements = this.diffElements;
		obj.group = this.group;
		obj.isLocalRunning = this.isLocalRunning;
		obj.path = this.path;
		obj.userName = this.userName;

		obj.analyzeInnerClasses = this.analyzeInnerClasses;
		obj.enableAbstractClassCountQualificationConfirmer = this.enableAbstractClassCountQualificationConfirmer;
		obj.isCalJavaClassCycle = this.isCalJavaClassCycle;

		obj.javaPackages = new ArrayList<JavaPackage>();
		for (JavaPackage javaPackage : this.javaPackages) {
			obj.javaPackages.add(javaPackage.clone(javaClasses));
		}

		return obj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
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
		AnalysisRunningContext other = (AnalysisRunningContext) obj;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder();
		if (this.group != null) {
			content.append("运行的组:");
			content.append(group);
			content.append("\n");
		}

		if (this.command != null) {
			content.append("运行的命令:");
			content.append(command);
			content.append("\n");
		}

		content.append("分析路径:");
		content.append(path);
		content.append("\n");

		content.append("是否采用本地服务:");
		content.append(getBooleanMsg(isLocalRunning));
		content.append("\n");

		content.append("是否分析了内部类:");
		content.append(getBooleanMsg(analyzeInnerClasses));
		content.append("\n");

		content.append("是否应用了抽象类资格评判器:");
		content.append(getBooleanMsg(enableAbstractClassCountQualificationConfirmer));
		content.append("\n");

		content.append("是否计算类级别的循环依赖:");
		content.append(getBooleanMsg(isCalJavaClassCycle));
		content.append("\n");

		content.append("执行时间:");
		content.append(this.analyseDate);
		content.append(" 使用V");
		content.append(VersionUtil.getVersion());
		content.append(" BuildDate:");
		content.append(VersionUtil.getBuildDate());
		content.append("\n");

		if (this.component != null && this.component instanceof CustomComponent) {
			content.append("\n");
			content.append(((CustomComponent) this.component).getComponentModelConf());
		}

		return content.toString();
	}

	private static String getBooleanMsg(boolean info) {
		if (info) {
			return "是";
		} else {
			return "否";
		}
	}
}
