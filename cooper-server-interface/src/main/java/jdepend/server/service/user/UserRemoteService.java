package jdepend.server.service.user;

import java.rmi.Remote;
import java.util.List;

import jdepend.framework.log.Operation;

/**
 * 用户远程服务接口
 * 
 * @author wangdg
 * 
 */
public interface UserRemoteService extends Remote {

	/**
	 * 处理积分
	 * 
	 * @param userName
	 * @param operation
	 * @throws java.rmi.RemoteException
	 */
	public void processCredits(String userName, Operation operation) throws java.rmi.RemoteException;

	/**
	 * 上传用户行为
	 * 
	 * @param items
	 * @throws java.rmi.RemoteException
	 */
	public void uploadUserAction(List<UserActionItem> items) throws java.rmi.RemoteException;
	
	/**
	 * 设置状态监听器
	 * 
	 * @param listener
	 */
	public void setUserStateChangeListener(UserStateChangeListener listener) throws java.rmi.RemoteException;

}
