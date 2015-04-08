package jdepend.ui.analyzer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import jdepend.core.remote.analyzer.AnalyzerRemoteMgr;
import jdepend.core.remote.session.RemoteSessionProxy;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.ui.JDependCooper;
import jdepend.util.analyzer.framework.Analyzer;

public class ClientAnalyzerPanel extends AnalyzerPanel {

	public ClientAnalyzerPanel(JDependCooper frame) {
		super(frame);
	}

	@Override
	protected JPopupMenu getPopupMenu() {
		JPopupMenu popupMenu = super.getPopupMenu();

		popupMenu.addSeparator();

		JMenuItem uploadItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Upload));
		uploadItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					upload();
					JOptionPane.showMessageDialog(frame, "上传成功", "alert", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, e1.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(uploadItem);

		JMenuItem downloadItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Download));
		downloadItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					download();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, e1.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(downloadItem);

		return popupMenu;

	}

	@Override
	protected JPopupMenu getPopupMenu1() {
		JPopupMenu popupMenu1 = super.getPopupMenu1();
		JMenuItem downloadItem1 = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Download));
		downloadItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					download();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, e1.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu1.add(downloadItem1);

		return popupMenu1;
	}

	private void upload() throws RemoteException, JDependException {
		if (!RemoteSessionProxy.getInstance().isValid()) {
			throw new JDependException("会话状态有问题，请重新登陆");
		} else {
			Analyzer analyzer = this.analyzers.get(this.currentGroup).get(this.currentRow);
			AnalyzerRemoteMgr.upload(analyzer);
		}
	}

	private void download() throws RemoteException, JDependException {
		if (!RemoteSessionProxy.getInstance().isValid()) {
			throw new JDependException("会话状态有问题，请重新登陆");
		} else {
			AnalyzerDownloadDialog d = new AnalyzerDownloadDialog(frame, this, currentGroup);

			d.setModal(true);
			d.setVisible(true);
		}
	}

}
