package jdepend.ui.wizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import jdepend.core.config.CommandConf;
import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;

public class CommandStep extends Step {

	private CommandTableModel commandTableModel;

	public transient static final String DEFAULT_PROPERTY_FILE = "command.xml";

	public CommandStep(NewGroupWizard wizard) {
		super("Command Setting", wizard);

		JPanel content = new JPanel(new BorderLayout());

		commandTableModel = new CommandTableModel();

		JTable t = new JTable(commandTableModel) {
			public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
				super.changeSelection(rowIndex, columnIndex, toggle, extend);
				super.editCellAt(rowIndex, columnIndex, null);
			}
		};

		content.add(BorderLayout.CENTER, new JScrollPane(t));

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createPriorButton());
		buttonBar.add(createNextButton());
		buttonBar.add(createCancelButton());

		this.add(BorderLayout.CENTER, content);

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	class CommandTableModel extends AbstractTableModel {

		Object[][] p;

		String[] n = { "命令名称", "说明", "参数" };

		public CommandTableModel() {
			super();
			try {
				loadCommandInfo();
			} catch (JDependException e) {
				e.printStackTrace();

			}
		}

		public void loadCommandInfo() throws JDependException {

			Properties properties = new Properties();

			String home = JDependContext.getWorkspacePath() + "\\" + PropertyConfigurator.DEFAULT_PROPERTY_DIR;

			InputStream is = null;

			try {
				is = new FileInputStream(new File(home, DEFAULT_PROPERTY_FILE));
				if (is != null) {
					properties.loadFromXML(is);
				} else {
					throw new JDependException("读取Commad模板配置文件出错。");
				}
			} catch (IOException ignore) {
				ignore.printStackTrace();
				throw new JDependException("读取Commad模板配置文件出错。", ignore);
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException ignore) {
				}
			}

			List<String> components = CommandStep.this.getWorker().getComponentModels();

			List<CommandConf> commandInfos = getCommandConfigurator(properties);
			p = new String[commandInfos.size() + components.size()][3];
			for (int i = 0; i < commandInfos.size(); i++) {
				p[i][0] = commandInfos.get(i).label;
				p[i][1] = commandInfos.get(i).tip;
				if (commandInfos.get(i).getArgInfo() != null)
					p[i][2] = commandInfos.get(i).getArgInfo();
				else
					p[i][2] = "";
			}

			int row;
			CommandConf info;
			for (int i = 0; i < components.size(); i++) {
				row = i + commandInfos.size();
				info = CommandConf.create(components.get(i), getWorker().getGroupName());
				p[row][0] = info.label;
				p[row][1] = info.tip;
				p[row][2] = info.getArgInfo();
			}
		}

		/**
		 * 得到所有命令信息
		 * 
		 * @return
		 */
		private List<CommandConf> getCommandConfigurator(Properties properties) {

			List<CommandConf> commandInfos = new ArrayList<CommandConf>();
			CommandConf info;

			String[] command;

			Enumeration e = properties.propertyNames();
			while (e.hasMoreElements()) {
				info = new CommandConf();
				info.label = (String) e.nextElement();
				try {
					command = properties.getProperty(info.label).split(",");
					info.group = getWorker().getGroupName();
					info.order = Integer.parseInt(command[0].trim());

					info.tip = command[1].trim();
					if (command.length > 2) {
						info.args = CommandConf.parseArgs(command[2].trim());
					}

					commandInfos.add(info);
				} catch (Exception ex) {
					LogUtil.getInstance(CommandStep.class).systemError("Command " + info.label + " 配置有问题。");
					ex.printStackTrace();
				}
			}

			Collections.sort(commandInfos);

			return commandInfos;
		}

		public int getColumnCount() {
			return n.length;
		}

		public int getRowCount() {
			return p.length;
		}

		public String getColumnName(int col) {
			return n[col];
		}

		public Object getValueAt(int row, int col) {
			return p[row][col];
		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		public void setValueAt(Object value, int row, int col) {
			p[row][col] = value;
			fireTableCellUpdated(row, col);
		}
	}

	@Override
	protected Step createNextStep() {
		return new FinishStep(this.getWizard());
	}

	@Override
	protected int doWork() throws JDependException {

		List<CommandConf> commandInfos = new ArrayList<CommandConf>();
		CommandConf commandInfo;
		for (int row = 0; row < commandTableModel.getRowCount(); row++) {
			commandInfo = new CommandConf();
			commandInfo.group = this.getWorker().getGroupName();
			commandInfo.order = row + 1;
			commandInfo.label = (String) commandTableModel.getValueAt(row, 0);
			commandInfo.tip = (String) commandTableModel.getValueAt(row, 1);
			commandInfo.args = getArgs(row);

			commandInfos.add(commandInfo);
		}
		this.getWorker().setCommandInfos(commandInfos);

		return DO_NEXT_STEP;
	}

	private String[] getArgs(int row) {
		if (commandTableModel.getValueAt(row, 2) != null) {
			String argInfo = (String) commandTableModel.getValueAt(row, 2);
			if (argInfo.length() > 0) {
				return argInfo.split("\\s{1,}");
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	@Override
	protected void validateData() throws JDependException {
		if (commandTableModel.getRowCount() == 0)
			throw new JDependException("你没有配置命令！");

	}

}
