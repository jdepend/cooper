package jdepend.service.remote.analyzer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import jdepend.framework.exception.JDependException;

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

	@Override
	public void delete(String className) throws JDependException {
		(new AnalyzerRepository()).delete(className);
		
	}

	@Override
	public List<AnalyzerSummaryDTO> queryAll() throws JDependException {
		return (new AnalyzerRepository()).queryAll();
	}

}
