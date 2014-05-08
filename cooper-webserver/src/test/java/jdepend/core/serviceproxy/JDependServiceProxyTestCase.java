package jdepend.core.serviceproxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileType;
import jdepend.framework.util.JarFileReader;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.ClassSearchUtil;
import jdepend.parse.util.SearchUtil;
import jdepend.service.AnalyseDataDTO;
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
		
		
		JDependServiceProxy proxy = new JDependServiceProxyFactory()
				.getJDependServiceProxy("无", "以包为单位输出分析报告");

		AnalyseDataDTO data = new AnalyseDataDTO();

		File jarFile = new File("C:\\dom4j-1.6.1.jar");

		JarFileReader reader = new JarFileReader(true);
		Map<FileType, List<byte[]>> fileDatases = null;
		try {
			InputStream in = new FileInputStream(jarFile);
			fileDatases = reader.readDatas(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		data.setClasses(fileDatases.get(FileType.classType));
		data.setConfigs(fileDatases.get(FileType.xmlType));

		Map<String, Collection<String>> targetFiles = new HashMap<String, Collection<String>>();
		targetFiles.put("dom4j-1.6.1.jar", reader.getEntryNames());

		data.setTargetFiles(targetFiles);

		proxy.setAnalyzeData(data);

		// 调用分析服务
		AnalysisResult result = proxy.analyze();

		System.out.println(result);

	}

}
