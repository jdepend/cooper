/*
 * The following source code is a plugin for the WilmaScope 3D Graph Drawing
 * Engine. WilmaScope and this plugin are distributed under the terms of the
 * GNU Lesser General Public License
 * (LGPL - http://www.gnu.org/copyleft/lesser.html).
 *
 * As usual we distribute it with no warranties and anything you chose to do
 * with it you do at your own risk.
 *
 * Copyright for this work is retained by Richard Webber, however it may be
 * used or modified to work as part of other software subject to the terms
 * of the LGPL.
 *
 * The WilmaScope software source repository is hosted by Source Forge:
 * http://www.sourceforge.net/projects/wilma
 *
 */

package org.wilmascope.graphgen.plugin;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.wilmascope.control.GraphControl;
import org.wilmascope.global.RandomGenerator;
import org.wilmascope.graphgen.GraphGenerator;
import org.wilmascope.gui.SpinnerSlider;

/**
 * Generates a tree. Proceeds by adding a random number of children to a random
 * node of the current tree. The probability of choosing a node is proportional
 * to the number of free spaces for its children. Generation can be limited by
 * the number of nodes, the maximum degree and optionally the tree height. The
 * root node can be forced to have a given number of children.
 * 
 * @author Richard Webber
 */

public class TreeGenerator extends GraphGenerator {

	/* Configuration options with default values */
	private int numNodes = 63;
	private int maxDegree = 2;
	private int rootDegree = 2; // overrules maxDegree and numNodes
	private boolean useRootDegree = false;
	private int maxHeight = 5; // height in edges
	private boolean useMaxHeight = false;
	private boolean useBulkInsert = true;

	/* The control GUI component */
	private JPanel controlPanel;

