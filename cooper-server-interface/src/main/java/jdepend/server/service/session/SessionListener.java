package jdepend.server.service.session;

/**
 * session监听者接口
 * 
 * @author wangdg
 * 
 */
public interface SessionListener {

	public void onCreateSession(JDependSession session);

	public void onRemoveSession(JDependSession session);

	public void onChangeState(JDependSession session);

}
