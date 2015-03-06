package jdepend.ui.circle.domain;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import jdepend.core.command.CommandAdapterMgr;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.StreamUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.ui.JDependCooper;

/**
 * 分析结果传输器
 * 
 * @author wangdg
 * 
 */
public final class ResultTransfer {

	private JDependCooper frame;

	public static byte[] Result;

	public ResultTransfer(JDependCooper frame) {
		super();
		this.frame = frame;
	}

	public void startAccept() throws IOException, JDependException, ClassNotFoundException {
		ServerSocket ss = null;
		Socket socket = null;
		InputStream in = null;
		try {
			ss = new ServerSocket(IpMsgConstant.IPMSG_DEFAULT_TCP_PORT);
			socket = ss.accept();
			in = socket.getInputStream();
			AnalysisResult result = AnalysisResult.create(StreamUtil.getData(in));
			// 清空历史
			frame.clearPriorResult();
			// 显示结果
			JDependUnitMgr.getInstance().setResult(result);
			frame.getResultPanelWrapper().showResults(true);

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void sendResult(String ip) {
		Socket socket = null;
		DataOutputStream out = null;
		try {
			socket = new Socket(ip, IpMsgConstant.IPMSG_DEFAULT_TCP_PORT);
			out = new DataOutputStream(socket.getOutputStream());
			out.write(Result);
			out.flush();
			frame.showStatusMessage("发送成功");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
