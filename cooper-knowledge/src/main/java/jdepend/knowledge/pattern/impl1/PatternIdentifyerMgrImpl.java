package jdepend.knowledge.pattern.impl1;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import jdepend.knowledge.pattern.AbstractPatternIdentifyerMgr;
import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.JavaClass;
import jdepend.model.result.AnalysisResult;

public final class PatternIdentifyerMgrImpl extends AbstractPatternIdentifyerMgr {

	private Map<String, PatternIdentifyer> identifyers;

	public PatternIdentifyerMgrImpl() {
		this.init();
	}

	protected Map<String, Collection<PatternInfo>> doIdentify(AnalysisResult result) {
		Collection<JavaClass> javaClasses = result.getClasses();
		Map<String, Collection<PatternInfo>> rtn = new LinkedHashMap<String, Collection<PatternInfo>>();
		Collection<PatternInfo> items;
		PatternIdentifyer identifyer;
		for (String identifyerName : identifyers.keySet()) {
			identifyer = identifyers.get(identifyerName);
			if (identifyer instanceof AbstractPatternIdentifyer) {
				((AbstractPatternIdentifyer) identifyer).setResult(result);
			}
			items = identifyer.identify(javaClasses);
			if (items != null && items.size() > 0) {
				rtn.put(identifyerName, items);
			} else {
				rtn.put(identifyerName, null);
			}
		}
		return rtn;
	}

	private void init() {
		identifyers = new LinkedHashMap<String, PatternIdentifyer>();
		identifyers.put("单例模式", new SingletonIdentifyer());
		identifyers.put("工厂模式", new FactoryMethodIdentifyer());
		identifyers.put("克隆模式", new PrototypeIdentifyer());
		identifyers.put("构建模式", new BuilderIdentifyer());
		identifyers.put("适配器Class模式", new AdapterClassIdentifyer());
		identifyers.put("组合模式", new CompositeIdentifyer());
		identifyers.put("装饰模式", new DecoratorIdentifyer());
		identifyers.put("门面模式", new FacadeIdentifyer());
		identifyers.put("享元模式", new FlyweightIdentifyer());
		identifyers.put("代理模式", new ProxyIdentifyer());
		identifyers.put("模板方法", new TemplateMethodIdentifyer());
		identifyers.put("策略模式", new StrategyIdentifyer());
		identifyers.put("状态模式", new StateIdentifyer());
		identifyers.put("观察者模式", new ObserverIdentifyer());
	}

	private Map<String, PatternIdentifyer> getIdentifyers() {
		return identifyers;
	}

	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("<strong>设计模式识别器</strong>是通过分析每种设计模式的特征，通过特征比对识别代码中使用设计模式的地方。<br><br>");

		Map<String, PatternIdentifyer> identifyers = this.getIdentifyers();
		for (String identifyerName : identifyers.keySet()) {
			explain.append(identifyers.get(identifyerName).getExplain());
		}

		return explain.toString();
	}
}
