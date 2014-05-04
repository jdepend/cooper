package jdepend.core.analyzer.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jdepend.core.session.RemoteSessionProxy;
import jdepend.framework.domain.PersistentBean;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.util.StreamUtil;
import jdepend.service.remote.analyzer.AnalyzerDTO;
import jdepend.service.remote.analyzer.AnalyzerSummaryDTO;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 客户端分析管理器
 * 
 * @author wangdg
 * 
 */
public final class AnalyzerMgr {

	private static AnalyzerMgr mgr;

	private List<String> types;

	private Map<String, List<Analyzer>> analyzers;

	private AnalyzerLocalRepository repository;

	private AnalyzerMgr() {
		this.init();
	}

	public static AnalyzerMgr getInstance() {
		if (mgr == null) {
			mgr = new AnalyzerMgr();
		}
		return mgr;
	}

	public void refresh() {
		this.init();
	}

	public void save() {
		for (String type : types) {
			for (Analyzer analyzer : this.analyzers.get(type)) {
				if (analyzer instanceof PersistentBean) {
					if (analyzer instanceof AbstractAnalyzer) {
						if (!((AbstractAnalyzer) analyzer).needSave()) {
							continue;
						}
					}
					LogUtil.getInstance(AnalyzerMgr.class).systemLog("保存分析器[" + analyzer.getName() + "]配置。。。");
					try {
						((PersistentBean) analyzer).save();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected void init() {

		this.repository = new AnalyzerLocalRepository();
		analyzers = this.repository.getAnalyzers();

		this.types = new ArrayList<String>();
		for (String type : analyzers.keySet()) {
			this.types.add(type);
			for (Analyzer analyzer : analyzers.get(type)) {
				try {
					analyzer.init();
				} catch (JDependException e) {
					e.printStackTrace();
					LogUtil.getInstance(AnalyzerMgr.class).systemError("分析器[" + analyzer.getName() + "]启动失败");
				}
			}
		}

		// 按热度排序
		for (String type : types) {
			Collections.sort(this.analyzers.get(type));
		}
	}

	public List<String> getTypes() {
		return this.types;
	}

	public List<Analyzer> getAnalyzers(String type) {
		return this.analyzers.get(type);
	}

	public Map<String, List<Analyzer>> getAnalyzers() {
		return analyzers;
	}

	public void upload(Analyzer analyzer) throws JDependException {

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

			defInputStream = this.repository.getDef(analyzer);
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

	public List<AnalyzerSummaryInfo> getRemoteAnalyzers(String type) throws JDependException {
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

	public void download(String className) throws JDependException {
		try {
			AnalyzerDTO analyzerDTO = RemoteAnalyzerProxy.getInstance().getAnalyzerService().download(className);
			Analyzer analyzer = AnalyzerConvertUtil.createAnalyzer(analyzerDTO);
			for (String type : this.analyzers.keySet()) {
				for (Analyzer obj : this.analyzers.get(type)) {
					if (obj.equals(analyzer)) {
						throw new JDependException("分析器已经存在");
					}
				}
			}
			this.repository.save(analyzerDTO);
			this.analyzers.get(analyzer.getType()).add(analyzer);
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

	public void delete(String className) throws JDependException {
		for (String type : this.types) {
			for (Analyzer analyzer : this.analyzers.get(type)) {
				if (analyzer.getClass().getName().equals(className)) {
					analyzer.release();
					this.getAnalyzers(type).remove(analyzer);
					break;
				}
			}
		}
		this.repository.delete(className);
	}
}
