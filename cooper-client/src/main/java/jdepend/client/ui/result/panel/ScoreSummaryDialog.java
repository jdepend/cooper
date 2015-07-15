package jdepend.client.ui.result.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.framework.UIPropertyConfigurator;
import jdepend.core.domain.WisdomAnalysisResult;
import jdepend.framework.exception.JDependException;
import jdepend.knowledge.domainanalysis.AdviseInfo;
import jdepend.knowledge.domainanalysis.StructureCategory;
import jdepend.model.JDependUnitMgr;

public class ScoreSummaryDialog extends JDialog implements ActionListener {

	private JDependCooper frame;

	// 时钟
	private Timer timer;

	@SuppressWarnings("restriction")
	public ScoreSummaryDialog(final JDependCooper frame) {

		this.frame = frame;

		WisdomAnalysisResult result = new WisdomAnalysisResult(JDependUnitMgr.getInstance().getResult());

		setSize(850, 80);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示
		setUndecorated(true);

		getContentPane().setLayout(new BorderLayout());

		AdviseInfo advise = null;
		try {
			advise = result.getAdvise(StructureCategory.Summary);
		} catch (JDependException e2) {
			e2.printStackTrace();
		}
		if (advise != null) {
			JLabel adviseLabel = new JLabel();
			adviseLabel.setFont(new Font("DialogInput", Font.BOLD, 16));
			adviseLabel.setText(advise.toString());

			CompoundBorder border = new CompoundBorder(new EtchedBorder(), new LineBorder(Color.BLACK));
			JPanel content = new JPanel() {
				@Override
				public String getToolTipText(MouseEvent e) {
					return "左键关闭";
				}
			};
			ToolTipManager.sharedInstance().registerComponent(content);
			content.setLayout(new GridBagLayout());
			content.setBorder(border);
			content.add(adviseLabel);

			this.getContentPane().add(content);

			content.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						ScoreSummaryDialog.this.dispose();
					}
				}
			});

			timer = new Timer(new UIPropertyConfigurator().getPopupSummary(), this);
			timer.start();
		} else {
			this.dispose();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.stop();
		this.dispose();
	}

}
