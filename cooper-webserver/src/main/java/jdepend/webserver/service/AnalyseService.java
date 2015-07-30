package jdepend.webserver.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jdepend.core.serviceproxy.framework.JDependServiceProxy;
import jdepend.core.serviceproxy.framework.JDependServiceProxyFactoryMgr;
import jdepend.framework.exception.JDependException;
import jdepend.framework.file.AnalyzeData;
import jdepend.framework.file.JarFileReader;
import jdepend.framework.file.TargetFileInfo;
import jdepend.knowledge.database.AnalysisResultRepository;
import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.component.CustomComponent;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.model.result.AnalysisResult;

import org.springframework.stereotype.Service;

@Service
public class AnalyseService {

	public Collection<JavaPackage> listPackages(AnalyzeData analyseData) throws JDependException {

		JDependServiceProxy serviceProxy = JDependServiceProxyFactoryMgr.getInstance().getFactory()
				.createJDependServiceProxy(null, null);

		serviceProxy.setAnalyseData(analyseData);

		Collection<JavaPackage> innerJavaPackages = new ArrayList<JavaPackage>();
		for (JavaPackage javaPackage : serviceProxy.getPackages()) {
			if (javaPackage.isInner()) {
				innerJavaPackages.add(javaPackage);
			}
		}

		return innerJavaPackages;
	}

	public AnalysisResult analyze(String group, String command, AnalyzeData data, Component component)
			throws JDependException {
		JDependServiceProxy proxy = JDependServiceProxyFactoryMgr.getInstance().getFactory()
				.createJDependServiceProxy(group, command);

		proxy.setAnalyseData(data);

		proxy.setComponent(component);

		// 调用分析服务
		AnalysisResult result = proxy.analyze();
		result.getRunningContext().setPath(data.getPath());

		// 保存分析结果
		AnalysisResultRepository.getInstance().save(result);

		return result;
	}

	public AnalyzeData createAnalyseData(Map<String, byte[]> fileDatas) {

		AnalyzeData data = new AnalyzeData();

		for (String fileName : fileDatas.keySet()) {
			byte[] fileData = fileDatas.get(fileName);
			JarFileReader reader = new JarFileReader(true);
			try {
				InputStream in = new ByteArrayInputStream(fileData);
				List<TargetFileInfo> targetFileInfos = reader.readDatas(in);
				for (TargetFileInfo targetFileInfo : targetFileInfos) {
					data.addFileInfo(fileName, targetFileInfo);
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return data;
	}

}
