package jdepend.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jdepend.core.framework.serverconf.ServerConfigurator;
import jdepend.core.local.command.CommandAdapterMgr;
import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.StatusField;
import jdepend.framework.util.FileUtil;
import jdepend.ui.shoppingcart.ShoppingCartPanel;

public class ClientStatusField extends StatusField {

	private ShoppingCartPanel resultContainerPanel;

	public ClientStatusField(JDependCooper frame) {
		super(frame);

		this.getStatusLeft().setPreferredSize(new Dimension(this.getStatusLeft().getPreferredSize().width - 32, 20));

		resultContainerPanel = new ShoppingCartPanel(frame);
		this.add(resultContainerPanel);

		// 单机版运行模式可以切换单机或联机运行模式
		if (JDependContext.isStandaloneMode()) {
			final JPopupMenu popupMenu = new JPopupMenu();

			JMenuItem localItem = new JMenuItem(JDependContext.Local);
			localItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDependContext.setIsLocalService(true);
					getStatusCenter().setText(JDependContext.Local);
					try {
						synServiceConf();
						CommandAdapterMgr.getInstance().refresh();
					} catch (JDependException e1) {
						e1.printStackTrace();
					}

				}
			});
			popupMenu.add(localItem);

			JMenuItem remoteItem = new JMenuItem(JDependContext.Remote);
			remoteItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDependContext.setIsLocalService(false);
					getStatusCenter().setText(JDependContext.Remote);
					try {
						synServiceConf();
						CommandAdapterMgr.getInstance().refresh();
					} catch (JDependException e1) {
						e1.printStackTrace();
					}
				}
			});
			popupMenu.add(remoteItem);

			getStatusCenter().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 3) {
						popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
					}
				}
			});
		}
	}

	@Override
	public void refresh() {
		this.resultContainerPanel.refreshState();
	}

	private void synServiceConf() throws JDependException {
		String filePath = JDependContext.getWorkspacePath() + "/" + PropertyConfigurator.DEFAULT_PROPERTY_DIR + "/"
				+ ServerConfigurator.DEFAULT_PROPERTY_FILE;

		StringBuilder content = FileUtil.readFileContent(filePath, "UTF-8");

		int startPos = content.indexOf("isLocalService=") + 15;
		int endPos = content.indexOf("\n", startPos);
		String value = content.substring(startPos, endPos);
		boolean isNeedSave = false;
		if (JDependContext.isLocalService() && value.equalsIgnoreCase("false")) {
			content.replace(startPos, endPos, "true");
			isNeedSave = true;
		} else if (!JDependContext.isLocalService() && value.equalsIgnoreCase("true")) {
			content.replace(startPos, endPos, "false");
			isNeedSave = true;
		}
		if (isNeedSave) {
			FileUtil.saveFileContent(filePath, content, "UTF-8");
		}
	}

}
