package jdepend.webserver.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdepend.core.serviceproxy.JDependServiceProxy;
import jdepend.core.serviceproxy.JDependServiceProxyFactory;
import jdepend.framework.exception.JDependException;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaPackage;
import jdepend.model.Relation;
import jdepend.model.component.SimpleComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisResultSummary;
import jdepend.parse.util.SearchUtil;
import jdepend.report.way.htmlui.HTMLListPackagePrinter;
import jdepend.report.way.htmlui.HTMLRelationPrinter;
import jdepend.report.way.htmlui.HTMLSummaryPrinter;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public final class AnalyseServlet extends HttpServlet {

	private ThreadLocal<String> fileName = new ThreadLocal<String>();
	private ThreadLocal<String> pathName = new ThreadLocal<String>();
	private ThreadLocal<String> componentInfo = new ThreadLocal<String>();

	private static final int UploadSuccess = 1;
	private static final int UploadFailure = -1;

	private static final String analyse = "analyse";
	private static final String listPackage = "listPackage";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String command = req.getParameter("command");
		if (command.equals(analyse)) {
			if (this.upload(req, resp) == UploadSuccess) {
				this.analyze(req, resp);
				this.deleteUploadTempFile();
			}
		} else if (command.equals(listPackage)) {
			if (this.upload(req, resp) == UploadSuccess) {
				this.listJavaPackage(req, resp);
				this.deleteUploadTempFile();
			}
		}

	}

	private void listJavaPackage(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		List<String> paths = new ArrayList<String>();
		paths.add(fileName.get());
		SearchUtil searchUtil = new SearchUtil(paths);
		searchUtil.setBuildClassRelation(false);
		List<JavaPackage> javaPackages = new ArrayList<JavaPackage>(searchUtil.getPackages());
		Collections.sort(javaPackages);

		HTMLListPackagePrinter printer = new HTMLListPackagePrinter();
		OutputStream stream = new ByteArrayOutputStream();
		printer.setStream(stream);
		printer.printPackageList(javaPackages);
		String detailText = stream.toString();
		stream.close();

		req.setAttribute("listPackageData", detailText);
		// 去结果页
		req.getRequestDispatcher("WEB-INF/pages/listPackage.jsp").forward(req, resp);

	}

	private void deleteUploadTempFile() {
		(new File(fileName.get())).delete();
	}

	private void analyze(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 设置组件信息
		SimpleComponent simpleComponent = null;
		if (componentInfo.get() != null && componentInfo.get().length() != 0) {
			simpleComponent = SimpleComponent.calSimpleComponent(componentInfo.get(), ";");
		}

		// 构造分析服务代理
		JDependServiceProxy serviceProxy = null;
		if (simpleComponent == null) {
			serviceProxy = new JDependServiceProxyFactory().getJDependServiceProxy("无", "以包为单位输出分析报告");
		} else {
			serviceProxy = new JDependServiceProxyFactory().getJDependServiceProxy("无", "以简单组件为单位输出分析报告");
			serviceProxy.setComponent(simpleComponent);
		}

		try {
			// 增加分析数据
			serviceProxy.addDirectory(fileName.get());

			// 调用分析服务
			AnalysisResult result = serviceProxy.analyze();
			Collection<Relation> relations = result.getRelations();

			// 设置JDependUnit
			JDependUnitMgr.getInstance().setComponents(result.getComponents());
			// 设置解析的文件路径
			result.getRunningContext().setPath(this.pathName.get());

			// 创建输出结果
			Map<String, String> outputData = this.createOutputData(result);
			req.setAttribute("outputData", outputData);
			// 去结果页
			req.getRequestDispatcher("WEB-INF/pages/result.jsp").forward(req, resp);

		} catch (JDependException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	private Map<String, String> createOutputData(AnalysisResult inputData) throws IOException {

		Map<String, String> result = new LinkedHashMap<String, String>();

		result.put(AnalysisResultSummary.Name, this.getTextarea(inputData.toString()));

		HTMLSummaryPrinter detailPrinter = new HTMLSummaryPrinter();
		OutputStream detailStream = new ByteArrayOutputStream();
		detailPrinter.setStream(detailStream);
		detailPrinter.printUnits(inputData);
		String detailText = detailStream.toString();
		detailStream.close();

		result.put("明细", detailText);

		HTMLRelationPrinter relationPrinter = new HTMLRelationPrinter();
		OutputStream relationStream = new ByteArrayOutputStream();
		relationPrinter.setStream(relationStream);
		relationPrinter.printRelationList(inputData.getRelations());
		String relationText = relationStream.toString();
		relationStream.close();

		result.put("关系", relationText);

		return result;
	}

	private String getTextarea(String text) {
		return "<textarea cols=\"120\" rows=\"25\" name=\"name\" wrap=\"off\">" + text + "</textarea>";
	}

	private int upload(HttpServletRequest request, HttpServletResponse response) throws IOException {

		final long MAX_SIZE = 10 * 1024 * 1024;// 设置上传文件最大为 10M
		// 允许上传的文件格式的列表
		final String[] allowedExt = new String[] { "jar" };
		response.setContentType("text/html");
		// 设置字符编码为UTF-8, 这样支持汉字显示
		response.setCharacterEncoding("UTF-8");

		// 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		dfif.setSizeThreshold(4096);// 设置上传文件时用于临时存放文件的内存大小,这里是4K.多于的部分将临时存在硬盘
		dfif.setRepository(new File(request.getRealPath("/") + "UploadTemp"));// 设置存放临时文件的目录,web根目录下的ImagesUploadTemp目录

		// 用以上工厂实例化上传组件
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		// 设置最大上传尺寸
		sfu.setSizeMax(MAX_SIZE);

		PrintWriter out = response.getWriter();
		// 从request得到 所有 上传域的列表
		List<FileItem> fileList = null;
		try {
			fileList = sfu.parseRequest(request);
		} catch (FileUploadException e) {// 处理文件尺寸过大异常
			if (e instanceof SizeLimitExceededException) {
				out.println("文件尺寸超过规定大小:" + MAX_SIZE + "字节<p />");
				out.println("<a href=\"upload\" target=\"_top\">返回</a>");
				return UploadFailure;
			}
			e.printStackTrace();
		}
		// 没有文件上传
		if (fileList == null || fileList.size() == 0) {
			out.println("请选择上传文件<p/>");
			out.println("<a href=\"upload\" target=\"_top\">返回</a>");
			return UploadFailure;
		}
		// 得到所有上传的文件
		Iterator<FileItem> fileItr = fileList.iterator();
		// 循环处理所有文件
		while (fileItr.hasNext()) {
			FileItem fileItem = null;
			String path = null;
			long size = 0;
			// 得到当前文件
			fileItem = (FileItem) fileItr.next();
			if (fileItem == null) {
				continue;
			}
			if (fileItem.isFormField()) {
				if ("componentInfo".equals(fileItem.getFieldName())) {
					this.componentInfo.set(fileItem.getString());
				}
			} else {
				// 得到文件的完整路径
				path = fileItem.getName();
				// 得到文件的大小
				size = fileItem.getSize();
				if ("".equals(path) || size == 0) {
					out.println("请选择上传文件<p />");
					out.println("<a href=\"upload\" target=\"_self\">返回</a>");
					return UploadFailure;
				}

				this.pathName.set(path);
				// 得到去除路径的文件名
				String t_name = path.substring(path.lastIndexOf("\\") + 1);
				// 得到文件的扩展名(无扩展名时将得到全名)
				String t_ext = t_name.substring(t_name.lastIndexOf(".") + 1);
				// 拒绝接受规定文件格式之外的文件类型
				int allowFlag = 0;
				int allowedExtCount = allowedExt.length;
				for (; allowFlag < allowedExtCount; allowFlag++) {
					if (allowedExt[allowFlag].equals(t_ext))
						break;
				}
				if (allowFlag == allowedExtCount) {
					out.println("请上传以下类型的文件<p />");
					for (allowFlag = 0; allowFlag < allowedExtCount; allowFlag++)
						out.println("*." + allowedExt[allowFlag] + "&nbsp;&nbsp;&nbsp;");
					out.println("<p /><a href=\"upload\" target=\"_top\">返回</a>");
					return UploadFailure;
				}

				long now = System.currentTimeMillis();
				// 根据系统时间生成上传后保存的文件名
				String prefix = String.valueOf(now);
				// 保存的最终文件完整路径,保存在web根目录下的ImagesUploaded目录下
				String u_name = request.getRealPath("/") + "UploadTemp/" + prefix + "." + t_ext;
				try {
					// 保存文件
					fileItem.write(new File(u_name));
					fileName.set(u_name);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return UploadSuccess;
	}
}
