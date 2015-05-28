package jdepend.webserver.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jdepend.framework.exception.JDependException;
import jdepend.framework.file.AnalyzeData;
import jdepend.metadata.JavaPackage;
import jdepend.model.component.modelconf.JavaPackageComponentModelConf;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.TableViewInfo;
import jdepend.model.util.TableViewUtil;
import jdepend.util.todolist.TODOItem;
import jdepend.util.todolist.TODOListIdentifyerFacade;
import jdepend.webserver.model.WebAnalysisResult;
import jdepend.webserver.service.AnalyseService;
import jdepend.webserver.web.WebRelationGraphUtil.RelationGraphData;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "analyse")
public class AnalyseController {

	private Logger logger = Logger.getLogger(AnalyseController.class);

	@Autowired
	private AnalyseService analyseService;

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String upload(HttpServletRequest request) throws JDependException {
		logger.info(request.getRemoteAddr() + " enter into upload jar page");
		return "upload";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(@RequestParam("files") List<MultipartFile> files, Model model, HttpServletRequest request)
			throws Exception {
		// 获取多个fileData
		Map<String, byte[]> fileDatas = new LinkedHashMap<String, byte[]>();
		for (MultipartFile file : files) {
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

		AnalyzeData analyseData = null;
		try {
			analyseData = analyseService.createAnalyseData(fileDatas);
		} catch (IllegalArgumentException e) {
			throw new JDependException("上传的jar文件中存在中文文件名", e);
		}
		List<JavaPackage> sortedInnerJavaPackages = new ArrayList<JavaPackage>(analyseService.listPackages(analyseData));
		Collections.sort(sortedInnerJavaPackages);
		model.addAttribute("listPackages", sortedInnerJavaPackages);
		model.addAttribute("analysePath", analyseData.getPath());

		request.getSession().setAttribute(WebConstants.SESSION_FILE, analyseData);

		logger.info(request.getRemoteAddr() + " enter into upload listPackages page");

		return "listPackages";

	}

	@RequestMapping(value = "/execute", method = RequestMethod.POST)
	public String execute(@ModelAttribute("componentModel") String componentModel, Model model,
			HttpServletRequest request) throws JDependException {

		JavaPackageComponentModelConf componentModelConf = new JavaPackageComponentModelConf();
		JSONObject componentModelObject = new JSONObject(componentModel);
		for (Object componentName : componentModelObject.keySet()) {
			JSONArray packageList = (JSONArray) componentModelObject.get((String) componentName);
			List<String> packageNames = new ArrayList<String>();
			for (int i = 0; i < packageList.length(); i++) {
				packageNames.add(packageList.getString(i));
			}
			componentModelConf.addComponentConf((String) componentName, 0, packageNames);
		}

		if (componentModelConf.getComponentConfs().size() == 0) {
			throw new JDependException("没有配置组件模型信息。");
		}

		AnalyzeData data = (AnalyzeData) request.getSession().getAttribute(WebConstants.SESSION_FILE);
		if (data == null) {
			throw new JDependException("Session 中不存在 AnalyseData");
		}

		AnalysisResult result = this.analyseService.analyze(WebConstants.DEFLAUT_GROUP, WebConstants.DEFLAUT_COMMAND,
				data, componentModelConf);

		WebAnalysisResult webResult = new WebAnalysisResult(result);
		model.addAttribute("result", webResult);
		request.getSession().setAttribute(WebConstants.SESSION_RESULT, webResult);

		TODOListIdentifyerFacade identify = new TODOListIdentifyerFacade();
		List<TODOItem> todoList = identify.identify(result);
		model.addAttribute("todoList", todoList);
		
		request.getSession().setAttribute(WebConstants.SESSION_RESULT_TODOLIST, todoList);

		List<TableViewInfo> tableInfos = TableViewUtil.view(result);
		model.addAttribute("tableList", tableInfos);
		// temp
		request.getSession().setAttribute("tableList", tableInfos);

		RelationGraphData relationGraphData = WebRelationGraphUtil.getGraphData(result.getRelations());
		model.addAttribute("relation_graph_data", relationGraphData);
		// temp
		request.getSession().setAttribute("relation_graph_data", relationGraphData);

		request.getSession().removeAttribute(WebConstants.SESSION_FILE);

		logger.info(request.getRemoteAddr() + " enter into upload result page");

		return "result";
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Model model, HttpServletRequest request) throws JDependException {
		model.addAttribute("result", request.getSession().getAttribute(WebConstants.SESSION_RESULT));
		model.addAttribute("todoList", request.getSession().getAttribute(WebConstants.SESSION_RESULT_TODOLIST));
		model.addAttribute("tableList", request.getSession().getAttribute("tableList"));
		model.addAttribute("relation_graph_data", request.getSession().getAttribute("relation_graph_data"));

		return "result";
	}
}
