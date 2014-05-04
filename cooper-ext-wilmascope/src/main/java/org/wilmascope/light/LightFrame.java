/**
 * @author Christine
 */
package org.wilmascope.light;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Enumeration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.PointLight;
import javax.media.j3d.SpotLight;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * This class is an gui to control light sources, including create, delete, turn
 * on/off, and change the lights' parameters
 * 
 */

public class LightFrame extends JFrame implements ListSelectionListener {
	// left button pane
	private JButton newButton = new JButton("new");
	private JButton deleteButton = new JButton("Delete");
	private JButton Save = new JButton("Save");
	private JButton Open = new JButton("Open");

	private JPanel contentPane = new JPanel();
	private JPanel rightPane = new JPanel();
	private JPanel leftPane = new JPanel();
	JScrollPane listScrollPane;
	private JSplitPane splitPane;

	// indicate the light panel currently using
	private JPanel lightPane;
	// four light panel
	private DirectionalLightPanel dirPane = new DirectionalLightPanel(this);
	private PointLightPanel pointPane = new PointLightPanel(this);
	private SpotLightPanel spotPane = new SpotLightPanel(this);
	private AmbientLightPanel ambPane = new AmbientLightPanel();
	// /////////////
	private JRadioButton[] lightTypes = new JRadioButton[4];
	private JFileChooser fc;
	private JLabel helpText = new JLabel();
	private String text;
	// 4 light list
	private JList ambList;
	private JList dirList;
	private JList pointList;
	private JList spotList;
	private JList selectedList;

	private DefaultListModel ambListModel = new DefaultListModel();
	private DefaultListModel dirListModel = new DefaultListModel();
	private DefaultListModel pointListModel = new DefaultListModel();
	private DefaultListModel spotListModel = new DefaultListModel();

	// parameter of the lights
	private Vector3f direction = new Vector3f();
	private Point3f position = new Point3f();
	private Point3f attenuation = new Point3f();
	private float SpreadAngle;
	private float Concentration;
	private Color colour = Color.white;
	private Bounds bounds;

	private LightManager lightManager;
	// indicate the lighttype currently select
	private String lightType;
	// for the ok cancel button pane
	private JButton OK = new JButton("OK");
	private JButton onButton = new JButton("on");
	private JButton offButton = new JButton("off");
	private JButton colorButton = new JButton("Color");
	private JPanel rightButtonPane = new JPanel();

	// light visualization
	// all the arrows, spheres and mouse controls are attached to visualGroup
	private BranchGroup visualGroup = new BranchGroup();
	private SpotLightCone cone;
	private PointLightSphere sphere;
	private BranchGroup behaviorGroup;

	private DirLightMouseCtrl dirLightMouseCtrl = new DirLightMouseCtrl(dirPane);
	private BranchGroup dirMouseCtrlGroup = new BranchGroup();
	private SpotLightMouseCtrl spotMouseCtrl;
	private PointLightMouseCtrl pointLightMouseCtrl = new PointLightMouseCtrl(pointPane);
	private BranchGroup pointLightMouseCtrlGroup = new BranchGroup();
	private SpotLightMouseCtrl spotLightMouseCtrl = new SpotLightMouseCtrl(spotPane);
	private BranchGroup spotLightMouseCtrlGroup = new BranchGroup();

	// indicate the new button is pressed