	/* Construct the control GUI component */
	public TreeGenerator() {

		/* Top-level panel - done this way to use BoxLayout */
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

		/* Input to limit the number of nodes */
		final SpinnerSlider numNodesSlider = new SpinnerSlider("Nodes", 1, 1000, numNodes);
		numNodesSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				numNodes = numNodesSlider.getValue();
			}
		});
		controlPanel.add(numNodesSlider);

		/* Input to limit the node degree */
		final SpinnerSlider maxDegreeSlider = new SpinnerSlider("Degree", 2, 10, maxDegree);
		maxDegreeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				maxDegree = maxDegreeSlider.getValue();
			}
		});
		controlPanel.add(maxDegreeSlider);

		/* Inputs to _set_ root degree */
		JPanel rootDegreePanel = new JPanel();
		rootDegreePanel.setLayout(new BoxLayout(rootDegreePanel, BoxLayout.X_AXIS));
		final JCheckBox useRootDegreeCheck = new JCheckBox("Set Root Degree", useRootDegree);
		useRootDegreeCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				useRootDegree = useRootDegreeCheck.isSelected();
			}
		});
		rootDegreePanel.add(useRootDegreeCheck);
		final SpinnerSlider rootDegreeSlider = new SpinnerSlider("Degree", 2, 20, rootDegree);
		rootDegreeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				rootDegree = rootDegreeSlider.getValue();
			}
		});
		rootDegreePanel.add(rootDegreeSlider);
		controlPanel.add(rootDegreePanel);

		/* Inputs to limit the tree height */
		JPanel maxHeightPanel = new JPanel();
		maxHeightPanel.setLayout(new BoxLayout(maxHeightPanel, BoxLayout.X_AXIS));
		final JCheckBox useMaxHeightCheck = new JCheckBox("Limit Height", useMaxHeight);
		useMaxHeightCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				useMaxHeight = useMaxHeightCheck.isSelected();
			}
		});
		maxHeightPanel.add(useMaxHeightCheck);
		final SpinnerSlider maxHeightSlider = new SpinnerSlider("Height (in edges)", 1, 9, maxHeight);
		maxHeightSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				maxHeight = maxHeightSlider.getValue();
			}
		});
		maxHeightPanel.add(maxHeightSlider);
		controlPanel.add(maxHeightPanel);

		/* Input to select bulk inserts */
		final JCheckBox useBulkInsertCheck = new JCheckBox("Use Bulk Insert", useBulkInsert);
		useBulkInsertCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				useBulkInsert = useBulkInsertCheck.isSelected();
			}
		});
		controlPanel.add(useBulkInsertCheck);
	}

	/* The menu name of the plugin */
	public String getName() {
		return "Tree";
	}

	/* Return the control GUI component */
	public JPanel getControls() {
		return controlPanel;
	}

	/* Container used to track the height of added nodes */
	private class TreeNode {
		GraphControl.Node node;
		int height;

		TreeNode(GraphControl.Node node, int height) {
			this.node = node;
			this.height = height;
		}
	}

	/* An ArrayList of TreeNodes */
	private class TreeNodeList extends ArrayList<TreeNode> {
		// Why is this protected in AbstractList?
		public void removeRange(int fromIndex, int toIndex) {
			super.removeRange(fromIndex, toIndex);
		}
	}

	/* Generate a tree within the set limitations */
	public void generate(GraphControl gc) {

		/* Clear the existing graph */
		GraphControl.Cluster rootCluster = gc.getRootCluster();
		rootCluster.freeze();
		rootCluster.deleteAll();

		/* Keep track of possible parents */
		TreeNodeList parentList = new TreeNodeList();

		/* Create a new root node */
		GraphControl.Node rootNode = rootCluster.addNode(getNodeView());
		rootNode.setColour(1f, 0f, 0f);
		parentList.ensureCapacity(maxDegree);
		int nodes = 1;
		if (useRootDegree) {

			/* Add rootDegree children under rootNode */
			for (int child = 0; child < rootDegree; child++) {
				GraphControl.Node childNode = rootCluster.addNode(getNodeView());
				childNode.setColour(1f, 1f, 0f);
				nodes++;
				rootCluster.addEdge(rootNode, childNode, getEdgeView());
				if (!useMaxHeight || maxHeight > 1) {

					/* Add the new child to the list of potential parents */
					for (int degree = 0; degree < maxDegree; degree++) {
						parentList.add(new TreeNode(childNode, 2));
					}
				}
			}
		} else {

			/* Make rootNode the only potential parent (so far) */
			for (int degree = 0; degree < maxDegree; degree++) {
				parentList.add(new TreeNode(rootNode, 1));
			}
		}

		/* Iteratively add new children */
		while (nodes < numNodes && !parentList.isEmpty()) {

			/* Find insertion point */
			int parentIndex = RandomGenerator.getRandom().nextInt(parentList.size());
			TreeNode parentTreeNode = parentList.get(parentIndex);
			System.out.println("Jump to " + parentIndex);
			while (parentIndex > 0 && parentList.get(parentIndex - 1).node == parentTreeNode.node) {
				parentIndex--;
			}
			System.out.println("Reverse to " + parentIndex);

			/* Insert a random number of nodes (up to maxDegree) */
			int numChildren = (useBulkInsert ? RandomGenerator.getRandom().nextInt(maxDegree) + 1 : 1);
			System.out.println("Insert " + numChildren);
			parentList.ensureCapacity(parentList.size() + numChildren * maxDegree);
			int insertIndex = parentIndex; // needed below
			while (insertIndex - parentIndex < numChildren) {

				/* Create a new node */
				GraphControl.Node childNode = rootCluster.addNode(getNodeView());
				childNode.setColour(1f, 0f, 1f);
				nodes++;
				rootCluster.addEdge(parentTreeNode.node, childNode, getEdgeView());
				if (!useMaxHeight || parentTreeNode.height < maxHeight) {

					/* Add the new child to the list of potential parents */
					for (int degree = 0; degree < maxDegree; degree++) {
						parentList.add(new TreeNode(childNode, parentTreeNode.height + 1));
					}
				}
				insertIndex++;

				/* Check if we've reached the end */
				if (nodes == numNodes || insertIndex == parentList.size()
						|| parentList.get(insertIndex).node != parentTreeNode.node) {
					System.out.println("Bail at " + insertIndex);
					break;
				}
			}

			/* Remove used TreeNodes */
			// insertIndex should point to one _after_ the last used TreeNode
			System.out.println("Remove from " + parentIndex + " to " + insertIndex);
			parentList.removeRange(parentIndex, insertIndex);
		}

		/* Unfreeze the layout engine */
		rootCluster.unfreeze();
	}

}
