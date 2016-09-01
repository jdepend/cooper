package jdepend.server.service.score;

import java.rmi.Remote;
import java.util.List;

/**
 * 分数接口
 * 
 * @author wangdg
 * 
 */
public interface ScoreRemoteService extends Remote {

	/**
	 * 上传客户端分数
	 * 
	 * @param scoreList
	 * @throws java.rmi.RemoteException
	 */
	public void uploadScore(List<ScoreDTO> scoreList) throws java.rmi.RemoteException;

}
