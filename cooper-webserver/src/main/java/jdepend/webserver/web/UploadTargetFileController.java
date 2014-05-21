package jdepend.webserver.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

@Controller
@RequestMapping(value = "analyse")
public class UploadTargetFileController {

	private Logger logger = Logger.getLogger(UploadTargetFileController.class);

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String upload() throws JDependException {
		logger.info("进入上传jar页面");
		return "upload";
	}

	/**
	 * 上传jar文件
	 * 
	 * @param fileUpload
	 *            文件名称
	 * @throws JDependException
	 */

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(@RequestParam("fileUpload") MultipartFile fileUpload, Model model, HttpServletRequest request)
			throws JDependException {
		if (!fileUpload.isEmpty()) {
			try {
				String fileName = fileUpload.getOriginalFilename();
				if (fileName.endsWith(".jar")) {
					byte[] fileData = fileUpload.getBytes();

					InputStream in = new ByteArrayInputStream(fileData);
					SearchUtil searchUtil = new SearchUtil(this.createAnalyseData(in));
					Collection<JavaPackage> innerJavaPackages = new ArrayList<JavaPackage>();
					for (JavaPackage javaPackage : searchUtil.getPackages()) {
						if (javaPackage.isInner()) {
							innerJavaPackages.add(javaPackage);
						}
					}
					List<JavaPackage> sortedInnerJavaPackages = new ArrayList<JavaPackage>(innerJavaPackages);
					Collections.sort(sortedInnerJavaPackages);
					model.addAttribute("listPackages", sortedInnerJavaPackages);
					in.close();

					request.getSession().setAttribute(WebConstants.SESSION_FILE_NAME, fileName);
					request.getSession().setAttribute(WebConstants.SESSION_FILE_DATA, fileData);

					logger.info("进入listPackages页面");

					return "listPackages";
				} else {
					throw new JDependException("上传的文件格式必须是jar");
				}

			} catch (IOException e) {
				e.printStackTrace();
				throw new JDependException(e.getMessage());
			}
		} else {
			throw new JDependException("上传的文件不能为空");
		}
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

		AnalyseData data = new AnalyseData();

		JarFileReader reader = new JarFileReader(true);
		Map<FileType, List<byte[]>> fileDatases = null;
		try {
			byte[] fileData = (byte[]) request.getSession().getAttribute(WebConstants.SESSION_FILE_DATA);
			InputStream in = new ByteArrayInputStream(fileData);
			fileDatases = reader.readDatas(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		data.setClasses(fileDatases.get(FileType.classType));
		data.setConfigs(fileDatases.get(FileType.xmlType));

		String fileName = (String) request.getSession().getAttribute(WebConstants.SESSION_FILE_NAME);
		Map<String, Collection<String>> targetFiles = new HashMap<String, Collection<String>>();
		targetFiles.put(fileName, reader.getEntryNames());

		data.setTargetFiles(targetFiles);

		proxy.setAnalyseData(data);

		proxy.setComponent(component);

		// 调用分析服务
		AnalysisResult result = proxy.analyze();
		result.getRunningContext().setPath(fileName);

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

	private ParseData createAnalyseData(InputStream in) {

		ParseData data = new ParseData();

		JarFileReader reader = new JarFileReader(true);
		Map<FileType, List<byte[]>> fileDatases = null;
		try {
			fileDatases = reader.readDatas(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		data.setClasses(fileDatases.get(FileType.classType));
		data.setConfigs(fileDatases.get(FileType.xmlType));

		return data;

	}

}
