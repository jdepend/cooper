package jdepend.server.service.analyzer;

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
			AnalyzerInfo analyzerInfo = analyzerDTO.toInfo();
			(new AnalyzerRepository()).save(analyzerInfo);
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
		AnalyzerInfo info;
		try {
			info = (new AnalyzerRepository()).getTheAnalyzer(className);
		} catch (JDependException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
		return new AnalyzerDTO(info);
	}
}
