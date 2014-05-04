package jdepend.ui.circle.thread;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import jdepend.framework.exception.JDependException;
import jdepend.ui.JDependCooper;
import jdepend.ui.circle.domain.DataPacket;
import jdepend.ui.circle.domain.IpMsgConstant;
import jdepend.ui.circle.domain.PacketQueue;
import jdepend.ui.circle.domain.ResultTransfer;
import jdepend.ui.circle.domain.SystemVar;
import jdepend.ui.circle.domain.UsersVo;
import jdepend.ui.circle.util.GuiUtil;
import jdepend.ui.circle.util.NetUtil;

/**
 * 数据包处理类
 * 
 * @author Sheldon wang
 */
public class DataPacketHandler implements Runnable {

	private JDependCooper frame;

	public DataPacketHandler(JDependCooper frame) {
		super();
		this.frame = frame;
	}

	public void run() {
		while (true) {
			try {
				SystemVar.PACKET_QUEUE_FULL.acquire();
				DataPacket dataPacket = PacketQueue.popPacket();
				SystemVar.PACKET_QUEUE_EMPTY.release();

				switch (dataPacket.getCommandFunction()) { // 命令功能判定
				case IpMsgConstant.IPMSG_ANSENTRY:
					// 登录后应答信息
					// 添加用户信息
					if (SystemVar.addUsers(UsersVo.changeDataPacket(dataPacket))) {
						// 添加成功设置在线用户数
						SystemVar.getUserListGui().updateUserNum(SystemVar.getUserList().size());
						SystemVar.getUserListGui().addUserVo(UsersVo.changeDataPacket(dataPacket));
					}
					break;
				case IpMsgConstant.IPMSG_SENDMSG:
					if ((IpMsgConstant.IPMSG_SENDCHECKOPT & dataPacket.getOption()) != 0) {
						// 需要发送检查
						DataPacket tmpPacket = new DataPacket(IpMsgConstant.IPMSG_RECVMSG);
						tmpPacket.setAdditional(Integer.toString(dataPacket.getPacketNo()));
						tmpPacket.setIp(dataPacket.getIp());
						NetUtil.sendUdpPacket(tmpPacket, tmpPacket.getIp());
					}
					GuiUtil.openMsgWindow(dataPacket.getAdditional(), dataPacket.getSenderName(),
							dataPacket.getSenderHost(), dataPacket.getIp());
					break;
				case IpMsgConstant.IPMSG_BR_ENTRY:
					// 其他用户登录
					SystemVar.addUsers(UsersVo.changeDataPacket(dataPacket));
					// 添加成功设置在线用户数
					SystemVar.getUserListGui().updateUserNum(SystemVar.getUserList().size());
					SystemVar.getUserListGui().updateUserList(SystemVar.getUserList());
					DataPacket dp = new DataPacket(IpMsgConstant.IPMSG_ANSENTRY);
					dp.setAdditional(SystemVar.USER_NAME + '\0' + "");
					dp.setIp(NetUtil.getLocalHostIp());
					UsersVo user = UsersVo.changeDataPacket(dp);
					SystemVar.addUsers(user);
					NetUtil.sendUdpPacket(dp, dataPacket.getIp());
					break;
				case IpMsgConstant.IPMSG_RECVRESULT:
					if (JOptionPane.showConfirmDialog(frame,
							"用户[" + dataPacket.getSenderName() + "](IP为[" + dataPacket.getIp() + "])向您发来分析结果，是否接收？",
							"提示", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
						// 在新线程中创建TCP链接，接收分析结果
						new Thread() {
							@Override
							public void run() {
								try {
									new ResultTransfer(frame).startAccept();
								} catch (IOException e) {
									e.printStackTrace();
								} catch (JDependException e) {
									e.printStackTrace();
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
							}
						}.start();
						// 通知对方发送分析结果
						DataPacket dp1 = new DataPacket(IpMsgConstant.IPMSG_SENDRESULT);
						dp1.setIp(NetUtil.getLocalHostIp());
						NetUtil.sendUdpPacket(dp1, dataPacket.getIp());
					}
					break;
				case IpMsgConstant.IPMSG_SENDRESULT:
					// 创建TCP链接，发送分析结果
					new ResultTransfer(frame).sendResult(dataPacket.getIp());
					break;
				}
			} catch (InterruptedException ex) {
				Logger.getLogger(DataPacketHandler.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}
}
