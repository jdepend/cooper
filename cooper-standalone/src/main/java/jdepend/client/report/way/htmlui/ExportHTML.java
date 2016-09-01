package jdepend.client.report.way.htmlui;

import java.io.StringWriter;
import java.util.Properties;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.VersionUtil;
import jdepend.model.result.AnalysisResult;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public final class ExportHTML {

	public static StringBuilder export(AnalysisResult result) throws JDependException {
		try {
			Properties p = new Properties();
			p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, "resource/report");
			p.setProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
			p.setProperty(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
			VelocityEngine ve = new VelocityEngine();
			ve.init(p);
			Template t = ve.getTemplate("Report.vm");
			VelocityContext context = new VelocityContext();
			context.put("result", result);
			context.put("version", VersionUtil.getVersion());
			context.put("buildDate", VersionUtil.getBuildDate());
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			return new StringBuilder(writer.getBuffer());
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException(e);
		}
	}

	public static String getCSSFileName() {
		return "resource/report/style.css";
	}

}
