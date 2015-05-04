package jdepend.util.analyzer.framework;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.util.analyzer.element.ActionFormOutWebLayer;
import jdepend.util.analyzer.element.ActionHaveState;
import jdepend.util.analyzer.element.ArgumentCount;
import jdepend.util.analyzer.element.CheckButterflyObject;
import jdepend.util.analyzer.element.CheckFissileObject;
import jdepend.util.analyzer.element.ClassLineCountAnalyzer;
import jdepend.util.analyzer.element.ComponentInterfaceCheck;
import jdepend.util.analyzer.element.DAOCallService;
import jdepend.util.analyzer.element.DIPPrinciple;
import jdepend.util.analyzer.element.DesignPatternIdentifyer;
import jdepend.util.analyzer.element.FieldTreeAnalyse;
import jdepend.util.analyzer.element.IdentifyAppService;
import jdepend.util.analyzer.element.IdentifyCallback;
import jdepend.util.analyzer.element.IdentifyDomainService;
import jdepend.util.analyzer.element.IdentifyJavaClassType;
import jdepend.util.analyzer.element.IdentifyWillMoveJavaClass;
import jdepend.util.analyzer.element.IdentifyWillMoveMethod;
import jdepend.util.analyzer.element.InheritTreeAnalyse;
import jdepend.util.analyzer.element.InvokeCount;
import jdepend.util.analyzer.element.JavaClassBalanceSort;
import jdepend.util.analyzer.element.JavaClassRelationView;
import jdepend.util.analyzer.element.JavaClassView;
import jdepend.util.analyzer.element.KeywordSearch;
import jdepend.util.analyzer.element.LSPPrinciple;
import jdepend.util.analyzer.element.OverrideCheck;
import jdepend.util.analyzer.element.PackagesViewer;
import jdepend.util.analyzer.element.RepeatClassAnalyzer;
import jdepend.util.analyzer.element.SearchDAONoPageMethod;
import jdepend.util.analyzer.element.ServiceHaveState;
import jdepend.util.analyzer.element.TableView;
import jdepend.util.analyzer.element.TogetherInvokeMethod;
import jdepend.util.analyzer.element.TransactionalAnnotationChecker;

public final class Analyzers {

	public static Map<String, List<Analyzer>> getStaticAnalyzers() {
		Map<String, List<Analyzer>> analyzers = new LinkedHashMap<String, List<Analyzer>>();

		List<Analyzer> allAnalyzers = new ArrayList<Analyzer>();

		allAnalyzers.add(new ActionFormOutWebLayer());
		allAnalyzers.add(new ActionHaveState());
		allAnalyzers.add(new ArgumentCount());
		allAnalyzers.add(new CheckButterflyObject());
		allAnalyzers.add(new CheckFissileObject());
		allAnalyzers.add(new ClassLineCountAnalyzer());
		allAnalyzers.add(new ComponentInterfaceCheck());
		allAnalyzers.add(new DAOCallService());
		allAnalyzers.add(new DesignPatternIdentifyer());
		allAnalyzers.add(new DIPPrinciple());
		allAnalyzers.add(new LSPPrinciple());
		allAnalyzers.add(new FieldTreeAnalyse());
		allAnalyzers.add(new IdentifyWillMoveJavaClass());
		allAnalyzers.add(new IdentifyWillMoveMethod());
		allAnalyzers.add(new InheritTreeAnalyse());
		allAnalyzers.add(new InvokeCount());
		allAnalyzers.add(new JavaClassBalanceSort());
		allAnalyzers.add(new JavaClassRelationView());
		allAnalyzers.add(new JavaClassView());
		allAnalyzers.add(new KeywordSearch());
		allAnalyzers.add(new OverrideCheck());
		allAnalyzers.add(new PackagesViewer());
		allAnalyzers.add(new SearchDAONoPageMethod());
		allAnalyzers.add(new ServiceHaveState());
		allAnalyzers.add(new TableView());
		allAnalyzers.add(new TransactionalAnnotationChecker());
		allAnalyzers.add(new RepeatClassAnalyzer());
		allAnalyzers.add(new TogetherInvokeMethod());
		allAnalyzers.add(new IdentifyCallback());
		allAnalyzers.add(new IdentifyAppService());
		allAnalyzers.add(new IdentifyDomainService());
		allAnalyzers.add(new IdentifyJavaClassType());

		List<Analyzer> analyzerTypes;
		for (Analyzer analyzer : allAnalyzers) {
			analyzerTypes = analyzers.get(analyzer.getType());
			if (analyzerTypes == null) {
				analyzerTypes = new ArrayList<Analyzer>();
				analyzers.put(analyzer.getType(), analyzerTypes);
			}
			analyzerTypes.add(analyzer);
		}
		return analyzers;
	}

}
