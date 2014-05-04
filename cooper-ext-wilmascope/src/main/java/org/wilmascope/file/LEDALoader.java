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
package org.wilmascope.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.WilmaMain;

class NotLEDAFileException extends Exception {
}

/**
 * @author dwyer
 * 
 *         Load a graph from a simple LEDA text file format (".lgr")
 * @see <a
 *      href="http://www.algorithmic-solutions.info/leda_manual/LEDA_graph_input_output.html">the
 *      LEDA file format definition </a>
 */

public class LEDALoader {
	public LEDALoader(GraphControl gc, String fileName) {
		try {
			BufferedReader f = new BufferedReader(new FileReader(fileName));
			// Check the header
			String s = f.readLine();
			if (!s.regionMatches(0, "LEDA.GRAPH", 0, 10)) {
				throw new NotLEDAFileException();
			}
			// ignore the second and third lines
			f.readLine();
			f.readLine();
			// read the number of nodes
			StringTokenizer st = new StringTokenizer(f.readLine());
			int nodeCount = Integer.parseInt(st.nextToken());
			GraphControl.Node[] nodeArray = new GraphControl.Node[nodeCount];
			// create the nodes, store references numbered from 1
			GraphControl.Cluster root = gc.getRootCluster();
			for (int i = 0; i < nodeCount; i++) {
				f.readLine();
				nodeArray[i] = root.addNode();
			}
			st = new StringTokenizer(f.readLine());
			int edgeCount = Integer.parseInt(st.nextToken());
			for (int i = 0; i < edgeCount; i++) {
				st = new StringTokenizer(f.readLine());
				int start = Integer.parseInt(st.nextToken()) - 1;
				int end = Integer.parseInt(st.nextToken()) - 1;
				root.addEdge(nodeArray[start], nodeArray[end]);
			}
		} catch (FileNotFoundException e) {
			WilmaMain.showErrorDialog("LEDA graph File Not Found: " + fileName, e);
		} catch (IOException e) {
			WilmaMain.showErrorDialog("Error reading LEDA graph file: " + fileName, e);
		} catch (NotLEDAFileException e) {
			WilmaMain.showErrorDialog("Not a valid LEDA graph file: " + fileName, e);
		}
	}
}
