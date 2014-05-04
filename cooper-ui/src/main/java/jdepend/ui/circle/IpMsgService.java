package jdepend.ui.circle;

import jdepend.ui.circle.domain.DataPacket;
import jdepend.ui.circle.domain.IpMsgConstant;
import jdepend.ui.circle.domain.ResultTransfer;
import jdepend.ui.circle.util.NetUtil;

/**
 * IpMsg封装的业务逻辑方法类
 * 
 * @author sheldon wang
 */
public class IpMsgService {

	/**
	 * 发送消息
	 * 
	 * @param Text
	 *            消息内容
	 * @param ips
	 *            目标ip
	 */
	public static void sendMessage(String Text, String[] ips) {
		Text = Text.trim();
		for (int i = 0; i < ips.length; i++) {
			DataPacket data = new DataPacket(IpMsgConstant.IPMSG_SENDMSG);
			data.setIp(ips[i]);
			data.setAdditional(Text);
			NetUtil.sendUdpPacket(data, data.getIp());
		}
	}

	/**
	 * 发送结果
	 * 
	 * @param Result
	 *            结果内容
	 * @param ips
	 *            目标ip
	 */
	public static void sendResult(byte[] Result, String[] ips) {
		ResultTransfer.Result = Result;
		for (int i = 0; i < ips.length; i++) {
			DataPacket data = new DataPacket(IpMsgConstant.IPMSG_RECVRESULT);
			data.setIp(ips[i]);
			NetUtil.sendUdpPacket(data, data.getIp());
		}
	}
}
