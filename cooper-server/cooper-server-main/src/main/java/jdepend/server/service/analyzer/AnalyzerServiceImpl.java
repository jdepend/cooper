package jdepend.server.service.analyzer;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.server.service.analyzer.AnalyzerDTO;
import jdepend.server.service.analyzer.AnalyzerInfo;
import jdepend.server.service.analyzer.AnalyzerService;
import jdepend.server.service.analyzer.AnalyzerSummaryDTO;

public final class AnalyzerServiceImpl extends UnicastRemoteObject implements AnalyzerService {

	public AnalyzerServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public void upload(AnalyzerDTO analyzerDTO) throws RemoteException {
		try {

			AnalyzerInfo info = new AnalyzerInfo();

			info.setClassName(analyzerDTO.getClassName());
			info.setClient(analyzerDTO.getClient());
			info.setDef(analyzerDTO.getDef());
			info.setDefaultData(analyzerDTO.getDefaultData());
			info.setUserName(analyzerDTO.getUserName());
			info.setName(analyzerDTO.getName());
			info.setTip(analyzerDTO.getTip());
			info.setType(analyzerDTO.getType());
			info.setBigTip(analyzerDTO.getBigTip());

			(new AnalyzerRepository()).save(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public List<AnalyzerSummaryDTO> getAnalyzsers(String type) throws RemoteException {
		try {
			return (new AnalyzerRepository()).queryByType(type);
		} catch (JDependException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public AnalyzerDTO download(String className) throws RemoteException {
		AnalyzerInfo info = null;
		try {
			info = (new AnalyzerRepository()).getTheAnalyzer(className);

			AnalyzerDTO analyzerDTO = new AnalyzerDTO();

			analyzerDTO.setDef(info.getDef());
			analyzerDTO.setDefaultData(info.getDefaultData());
			analyzerDTO.setClassName(info.getClassName());
			analyzerDTO.setClient(info.getClient());
			analyzerDTO.setUserName(info.getUserName());

			return analyzerDTO;
		} catch (JDependException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

}
