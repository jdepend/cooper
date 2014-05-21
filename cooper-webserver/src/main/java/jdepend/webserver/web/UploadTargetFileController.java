package jdepend.webserver.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdepend.core.serviceproxy.JDependServiceProxy;
import jdepend.core.serviceproxy.JDependServiceProxyFactory;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileType;
import jdepend.framework.util.JarFileReader;
import jdepend.knowledge.architectpattern.ArchitectPatternMgr;
import jdepend.knowledge.architectpattern.ArchitectPatternResult;
import jdepend.model.JavaPackage;
import jdepend.model.Measurable;
import jdepend.model.Relation;
import jdepend.model.component.CustomComponent;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.RelationByMetricsComparator;
import jdepend.model.util.TableViewInfo;
import jdepend.model.util.TableViewUtil;
import jdepend.parse.impl.Constant;
import jdepend.parse.impl.ParseData;
import jdepend.parse.util.SearchUtil;
import jdepend.service.local.AnalyseData;
import jdepend.util.todolist.TODOItem;
import jdepend.util.todolist.TODOListIdentify;
import jdepend.webserver.web.WebRelationGraphUtil.RelationGraphData;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping(value = "analyse")
public class UploadTargetFileController {

	private Logger logger = Logger.getLogger(UploadTargetFileController.class);

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String upload() throws JDependException {
		logger.info("进入上传jar页面");
		return "upload";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload2(MultipartHttpServletRequest multipartRequest, Model model, HttpServletRequest request)
			throws Exception {
		// 获取多个fileData
		Map<String, byte[]> fileDatas = new LinkedHashMap<String, byte[]>();
		for (Iterator<String> it = multipartRequest.getFileNames(); it.hasNext();) {
			String key = (String) it.next();
			MultipartFile file = multipartRequest.getFile(key);
			try {
				String fileName = file.getOriginalFilename();
				if (fileName.endsWith(".jar")) {
					byte[] fileData = file.getBytes();
					fileDatas.put(fileName, fileData);
				} else {
					throw new JDependException("上传的文件格式必须是jar");
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new JDependException(e.getMessage());
			}
		}
		AnalyseData analyseData = this.createAnalyseData(fileDatas);
		SearchUtil searchUtil = new SearchUtil(analyseData.toParseData());
		Collection<JavaPackage> innerJavaPackages = new ArrayList<JavaPackage>();
		for (JavaPackage javaPackage : searchUtil.getPackages()) {
			if (javaPackage.isInner()) {
				innerJavaPackages.add(javaPackage);
			}
		}
		List<JavaPackage> sortedInnerJavaPackages = new ArrayList<JavaPackage>(innerJavaPackages);
		Collections.sort(sortedInnerJavaPackages);
		model.addAttribute("listPackages", sortedInnerJavaPackages);

		request.getSession().setAttribute(WebConstants.SESSION_FILE, analyseData);

		logger.info("进入listPackages页面");

		return "listPackages";

	}

	@RequestMapping(value = "/execute", method = RequestMethod.POST)
	public String upload(@ModelAttribute("componentModel") String componentModel, Model model,
			HttpServletRequest request) throws JDependException {

		ComponentModelConf componentModelConf = new ComponentModelConf();
		JSONObject componentModelObject = new JSONObject(componentModel);
		for (Object componentName : componentModelObject.keySet()) {
			JSONArray packageList = (JSONArray) componentModelObject.get((String) componentName);
			List<String> packageNames = new ArrayList<String>();
			for (int i = 0; i < packageList.length(); i++) {
				packageNames.add(packageList.getString(i));
			}
			componentModelConf.addComponentConf((String) componentName, 0, packageNames);
		}

		CustomComponent component = new CustomComponent();
		component.setComponentInfo(componentModelConf);

		JDependServiceProxy proxy = new JDependServiceProxyFactory().getJDependServiceProxy("无", "以自定义组件为单位输出分析报告");

		AnalyseData data = (AnalyseData) request.getSession().getAttribute(WebConstants.SESSION_FILE);

		proxy.setAnalyseData(data);

		proxy.setComponent(component);

		// 调用分析服务
		AnalysisResult result = proxy.analyze();
		result.getRunningContext().setPath(data.getPath());

		WebAnalysisResult webResult = new WebAnalysisResult(result);
		model.addAttribute("result", webResult);
		request.getSession().setAttribute(WebConstants.SESSION_RESULT, webResult);

		List<Measurable> summarys = new ArrayList<Measurable>(result.getComponents());
		summarys.add(result.getSummary());
		model.addAttribute("summarys", summarys);
		// temp
		request.getSession().setAttribute("summarys", summarys);

		List<Relation> relations = new ArrayList<Relation>(result.getRelations());
		Collections.sort(relations, new RelationByMetricsComparator(Relation.AttentionLevel, false));
		model.addAttribute("relations", relations);
		// temp
		request.getSession().setAttribute("relations", relations);

		TODOListIdentify identify = new TODOListIdentify();
		List<TODOItem> todoList = identify.identify(result);
		model.addAttribute("todoList", todoList);
		// temp
		request.getSession().setAttribute("todoList", todoList);

		List<TableViewInfo> tableInfos = TableViewUtil.view(result);
		model.addAttribute("tableList", tableInfos);
		// temp
		request.getSession().setAttribute("tableList", tableInfos);

		RelationGraphData relationGraphData = WebRelationGraphUtil.getGraphData(result.getRelations());
		model.addAttribute("relation_graph_data", relationGraphData);
		// temp
		request.getSession().setAttribute("relation_graph_data", relationGraphData);

		ArchitectPatternResult apResult = null;
		try {
			apResult = ArchitectPatternMgr.getInstance().identify(result);
		} catch (JDependException e) {
			e.printStackTrace();
		}
		if (apResult != null) {
			String apResultInfo = apResult.getResult();
			model.addAttribute("structure_tip", apResultInfo);
			// temp
			request.getSession().setAttribute("structure_tip", apResultInfo);
		}

		// request.getSession().removeAttribute(WebConstants.SESSION_FILE_DATA);

		logger.info("进入result页面");

		return "result";
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Model model, HttpServletRequest request) throws JDependException {
		model.addAttribute("result", request.getSession().getAttribute(WebConstants.SESSION_RESULT));
		model.addAttribute("todoList", request.getSession().getAttribute("todoList"));
		model.addAttribute("tableList", request.getSession().getAttribute("tableList"));
		model.addAttribute("relation_graph_data", request.getSession().getAttribute("relation_graph_data"));
		model.addAttribute("summarys", request.getSession().getAttribute("summarys"));
		model.addAttribute("relations", request.getSession().getAttribute("relations"));
		model.addAttribute("structure_tip", request.getSession().getAttribute("structure_tip"));

		return "result";
	}

	private AnalyseData createAnalyseData(Map<String, byte[]> fileDatas) {
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
