package jdepend.server.service.score;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.service.remote.score.ScoreDTO;
import jdepend.service.remote.score.ScoreRemoteService;

public class ScoreRemoteServiceImpl extends UnicastRemoteObject implements ScoreRemoteService {

	public ScoreRemoteServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public void uploadScore(List<ScoreDTO> scoreList) throws RemoteException {
		try {
			new ScoreListRepository().save(scoreList);
		} catch (JDependException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}

	}

}
