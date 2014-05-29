package jdepend.webserver.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jdepend.framework.exception.JDependException;
import jdepend.knowledge.database.AnalysisResultRepository;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.TableViewInfo;
import jdepend.model.util.TableViewUtil;
import jdepend.util.todolist.TODOItem;
import jdepend.util.todolist.TODOListIdentify;
import jdepend.webserver.web.WebRelationGraphUtil.RelationGraphData;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "admin")
public class AdminController {

	private Logger logger = Logger.getLogger(AdminController.class);

	@RequestMapping(value = "/result/list", method = RequestMethod.GET)
	public String upload(Model model, HttpServletRequest request) throws JDependException {
		model.addAttribute("resultSummrys",
				AnalysisResultRepository.getResultSummrys(WebConstants.DEFLAUT_GROUP, WebConstants.DEFLAUT_COMMAND));
		return "results";
	}

	@RequestMapping(value = "/result/{id}/view", method = RequestMethod.GET)
	public String view(Model model, @PathVariable String id) throws JDependException {

		AnalysisResult result = AnalysisResultRepository.getResult(id);

		WebAnalysisResult webResult = new WebAnalysisResult(result);
		model.addAttribute("result", webResult);

		TODOListIdentify identify = new TODOListIdentify();
		List<TODOItem> todoList = identify.identify(result);
		model.addAttribute("todoList", todoList);

		List<TableViewInfo> tableInfos = TableViewUtil.view(result);
		model.addAttribute("tableList", tableInfos);

		RelationGraphData relationGraphData = WebRelationGraphUtil.getGraphData(result.getRelations());
		model.addAttribute("relation_graph_data", relationGraphData);

		return "result";
	}

}
