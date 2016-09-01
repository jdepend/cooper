/**
 * 更新用户列表的接口
 */

package jdepend.client.ui.circle.gui;

import java.util.List;

import jdepend.client.ui.circle.domain.UsersVo;

/**
 * 
 * @author sheldon wang
 */
public interface IUserListGui {

	/**
	 * 更新用户数
	 * 
	 * @param num
	 *            用户数
	 */
	public void updateUserNum(int num);

	/**
	 * 更新用户列表
	 * 
	 * @param userList
	 */
	public void updateUserList(List<UsersVo> userList);

	/**
	 * 增加用户信息
	 * 
	 * @param users
	 *            用户信息
	 */
	public void addUserVo(UsersVo users);

	/**
	 * 回复消息
	 * 
	 * @param rowIdex
	 *            用户列表行索引
	 * @param content
	 *            用户内容
	 */
	public void rebackMsg(int rowIdex, String content);

}
