package jdepend.framework.ui.action;

import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.ui.component.TextViewer;

/**
 * 异步执行Action（具有进度条跟踪）
 * 
 * @author wangdg
 * 
 */
public abstract class AsynAction extends AbstractAction {

	private String name;

	private JDependFrame frame;

	private Map<String, JComponent> result;

	private static running running;

	public AsynAction(JDependFrame frame, String name) {
		super();
		this.frame = frame;
		this.name = name;
		if (running == null) {
			running = new running();
		}
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		synchronized (running) {
			if (running.running) {
				JOptionPane.showMessageDialog(frame, "正在有命令运行！", "alert", JOptionPane.INFORMATION_MESSAGE);
				return;
			} else {
				running.running = true;
			}
		}
		result = new LinkedHashMap<String, JComponent>();
		try {
			this.beforeAnalyse();
			this.frame.startProgressMonitor(this.getProcess());
			// 在独立的线程中执行分析
			new Thread() {
				public void run() {
					try {
						// 执行核心服务
						analyse(e);
						// 截止进度条
						frame.stopProgressMonitor();
						stopProgressLater();
						// 显示第一批结果
						frame.show(result);
						// 显示后续结果
						new Thread() {
							public void run() {
								try {
									showResultLater();
								} catch (JDependException e2) {
									e2.printStackTrace();
									result = createErrorResult(e2);
									synchronized (running) {
										running.running = false;
									}
									// 显示结果
									frame.show(result);
								}
								// 设置执行截止标志
								synchronized (running) {
									running.running = false;
								}
							}
						}.start();

					} catch (Exception e1) {
						e1.printStackTrace();
						result = createErrorResult(e1);
						synchronized (running) {
							running.running = false;
						}
						// 显示结果
						frame.show(result);
						// 截止进度条
						frame.stopProgressMonitor();
					}
				}
			}.start();
		} catch (JDependException e2) {
			e2.printStackTrace();
			result = createErrorResult(e2);
			synchronized (running) {
				running.running = false;
			}
			// 显示结果
			frame.show(result);
		}
	}

	protected void stopProgressLater() {
		frame.showStatusMessage("完成了[" + name + "]分析。");
	}

	protected void beforeAnalyse() throws JDependException {
	}

	protected void showResultLater() throws JDependException {
	}

	protected void addResult(String label, JComponent component) {
		result.put(label, component);
	}

	protected void addResults(Map<String, ? extends JComponent> results) {
		result.putAll(results);
	}

	protected void progress() {
		this.frame.progress();
	}

	protected Map<String, JComponent> createErrorResult(Exception e) {
		Map<String, JComponent> r = new HashMap<String, JComponent>();

		StringBuilder error = new StringBuilder(100);
		error.append("[" + name + "]分析运行失败！\n");

		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		error.append(sw.toString());

		TextViewer errorText = new TextViewer();
		errorText.setText(error.toString());

		r.put("error", new JScrollPane(errorText));

		return r;
	}

	protected abstract int getProcess() throws JDependException;

	protected abstract void analyse(ActionEvent e) throws JDependException;

	class running {
		boolean running = false;
	}

}
