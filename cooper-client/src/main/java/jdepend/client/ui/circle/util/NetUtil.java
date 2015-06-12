/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jdepend.client.ui.circle.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdepend.client.ui.circle.domain.DataPacket;
import jdepend.client.ui.circle.domain.IpMsgConstant;
import jdepend.client.ui.circle.domain.SocketManage;
import jdepend.client.ui.circle.domain.SystemVar;

/**
 * 
 * 网络工具类
 * 
 * @author Sheldon wang
 */
public class NetUtil {

	/**
	 * 判断端口是否被占用
	 * 
	 * @return
	 */
	public static boolean checkPort() {
		try {
			new DatagramSocket(IpMsgConstant.IPMSG_DEFAULT_PORT).close();
			return true;
		} catch (SocketException ex) {
			return false;
		}
	}

	/**
	 * 发送UDP数据包
	 * 
	 * @param dataPacket
	 *            封装的数据包
	 * @param targetIp
	 *            目标IP
	 */
	public static void sendUdpPacket(DataPacket dataPacket, String targetIp) {
		try {
			byte[] dataBit = dataPacket.toString().getBytes(SystemVar.DEFAULT_CHARACT);
			DatagramPacket sendPacket = new DatagramPacket(dataBit, dataBit.length, InetAddress.getByName(targetIp),
					IpMsgConstant.IPMSG_DEFAULT_PORT);
			SocketManage.getInstance().getUdpSocket().send(sendPacket);
		} catch (SocketException ex) {
			Logger.getLogger(NetUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(NetUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(NetUtil.class.getName()).log(Level.SEVERE, null, ex);

		}
	}

	/**
	 * 发送局域网内广播
	 * 
	 * @param dataPacket
	 */
	public static void broadcastUdpPacket(DataPacket dataPacket) {
		sendUdpPacket(dataPacket, "255.255.255.255");
	}

	/**
	 * 获得本机ip
	 * 
	 * @return 本机ip
	 */
	public static String getLocalHostIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ex) {
			Logger.getLogger(NetUtil.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

}
