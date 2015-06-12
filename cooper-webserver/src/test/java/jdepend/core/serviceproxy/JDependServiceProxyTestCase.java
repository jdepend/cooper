package jdepend.core.serviceproxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jdepend.core.framework.serviceproxy.JDependServiceProxy;
import jdepend.core.framework.serviceproxy.JDependServiceProxyFactoryMgr;
import jdepend.framework.exception.JDependException;
import jdepend.framework.file.AnalyzeData;
import jdepend.framework.file.JarFileReader;
import jdepend.framework.file.TargetFileInfo;
import jdepend.metadata.util.ClassSearchUtil;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.util.SearchUtil;
import junit.framework.TestCase;

public class JDependServiceProxyTestCase extends TestCase {

	@Override
	protected void setUp() throws Exception {

		SearchUtil search = new SearchUtil();
		for (String path : ClassSearchUtil.getSelfPath()) {
			search.addPath(path);
		}
		ClassSearchUtil.getInstance().setClassList(search.getClasses());
	}

	public void testSetAnalyzeData() throws JDependException {

		JDependServiceProxy proxy = JDependServiceProxyFactoryMgr.getInstance().getFactory()
				.createJDependServiceProxy("无", "以包为单位输出分析报告");

		AnalyzeData data = new AnalyzeData();

		File jarFile = new File("C:\\dom4j-1.6.1.jar");

		JarFileReader reader = new JarFileReader(true);
		List<TargetFileInfo> fileDatases = null;
		try {
			InputStream in = new FileInputStream(jarFile);
			fileDatases = reader.readDatas(in);
			for (TargetFileInfo targetFileInfo : fileDatases) {
				data.addFileInfo(jarFile.getName(), targetFileInfo);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		proxy.setAnalyseData(data);
		// 调用分析服务
		AnalysisResult result = proxy.analyze();

		System.out.println(result);

	}

}
