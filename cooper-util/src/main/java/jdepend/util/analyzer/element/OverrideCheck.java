package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;
import jdepend.model.Method;
import jdepend.model.result.AnalysisResult;
import jdepend.model.tree.JavaClassInheritTreeCreator;
import jdepend.model.tree.JavaClassNode;
import jdepend.model.tree.JavaClassTree;
import jdepend.model.tree.Node;
import jdepend.model.util.JavaClassUnitUtil;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class OverrideCheck extends AbstractAnalyzer {

	private transient List<Method> methods = new ArrayList<Method>();// 缓存在一次继承树扫描时的method集合

	private Boolean isPrintNotOverrideMethod;

	private Boolean isPrintOverrideMethod;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6965754729091695315L;

	public OverrideCheck() {
		super("覆盖检查", Analyzer.Attention, "检查在继承树上覆盖父类方法的情况");

		if (isPrintNotOverrideMethod == null) {
			isPrintNotOverrideMethod = true;
		}

		if (isPrintOverrideMethod == null) {
			isPrintOverrideMethod = true;
		}
	}

	protected void doSearch(AnalysisResult result) throws JDependException {

		List<JavaClassTree> trees = (new JavaClassInheritTreeCreator()).create(JavaClassUnitUtil.getJavaClasses(result
				.getClasses()));

		for (JavaClassTree tree : trees) {
			overrideCheck(tree);
		}

	}

	private void overrideCheck(JavaClassTree tree) {
		for (JavaClassNode node : tree.getNodes()) {
			search(node.getJavaClass(), tree);
		}
		// 清空缓存
		methods = new ArrayList<Method>();

	}

	private void search(JavaClass javaClass, JavaClassTree tree) {
		boolean found;
		boolean override;
		ArrayList<Method> theMethods;
		ArrayList<Method> overrideMethods;

		Map<Method, ArrayList<Method>> printNotOverrideMethods = new LinkedHashMap<Method, ArrayList<Method>>();
		Map<Method, ArrayList<Method>> printOverrideMethods = new LinkedHashMap<Method, ArrayList<Method>>();
		// search
		for (Method currentMethod : getCheckingMethods(javaClass)) {
			found = false;
			override = false;
			overrideMethods = new ArrayList<Method>();
			theMethods = this.getMethods(currentMethod, tree);
			for (Method method : theMethods) {
				found = true;
				if (this.override(currentMethod, method)) {
					override = true;
					// 对于当前JavaClass，删除之前记录错误的未覆盖方法
					for (Method printMethod : printNotOverrideMethods.keySet()) {
						if (printMethod.getName().equals(currentMethod)) {
							printNotOverrideMethods.remove(printMethod);
							break;
						}
					}
					// 记录覆盖的方法
					if (isNoteOverrideMethod(method)) {
						overrideMethods.add(method);
					}
				}
			}
			if (!found) {
				this.methods.add(currentMethod);
			} else if (!override) {
				printNotOverrideMethods.put(currentMethod, theMethods);
			} else if (overrideMethods.size() > 0) {
				printOverrideMethods.put(currentMethod, overrideMethods);
			}
		}
		// print
		if (isPrintNotOverrideMethod) {
			for (Method printMethod : printNotOverrideMethods.keySet()) {
				this.print("JavaClass[" + printMethod.getJavaClass().getName() + "] Method[" + printMethod.getInfo()
						+ "] not Override Super Method:\n");
				for (Method method : printNotOverrideMethods.get(printMethod)) {
					this.printTab();
					this.print("JavaClass[" + method.getJavaClass().getName() + "] Method[" + method.getInfo() + "]\n");
				}
				this.print("\n");
			}
		}
		if (isPrintOverrideMethod) {
			for (Method printMethod : printOverrideMethods.keySet()) {
				this.print("JavaClass[" + printMethod.getJavaClass().getName() + "] Method[" + printMethod.getInfo()
						+ "] override Super Method:\n");
				for (Method method : printOverrideMethods.get(printMethod)) {
					this.printTab();
					this.print("JavaClass[" + method.getJavaClass().getName() + "] Method[" + method.getInfo() + "]\n");
				}
				this.print("\n");
			}
		}

		if (isPrintNotOverrideMethod) {
			for (Method printMethod : printNotOverrideMethods.keySet()) {
				for (Method method : printNotOverrideMethods.get(printMethod)) {
					this.printTable("Type", "NotOverride");
					this.printTable("JavaClass", printMethod.getJavaClass().getName());
					this.printTable("Method", printMethod.getInfo());
					this.printTable("SuperJavaClass", method.getJavaClass().getName());
					this.printTable("SuperMethod", method.getInfo());
				}
			}
		}
		if (isPrintOverrideMethod) {
			for (Method printMethod : printOverrideMethods.keySet()) {
				for (Method method : printOverrideMethods.get(printMethod)) {
					this.printTable("Type", "Override");
					this.printTable("JavaClass", printMethod.getJavaClass().getName());
					this.printTable("Method", printMethod.getInfo());
					this.printTable("SuperJavaClass", method.getJavaClass().getName());
					this.printTable("SuperMethod", method.getInfo());
				}

			}
		}

	}

	/**
	 * 得到可能需要覆盖的方法集合
	 * 
	 * @param name
	 * @return
	 */
	private ArrayList<Method> getMethods(Method currentMethod, JavaClassTree tree) {

		ArrayList<Method> theMethods = new ArrayList<Method>();

		for (Method method : this.methods) {
			if (method.getName().equals(currentMethod.getName())
					&& this.isSubJavaClass(currentMethod.getJavaClass(), method.getJavaClass(), tree)) {
				theMethods.add(method);
			}
		}
		return theMethods;
	}

	/**
	 * 判断current处于javaClass的下层
	 * 
	 * @param javaClass
	 * @param current
	 * @return
	 */
	private boolean isSubJavaClass(JavaClass current, JavaClass javaClass, JavaClassTree tree) {
		return this.getNode(current, tree).getLayer() > this.getNode(javaClass, tree).getLayer();
	}

	private Node getNode(JavaClass javaClass, JavaClassTree tree) {
		for (JavaClassNode node : tree.getNodes()) {
			if (node.getJavaClass().equals(javaClass)) {
				return node;
			}
		}
		return null;
	}

	private List<Method> getCheckingMethods(JavaClass javaClass) {
		List<Method> theMethods = new ArrayList<Method>();
		for (Method method : javaClass.getSelfMethods()) {
			if (!method.isConstruction()// 初始化方法 不检查
					&& !method.isPrivate()) {// 私有方法不检查
				theMethods.add(method);
			}
		}
		return theMethods;
	}

	private boolean override(Method subMethod, Method superMethod) {
		return subMethod.equals(superMethod);
	}

	private boolean isNoteOverrideMethod(Method method) {
		return !method.getJavaClass().isInterface() && method.getInfo().indexOf("abstract") == -1;
	}

	public Boolean getIsPrintNotOverrideMethod() {
		return isPrintNotOverrideMethod;
	}

	public void setIsPrintNotOverrideMethod(Boolean isPrintNotOverrideMethod) {
		this.isPrintNotOverrideMethod = isPrintNotOverrideMethod;
	}

	public Boolean getIsPrintOverrideMethod() {
		return isPrintOverrideMethod;
	}

	public void setIsPrintOverrideMethod(Boolean isPrintOverrideMethod) {
		this.isPrintOverrideMethod = isPrintOverrideMethod;
	}
}
