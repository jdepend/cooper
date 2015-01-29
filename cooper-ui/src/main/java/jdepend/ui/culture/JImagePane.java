package jdepend.ui.culture;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;

import jdepend.framework.ui.ImagePanel;
import jdepend.framework.ui.JDependUIUtil;
import jdepend.ui.JDependCooper;
import jdepend.ui.framework.AlphaPane;

public class JImagePane extends AlphaPane {
	/**
	 * 居中
	 */
	public static final String CENTRE = "Centre";

	/**
	 * 平铺
	 */
	public static final String TILED = "Tiled";

	/**
	 * 拉伸
	 */
	public static final String SCALED = "Scaled";

	/**
	 * 背景图片
	 */
	private Image backgroundImage;

	/**
	 * 背景图片显示模式
	 */
	private String imageDisplayMode;

	/**
	 * 背景图片显示模式索引（引入此属性有助于必要时扩展）
	 */
	private int modeIndex;

	private JDependCooper frame;

	private List<TipImage> images = new ArrayList<TipImage>();;

	private int imageIndex = 0;

	private int imageTotal = 0;

	private String oldTip;

	private boolean isLeftPos;

	private Image prevArrow = JDependUIUtil.getImage("previous.png");
	private Image nextArrow = JDependUIUtil.getImage("next.png");
	
	
	private final static String backgroundImageName = "mascot.jpg";

	/**
	 * 构造一个没有背景图片的JImagePane
	 */
	public JImagePane(final JDependCooper frame) {
		this(frame, false);
	}

