package jdepend.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.file.AnalyzeData;
import jdepend.framework.file.TargetFileManager;
import jdepend.model.Component;
import jdepend.model.JavaPackage;
import jdepend.model.component.AptitudeComponent;
import jdepend.model.component.CustomComponent;
import jdepend.model.component.SimpleComponent;
import jdepend.model.component.judge.ComponentJudge;
import jdepend.model.component.judge.LayerComponentJudge;
import jdepend.model.component.judge.WisdomLayerComponentJudge;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.model.component.modelconf.JavaPackageComponentModelConf;
import junit.framework.TestCase;

public class JDependTest extends TestCase {

	private Collection<JavaPackage> javaPackages;

	@Override
	protected void setUp() throws Exception {

		Parse jdepend = new Parse();

		jdepend
				.addDirectorys("E:\\my_workspace\\project\\Cooper\\projects\\cooper\\cooper-parse\\testjar\\DataModel.jar");

		javaPackages = jdepend.execute();
	}

	public void testParse() throws IOException {

		System.out.println(javaPackages.size());

		assertTrue(javaPackages.size() > 0);
	}

	public void testParse1() throws IOException, JDependException {

		List<String> directories = new ArrayList<String>();
		directories.add("E:\\my_workspace\\project\\Cooper\\projects\\cooper\\cooper-parse\\testjar\\DataModel.jar");

		TargetFileManager fileManager = new TargetFileManager();
		for (String dir : directories) {
			fileManager.addDirectory(dir);
		}

		Parse jdepend = new Parse();

		AnalyzeData fileData = fileManager.getAnalyzeData();
		
		jdepend.setAnalyseData(fileData);

		Collection<JavaPackage> javaPackages = jdepend.execute();

		System.out.println(javaPackages.size());

		assertTrue(javaPackages.size() > 0);
	}

	public void testParse2() throws IOException, JDependException {

		CustomComponent component = new CustomComponent();

		Collection<Component> units = component.list(new ArrayList<JavaPackage>(javaPackages));

		System.out.println(units.size());

		assertTrue(units.size() > 0);
	}

	public void testParse3() throws IOException, JDependException {

		CustomComponent component = new CustomComponent();
		JavaPackageComponentModelConf conf = new JavaPackageComponentModelConf();
		conf.setName("big");

		List<String> packages = new ArrayList<String>();
		packages.add("com.neusoft.tax.common.datamodel.util");
		packages.add("com.neusoft.tax.common.datamodel.validator");
		packages.add("com.neusoft.tax.common.datamodel.spy");
		packages.add("com.neusoft.tax.common.datamodel.business");
		packages.add("com.neusoft.tax.common.datamodel.data");
		packages.add("com.neusoft.tax.common.datamodel.config.validator");
		packages.add("com.neusoft.tax.common.datamodel.config.domainObj");
		packages.add("com.neusoft.tax.common.datamodel.exception");
		packages.add("com.neusoft.tax.common.datamodel.version");

		conf.addComponentConf("tax", Component.UndefinedComponentLevel, packages);

		packages = new ArrayList<String>();
		packages.add("com.neusoft.drm.dataexpress");
		packages.add("com.neusoft.drm.common.metadata");

		conf.addComponentConf("drm", Component.UndefinedComponentLevel, packages);

		component.setComponentInfo(conf);

		Collection<Component> units = component.list(new ArrayList<JavaPackage>(javaPackages));

		System.out.println("CustomComponent：" + units.size());

		assertTrue(units.size() > 0);

	}

	public void testParse4() throws IOException, JDependException {

		SimpleComponent component = new SimpleComponent();

		List<String> componentNames = new ArrayList<String>();
		componentNames.add("com.neusoft.tax.common.datamodel");
		componentNames.add("com.neusoft.drm");
		component.setComponentNames(componentNames);

		Collection<Component> units = component.list(new ArrayList<JavaPackage>(javaPackages));

		System.out.println("SimpleComponent：" + units.size());

		assertTrue(units.size() > 0);

	}

	public void testParse5() throws IOException, JDependException {

		AptitudeComponent component = new AptitudeComponent();

		ComponentJudge judge = new LayerComponentJudge(4);
		component.addJudge(judge);
		Collection<Component> units = component.list(new ArrayList<JavaPackage>(javaPackages));

		System.out.println("AptitudeComponent（LayerComponentJudge）：" + units.size());

		assertTrue(units.size() > 0);

	}

	public void testParse6() throws IOException, JDependException {

		AptitudeComponent component = new AptitudeComponent();

		ComponentJudge judge = new WisdomLayerComponentJudge();
		component.addJudge(judge);
		Collection<Component> units = component.list(new ArrayList<JavaPackage>(javaPackages));

		System.out.println("AptitudeComponent（AutoLayerComponentJudge）：" + units.size());

		assertTrue(units.size() > 0);

	}

}
