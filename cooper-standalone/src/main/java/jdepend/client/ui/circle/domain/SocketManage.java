package jdepend.client.ui.circle.domain;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 管理Socket
 * 
 * @author sheldon wang
 */
public class SocketManage {

	// udp SOCKET
	private static DatagramSocket udpSocket;

	private static SocketManage instance;

	private SocketManage() throws SocketException {
		udpSocket = new DatagramSocket(IpMsgConstant.IPMSG_DEFAULT_PORT);
	}

	/**
	 * 返回单例对象
	 * 
	 * @return SocketManage
	 * @throws SocketException
	 */
	public static SocketManage getInstance() throws SocketException {
		if (instance == null)
			return instance = new SocketManage();
		else
			return instance;
	}

	/**
	 * 返回IPMsg通信端口的 UDP SOCKET
	 * 
	 * @return UDP SOCKET
	 */
	public DatagramSocket getUdpSocket() {
		return udpSocket;
	}

}
