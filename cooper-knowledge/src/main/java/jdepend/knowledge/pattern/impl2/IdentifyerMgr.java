package jdepend.knowledge.pattern.impl2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.knowledge.pattern.AbstractPatternIdentifyerMgr;
import jdepend.knowledge.pattern.PatternInfo;
import jdepend.knowledge.pattern.impl2.feature.AdapterClassFeature;
import jdepend.knowledge.pattern.impl2.feature.BuilderFeature;
import jdepend.knowledge.pattern.impl2.feature.DecoratorFeature;
import jdepend.knowledge.pattern.impl2.feature.FacadeFeature;
import jdepend.knowledge.pattern.impl2.feature.FactoryMethodFeature;
import jdepend.knowledge.pattern.impl2.feature.FlyweightFeature;
import jdepend.knowledge.pattern.impl2.feature.ObserverFeature;
import jdepend.knowledge.pattern.impl2.feature.PrototypeFeature;
import jdepend.knowledge.pattern.impl2.feature.ProxyFeature;
import jdepend.knowledge.pattern.impl2.feature.SingletonFeature;
import jdepend.knowledge.pattern.impl2.feature.StateFeature;
import jdepend.knowledge.pattern.impl2.feature.StrategyFeature;
import jdepend.knowledge.pattern.impl2.feature.TemplateMethodFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.AbstractAttributeFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.AbstractAttributeHaveSubClassesFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.AbstractAttributeIsSelfSuperFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.AbstractClassFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.AbstractMethodFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.CollectionAttributeFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.CollectionAttributeHaveSubClassesFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.CollectionAttributeIsSelfSuperFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.ConstructorArgIsSuperFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.HaveSuperFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.NotEnumTypeFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.OverrideMethodFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.OverrideMethodReturnIsSuperFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.PrivateConstructorFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.SelfHaveStateFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.StaticAttributeFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.StaticMethodFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.SubClassesFeature;
import jdepend.knowledge.pattern.impl2.feature.leaf.SuperHaveOtherSubClassFeature;
import jdepend.model.JavaClass;
import jdepend.model.result.AnalysisResult;

public class IdentifyerMgr extends AbstractPatternIdentifyerMgr {

	private List<Feature> features = new ArrayList<Feature>();

	private List<Identifyer> identifyers = new ArrayList<Identifyer>();

