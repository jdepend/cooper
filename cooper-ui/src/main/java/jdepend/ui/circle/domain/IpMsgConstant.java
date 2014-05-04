package jdepend.ui.circle.domain;

/**
 * 
 * @author Sheldon wang
 */
public class IpMsgConstant {

	public final static int PACKET_LENGTH = 1500; // 数据流大小限制

	// public final static int IPMSG_DEFAULT_PORT=0x0979; //IPMSG默认端口号 2425
	public final static int IPMSG_DEFAULT_PORT = 2426; // Cooper UDP端口号 2426

	public final static int IPMSG_DEFAULT_TCP_PORT = 2427; // Cooper TCP端口号 2427

	public final static char OS_LINUX = 1;

	public final static char OS_WINDOWS = 0;

	public final static char OS_OTHER = 2;

	public final static String IPMSG_VERSION = "1";

	/*
	 * ================================================================= 1.
	 * Command & Option
	 * ==================================================================
	 */

	/*
	 * 1) Command functions (Low 8 bits from command number 32 bits)
	 */
	public final static int IPMSG_NOOPERATION = 0x00000000; // No Operation
	public final static int IPMSG_BR_ENTRY = 0x00000001; // Entry to service
	// (Start-up with a
	// Broadcast
	// command)
	public final static int IPMSG_BR_EXIT = 0x00000002; // Exit from service
	// (End with a Broadcast
	// command)
	public final static int IPMSG_ANSENTRY = 0x00000003; // Notify a new entry
	public final static int IPMSG_BR_ABSENCE = 0x00000004; // Change absence
	// mode

	public final static int IPMSG_SENDMSG = 0x00000020; // Message transmission
	public final static int IPMSG_RECVMSG = 0x00000021; // Message receiving
	// check

	public final static int IPMSG_SENDRESULT = 0x00000030; // 发送结果
	public final static int IPMSG_RECVRESULT = 0x00000031; // 接收结果

	/**
	 * 2) Option flag (High 24 bits from command number 32 bits)
	 */
	public final static int IPMSG_SENDCHECKOPT = 0x00000100; // Transmission
	// check

}
