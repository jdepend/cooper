package jdepend.webserver.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jdepend.core.serviceproxy.JDependServiceProxy;
import jdepend.core.serviceproxy.JDependServiceProxyFactory;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileType;
import jdepend.framework.util.JarFileReader;
import jdepend.model.JavaPackage;
import jdepend.model.component.CustomComponent;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.impl.ParseData;
import jdepend.parse.util.SearchUtil;
import jdepend.service.local.AnalyseData;

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

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String upload() throws JDependException {
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
					model.addAttribute("listPackages", innerJavaPackages);
					in.close();

					request.getSession().setAttribute(WebConstants.SESSION_FILE_NAME, fileName);
					request.getSession().setAttribute(WebConstants.SESSION_FILE_DATA, fileData);

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

		// request.getSession().removeAttribute(WebConstants.SESSION_FILE_DATA);

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