	public IdentifyerMgr() {
		// 创建识别器
		Identifyer singletonIdentifyer = new IdentifyerImpl("单例模式");
		identifyers.add(singletonIdentifyer);

		Identifyer factoryMethodIdentifyer = new IdentifyerImpl("工厂模式");
		identifyers.add(factoryMethodIdentifyer);

		Identifyer prototypeIdentifyer = new IdentifyerImpl("克隆模式");
		identifyers.add(prototypeIdentifyer);

		Identifyer builderIdentifyer = new IdentifyerImpl("构建模式");
		identifyers.add(builderIdentifyer);

		Identifyer compositeIdentifyer = new IdentifyerImpl("组合模式");
		identifyers.add(compositeIdentifyer);

		Identifyer adapterClassIdentifyer = new IdentifyerImpl("适配器Class模式");
		identifyers.add(adapterClassIdentifyer);

		Identifyer decoratorIdentifyer = new IdentifyerImpl("装饰器模式");
		identifyers.add(decoratorIdentifyer);

		Identifyer facadeIdentifyer = new IdentifyerImpl("门面模式");
		identifyers.add(facadeIdentifyer);

		Identifyer flyweightIdentifyer = new IdentifyerImpl("享元模式");
		identifyers.add(flyweightIdentifyer);

		Identifyer proxyIdentifyer = new IdentifyerImpl("代理模式");
		identifyers.add(proxyIdentifyer);

		Identifyer templateMethodIdentifyer = new IdentifyerImpl("模板方法");
		identifyers.add(templateMethodIdentifyer);

		Identifyer strategyIdentifyer = new IdentifyerImpl("策略模式");
		identifyers.add(strategyIdentifyer);

		Identifyer stateIdentifyer = new IdentifyerImpl("状态模式");
		identifyers.add(stateIdentifyer);
		
		Identifyer observerIdentifyer = new IdentifyerImpl("观察者模式");
		identifyers.add(observerIdentifyer);

		// 创建特征集合
		Feature feature;

		feature = new NotEnumTypeFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(singletonIdentifyer);

		this.features.add(feature);

		feature = new AbstractClassFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(templateMethodIdentifyer);
		feature.addIdentifyer(strategyIdentifyer);
		feature.addIdentifyer(stateIdentifyer);

		this.features.add(feature);

		feature = new HaveSuperFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(compositeIdentifyer);
		feature.addIdentifyer(factoryMethodIdentifyer);
		feature.addIdentifyer(prototypeIdentifyer);
		feature.addIdentifyer(adapterClassIdentifyer);
		feature.addIdentifyer(decoratorIdentifyer);
		feature.addIdentifyer(proxyIdentifyer);

		this.features.add(feature);

		feature = new SuperHaveOtherSubClassFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(compositeIdentifyer);
		feature.addIdentifyer(decoratorIdentifyer);
		feature.addIdentifyer(proxyIdentifyer);

		this.features.add(feature);

		feature = new SubClassesFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(templateMethodIdentifyer);
		feature.addIdentifyer(strategyIdentifyer);
		feature.addIdentifyer(stateIdentifyer);

		this.features.add(feature);

		feature = new SelfHaveStateFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(stateIdentifyer);

		this.features.add(feature);

		feature = new CollectionAttributeFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(compositeIdentifyer);
		feature.addIdentifyer(flyweightIdentifyer);

		this.features.add(feature);

		feature = new CollectionAttributeIsSelfSuperFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(compositeIdentifyer);

		this.features.add(feature);

		feature = new CollectionAttributeHaveSubClassesFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(flyweightIdentifyer);

		this.features.add(feature);

		feature = new StaticAttributeFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(singletonIdentifyer);

		this.features.add(feature);

		feature = new AbstractAttributeFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(builderIdentifyer);
		feature.addIdentifyer(decoratorIdentifyer);
		feature.addIdentifyer(observerIdentifyer);

		this.features.add(feature);

		feature = new AbstractAttributeHaveSubClassesFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(builderIdentifyer);
		feature.addIdentifyer(decoratorIdentifyer);

		this.features.add(feature);

		feature = new AbstractAttributeIsSelfSuperFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(decoratorIdentifyer);

		this.features.add(feature);

		feature = new PrivateConstructorFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(singletonIdentifyer);

		this.features.add(feature);

		feature = new ConstructorArgIsSuperFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(decoratorIdentifyer);

		this.features.add(feature);

		feature = new StaticMethodFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(singletonIdentifyer);

		this.features.add(feature);

		feature = new OverrideMethodFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(factoryMethodIdentifyer);
		feature.addIdentifyer(prototypeIdentifyer);
		feature.addIdentifyer(decoratorIdentifyer);
		feature.addIdentifyer(proxyIdentifyer);

		this.features.add(feature);

		feature = new OverrideMethodReturnIsSuperFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(factoryMethodIdentifyer);
		feature.addIdentifyer(prototypeIdentifyer);

		this.features.add(feature);

		feature = new AbstractMethodFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(templateMethodIdentifyer);
		feature.addIdentifyer(strategyIdentifyer);
		feature.addIdentifyer(stateIdentifyer);

		this.features.add(feature);

		feature = new SingletonFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(singletonIdentifyer);

		this.features.add(feature);

		feature = new FactoryMethodFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(factoryMethodIdentifyer);

		this.features.add(feature);

		feature = new PrototypeFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(prototypeIdentifyer);

		this.features.add(feature);

		feature = new BuilderFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(builderIdentifyer);

		this.features.add(feature);

		feature = new AdapterClassFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(adapterClassIdentifyer);

		this.features.add(feature);

		feature = new DecoratorFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(decoratorIdentifyer);

		this.features.add(feature);

		feature = new FacadeFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(facadeIdentifyer);

		this.features.add(feature);

		feature = new FlyweightFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(flyweightIdentifyer);

		this.features.add(feature);

		feature = new ProxyFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(proxyIdentifyer);

		this.features.add(feature);

		feature = new TemplateMethodFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(templateMethodIdentifyer);

		this.features.add(feature);

		feature = new StrategyFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(strategyIdentifyer);

		this.features.add(feature);

		feature = new StateFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(stateIdentifyer);

		this.features.add(feature);
		
		feature = new ObserverFeature();
		// 将识别器注册到特征中
		feature.addIdentifyer(observerIdentifyer);

		this.features.add(feature);

	}

	protected Map<String, Collection<PatternInfo>> doIdentify(AnalysisResult result) {

		FeatureCheckContext context;
		for (JavaClass javaClass : result.getClasses()) {
			context = new FeatureCheckContext(javaClass);
			for (Feature feature : this.features) {
				feature.check(context);
			}
		}
		Map<String, Collection<PatternInfo>> results = new LinkedHashMap<String, Collection<PatternInfo>>();
		Collection<PatternInfo> patternInfos;

		for (Identifyer identifyer : this.identifyers) {
			patternInfos = new ArrayList<PatternInfo>();
			Map<JavaClass, String> resultData = identifyer.getResult();
			for (JavaClass javaClass : resultData.keySet()) {
				patternInfos.add(new PatternInfo(javaClass, resultData.get(javaClass)));
			}
			results.put(identifyer.getName(), patternInfos);
		}

		this.clear();

		return results;
	}

	private void clear() {
		for (Identifyer identifyer : this.identifyers) {
			identifyer.clear();
		}
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("<strong>设计模式识别器</strong>是通过分析每种设计模式的特征，通过特征比对识别代码中使用设计模式的地方。<br><br>");

		for (Identifyer Identifyer : identifyers) {
			explain.append(Identifyer.getExplain());
		}

		return explain.toString();
	}
}
