package jdepend.ui.circle.domain;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * IpMsg数据包
 * 
 * @author Sheldon wang
 */
public class DataPacket {

	private String version;
	private int commandNo;
	private int packetNo;
	private String senderName = null;
	private String senderHost = null;
	private String additional = null;
	private String ip = null;

	public DataPacket() {

	}

	public DataPacket(int commandNo) {
		this.commandNo = commandNo;
		this.packetNo = (int) new Date().getTime();
		this.version = IpMsgConstant.IPMSG_VERSION;
		this.senderName = SystemVar.USER_NAME;
		this.senderHost = SystemVar.HOST_NAME;
	}

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

	public int getCommandNo() {
		return commandNo;
	}

	public void setCommandNo(int commandNo) {
		this.commandNo = commandNo;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPacketNo() {
		return packetNo;
	}

	public void setPacketNo(int packetNo) {
		this.packetNo = packetNo;
	}

	public String getSenderHost() {
		return senderHost;
	}

	public void setSenderHost(String senderHost) {
		this.senderHost = senderHost;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return Low 8 bits from command number 32 bits
	 */
	public int getCommandFunction() {
		return commandNo & 0x000000FF;
	}

	/**
	 * @return (High 24 bits from command number 32 bits)
	 */
	public int getOption() {
		return commandNo & 0xFFFFFF00;
	}

	/**
	 * 转换输入的二进制流为数据包
	 * 
	 * @param data
	 *            数据包
	 * @param ip
	 *            发送端IP
	 * @return
	 */
	public static DataPacket createDataPacket(byte[] data, String ip) {
		if (data == null || ip == null)
			return null;
		try {
			String dataStr = new String(data, SystemVar.DEFAULT_CHARACT);
			String[] dataArr = dataStr.split(":");
			if (dataArr == null)
				return null;
			else {
				DataPacket packet = new DataPacket();
				packet.setVersion(dataArr.length >= 1 ? dataArr[0] : "");
				packet.setPacketNo((dataArr.length >= 2 ? Integer.parseInt(dataArr[1]) : -1));
				packet.setSenderName(dataArr.length >= 3 ? dataArr[2] : "");
				packet.setSenderHost(dataArr.length >= 4 ? dataArr[3] : "");
				packet.setCommandNo(dataArr.length >= 5 ? Integer.parseInt(dataArr[4]) : 0);
				if (dataArr.length >= 6) { // 存在附件
					StringBuilder strBuf = new StringBuilder();
					for (int i = 5; i < dataArr.length; i++) {
						strBuf.append(dataArr[i]);
					}
					packet.setAdditional(strBuf.toString());
				}
				packet.setIp(ip);
				return packet;
			}
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(DataPacket.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		} catch (Exception e) {
			Logger.getLogger(DataPacket.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
	}

	public String toString() {
		return version + ":" + packetNo + ":" + senderName + ":" + senderHost + ":" + commandNo + ":" + additional;
	}
}