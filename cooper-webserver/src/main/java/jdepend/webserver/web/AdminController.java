package jdepend.webserver.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.knowledge.database.AnalysisResultRepository;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.TableViewInfo;
import jdepend.model.util.TableViewUtil;
import jdepend.util.todolist.TODOItem;
import jdepend.util.todolist.TODOListIdentifyerFacade;
import jdepend.webserver.web.WebRelationGraphUtil.RelationGraphData;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "admin")
public class AdminController {

	private Logger logger = Logger.getLogger(AdminController.class);

	@RequiresPermissions("admin:list")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String admin(Model model, HttpServletRequest request) throws JDependException {
		return this.list(model, request);
	}

	@RequiresPermissions("admin:list")
	@RequestMapping(value = "/result/list", method = RequestMethod.GET)
	public String list(Model model, HttpServletRequest request) throws JDependException {
		model.addAttribute("resultSummrys",
				AnalysisResultRepository.getResultSummrys(WebConstants.DEFLAUT_GROUP, WebConstants.DEFLAUT_COMMAND));
		return "results";
	}

	@RequiresPermissions("admin:view")
	@RequestMapping(value = "/result/{id}/view", method = RequestMethod.GET)
	public String view(Model model, @PathVariable String id, HttpServletRequest request) throws JDependException {

		AnalysisResult result = AnalysisResultRepository.getResult(id);

		WebAnalysisResult webResult = new WebAnalysisResult(result);
		model.addAttribute("result", webResult);
		request.getSession().setAttribute(WebConstants.SESSION_RESULT, webResult);

		TODOListIdentifyerFacade identify = new TODOListIdentifyerFacade();
		List<TODOItem> todoList = identify.identify(result);
		model.addAttribute("todoList", todoList);

		List<TableViewInfo> tableInfos = TableViewUtil.view(result);
		model.addAttribute("tableList", tableInfos);

		RelationGraphData relationGraphData = WebRelationGraphUtil.getGraphData(result.getRelations());
		model.addAttribute("relation_graph_data", relationGraphData);

		return "result";
	}

	@RequiresPermissions("admin:delete")
	@RequestMapping(value = "result/delete.ajax", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> delete(@ModelAttribute("ids") String ids, HttpServletRequest request) throws JDependException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JSONArray idArray = new JSONArray(ids);
		for (int i = 0; i < idArray.length(); i++) {
			AnalysisResultRepository.delete(idArray.getString(i));
		}

		resultMap.put("code", "1");
		resultMap.put("msg", "删除成功！");

		return resultMap;
	}

	@RequiresPermissions("admin:download")
	@RequestMapping(value = "result/data/download", method = RequestMethod.GET)
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment; filename=knowledge.zip");

		BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());

		File knowledgeFile = new File(JDependContext.getWorkspacePath() + File.separator + "knowledge");
		zip(bos, knowledgeFile);

		bos.close();
	}

	private void zip(OutputStream os, File inputFile) throws Exception {
		ZipOutputStream out = new ZipOutputStream(os);
		zip(out, inputFile, "");
		out.close();
	}

	private void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}
}
