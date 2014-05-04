package jdepend.ui.circle.domain;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdepend.ui.circle.gui.IUserListGui;

/**
 * 
 * 系统环境变量
 * 
 * @author Sheldon wang
 */
public class SystemVar {

	// 信号量
	public static Semaphore PACKET_QUEUE_FULL = new Semaphore(0);
	public static Semaphore PACKET_QUEUE_EMPTY = new Semaphore(100); // 队列最大容量

	// 运行时可变变量
	public static String USER_NAME; // 显示的用户名称
	public static String HOST_NAME; // 显示的主机名称

	private static List<UsersVo> USER_LIST; // 用户列表

	private static IUserListGui userListGui; // 用户列表界面操作

	// 运行时不变参数
	public static String USER_HOME; // 用户工作路径
	public static String DEFAULT_CHARACT; // 默认编码
	public static String LINE_SEPARATOR; // 换行标识
	public static String FILE_SEPARATOR; // 文件分割标识
	public static char OS; // 操作系统

	/**
	 * 系统参数初始化
	 */
	public static void init() {

		USER_NAME = System.getProperty("user.name");
		USER_HOME = System.getProperty("user.home");
		LINE_SEPARATOR = System.getProperty("line.separator");
		FILE_SEPARATOR = System.getProperty("file.separator");

		if (System.getProperty("os.name").equalsIgnoreCase("linux"))
			OS = IpMsgConstant.OS_LINUX;
		else if (System.getProperty("os.name").equalsIgnoreCase("window"))
			OS = IpMsgConstant.OS_WINDOWS;
		else
			OS = IpMsgConstant.OS_OTHER;
		try {
			HOST_NAME = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException ex) {
			HOST_NAME = "";
			Logger.getLogger(SystemVar.class.getName()).log(Level.SEVERE, null, ex);
		}
		DEFAULT_CHARACT = "GBK";

		USER_LIST = new ArrayList<UsersVo>();

	}

	/**
	 * 获得在线用户的集合
	 * 
	 * @return
	 */
	public static List<UsersVo> getUserList() {
		return SystemVar.USER_LIST;
	}

	/**
	 * 设置在线用户集合
	 * 
	 * @param userList
	 *            用户集合
	 */
	public static void setUserList(List<UsersVo> userList) {
		SystemVar.USER_LIST = userList;
	}

	/**
	 * 向在线用户集合中添加用户
	 * 
	 * @param user
	 *            用户对象
	 * @return true 添加成功 添加失败
	 */
	public static synchronized boolean addUsers(UsersVo user) {
		for (int i = 0; i < USER_LIST.size(); i++) {
			if (USER_LIST.get(i).getIp().equals(user.getIp())) {
				USER_LIST.set(i, user);
				return false;
			}
		}
		USER_LIST.add(user);
		return true;
	}

	/**
	 * 清空现在用户集合
	 */
	public static void clearUsers() {
		USER_LIST.clear();
	}

	public static IUserListGui getUserListGui() {
		return userListGui;
	}

	public static void setUserListGui(IUserListGui userListGui) {
		SystemVar.userListGui = userListGui;
	}

	public static void main(String args[]) {
		try {
			System.out.println(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException ex) {
			Logger.getLogger(SystemVar.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * 获得有该ip的用户所在的索引号
	 * 
	 * @param ip
	 * @return
	 */
	public static int getUserIndex(String ip) {
		for (int i = 0; i < USER_LIST.size(); i++) {
			if (USER_LIST.get(i).getIp().equals(ip)) {
				return i;
			}
		}
		return 0;
	}

}
