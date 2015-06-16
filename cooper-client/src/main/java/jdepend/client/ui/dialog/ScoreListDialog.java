package jdepend.client.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jdepend.client.core.command.CommandAdapter;
import jdepend.client.core.command.CommandAdapterMgr;
import jdepend.client.report.way.htmlui.ExportHTML;
import jdepend.client.report.way.textui.TextSummaryPrinter;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.util.AnalysisResultExportUtil;
import jdepend.core.local.score.ScoreFacade;
import jdepend.core.local.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.FileUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;

public final class ScoreListDialog extends CooperDialog {

	public ScoreListDialog(final JDependCooper frame) {

		super("分数列表");

		this.add(BorderLayout.CENTER, new ScoreListPanel(frame) {
			@Override
			protected JScrollPane initTable() throws JDependException {
				JScrollPane pane = super.initTable();
				final JPopupMenu popupMenu = new JPopupMenu();
				JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
				deleteItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (JOptionPane.showConfirmDialog(ScoreListDialog.this, "您是否确认删除？", "提示",
								JOptionPane.YES_NO_OPTION) == 0) {
							try {
								delete();
								refresh();
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(ScoreListDialog.this, ex.getMessage(), "alert",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				});
				popupMenu.add(deleteItem);

				JMenuItem viewResultItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewResult));
				viewResultItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							viewResult();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(ScoreListDialog.this, ex.getMessage(), "alert",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				});
				popupMenu.add(viewResultItem);
				popupMenu.addSeparator();

				JMenuItem exportTXTItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAsTxt));
				exportTXTItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							exportTXT();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(ScoreListDialog.this, ex.getMessage(), "alert",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				});
				popupMenu.add(exportTXTItem);

				JMenuItem exportHTMLItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAsHTML));
				exportHTMLItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							exportHTML();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(ScoreListDialog.this, ex.getMessage(), "alert",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				});
				popupMenu.add(exportHTMLItem);

				JMenuItem exportListItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ExportList));
				exportListItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							exportList();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(ScoreListDialog.this, ex.getMessage(), "alert",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				popupMenu.add(exportListItem);
				popupMenu.addSeparator();

				JMenuItem exportItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Export));
				exportItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							export();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(ScoreListDialog.this, ex.getMessage(), "alert",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				popupMenu.add(exportItem);

				scoreListTable.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						JTable table = (JTable) e.getSource();
						if (e.getButton() == 3) {
							popupMenu.show(table, e.getX(), e.getY());
						}
					}
				});

				return pane;
			}

			private void delete() throws JDependException {
				for (Object id : this.getCurrentes()) {
					ScoreFacade.delete((String) id);
				}
			}

			private void viewResult() throws JDependException {
				if (this.getCurrentes().size() > 1) {
					throw new JDependException("请选择一条信息");
				}
				final AnalysisResult result = ScoreFacade.getTheResult((String) this.getId());
				if (result != null) {
					new Thread() {
						@Override
						public void run() {
							try {
								// 清空历史
								frame.clearPriorResult();
								// 显示结果
								JDependUnitMgr.getInstance().setResult(result);
								frame.getResultPanelWrapper().showResults(true);
							} catch (JDependException e) {
								e.printStackTrace();
								frame.showStatusError(e.getMessage());
							}
						}
					}.start();
					ScoreListDialog.this.dispose();
				} else {
					throw new JDependException("未存储结果信息");
				}
			}

			private void exportTXT() throws JDependException {
				if (this.getCurrentes().size() > 1) {
					throw new JDependException("请选择一条信息");
				}
				JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
				int rtn = jFileChooser.showSaveDialog(null);
				if (rtn == JFileChooser.APPROVE_OPTION) {
					File f = jFileChooser.getSelectedFile();

					OutputStream stream = null;
					try {
						// 获取结果
						AnalysisResult result = ScoreFacade.getTheResult((String) this.getId());
						// 生成报告
						TextSummaryPrinter printer = new TextSummaryPrinter();
						stream = new ByteArrayOutputStream();
						printer.setStream(stream);
						printer.printBasic(result);
						StringBuilder detailText = new StringBuilder(stream.toString());
						stream.close();
						// 创建TXT文件
						String targetFile = null;
						if (f.getAbsolutePath().endsWith(".txt")) {
							targetFile = f.getAbsolutePath();
						} else {
							targetFile = f.getAbsolutePath() + ".txt";
						}
						// 保存文件
						FileUtil.saveFileContent(targetFile, detailText);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (stream != null) {
							try {
								stream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			private void exportHTML() throws JDependException {
				if (this.getCurrentes().size() > 1) {
					throw new JDependException("请选择一条信息");
				}
				JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
				int rtn = jFileChooser.showSaveDialog(null);
				if (rtn == JFileChooser.APPROVE_OPTION) {
					// 获取结果
					AnalysisResult result = ScoreFacade.getTheResult((String) this.getId());
					File f = jFileChooser.getSelectedFile();
					// 创建HTML文件
					String targetFile = null;
					if (f.getAbsolutePath().endsWith(".html")) {
						targetFile = f.getAbsolutePath();
					} else {
						targetFile = f.getAbsolutePath() + ".html";
					}
					FileUtil.saveFileContent(targetFile, ExportHTML.export(result));
					// copy CSS
					String fileFromPath = System.getProperty("user.dir") + File.separator + ExportHTML.getCSSFileName();
					String fileToPath = f.getParent() + File.separator
							+ FileUtil.getFileName(ExportHTML.getCSSFileName());
					FileUtil.copyFile(fileFromPath, fileToPath);
					JOptionPane.showMessageDialog(null, "保存成功。", "alert", JOptionPane.INFORMATION_MESSAGE);
				}
			}

			private void exportList() throws JDependException {

				StringBuilder contentList = new StringBuilder();
				List<ScoreInfo> scoreList = ScoreFacade.getScoreList();
				Collections.sort(scoreList);
				for (ScoreInfo scoreInfo : scoreList) {
					contentList.append(scoreInfo.group);
					contentList.append("	");
					contentList.append(scoreInfo.command);
					contentList.append("	");
					contentList.append(scoreInfo.lc);
					contentList.append("	");
					contentList.append(scoreInfo.distance);
					contentList.append("	");
					contentList.append(scoreInfo.balance);
					contentList.append("	");
					contentList.append(scoreInfo.encapsulation);
					contentList.append("	");
					contentList.append(scoreInfo.relation);
					contentList.append("	");
					contentList.append(scoreInfo.score);
					// contentList.append("	");
					// contentList.append(scoreInfo.oo);
					contentList.append("	");
					contentList.append(scoreInfo.getCreateDate());
					contentList.append("\n");
				}

				JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));

				int result = jFileChooser.showSaveDialog(null);

				if (result == JFileChooser.APPROVE_OPTION) {
					File f = jFileChooser.getSelectedFile();
					FileUtil.saveFileContent(f.getAbsolutePath(), contentList, "GBK");
					JOptionPane.showMessageDialog(null, "导出分数列表成功。", "alert", JOptionPane.INFORMATION_MESSAGE);
				}
			}

			private void export() throws JDependException, IOException {
				if (this.getCurrentes().size() > 1) {
					throw new JDependException("请选择一条信息");
				}
				AnalysisResult result = ScoreFacade.getTheResult((String) this.getId());
				AnalysisResultExportUtil.exportResult(frame, result);
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.createReCalculateButton());
		buttonPanel.add(this.createCloseButton());
		this.add(BorderLayout.SOUTH, buttonPanel);

	}

	public void reCalculate() throws JDependException {
		List<ScoreInfo> scoreList = ScoreFacade.getScoreList();
		for (ScoreInfo scoreInfo : scoreList) {
			try {
				CommandAdapter adapter = CommandAdapterMgr.getInstance().getTheCommandAdapter(scoreInfo.group,
						scoreInfo.command);
				adapter.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Component createReCalculateButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_ReCalculate));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					reCalculate();
				} catch (JDependException ex) {
					ex.printStackTrace();
					Component source = (Component) e.getSource();
					JOptionPane.showMessageDialog(source, "重新计算失败", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}
}
