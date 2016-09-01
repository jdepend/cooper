package jdepend.client.ui.circle.thread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdepend.client.ui.circle.domain.DataPacket;
import jdepend.client.ui.circle.domain.IpMsgConstant;
import jdepend.client.ui.circle.domain.PacketQueue;
import jdepend.client.ui.circle.domain.SocketManage;
import jdepend.client.ui.circle.domain.SystemVar;

/**
 * 收取数据包线程
 * 
 * @author Sheldon wang
 */
public class RecvPacketDaemon implements Runnable {

	public void run() {
		try {
			DatagramSocket defaultSocket = SocketManage.getInstance().getUdpSocket();
			DatagramPacket pack = new DatagramPacket(new byte[IpMsgConstant.PACKET_LENGTH], IpMsgConstant.PACKET_LENGTH);
			while (true) {
				// 接收数据
				defaultSocket.receive(pack);
				byte[] buffer = new byte[pack.getLength()];
				System.arraycopy(pack.getData(), 0, buffer, 0, buffer.length);
				DataPacket dataPacket = DataPacket.createDataPacket(buffer, pack.getAddress().getHostAddress());

				if (dataPacket != null) {
					SystemVar.PACKET_QUEUE_EMPTY.acquire();
					PacketQueue.pushPacket(dataPacket);
					SystemVar.PACKET_QUEUE_FULL.release();
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
			Logger.getLogger(RecvPacketDaemon.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			Logger.getLogger(RecvPacketDaemon.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
