package jdepend.webserver.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.core.serviceproxy.JDependServiceProxy;
import jdepend.core.serviceproxy.JDependServiceProxyFactory;
import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileType;
import jdepend.framework.util.JarFileReader;
import jdepend.knowledge.database.AnalysisResultRepository;
import jdepend.model.JavaPackage;
import jdepend.model.component.CustomComponent;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.util.SearchUtil;
import jdepend.service.local.AnalyseData;

import org.springframework.stereotype.Service;

@Service
public class AnalyseService {

	public Collection<JavaPackage> listPackages(AnalyseData analyseData) {

		SearchUtil searchUtil = new SearchUtil(analyseData.toParseData());
		Collection<JavaPackage> innerJavaPackages = new ArrayList<JavaPackage>();
		for (JavaPackage javaPackage : searchUtil.getPackages()) {
			if (javaPackage.isInner()) {
				innerJavaPackages.add(javaPackage);
			}
		}

		return innerJavaPackages;
	}

	public AnalysisResult analyze(AnalyseData data, ComponentModelConf componentModelConf) throws JDependException {

		CustomComponent component = new CustomComponent();
		component.setComponentInfo(componentModelConf);

		JDependServiceProxy proxy = new JDependServiceProxyFactory().getJDependServiceProxy("无", "以自定义组件为单位输出分析报告");

		proxy.setAnalyseData(data);

		proxy.setComponent(component);

		// 调用分析服务
		AnalysisResult result = proxy.analyze();
		result.getRunningContext().setPath(data.getPath());
		
		//保存分析结果
//		if (new PropertyConfigurator().isSaveResult()) {
//			AnalysisResultRepository.save(result);
//		}

		return result;
	}

	public AnalyseData createAnalyseData(Map<String, byte[]> fileDatas) {

		AnalyseData data = new AnalyseData();

		List<byte[]> classes = new ArrayList<byte[]>();
		List<byte[]> configs = new ArrayList<byte[]>();
		Map<String, Collection<String>> targetFiles = new LinkedHashMap<String, Collection<String>>();

		for (String fileName : fileDatas.keySet()) {
			byte[] fileData = fileDatas.get(fileName);
			JarFileReader reader = new JarFileReader(true);
			Map<FileType, List<byte[]>> fileDatases = null;
			try {
				InputStream in = new ByteArrayInputStream(fileData);
				fileDatases = reader.readDatas(in);
				targetFiles.put(fileName, reader.getEntryNames());
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			classes.addAll(fileDatases.get(FileType.classType));
			configs.addAll(fileDatases.get(FileType.xmlType));
		}

		data.setClasses(classes);
		data.setConfigs(configs);
		data.setTargetFiles(targetFiles);

		return data;
	}

}
