package jdepend.core.remote.analyzer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import jdepend.core.local.analyzer.AnalyzerConvertUtil;
import jdepend.core.local.analyzer.AnalyzerMgr;
import jdepend.core.local.analyzer.AnalyzerSummaryInfo;
import jdepend.core.remote.session.RemoteSessionProxy;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.StreamUtil;
import jdepend.service.remote.analyzer.AnalyzerDTO;
import jdepend.service.remote.analyzer.AnalyzerSummaryDTO;
import jdepend.util.analyzer.framework.Analyzer;

import org.apache.commons.beanutils.BeanUtils;

public class AnalyzerRemoteMgr {

	public static void upload(Analyzer analyzer) throws JDependException {

		// List<String> importPackages = this.getImportedPackages(analyzer);
		// if (importPackages.size() > 0) {
		// throw new JDependException("该分析器依赖了" + importPackages);
		// }

		ByteArrayOutputStream out = null;
		ObjectOutputStream s = null;
		InputStream defInputStream = null;

		byte[] defaultData = null;
		String className;
		byte[] def = null;

		try {
			out = new ByteArrayOutputStream();
			s = new ObjectOutputStream(out);
			s.writeObject(analyzer);
			out.flush();
			defaultData = out.toByteArray();

			className = analyzer.getClass().getCanonicalName();

			defInputStream = AnalyzerMgr.getInstance().getDef(analyzer);
			def = StreamUtil.getData(defInputStream);

			AnalyzerDTO analyzerDTO = new AnalyzerDTO();
			analyzerDTO.setClassName(className);
			analyzerDTO.setDef(def);
			analyzerDTO.setDefaultData(defaultData);
			analyzerDTO.setClient(InetAddress.getLocalHost().getHostAddress());
			analyzerDTO.setUserName(RemoteSessionProxy.getInstance().getUserName());

			analyzerDTO.setName(analyzer.getName());
			analyzerDTO.setTip(analyzer.getTip());
			analyzerDTO.setType(analyzer.getType());
			// 上传
			RemoteAnalyzerProxy.getInstance().getAnalyzerService().upload(analyzerDTO);

		} catch (IOException ex) {
			throw new JDependException(ex);
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (defInputStream != null) {
				try {
					defInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static List<AnalyzerSummaryInfo> getRemoteAnalyzers(String type) throws JDependException {
		try {
			List<AnalyzerSummaryDTO> analyzerSummaryDTOs = RemoteAnalyzerProxy.getInstance().getAnalyzerService()
					.getAnalyzsers(type);
			AnalyzerSummaryInfo analyzerSummaryInfo;
			List<AnalyzerSummaryInfo> analyzerSummaryInfos = new ArrayList<AnalyzerSummaryInfo>();
			for (AnalyzerSummaryDTO analyzerSummaryDTO : analyzerSummaryDTOs) {
				analyzerSummaryInfo = new AnalyzerSummaryInfo();
				BeanUtils.copyProperties(analyzerSummaryInfo, analyzerSummaryDTO);
				analyzerSummaryInfos.add(analyzerSummaryInfo);
			}
			return analyzerSummaryInfos;
		} catch (RemoteException e) {
			throw new JDependException(e);
		} catch (IllegalAccessException e) {
			throw new JDependException(e);
		} catch (InvocationTargetException e) {
			throw new JDependException(e);
		}
	}

	public static void download(String className) throws JDependException {
		try {
			AnalyzerDTO analyzerDTO = RemoteAnalyzerProxy.getInstance().getAnalyzerService().download(className);
			Analyzer analyzer = AnalyzerConvertUtil.createAnalyzer(analyzerDTO);
			if (AnalyzerMgr.getInstance().containsAnalyzer(analyzer)) {
				throw new JDependException("分析器已经存在");
			} else {
				AnalyzerMgr.getInstance().save(analyzerDTO);
				AnalyzerMgr.getInstance().addAnalyzer(analyzer);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new JDependException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new JDependException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new JDependException(e);
		}
	}
}
