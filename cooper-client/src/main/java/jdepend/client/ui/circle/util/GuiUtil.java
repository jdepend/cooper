/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jdepend.client.ui.circle.util;

import java.awt.Component;

import javax.swing.JOptionPane;

import jdepend.client.ui.circle.MsgWindow;

/**
 * 
 * @author Sheldon wang
 */
public class GuiUtil {

	/**
	 * 弹出提示消息
	 * 
	 * @param comp
	 *            基于的gui组件
	 * @param content
	 *            需要提示的内容
	 */
	public static void showNotice(Component comp, String content) {
		JOptionPane.showMessageDialog(comp, "\n" + content + "\n");
	}

	/**
	 * 显示消息窗口
	 * 
	 * @param content
	 *            内容
	 * @param senderName
	 *            发送者昵称
	 * @param senderHost
	 *            发送者机器
	 * @param ip
	 *            发送者ip
	 */
	public static void openMsgWindow(String content, String senderName, String senderHost, String ip) {
		new MsgWindow(content, senderName, senderHost, ip).setVisible(true);
	}
}
