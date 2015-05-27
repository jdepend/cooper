package jdepend.ui.analyzer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import jdepend.core.local.analyzer.AnalyzerMgr;
import jdepend.core.remote.analyzer.AnalyzerRemoteMgr;
import jdepend.core.remote.session.RemoteSessionProxy;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.ui.JDependCooper;
import jdepend.util.analyzer.framework.Analyzer;

public class ClientAnalyzerPanel extends AnalyzerPanel {

	private AnalyzerRemoteMgr analyzerRemoteMgr;

	public ClientAnalyzerPanel(JDependCooper frame) {
		super(frame);

		this.analyzerRemoteMgr = new AnalyzerRemoteMgr();
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

		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (JOptionPane.showConfirmDialog(frame, "您是否确认删除？", "提示", JOptionPane.YES_NO_OPTION) == 0) {
						delete();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, e1.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(deleteItem);

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
			this.analyzerRemoteMgr.upload(analyzer);
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

	private void delete() throws JDependException {
		String className = this.analyzers.get(this.currentGroup).get(this.currentRow).getClass().getName();
		AnalyzerMgr.getInstance().delete(className);
		this.analyzerRemoteMgr.delete(className);
		this.refresh();

	}

}
