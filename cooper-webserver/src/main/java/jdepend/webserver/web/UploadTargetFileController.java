package jdepend.webserver.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileType;
import jdepend.framework.util.JarFileReader;
import jdepend.model.JavaPackage;
import jdepend.parse.impl.AnalyseData;
import jdepend.parse.util.SearchUtil;

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
	public String upload(@RequestParam("fileUpload") MultipartFile fileUpload, Model model) throws JDependException {
		if (!fileUpload.isEmpty()) {
			try {
				String fileName = fileUpload.getOriginalFilename();
				if (fileName.endsWith(".jar")) {
					InputStream in = fileUpload.getInputStream();
					SearchUtil searchUtil = new SearchUtil(this.createAnalyseData(in));
					Collection<JavaPackage> innerJavaPackages = new ArrayList<JavaPackage>();
					for (JavaPackage javaPackage : searchUtil.getPackages()) {
						if (javaPackage.isInner()) {
							innerJavaPackages.add(javaPackage);
						}
					}
					model.addAttribute("listPackages", innerJavaPackages);
					in.close();

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
	public String upload(@ModelAttribute("componentModel") String componentModel, Model model) throws JDependException {
		return "result";
	}

	private AnalyseData createAnalyseData(InputStream in) {

		AnalyseData data = new AnalyseData();

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