	public JImagePane(final JDependCooper frame, final boolean inner) {

		this(null, SCALED);
		this.frame = frame;
		this.setPreferredSize(new Dimension(JDependCooper.LeftWidth, 190));
		this.initImageList();
		setBackgroundImage(images.get(0).image);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JImagePane obj = (JImagePane) e.getSource();
				if (isLeftPos) {
					obj.prevImage();
				} else {
					obj.nextImage();
				}
				if (!inner) {
					frame.getResultPanel().addResult("MINICooper", images.get(imageIndex).content);
				}
				frame.showStatusMessage(images.get(imageIndex).tip);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				oldTip = frame.getStatusField().getText();
				frame.showStatusMessage(images.get(imageIndex).tip);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				frame.showStatusMessage(oldTip);
			}
		});

		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				if (e.getX() < JImagePane.this.getWidth() / 2) {
					JImagePane.this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(prevArrow,
							new Point(0, 0), "上一张"));
					isLeftPos = true;
				} else {
					JImagePane.this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(nextArrow,
							new Point(0, 0), "下一张"));
					isLeftPos = false;
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});

		ToolTipManager.sharedInstance().registerComponent(this);

		if (!inner) {
			frame.getResultPanel().addResult("MINICooper", images.get(0).content);
		}
	}

	/**
	 * 构造一个具有指定背景图片和指定显示模式的JImagePane
	 * 
	 * @param image
	 *            背景图片
	 * @param modeName
	 *            背景图片显示模式
	 */
	public JImagePane(Image image, String modeName) {
		super();
		setBackgroundImage(image);
		setImageDisplayMode(modeName);
	}

	@Override
	public String getToolTipText(MouseEvent e) {
		return this.images.get(this.imageIndex).tip;
	}

	public void nextImage() {
		this.imageIndex++;
		if (this.imageIndex >= this.getImageTotal()) {
			this.imageIndex = 0;
		}
		this.setBackgroundImage(images.get(this.imageIndex).image);
		this.closeTimer();
	}

	public void prevImage() {
		this.imageIndex--;
		if (this.imageIndex < 0) {
			this.imageIndex = this.getImageTotal() - 1;
		}
		this.setBackgroundImage(images.get(this.imageIndex).image);
		this.closeTimer();
	}

	public List<TipImage> getImages() {
		return images;
	}

	/**
	 * 设置背景图片
	 * 
	 * @param image
	 *            背景图片
	 */
	public void setBackgroundImage(Image image) {
		this.backgroundImage = image;
		this.repaint();
	}

	/**
	 * 获取背景图片
	 * 
	 * @return 背景图片
	 */
	public Image getBackgroundImage() {
		return backgroundImage;
	}

	/**
	 * 设置背景图片显示模式
	 * 
	 * @param modeName
	 *            模式名称，取值仅限于ImagePane.TILED ImagePane.SCALED ImagePane.CENTRE
	 */
	public void setImageDisplayMode(String modeName) {
		if (modeName != null) {
			modeName = modeName.trim();

			// 居中
			if (modeName.equalsIgnoreCase(CENTRE)) {
				this.imageDisplayMode = CENTRE;
				modeIndex = 0;
			}
			// 平铺
			else if (modeName.equalsIgnoreCase(TILED)) {
				this.imageDisplayMode = TILED;
				modeIndex = 1;
			}
			// 拉伸
			else if (modeName.equalsIgnoreCase(SCALED)) {
				this.imageDisplayMode = SCALED;
				modeIndex = 2;
			}

			this.repaint();
		}
	}

	/**
	 * 获取背景图片显示模式
	 * 
	 * @return 显示模式
	 */
	public String getImageDisplayMode() {
		return imageDisplayMode;
	}

	/**
	 * 绘制组件
	 * 
	 * @see javax.swing.JComponent#paintComponent(Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 如果设置了背景图片则显示
		if (backgroundImage != null) {
			int width = this.getWidth();
			int height = this.getHeight();
			int imageWidth = backgroundImage.getWidth(this);
			int imageHeight = backgroundImage.getHeight(this);

			switch (modeIndex) {
			// 居中
			case 0: {
				int x = (width - imageWidth) / 2;
				int y = (height - imageHeight) / 2;
				g.drawImage(backgroundImage, x, y, this);
				break;
			}
			// 平铺
			case 1: {
				for (int ix = 0; ix < width; ix += imageWidth) {
					for (int iy = 0; iy < height; iy += imageHeight) {
						g.drawImage(backgroundImage, ix, iy, this);
					}
				}

				break;
			}
			// 拉伸
			case 2: {
				g.drawImage(backgroundImage, 0, 0, width, height, this);
				break;
			}
			}
		}
	}

	protected void initImageList() {

		this.setImageTotal(7);

		images = new ArrayList<TipImage>(this.getImageTotal());

		images.add(new TipImage(JDependUIUtil.getImage("Cooper1.jpg"), "1962 Austin Mini Cooper", this.getContent1()));
		images.add(new TipImage(JDependUIUtil.getImage("Cooper2.jpg"), "1964 Austin Mini Super", this.getContent2()));
		images.add(new TipImage(JDependUIUtil.getImage("Cooper3.jpg"), "1968 Morris Mini SMK II", this.getContent3()));
		images.add(new TipImage(JDependUIUtil.getImage("Cooper4.jpg"), "老款Mini Cooper S", this.getContent4()));
		images.add(new TipImage(JDependUIUtil.getImage("Cooper5.jpg"), "老款Mini Knightsbridge", this.getContent5()));
		images.add(new TipImage(JDependUIUtil.getImage("Cooper6.jpg"), "老款Mini Cooper", this.getContent6()));
		images.add(new TipImage(JDependUIUtil.getImage("Cooper7.jpg"), "2012款 MINI Cooper跑车", this.getContent7()));
	}

	private JComponent getContent1() {

		JPanel panel = new WelcomePanel();

		JScrollPane rtn = new JScrollPane(panel);
		rtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		return rtn;
	}

	private JComponent getContent2() {

		JPanel panel = new ImagePanel(backgroundImageName);
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("1960年第一部Mini登陆美国.全球共计售出63,000部Mini汽车.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 1));

		panel.add(new JLabel("\n"), createConstraints(1, 2));

		nameLabel = new JLabel("1961年Mini声名大噪!全球刮起一阵\"Mini旋风\"!在此同时,Mini Cooper997问世.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 3));

		JScrollPane rtn = new JScrollPane(panel);
		rtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return rtn;
	}

	private JComponent getContent3() {

		JPanel panel = new ImagePanel(backgroundImageName);
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("1963年Mini在Alpine Rally赛事中赢得冠军.Cooper S车型的获胜,也使得Mini被");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 1));

		nameLabel = new JLabel("定位成性能取向的车款.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 2));

		JScrollPane rtn = new JScrollPane(panel);
		rtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return rtn;
	}

	private JComponent getContent4() {

		JPanel panel = new ImagePanel(backgroundImageName);
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("1964年Mini赢得蒙地卡罗大赛冠军.而在1965,(1966)及1967年的赛事中,Mini依");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 1));

		nameLabel = new JLabel("旧蝉连冠军.(注)1966年因规格不符,取消资格.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 2));

		panel.add(new JLabel("\n"), createConstraints(1, 3));

		nameLabel = new JLabel("1965年Mini之年!发自Mini的灵感,伦敦的一位服装设计师设计出了风靡全球的");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 4));

		nameLabel = new JLabel("迷你裙.同时,Mini也在众多赛事中屡传捷报.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 5));

		JScrollPane rtn = new JScrollPane(panel);
		rtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return rtn;
	}

	private JComponent getContent5() {

		JPanel panel = new ImagePanel(backgroundImageName);
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("1967年Mini Moke (jeep-style)成为电视影集\"The Prisoner\"的指定用车.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 1));

		JScrollPane rtn = new JScrollPane(panel);
		rtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return rtn;
	}

	private JComponent getContent6() {

		JPanel panel = new ImagePanel(backgroundImageName);
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("1994年BMW Group购并Rover Group, Mini, Land Rover, Rover及MG等品牌,正式");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 1));

		nameLabel = new JLabel("成为BMW Group旗下一员.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 2));

		panel.add(new JLabel("\n"), createConstraints(1, 3));

		nameLabel = new JLabel("1995年Mini被Autocar票选为欧洲的\"世纪之车\".Mini的横置引擎,迷人的外型及");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 4));

		nameLabel = new JLabel("令人印象深刻的操控性,彻底改变人们对于汽车驾驶的既定印象.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 5));

		panel.add(new JLabel("\n"), createConstraints(1, 6));

		nameLabel = new JLabel("1996年，《古典与汽车运动》杂志评Mini为“古典轿车的首选”，宝马宣布");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 7));

		nameLabel = new JLabel("全新设计MINI，MINI名称变为全部大写字母。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 8));

		panel.add(new JLabel("\n"), createConstraints(1, 9));

		nameLabel = new JLabel("1997年，法兰克福车展上，全新的MINI概念车初露端倪就备受褒奖。MINI支");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 10));

		nameLabel = new JLabel("持率上升。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 11));

		panel.add(new JLabel("\n"), createConstraints(1, 12));

		nameLabel = new JLabel("1998年，MINI被《吉尼斯世界记录大全》评为“英国史上最成功的汽车”，");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 13));

		nameLabel = new JLabel("这使MINI的地位超过了Jaguar、Bentley、Aston Martin等老牌英国汽车。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 14));

		panel.add(new JLabel("\n"), createConstraints(1, 15));

		nameLabel = new JLabel("1999年为庆祝MINI上市四十周年,纽约的25位MINI车迷同时挤进一部MINI车，");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 16));

		nameLabel = new JLabel("内,成功打破记录.同时，130位国际著名撰稿人投票评选MINI为“欧洲世纪之");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 17));

		nameLabel = new JLabel("车”。仅仅位于T型车之后名列第二。　");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 18));

		panel.add(new JLabel("\n"), createConstraints(1, 19));

		nameLabel = new JLabel("2000年，9月新款MINI在蒙特罗车展种正式亮相,而旧款MINI也宣告停产，至");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 20));

		nameLabel = new JLabel("此全球生产出了5,387,862辆MINI汽车；12月，John Cooper(1923-2000)逝世。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 21));

		panel.add(new JLabel("\n"), createConstraints(1, 22));

		nameLabel = new JLabel("2001年MINI回来了!一月份,新款MINI再度在北美车展中亮相.《汽车》杂志");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 23));

		nameLabel = new JLabel("评MINI为“历史上最伟大的汽车”。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 24));

		panel.add(new JLabel("\n"), createConstraints(1, 25));

		nameLabel = new JLabel("2002年，MINI Cooper来到中国。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 26));

		JScrollPane rtn = new JScrollPane(panel);
		rtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return rtn;
	}

	private JComponent getContent7() {

		JPanel panel = new ImagePanel(backgroundImageName);
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = null;

		nameLabel = new JLabel("2011年6月21日MINI发布了一款2012款MINI Cooper轿跑车。这款车采用1.6升直列4缸双涡");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 1));

		nameLabel = new JLabel("轮增压发动机，最高能达184马力/5500转，0到100公里/小时只需6.4秒，最高时速240");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 2));

		nameLabel = new JLabel("公里，按欧盟标准平均油耗在7.1公升，每公里二氧化碳排放165克。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 18));
		panel.add(nameLabel, createConstraints(1, 3));

		JPanel igPanel = new JPanel(new FlowLayout());
		igPanel.add(new JImagePane(frame, true) {
			@Override
			protected void initImageList() {
				this.setImageTotal(11);
				this.setPreferredSize(new Dimension(400, 300));

				this.getImages().add(new TipImage(JDependUIUtil.getImage("2012_1.jpg"), "正面", null));
				this.getImages().add(new TipImage(JDependUIUtil.getImage("2012_2.jpg"), "侧面", null));
				this.getImages().add(new TipImage(JDependUIUtil.getImage("2012_3.jpg"), "正面", null));
				this.getImages().add(new TipImage(JDependUIUtil.getImage("2012_4.jpg"), "正面", null));
				this.getImages().add(new TipImage(JDependUIUtil.getImage("2012_5.jpg"), "背面", null));
				this.getImages().add(new TipImage(JDependUIUtil.getImage("2012_6.jpg"), "驾驶室", null));
				this.getImages().add(new TipImage(JDependUIUtil.getImage("2012_7.jpg"), "车门", null));
				this.getImages().add(new TipImage(JDependUIUtil.getImage("2012_8.jpg"), "发动机", null));
				this.getImages().add(new TipImage(JDependUIUtil.getImage("2012_9.jpg"), "踏板", null));
				this.getImages().add(new TipImage(JDependUIUtil.getImage("2012_10.jpg"), "驾驶室", null));
				this.getImages().add(new TipImage(JDependUIUtil.getImage("2012_11.jpg"), "尾翼", null));
			}
		});

		panel.add(igPanel, createConstraints(1, 4));

		JScrollPane rtn = new JScrollPane(panel);
		rtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return rtn;
	}

	public void setImageTotal(int imageTotal) {
		this.imageTotal = imageTotal;
	}

	public int getImageTotal() {
		return imageTotal;
	}

	/**
	 * Creates and returns a grid bag constraint with the specified x and y
	 * values.
	 * 
	 * @param x
	 *            X-coordinate.
	 * @param y
	 *            Y-coordinate.
	 * @return GridBagConstraints
	 */
	private GridBagConstraints createConstraints(int x, int y) {

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;

		return constraints;
	}

	class TipImage {
		Image image;
		String tip;
		JComponent content;

		public TipImage(Image image, String tip, JComponent content) {
			this.image = image;
			this.tip = tip;
			this.content = content;
		}
	}
}
