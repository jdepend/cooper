/*
 * The following source code is part of the WilmaScope 3D Graph Drawing Engine
 * which is distributed under the terms of the GNU Lesser General Public License
 * (LGPL - http://www.gnu.org/copyleft/lesser.html).
 *
 * As usual we distribute it with no warranties and anything you chose to do
 * with it you do at your own risk.
 *
 * Copyright for this work is retained by Tim Dwyer and the WilmaScope organisation
 * (www.wilmascope.org) however it may be used or modified to work as part of
 * other software subject to the terms of the LGPL.  I only ask that you cite
 * WilmaScope as an influence and inform us (tgdwyer@yahoo.com)
 * if you do anything really cool with it.
 *
 * The WilmaScope software source repository is hosted by Source Forge:
 * www.sourceforge.net/projects/wilma
 *
 * -- Tim Dwyer, 2001
 */
package jdepend.report.way.dddui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.vecmath.Color3f;

import jdepend.model.Element;
import jdepend.model.Relation;
import jdepend.report.util.IntensitySizeCalculator;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.GraphControl.Edge;
import org.wilmascope.forcelayout.ForceLayout;
import org.wilmascope.view.EdgeView;
import org.wilmascope.view.GraphCanvas;
import org.wilmascope.view.GraphElementView;
import org.wilmascope.view.ViewManager;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;

/**
 * 
 * @author wangdg
 * 
 */
public class DDDJDepend extends JPanel {

	// private JDependFrame frame;

	private GraphControl.Cluster cluster;

	private JLabel tipLablel;

	private GraphCanvas graphCanvas;

	public DDDJDepend(Collection<Relation> relations) {

		// this.frame = frame;

		this.setLayout(new BorderLayout());
		try {
			final GraphControl graphControl = new GraphControl(500, 500);
			// 设置线的风格
			ViewManager.getInstance().getEdgeViewRegistry().setDefaultView("Arrow");
			// 设置线的粗细
			Properties p = new Properties();
			p.setProperty("Radius", "0.01f");
			ViewManager.getInstance().getEdgeViewRegistry().getDefaultViewPrototype().setProperties(p);
			// 创建graphControl
			graphCanvas = graphControl.getGraphCanvas();
			cluster = graphControl.getRootCluster();
			cluster.setLayoutEngine(ForceLayout.createDefaultForceLayout(cluster.getCluster()));
			// 设置鼠标事件
			RootClusterMenu rootMenu = new RootClusterMenu(cluster, graphCanvas);
			graphControl.setRootPickingClient(rootMenu);

			GraphControl.getPickListener().setNodeOptionsClient(new NodeOptionsMenu(this));
			GraphControl.getPickListener().setEdgeOptionsClient(new EdgeOptionsMenu(this));

			this.add(BorderLayout.CENTER, graphCanvas);

			this.createGraph(relations);

			this.add(BorderLayout.SOUTH, this.createTipPanel());

		} catch (java.lang.UnsatisfiedLinkError e) {
			e.printStackTrace();
			this.processException();
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof java.lang.ClassNotFoundException) {
				processException();
			}
		}
	}

	private void processException() {
		final JLabel tip = new JLabel("<html><a href='java3d.java.net'>请安装java3D引擎，并重启Cooper。</a></html>");
		tip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tip.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URI("http://java3d.java.net/"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		JPanel content = new JPanel();
		content.setLayout(new GridBagLayout());
		content.add(tip);
		this.add(content);
	}

	private Component createTipPanel() {
		JPanel tipPanel = new JPanel(new BorderLayout());
		tipLablel = new JLabel();
		tipPanel.add(BorderLayout.WEST, tipLablel);
		return tipPanel;
	}

	public void showTip(String tip) {
		this.tipLablel.setText(tip);
	}

	private void createGraph(Collection<Relation> relations) {

		Collection<Element> elements = Relation.calElements(relations);

		Map<Relation, Integer> relationSizes = IntensitySizeCalculator.calRelationSize(relations);
		Map<Element, Integer> elementSizes = IntensitySizeCalculator.calElementSize(elements);

		Map<String, GraphControl.Node> nodes = new HashMap<String, GraphControl.Node>();

		for (Element element : elements) {
			GraphControl.Node node = cluster.addNode();
			node.setLabel(element.getName());
			node.setColour(0.0f, 1.0f, 0.0f);
			node.setRadius(elementSizes.get(element) * 0.01F);
			// this.addMouseMoveEventListener(node.getView());

			nodes.put(element.getName(), node);
		}
		Edge e;
		for (Relation relation : relations) {
			e = cluster.addEdge(nodes.get(relation.getCurrent().getName()), nodes.get(relation.getDepend().getName()));
			((EdgeView) e.getEdge().getView()).setRadius(relationSizes.get(relation) * 0.005F);
			// this.addMouseMoveEventListener(e.getEdge().getView());
			if (relation.isAttention()) {
				if (relation.getAttentionType() == Relation.MutualDependAttentionType) {
					e.getView().getAppearance().setMaterial(createMaterial(1));
				} else if (relation.getAttentionType() == Relation.CycleDependAttentionType) {
					e.getView().getAppearance().setMaterial(createMaterial(2));
				} else if (relation.getAttentionType() == Relation.ComponentLayerAttentionType) {
					e.getView().getAppearance().setMaterial(createMaterial(3));
				} else if (relation.getAttentionType() == Relation.SDPAttentionType) {
					e.getView().getAppearance().setMaterial(createMaterial(4));
				}
			} else {
				e.getView().setColour(new Color(100, 200, 200));
			}
		}
	}

	private Material createMaterial(int gray) {

		Color3f ambientColour = new Color3f(1.0f, 0.0f, 0.0f);
		Color3f emissiveColour = new Color3f(1.0f, 0.0f, 0.0f);
		Color3f specularColour = new Color3f(1.0f, 0.0f, 0.0f);
		Color3f diffuseColour = new Color3f(1.0f, 0.0f, 0.0f);

		if (gray > 3) {
			ambientColour.set(Color.GRAY);
		}
		if (gray > 2) {
			specularColour.set(Color.GRAY);
		}

		if (gray > 1) {
			diffuseColour.set(Color.GRAY);
		}

		float shininess = 20.0f;
		return new Material(ambientColour, emissiveColour, diffuseColour, specularColour, shininess);
	}

	private void addMouseMoveEventListener(GraphElementView view) {

		BranchGroup bg = view.getBranchGroup();
		BranchGroup rbg = new BranchGroup();
		MouseBehavior gb = new MouseBehavior(view.getTransformGroup()) {
			/**
			 * Initializes the wake up events
			 */
			public void initialize() {
				mouseEvents = new WakeupCriterion[2];
				mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_ENTERED);
				mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_EXITED);
				mouseCriterion = new WakeupOr(mouseEvents);
				wakeupOn(mouseCriterion);
			}

			@Override
			public void processStimulus(Enumeration arg0) {
				System.out.println("xx");
				wakeupOn(mouseCriterion);
			}
		};
		gb.setSchedulingBounds(graphCanvas.getBoundingSphere());
		rbg.addChild(gb);
		bg.addChild(rbg);
	}
}