	public LightFrame(LightManager lightManager) {
		super("Light Source");
		this.lightManager = lightManager;
		bounds = lightManager.getBoundingSphere();

		initLightList();
		initVisualObj();

		// right
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
		rightPane.add(createRadioButtonPane());
		lightPane = dirPane;
		rightPane.add(lightPane);
		lightType = "Directional";
		visualLight();
		showDirectionalLight(0);
		createRightButtonPane();
		rightPane.add(rightButtonPane);

		// left
		leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));
		listScrollPane = new JScrollPane(dirList);
		leftPane.add(listScrollPane);
		leftPane.add(Box.createRigidArea(new Dimension(0, 10)));
		leftPane.add(createLeftButtonPane());
		leftPane.add(Box.createRigidArea(new Dimension(0, 10)));
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, rightPane);
		splitPane.setDividerLocation(150);

		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		splitPane.setBorder(BorderFactory.createRaisedBevelBorder());
		splitPane.setSize(370, 380);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(splitPane);
		contentPane.add(helpText);
		setContentPane(contentPane);

		disableOK();

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cleanup();
			}
		});

		this.setResizable(false);
		// file initialize
		fc = new JFileChooser();
		fc.addChoosableFileFilter(new PropertyFileFilter());
		fc.setCurrentDirectory(new File("c:/christine/Wilma/lib"));
		// help text label
		text = "<html>\n" + "<ul>\n" + "<li><font color=blue>Left mouse+drag to change the light's direction</font>\n"
				+ "<li><font color=blue>Right mouse+drag to change the arrow's position</font>\n" + "</ul>\n";
		helpText.setText(text);
		helpText.setPreferredSize(new Dimension(370, 100));
		helpText.setVerticalAlignment(SwingConstants.CENTER);
		helpText.setHorizontalAlignment(SwingConstants.LEFT);

	}

	// init the light list to appear in the left panel
	private void initLightList() {
		for (int i = 0; i < lightManager.getAmbLightVector().size(); i++)
			ambListModel.addElement("Ambient Light" + i);

		for (int i = 0; i < lightManager.getDirLightVector().size(); i++)
			dirListModel.addElement("Directional Light" + i);

		for (int i = 0; i < lightManager.getPointLightVector().size(); i++)
			pointListModel.addElement("Point Light" + i);
		for (int i = 0; i < lightManager.getSpotLightVector().size(); i++)
			spotListModel.addElement("Spot Light" + i);
		// Create the list and put it in a scroll pane
		ambList = new JList(ambListModel);
		ambList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (ambListModel.size() > 0)
			ambList.setSelectedIndex(0);
		ambList.addListSelectionListener(this);

		dirList = new JList(dirListModel);
		dirList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (dirListModel.size() > 0)
			dirList.setSelectedIndex(0);
		dirList.addListSelectionListener(this);

		pointList = new JList(pointListModel);
		pointList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (pointListModel.size() > 0)
			pointList.setSelectedIndex(0);
		pointList.addListSelectionListener(this);

		spotList = new JList(spotListModel);
		spotList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (spotListModel.size() > 0)
			spotList.setSelectedIndex(0);
		spotList.addListSelectionListener(this);

		selectedList = dirList;

	}

	// initialize the visualGroup, the light mouse control behavior
	private void initVisualObj() {
		// the branch group to attach the arrows, spheres,cones
		visualGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		visualGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		visualGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		lightManager.getBranchGroup().addChild(visualGroup);

		// for the light Behavior
		dirMouseCtrlGroup = new BranchGroup();
		dirMouseCtrlGroup.setCapability(BranchGroup.ALLOW_DETACH);
		dirLightMouseCtrl.setSchedulingBounds(bounds);
		dirMouseCtrlGroup.addChild(dirLightMouseCtrl);

		// for the point light
		pointLightMouseCtrlGroup = new BranchGroup();
		pointLightMouseCtrlGroup.setCapability(BranchGroup.ALLOW_DETACH);
		pointLightMouseCtrl.setSchedulingBounds(bounds);
		pointLightMouseCtrlGroup.addChild(pointLightMouseCtrl);
		// for the spot light
		spotLightMouseCtrlGroup = new BranchGroup();
		spotLightMouseCtrlGroup.setCapability(BranchGroup.ALLOW_DETACH);
		spotLightMouseCtrl.setSchedulingBounds(bounds);
		spotLightMouseCtrlGroup.addChild(spotLightMouseCtrl);
	}

	private JPanel createLeftButtonPane() {
		JPanel buttonPane = new JPanel();
		buttonPane.setPreferredSize(new Dimension(150, 0));
		GridLayout layout = new GridLayout(2, 2);
		layout.setHgap(5);
		layout.setVgap(25);
		buttonPane.setLayout(layout);
		buttonPane.add(newButton);
		newButton.addActionListener(new NewButtonListener());
		buttonPane.add(deleteButton);
		deleteButton.addActionListener(new DeleteButtonListener());
		buttonPane.add(Save);
		Save.addActionListener(new SaveButtonListerner());
		buttonPane.add(Open);
		Open.addActionListener(new OpenButtonListerner());

		return buttonPane;
	}

	private JPanel createRadioButtonPane() {
		ButtonGroup choice = new ButtonGroup();
		RadioButtonListener myListener = new RadioButtonListener();
		lightTypes[0] = new JRadioButton("Ambient Light");
		lightTypes[0].setActionCommand("Ambient");
		lightTypes[1] = new JRadioButton("Directional Light", true);
		lightTypes[1].setActionCommand("Directional");
		lightTypes[2] = new JRadioButton("Point Light");
		lightTypes[2].setActionCommand("Point");
		lightTypes[3] = new JRadioButton("Spot Light");
		lightTypes[3].setActionCommand("Spot");
		JPanel radioButtonPane = new JPanel();
		radioButtonPane.setLayout(new GridLayout(4, 1));
		for (int i = 0; i < 4; i++) {
			radioButtonPane.add(lightTypes[i]);
			lightTypes[i].addActionListener(myListener);
			choice.add(lightTypes[i]);
		}
		radioButtonPane.setBorder(BorderFactory.createTitledBorder("Select a light source type"));

		return radioButtonPane;
	}

	// show the selected lights when selection changed
	public void valueChanged(ListSelectionEvent e) {
		int index = selectedList.getSelectedIndex();

		if (lightType == "Ambient")
			showAmbientLight(index);
		if (lightType == "Directional")
			showDirectionalLight(index);
		if (lightType == "Point")
			showPointLight(index);
		if (lightType == "Spot")
			showSpotLight(index);
		if (index >= 0)
			deleteButton.setEnabled(true);

	}

	// use different panel when different kind of lights was selected
	class RadioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			lightType = e.getActionCommand();
			// when changes to display a different light type, detach the
			// orginal 3D shapes that
			// represent the lights and detach the mouse behavior to control the
			// lights
			cleanup();
			disableOK();

			if (lightType == "Ambient") {
				rightPane.setVisible(false);
				rightPane.remove(lightPane);
				rightPane.remove(rightButtonPane);
				lightPane = ambPane;
				rightPane.add(lightPane);
				rightPane.add(rightButtonPane);
				rightPane.setVisible(true);

				leftPane.setVisible(false);
				selectedList = ambList;
				listScrollPane.setViewportView(selectedList);
				leftPane.setVisible(true);
				if (ambListModel.size() > 0) {
					deleteButton.setEnabled(true);
					showAmbientLight(0);
				} else
					showAmbientLight(-1);
				helpText.setText("");
			}
			if (lightType == "Directional") {
				rightPane.setVisible(false);
				rightPane.remove(lightPane);
				rightPane.remove(rightButtonPane);
				lightPane = dirPane;
				rightPane.add(lightPane);
				rightPane.add(rightButtonPane);
				rightPane.setVisible(true);

				leftPane.setVisible(false);
				selectedList = dirList;
				listScrollPane.setViewportView(selectedList);
				leftPane.setVisible(true);
				visualLight();
				if (dirListModel.size() > 0) {
					deleteButton.setEnabled(true);
					showDirectionalLight(0);
				} else {
					showDirectionalLight(-1);
					helpText.setText("  ");
				}

			}
			if (lightType == "Point") {

				rightPane.setVisible(false);
				rightPane.remove(lightPane);
				rightPane.remove(rightButtonPane);
				lightPane = pointPane;
				rightPane.add(lightPane);
				rightPane.add(rightButtonPane);
				rightPane.setVisible(true);

				leftPane.setVisible(false);
				selectedList = pointList;
				listScrollPane.setViewportView(selectedList);
				leftPane.setVisible(true);
				visualLight();
				if (pointListModel.size() > 0) {
					deleteButton.setEnabled(true);
					showPointLight(0);

				} else {
					showPointLight(-1);
					helpText.setText("  ");
				}
			}
			if (lightType == "Spot") {

				rightPane.setVisible(false);
				rightPane.remove(lightPane);
				rightPane.remove(rightButtonPane);
				lightPane = spotPane;
				rightPane.add(lightPane);
				rightPane.add(rightButtonPane);
				rightPane.setVisible(true);

				leftPane.setVisible(false);
				selectedList = spotList;
				listScrollPane.setViewportView(selectedList);
				leftPane.setVisible(true);
				visualLight();
				if (spotListModel.size() > 0) {
					deleteButton.setEnabled(true);
					showSpotLight(0);

				} else {
					showSpotLight(-1);
					helpText.setText("  ");
				}

			}
		}

	}

	private void showAmbientLight(int index) {

		WilmaLight w;
		Color3f colour = new Color3f();
		AmbientLight ambLight;

		if ((index < ambListModel.size()) && (index >= 0)) {
			colorButton.setEnabled(true);
			w = (WilmaLight) lightManager.getAmbLightVector().get(index);
			ambLight = (AmbientLight) w.getLight();
			ambLight.getColor(colour);
			ambPane.label.setBackground(new Color(colour.x, colour.y, colour.z));
			if (ambLight.getEnable() == true) {
				onButton.setEnabled(false);
				offButton.setEnabled(true);
			} else {
				onButton.setEnabled(true);
				offButton.setEnabled(false);
			}

		} else {
			ambPane.clear();
			deleteButton.setEnabled(false);
			onButton.setEnabled(false);
			offButton.setEnabled(false);
			colorButton.setEnabled(false);
		}
	}

	private void showDirectionalLight(int index) {
		WilmaLight w;
		DirectionalLight dirLight;
		for (int i = 0; i < lightManager.getDirLightVector().size(); i++) {
			w = (WilmaLight) lightManager.getDirLightVector().get(i);
			w.getArrow().setTransparency(1f);
		}
		if ((index < dirListModel.size()) && (index >= 0)) {
			colorButton.setEnabled(true);
			dirPane.EnableEdit();
			w = (WilmaLight) lightManager.getDirLightVector().get(index);
			w.getArrow().setTransparency(0.7f);
			// tells the dirLightMouseCtrl which arrow and light to control
			dirLightMouseCtrl.setArrow(w.getArrow());
			dirLight = (DirectionalLight) w.getLight();
			dirLightMouseCtrl.setLight(dirLight);

			dirLight.getDirection(direction);
			dirPane.xDir.setText(String.valueOf(direction.x));
			dirPane.yDir.setText(String.valueOf(direction.y));
			dirPane.zDir.setText(String.valueOf(direction.z));

			if (dirLight.getEnable() == true) {
				onButton.setEnabled(false);
				offButton.setEnabled(true);
			} else {
				onButton.setEnabled(true);
				offButton.setEnabled(false);
			}
			String text = "<html>\n" + "<ul>\n"
					+ "<li><font color=blue>Left mouse+drag to change the light's direction</font>\n"
					+ "<li><font color=blue>Right mouse+drag to change the arrow's position</font>\n" + "</ul>\n";
			helpText.setText(text);
		} else {
			dirPane.clear();
			deleteButton.setEnabled(false);
			onButton.setEnabled(false);
			offButton.setEnabled(false);
			colorButton.setEnabled(false);
		}
	}

	private void showPointLight(int index) {
		WilmaLight w;
		PointLight pointLight;
		for (int i = 0; i < lightManager.getPointLightVector().size(); i++) {
			w = (WilmaLight) lightManager.getPointLightVector().get(i);
			w.getSphere().setTransparency(1f);
		}

		if ((index < pointListModel.size()) && (index >= 0)) {
			colorButton.setEnabled(true);
			pointPane.EnableEdit();
			w = (WilmaLight) lightManager.getPointLightVector().get(index);
			w.getSphere().setTransparency(0.7f);

			pointLight = (PointLight) w.getLight();
			// tells the pointLightMouseCtrl which sphere and light to control
			pointLightMouseCtrl.setSphere(w.getSphere());
			pointLightMouseCtrl.setLight(pointLight);
			// position
			pointLight.getPosition(position);
			pointPane.xPos.setText(String.valueOf(position.x));
			pointPane.yPos.setText(String.valueOf(position.y));
			pointPane.zPos.setText(String.valueOf(position.z));
			// attenuation
			pointPane.Constant.setText(String.valueOf(attenuation.x));
			pointPane.Linear.setText(String.valueOf(attenuation.y));
			pointPane.Quadratic.setText(String.valueOf(attenuation.z));
			if (pointLight.getEnable() == true) {
				onButton.setEnabled(false);
				offButton.setEnabled(true);
			} else {
				onButton.setEnabled(true);
				offButton.setEnabled(false);
			}

			text = "<html>\n" + "<ul>\n"
					+ "<li><font color=blue>Left mouse+drag to change the light's X/Y position.</font>\n"
					+ "<li><font color=blue>Right mouse+drag left/right to change the light's Z position</font>\n"
					+ "</ul>\n";
			helpText.setText(text);
		} else {
			pointPane.clear();
			deleteButton.setEnabled(false);
			onButton.setEnabled(false);
			offButton.setEnabled(false);
			colorButton.setEnabled(false);
		}

	}

	private void showSpotLight(int index) {
		SpotLight spotLight;
		WilmaLight w;
		for (int i = 0; i < lightManager.getSpotLightVector().size(); i++) {
			w = (WilmaLight) lightManager.getSpotLightVector().get(i);
			w.getCone().setTransparency(1f);
		}
		if ((index < spotListModel.size()) && (index >= 0)) {
			colorButton.setEnabled(true);
			spotPane.EnableEdit();

			w = (WilmaLight) lightManager.getSpotLightVector().get(index);
			w.getCone().setTransparency(0.7f);
			// tells the spotLightMouseCtrl which cone and light to control
			spotLight = (SpotLight) w.getLight();
			spotLightMouseCtrl.setCone(w.getCone());
			spotLightMouseCtrl.setLight(spotLight);

			spotLight.getDirection(direction);
			spotPane.xDir.setText(String.valueOf(direction.x));
			spotPane.yDir.setText(String.valueOf(direction.y));
			spotPane.zDir.setText(String.valueOf(direction.z));

			// position
			spotLight.getPosition(position);
			spotPane.xPos.setText(String.valueOf(position.x));
			spotPane.yPos.setText(String.valueOf(position.y));
			spotPane.zPos.setText(String.valueOf(position.z));
			// attenuation
			spotPane.Constant.setText(String.valueOf(attenuation.x));
			spotPane.Linear.setText(String.valueOf(attenuation.y));
			spotPane.Quadratic.setText(String.valueOf(attenuation.z));
			// other

			SpreadAngle = spotLight.getSpreadAngle();
			Concentration = spotLight.getConcentration();
			spotPane.SpreadAngle.setText(String.valueOf(SpreadAngle));
			spotPane.Concentration.setText(String.valueOf(Concentration));
			if (spotLight.getEnable() == true) {
				onButton.setEnabled(false);
				offButton.setEnabled(true);
			} else {
				onButton.setEnabled(true);
				offButton.setEnabled(false);
			}
			String text = "<html>\n"
					+ "<ul>\n"
					+ "<li><font color=blue>Left mouse+drag to change thelight's direction</font>\n"
					+ "<li><font color=blue>Right mouse+drag to change light's X/Y position.</font>\n"
					+ "<li><font color=blue>Shift+any mouse button+drag left/right to change light's Z position.</font>\n"
					+ "<li><font color=blue>Alt+any mouse button+drag lefg/right to adjust the light's spread angle</font>\n"
					+ "</ul>\n";
			helpText.setText(text);
		} else {
			spotPane.clear();
			deleteButton.setEnabled(false);
			onButton.setEnabled(false);
			offButton.setEnabled(false);
			colorButton.setEnabled(false);
		}

	}

	// attaches the 3D shapes represent the lights and light mouse control
	// behavior
	// to visualGroup
	private void visualLight() {
		Point3f position = new Point3f();
		Vector3f direction = new Vector3f();
		Color3f color = new Color3f();
		WilmaLight w;
		float spreadAngle;

		if (lightType == "Directional") {
			for (int i = 0; i < lightManager.getDirLightVector().size(); i++) {
				w = (WilmaLight) lightManager.getDirLightVector().get(i);
				visualGroup.addChild(w.getObjGroup());
			}
			visualGroup.addChild(dirMouseCtrlGroup);

		}
		if (lightType == "Point") {
			for (int i = 0; i < lightManager.getPointLightVector().size(); i++) {
				w = (WilmaLight) lightManager.getPointLightVector().get(i);
				visualGroup.addChild(w.getObjGroup());
			}
			visualGroup.addChild(pointLightMouseCtrlGroup);
		}
		if (lightType == "Spot") {
			for (int i = 0; i < lightManager.getSpotLightVector().size(); i++) {
				w = (WilmaLight) lightManager.getSpotLightVector().get(i);
				visualGroup.addChild(w.getObjGroup());
			}
			visualGroup.addChild(spotLightMouseCtrlGroup);
		}

	}

	private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;
	}

	private void createRightButtonPane() {
		GridBagLayout gridbag = new GridBagLayout();
		rightButtonPane.setLayout(gridbag);
		GridBagConstraints constraints = new GridBagConstraints();

		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showColorDialog(e);

			}
		});

		buildConstraints(constraints, 0, 0, 1, 1, 50, 50);
		constraints.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(onButton, constraints);
		rightButtonPane.add(onButton);
		onButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lightON();

			}
		});

		buildConstraints(constraints, 1, 0, 1, 1, 50, 50);
		constraints.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(offButton, constraints);
		rightButtonPane.add(offButton);
		offButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lightOff();
			}
		});

		buildConstraints(constraints, 0, 1, 1, 1, 50, 50);
		constraints.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(colorButton, constraints);
		rightButtonPane.add(colorButton);

		buildConstraints(constraints, 1, 1, 1, 1, 50, 50);
		constraints.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(OK, constraints);
		rightButtonPane.add(OK);
		OK.addActionListener(new OKButtonListener());

	}

	private void showColorDialog(ActionEvent e) {
		Color3f oldColor = new Color3f();
		WilmaLight w;
		int index;
		index = selectedList.getSelectedIndex();
		Color newColour;
		if ((lightType == "Ambient") && (index != -1)) {
			w = (WilmaLight) lightManager.getAmbLightVector().get(index);
			((AmbientLight) w.getLight()).getColor(oldColor);
			newColour = JColorChooser.showDialog(this, "Please select nice colours...", oldColor.get());
			if (newColour != null) {
				ambPane.label.setBackground(newColour);
				((AmbientLight) w.getLight()).setColor(new Color3f(newColour));
			}
		}
		if ((lightType == "Directional") && (index != -1)) {
			w = (WilmaLight) lightManager.getDirLightVector().get(index);
			((DirectionalLight) w.getLight()).getColor(oldColor);
			newColour = JColorChooser.showDialog(this, "Please select nice colours...", oldColor.get());
			if (newColour != null) {
				((DirectionalLight) w.getLight()).setColor(new Color3f(newColour));
				w.getArrow().setColor(new Color3f(newColour));
			}

		}
		if ((lightType == "Point") && (index != -1)) {
			w = (WilmaLight) lightManager.getPointLightVector().get(index);
			((PointLight) w.getLight()).getColor(oldColor);
			newColour = JColorChooser.showDialog(this, "Please select nice colours...", oldColor.get());
			if (newColour != null) {
				((PointLight) w.getLight()).setColor(new Color3f(newColour));
				w.getSphere().setColor(new Color3f(newColour));
			}

		}
		if ((lightType == "Spot") && (index != -1)) {
			w = (WilmaLight) lightManager.getSpotLightVector().get(index);
			((SpotLight) w.getLight()).getColor(oldColor);
			newColour = JColorChooser.showDialog(this, "Please select nice colours...", oldColor.get());
			if (newColour != null) {
				((SpotLight) w.getLight()).setColor(new Color3f(newColour));
				w.getCone().setColor(new Color3f(newColour));
			}
		}

	}

	class NewButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			disableOK();
			// when create a new light,display the default value to the
			// textfield
			if (lightType == "Ambient") {
				ambPane.setDefaultValue();
			}
			if (lightType == "Directional") {
				dirPane.setDefaultValue();

			}
			if (lightType == "Point") {
				pointPane.setDefaultValue();
			}
			if (lightType == "Spot") {
				spotPane.setDefaultValue();
			}
			createNewLight();
		}
	}

	// delete the selected lights
	class DeleteButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			int index = selectedList.getSelectedIndex();
			Light selectedLight;

			if (index >= 0) {
				if (lightType == "Ambient") {
					lightManager.deleteAmbLight(index);
					// if the light vector is empty after remove, clear the
					// textfield of the panel
					if (lightManager.getAmbLightVector().size() == 0) {
						deleteButton.setEnabled(false);
						ambPane.clear();
						ambList.setSelectedIndex(-1);
						ambListModel.removeAllElements();
					}
					// if not empty after remove, rename all the lights from 0
					// show the characteristics of the first light in the light
					// Vector
					else {
						ambListModel.removeAllElements();
						for (int i = 0; i < lightManager.getAmbLightVector().size(); i++)
							ambListModel.addElement("Ambient Light" + i);
						ambList.setSelectedIndex(0);

					}

				}
				if (lightType == "Directional") {
					WilmaLight w = (WilmaLight) lightManager.getDirLightVector().get(index);
					w.deleteObj();
					lightManager.deleteDirLight(index);
					if (lightManager.getDirLightVector().size() == 0) {
						deleteButton.setEnabled(false);
						dirPane.clear();
						dirList.setSelectedIndex(-1);
						dirListModel.removeAllElements();
					} else {
						dirListModel.removeAllElements();
						for (int i = 0; i < lightManager.getDirLightVector().size(); i++)
							dirListModel.addElement("Directional Light" + i);
						dirList.setSelectedIndex(0);

					}

				}
				if (lightType == "Point") {
					WilmaLight w = (WilmaLight) lightManager.getPointLightVector().get(index);
					w.deleteObj();
					lightManager.deletePointLight(index);
					if (lightManager.getPointLightVector().size() == 0) {

						deleteButton.setEnabled(false);
						pointPane.clear();
						pointList.setSelectedIndex(-1);
						pointListModel.removeAllElements();
					} else {
						pointListModel.removeAllElements();
						for (int i = 0; i < lightManager.getPointLightVector().size(); i++)
							pointListModel.addElement("Point Light" + i);
						pointList.setSelectedIndex(0);

					}

				}
				if (lightType == "Spot") {
					WilmaLight w = (WilmaLight) lightManager.getSpotLightVector().get(index);
					w.deleteObj();
					lightManager.deleteSpotLight(index);
					if (lightManager.getSpotLightVector().size() == 0) {

						spotListModel.removeAllElements();
						deleteButton.setEnabled(false);
						spotPane.clear();
						spotList.setSelectedIndex(-1);

					} else {
						spotListModel.removeAllElements();
						for (int i = 0; i < lightManager.getSpotLightVector().size(); i++)
							spotListModel.addElement("Spot Light" + i);
						spotList.setSelectedIndex(0);

					}

				}
			}
			if (selectedList.getSelectedIndex() == -1)
				deleteButton.setEnabled(false);

		}
	}

	class OKButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = selectedList.getSelectedIndex();
			if (index != -1)
				adjustLight();

			disableOK();

		}
	}

	// show a open file dialog to load the light configuration
	class OpenButtonListerner implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			int returnVal = fc.showOpenDialog(LightFrame.this);
			File file;
			String name;
			String ext = null;
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				file = fc.getSelectedFile();
				name = file.getName();

				// load the light configuration
				lightManager.loadFile(name);
				cleanup();
				ambListModel.clear();
				dirListModel.clear();
				pointListModel.clear();
				spotListModel.clear();

				for (int i = 0; i < lightManager.getAmbLightVector().size(); i++)
					ambListModel.addElement("Ambient Light" + i);
				for (int i = 0; i < lightManager.getDirLightVector().size(); i++)
					dirListModel.addElement("Directional Light" + i);
				for (int i = 0; i < lightManager.getPointLightVector().size(); i++)
					pointListModel.addElement("Point Light" + i);
				for (int i = 0; i < lightManager.getSpotLightVector().size(); i++)
					spotListModel.addElement("Spot Light" + i);
				if (ambListModel.size() > 0)
					ambList.setSelectedIndex(0);

				if (dirListModel.size() > 0)
					dirList.setSelectedIndex(0);

				if (pointListModel.size() > 0)
					pointList.setSelectedIndex(0);

				if (spotListModel.size() > 0)
					spotList.setSelectedIndex(0);
				visualLight();
			}
		}
	}

	// show a save file dialog to save the light configuration
	class SaveButtonListerner implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			int returnVal = fc.showSaveDialog(LightFrame.this);
			File file;
			String name;
			String ext = null;
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				name = file.getName();
				if (name.equals("WILMA_CONSTANTS.properties"))
					lightManager.saveLight();
				else {
					if (!name.endsWith(".properties"))
						name += ".properties";

					lightManager.saveToNewFile(name);
				}

			}
		}
	}

	// create new light
	private void createNewLight() {
		try {
			int size = selectedList.getModel().getSize();

			if (lightType == "Ambient") {
				lightManager.createAmbLight();
				ambListModel.addElement("Ambient Light" + size);
				selectedList.setSelectedIndex(size);
			}
			if (lightType == "Directional") {
				// create new directional light
				lightManager.createDirLight();
				dirListModel.addElement("Directional Light" + size);
				selectedList.setSelectedIndex(size);
				WilmaLight w = (WilmaLight) lightManager.getDirLightVector().get(size);
				visualGroup.addChild(w.getObjGroup());
			}
			if (lightType == "Point") {

				lightManager.createPointLight();
				// add to the list
				pointListModel.addElement("Point Light" + size);
				selectedList.setSelectedIndex(size);
				WilmaLight w = (WilmaLight) lightManager.getPointLightVector().get(size);
				visualGroup.addChild(w.getObjGroup());

			}
			if (lightType == "Spot") {
				lightManager.createSpotLight();
				spotListModel.addElement("Spot Light" + size);
				selectedList.setSelectedIndex(size);
				WilmaLight w = (WilmaLight) lightManager.getSpotLightVector().get(size);
				visualGroup.addChild(w.getObjGroup());
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), " Input parameters error. Please check again.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	// adjust the light characteristic
	private void adjustLight() {
		int index = selectedList.getSelectedIndex();
		try {

			if (lightType == "Directional") {
				// direction
				direction.x = Float.parseFloat(dirPane.xDir.getText());
				direction.y = Float.parseFloat(dirPane.yDir.getText());
				direction.z = Float.parseFloat(dirPane.zDir.getText());

				WilmaLight lightSelected = (WilmaLight) lightManager.getDirLightVector().get(index);
				DirectionalLight dirLight = (DirectionalLight) lightSelected.getLight();
				dirLight.setDirection(direction);
				lightSelected.getArrow().setDirection(direction);
			}
			if (lightType == "Point") {
				// position
				position.x = Float.parseFloat(pointPane.xPos.getText());
				position.y = Float.parseFloat(pointPane.yPos.getText());
				position.z = Float.parseFloat(pointPane.zPos.getText());
				// attenuation
				attenuation.x = Float.parseFloat(pointPane.Constant.getText());
				attenuation.y = Float.parseFloat(pointPane.Linear.getText());
				attenuation.z = Float.parseFloat(pointPane.Quadratic.getText());

				WilmaLight lightSelected = (WilmaLight) lightManager.getPointLightVector().get(index);
				PointLight pointLight = (PointLight) lightSelected.getLight();
				pointLight.setPosition(position);
				pointLight.setAttenuation(attenuation);
				lightSelected.getSphere().setPosition(position);
			}
			if (lightType == "Spot") {
				// direction
				direction.x = Float.parseFloat(spotPane.xDir.getText());
				direction.y = Float.parseFloat(spotPane.yDir.getText());
				direction.z = Float.parseFloat(spotPane.zDir.getText());

				// position
				position.x = Float.parseFloat(spotPane.xPos.getText());
				position.y = Float.parseFloat(spotPane.yPos.getText());
				position.z = Float.parseFloat(spotPane.zPos.getText());
				// attenuation
				attenuation.x = Float.parseFloat(spotPane.Constant.getText());
				attenuation.y = Float.parseFloat(spotPane.Linear.getText());
				attenuation.z = Float.parseFloat(spotPane.Quadratic.getText());
				// others
				SpreadAngle = Float.parseFloat(spotPane.SpreadAngle.getText());
				Concentration = Float.parseFloat(spotPane.Concentration.getText());

				WilmaLight lightSelected = (WilmaLight) lightManager.getSpotLightVector().get(index);
				SpotLight spotLight = (SpotLight) lightSelected.getLight();
				spotLight.setPosition(position);
				spotLight.setDirection(direction);
				spotLight.setAttenuation(attenuation);
				spotLight.setSpreadAngle(SpreadAngle);
				spotLight.setConcentration(Concentration);

				if (SpreadAngle > Math.PI / 2)
					SpreadAngle = (float) Math.PI;
				lightSelected.getCone().setSpreadAngle(SpreadAngle);
				lightSelected.getCone().setDirection(direction);
				lightSelected.getCone().setPosition(position);
			}
		} catch (Exception e) {

			JOptionPane.showMessageDialog(new JFrame(), " Input parameters error. Please check again.", "Error",
					JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * Enables the OK button
	 * */

	void enableOK() {
		OK.setEnabled(true);
	}

	void disableOK() {
		/**
		 * disables the OK button
		 * 
		 * */
		OK.setEnabled(false);
	}

	// turn on the selected light
	private void lightON() {
		int index = selectedList.getSelectedIndex();
		if (lightType == "Ambient") {
			WilmaLight lightSelected = (WilmaLight) lightManager.getAmbLightVector().get(index);
			AmbientLight l = (AmbientLight) lightSelected.getLight();
			l.setEnable(true);
		}
		if (lightType == "Directional") {
			WilmaLight lightSelected = (WilmaLight) lightManager.getDirLightVector().get(index);
			DirectionalLight l = (DirectionalLight) lightSelected.getLight();
			l.setEnable(true);

		}
		if (lightType == "Point") {
			WilmaLight lightSelected = (WilmaLight) lightManager.getPointLightVector().get(index);
			PointLight l = (PointLight) lightSelected.getLight();
			l.setEnable(true);
		}
		if (lightType == "Spot") {
			WilmaLight lightSelected = (WilmaLight) lightManager.getSpotLightVector().get(index);
			SpotLight l = (SpotLight) lightSelected.getLight();
			l.setEnable(true);
		}
		onButton.setEnabled(false);
		offButton.setEnabled(true);
	}

	// turn off the selected light
	private void lightOff() {
		int index = selectedList.getSelectedIndex();
		if (lightType == "Ambient") {
			WilmaLight lightSelected = (WilmaLight) lightManager.getAmbLightVector().get(index);
			AmbientLight l = (AmbientLight) lightSelected.getLight();
			l.setEnable(false);
		}
		if (lightType == "Directional") {
			WilmaLight lightSelected = (WilmaLight) lightManager.getDirLightVector().get(index);
			DirectionalLight l = (DirectionalLight) lightSelected.getLight();
			l.setEnable(false);

		}
		if (lightType == "Point") {
			WilmaLight lightSelected = (WilmaLight) lightManager.getPointLightVector().get(index);
			PointLight l = (PointLight) lightSelected.getLight();
			l.setEnable(false);
		}
		if (lightType == "Spot") {
			WilmaLight lightSelected = (WilmaLight) lightManager.getSpotLightVector().get(index);
			SpotLight l = (SpotLight) lightSelected.getLight();
			l.setEnable(false);
		}
		offButton.setEnabled(false);
		onButton.setEnabled(true);
	}

	/**
	 * @return return the light list that currently selected, such as
	 *         AmbientLight list, DirectionalLight list
	 */
	JList getselectedList() {
		return selectedList;
	}

	// detach all the children from visualGroup
	private void cleanup() {

		Enumeration e = visualGroup.getAllChildren();
		while (e.hasMoreElements()) {
			BranchGroup b = (BranchGroup) e.nextElement();
			b.detach();
		}
	}

}
