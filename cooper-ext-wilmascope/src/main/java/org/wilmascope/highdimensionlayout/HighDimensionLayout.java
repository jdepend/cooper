/*
 * Created on Jun 10, 2004
 *
 */
package org.wilmascope.highdimensionlayout;

import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

import org.wilmascope.graph.Edge;
import org.wilmascope.graph.EdgeLayout;
import org.wilmascope.graph.EdgeList;
import org.wilmascope.graph.LayoutEngine;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeLayout;
import org.wilmascope.graph.NodeList;

import Jama.Matrix;

/**
 * @author cmurray
 * 
 */
public class HighDimensionLayout extends LayoutEngine {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#calculateLayout()
	 */

	public static final int m = 50;

	public static final int k = 3; // must equal 2 or 3

	public void calculateLayout() {
		// how should graph-theoretic distance work for unconnected graphs

		NodeList nodes = getRoot().getNodes();
		Random rand = new Random(System.currentTimeMillis());
		int n = nodes.size();
		Node pivot = nodes.get(rand.nextInt(n));

		int X[][] = new int[m][n];

		int d[] = new int[n];
		for (int j = 0; j != n; ++j) {
			d[j] = Integer.MAX_VALUE;
		}

		for (int i = 0; i != m; ++i) {
			// System.out.println("Working");
			bfs(nodes, pivot);
			// System.out.println("bfs done");

			/*
			 * for (nodes.resetIterator(); nodes.hasNext();) { Node bfsNode =
			 * nodes.nextNode(); System.out.println(bfsNode.getPosition() + " "
			 * + ((HDNodeLayout)bfsNode.getLayout()).graphTheoreticDist); }
			 */

			for (int j = 0; j != n; ++j) {
				X[i][j] = ((HDNodeLayout) (nodes.get(j).getLayout())).graphTheoreticDist;
				d[j] = Math.min(d[j], X[i][j]);
			}

			int maxj = 0;
			for (int j = 1; j != n; ++j) {
				if (d[j] > d[maxj]) {
					maxj = j;
				}
			}
			pivot = nodes.get(maxj);
		}

		Matrix Xhat = new Matrix(m, n);
		for (int i = 0; i != m; ++i) {
			double mi = 0.0f;
			for (int j = 0; j != n; ++j) {
				mi += (double) X[i][j];
			}
			mi /= (double) n;
			for (int j = 0; j != n; ++j) {
				Xhat.set(i, j, (double) (X[i][j]) - mi);
			}
		}

		Matrix XhatT = Xhat.transpose();

		Matrix S = (Xhat.times(XhatT)).times(1.0 / ((double) n));

		Matrix eigenvectors = S.transpose().times(S).eig().getV();
		// eigenvectors are columns of matrix, sorted according to the
		// corresponding
		// eigenvalue in ascending order

		// get the first k eigenvectors (those that correspond to the largest
		// eigenvalues)
		double u[][] = new double[m][k];
		int column = m - 1;
		for (int j = 0; j != k; ++j) {
			// normalise and store eigenvector
			double length = 0.0;
			for (int i = 0; i != m; ++i) {
				length += (eigenvectors.get(i, column - j)) * (eigenvectors.get(i, column - j));
			}
			length = Math.sqrt(length);
			for (int i = 0; i != m; ++i) {
				u[i][j] = (eigenvectors.get(i, column - j)) / length;
			}
		}

		Vector Y = new Vector();
		for (int j = 0; j != k; ++j) {
			Matrix ui = new Matrix(m, 1);
			for (int i = 0; i != m; ++i) {
				ui.set(i, 0, u[i][j]);
			}

			Matrix Yi = XhatT.times(ui);
			Y.add(Yi);
		}

		for (int i = 0; i != n; ++i) {
			Node currentNode = nodes.get(i);

			Matrix Y0 = (Matrix) Y.get(0);
			float Xpos = (float) (Y0.get(i, 0));

			Matrix Y1 = (Matrix) Y.get(1);
			float Ypos = (float) (Y1.get(i, 0));

			float Zpos = 0.0f;

			if (k == 3) {
				Matrix Y2 = (Matrix) Y.get(2);
				Zpos = (float) (Y2.get(i, 0));
			}

			((HDNodeLayout) currentNode.getLayout()).HDPos.set(Xpos, Ypos, Zpos);
		}
	}

	// performs breadth first search starting from pivot node and stores
	// graph-theoretic distance from pivot in each nodes HDNodeLayout
	private void bfs(NodeList nodes, Node pivot) {
		for (int i = 0; i != nodes.size(); ++i) {
			((HDNodeLayout) (nodes.get(i).getLayout())).graphTheoreticDist = -1;
		}

		LinkedList queue = new LinkedList();
		((HDNodeLayout) (pivot.getLayout())).graphTheoreticDist = 0;
		queue.addLast(pivot);

		// int visited = 0;

		while (!queue.isEmpty()) {
			Node currentNode = (Node) queue.removeFirst();
			// ++visited;
			// System.out.println(visited);

			EdgeList edges = currentNode.getEdges();
			for (Edge currentEdge : edges) {
				Node neighbour = currentEdge.getNeighbour(currentNode);
				if (((HDNodeLayout) neighbour.getLayout()).graphTheoreticDist == -1) {
					int currentNodeDist = ((HDNodeLayout) (currentNode.getLayout())).graphTheoreticDist;

					((HDNodeLayout) (neighbour.getLayout())).graphTheoreticDist = currentNodeDist + 1;

					queue.addLast(neighbour);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#applyLayout()
	 */
	public boolean applyLayout() {
		NodeList nodes = getRoot().getNodes();
		EdgeList edges = getRoot().getInternalEdges();
		for (Node n : nodes) {
			((HDNodeLayout) (n.getLayout())).setPos();
		}

		for (Edge e : edges) {
			e.recalculate();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#getName()
	 */
	public String getName() {
		return "High-Dimensional Embedding";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.wilmascope.graph.LayoutEngine#createNodeLayout(org.wilmascope.graph
	 * .Node)
	 */
	public NodeLayout createNodeLayout(Node n) {
		return new HDNodeLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.wilmascope.graph.LayoutEngine#createEdgeLayout(org.wilmascope.graph
	 * .Edge)
	 */
	public EdgeLayout createEdgeLayout(Edge e) {
		return new HDEdgeLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#getControls()
	 */
	public JPanel getControls() {
		return new JPanel();
	}

}
