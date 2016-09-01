package jdepend.client.core.remote.analyzer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import jdepend.server.service.analyzer.AnalyzerDTO;
import jdepend.util.analyzer.framework.Analyzer;

public final class AnalyzerConvertUtil {

	public static Analyzer createAnalyzer(AnalyzerDTO analyzerDTO) throws ClassNotFoundException, IOException {
		return createAnalyzer(analyzerDTO.getDef(), analyzerDTO.getClassName(), analyzerDTO.getDefaultData());
	}

	public static Analyzer createAnalyzer(byte[] def, String className, byte[] data) throws ClassNotFoundException,
			IOException {
		// 加载类定义
		(new AnalyzerClassLoader(def)).loadClass(className);
		// 生成类实例
		ObjectInputStream s = null;
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(data);
			s = new ObjectInputStream(in);
			Analyzer analyzer = (Analyzer) s.readObject();

			return analyzer;
		} finally {
			if (in != null) {
				in.close();
			}
			if (s != null) {
				s.close();
			}
		}
	}

}
