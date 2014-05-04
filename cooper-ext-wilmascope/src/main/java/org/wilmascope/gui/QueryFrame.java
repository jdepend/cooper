package org.wilmascope.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.wilmascope.columnlayout.ColumnCluster;
import org.wilmascope.columnlayout.ColumnLayout;
import org.wilmascope.control.GraphControl;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.forcelayout.BalancedEventClient;
import org.wilmascope.forcelayout.Origin;
import org.wilmascope.forcelayout.Repulsion;
import org.wilmascope.forcelayout.Spring;
import org.wilmascope.global.GlobalConstants;
import org.wilmascope.view.EdgeView;
import org.wilmascope.view.ElementData;

/**
 * A demonstration of how to construct graph visualisations from an online query
 * of data from the CityWatch database.
 */
public class QueryFrame extends JFrame {
	static float DOT_COLUMN_SCALE = 40f;
	static float DOT_LAYOUT_SCALE = 15f;
	GraphControl graphControl;

	public QueryFrame(GraphControl c) {
		QueryFrame.graphRoot = c.getRootCluster();
		this.graphControl = c;
		box1 = Box.createHorizontalBox();
		jLabel2.setText("End Date:");
		endDateField.setPreferredSize(new Dimension(100, 27));
		endDateField.setText("01-apr-01");
		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okButton_actionPerformed(e);
			}
		});
		jLabel1.setText("Start Date:");
		fmMovementPanel.setLayout(gridLayout1);
		startDateField.setPreferredSize(new Dimension(100, 27));
		startDateField.setText("01-jan-01");
		gridLayout1.setRows(2);
		gridLayout1.setColumns(2);
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});
		jLabel3.setText("First Fund Manager:");
		fundmanField.setText("Sharelink");
		fmMovement2Panel.setLayout(gridLayout2);
		jLabel4.setText("Start Date");
		jLabel5.setText("End Date");
		startDate2Field.setText("1-dec-01");
		endDate2Field.setText("12-dec-01");
		gridLayout2.setColumns(2);
		gridLayout2.setRows(6);
		jLabel6.setText("Sector Code");
		sectorField.setText("97");
		wormRadioButton.setText("Worms");
		wormRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wormRadioButton_actionPerformed(e);
			}
		});
		dotColumnsRadioButton.setActionCommand("dotColumnsRadioButton");
		dotColumnsRadioButton.setSelected(true);
		dotColumnsRadioButton.setText("Dot Columns");
		dotColumnsRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dotColumnsRadioButton_actionPerformed(e);
			}
		});
		forceColumnRadioButton.setText("Force Directed Columns");
		forceColumnRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceColumnRadioButton_actionPerformed(e);
			}
		});
		sectorPanel.setLayout(gridLayout3);
		jLabel7.setText("Start Date");
		jLabel8.setText("End Date");
		sectorStartField.setText("01-dec-2001");
		sectorEndField.setText("12-dec-2001");
		gridLayout3.setColumns(2);
		gridLayout3.setRows(4);
		jLabel9.setText("Visible Edges");
		planarSectorRadioButton.setText("Layout in plane");
		sectorEdgeSlider.setMajorTickSpacing(20);
		sectorEdgeSlider.setMinorTickSpacing(10);
		sectorEdgeSlider.setPaintLabels(true);
		sectorEdgeSlider.setPaintTicks(true);
		scaleButton.setText("Adjust Scale...");
		scaleButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaleButton_actionPerformed(e);
			}
		});
		jLabel10.setText("Dot Scale");
		scaleField.setText("2");
		this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
		jPanel1.add(okButton, null);
		this.getContentPane().add(queryPane, BorderLayout.NORTH);
		fmMovementPanel.add(jLabel1, null);
		fmMovementPanel.add(startDateField, null);
		fmMovementPanel.add(jLabel2, null);
		fmMovementPanel.add(endDateField, null);
		ownershipPanel.add(jLabel3, null);
		ownershipPanel.add(fundmanField, null);
		fmMovement2Panel.add(jLabel4, null);
		fmMovement2Panel.add(startDate2Field, null);
		fmMovement2Panel.add(jLabel5, null);
		fmMovement2Panel.add(endDate2Field, null);
		fmMovement2Panel.add(jLabel6, null);
		fmMovement2Panel.add(sectorField, null);
		fmMovement2Panel.add(dotColumnsRadioButton, null);
		fmMovement2Panel.add(jPanel3, null);
		jPanel3.add(scaleButton, null);
		fmMovement2Panel.add(forceColumnRadioButton, null);
		fmMovement2Panel.add(jPanel2, null);
		jPanel2.add(jLabel10, null);
		jPanel2.add(scaleField, null);
		jPanel2.add(box1, null);
		fmMovement2Panel.add(wormRadioButton, null);
		jPanel1.add(cancelButton, null);
		fmMovement3DotButton.setText("Dot Layout");
		fmMovement3ForceButton.setText("Force Layout");
		columnThresholdField.setText(columnThreshold + "");
		edgeThresholdField.setText(edgeThreshold + "");
		dotScaleField.setText("" + DOT_LAYOUT_SCALE);
		dotEdgeMinField.setText("0.01");
		dotEdgeMaxField.setText("0.1");
		dotColumnScaleField.setText("" + DOT_COLUMN_SCALE);
		fmMovement3Panel.setLayout(new GridLayout(4, 4));
		fmMovement3Panel.add(new JLabel("Column Threshold:"));
		fmMovement3Panel.add(columnThresholdField);
		fmMovement3Panel.add(new JLabel("Edge Threshold:"));
		fmMovement3Panel.add(edgeThresholdField);
		fmMovement3Panel.add(new JLabel("Edge Radius Range:"));
		fmMovement3Panel.add(dotEdgeMinField);
		fmMovement3Panel.add(dotEdgeMaxField);
		fmMovement3Panel.add(edgeDiffByCountCheckBox);
		fmMovement3Panel.add(new JLabel("Column Scale:"));
		fmMovement3Panel.add(dotColumnScaleField);
		fmMovement3Panel.add(new JLabel());
		fmMovement3Panel.add(new JLabel());
		fmMovement3Panel.add(new JLabel("Dot Scale:"));
		fmMovement3Panel.add(dotScaleField);
		fmMovement3Panel.add(fmMovement3DotButton);
		fmMovement3Panel.add(fmMovement3ForceButton);
		fmMovement3DotButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portfolioColumns(ColumnCluster.DOTCOLUMNS);
			}
		});
		fmMovement3ForceButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portfolioColumns(ColumnCluster.FORCECOLUMNS);
			}
		});
		queryPane.add(fmMovement3Panel, "Single FM Movement");
		queryPane.add(fmMovementPanel, "FM Movement");
		queryPane.add(sectorPanel, "Sector Movement");
		queryPane.add(fmMovement2Panel, "FM Movement Across Time");
		queryPane.add(ownershipPanel, "Ownership");
		pack();
		styleButtonGroup.add(wormRadioButton);
		styleButtonGroup.add(dotColumnsRadioButton);
		styleButtonGroup.add(forceColumnRadioButton);
		sectorPanel.add(jLabel7, null);
		sectorPanel.add(sectorStartField, null);
		sectorPanel.add(jLabel8, null);
		sectorPanel.add(sectorEndField, null);
		sectorPanel.add(jLabel9, null);
		sectorPanel.add(sectorEdgeSlider, null);
		sectorPanel.add(planarSectorRadioButton, null);
	}

	void cancelButton_actionPerformed(ActionEvent e) {
		this.dispose();
	}

	/**
	 * The query for the selected panel is initiated when the OK button is
	 * pressed.
	 */
	void okButton_actionPerformed(ActionEvent ev) {
		Component selected = queryPane.getSelectedComponent();
		if (selected == fmMovementPanel) {
			fmMovementQuery(startDateField.getText(), endDateField.getText());
		} else if (selected == ownershipPanel) {
			ownershipQuery(fundmanField.getText());
		} else if (selected == fmMovement2Panel) {
			fmMovementAcrossTimeQuery(startDate2Field.getText(), endDate2Field.getText());
		} else if (selected == sectorPanel) {
			sectorQuery(sectorStartField.getText(), sectorEndField.getText());
		}
		this.hide();
	}

	// ====================================================================
	// BEGIN OWNERSHIP VIEW
	Hashtable fmList = new Hashtable();
	Hashtable companyList = new Hashtable();

	/**
	 * All nodes in the ownership view have a sub-class of this referenced by
	 * their UserData pointer, defining a custom action for Wilma to add to the
	 * node's Options menu (the right click menu). We define an action which
	 * either expands or collapses the node's neighbours in the bipartite
	 * company / fund manager graph.
	 */
	public abstract class QueryNodeData extends ElementData {
		/**
		 * When the user requests to see the node's neighbours the following
		 * method is called, it sets up the action to take when the node is
		 * collapsed
		 */
		void setExpanded() {
			setActionDescription("Hide Neighbours...");
			setActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					collapseNeighbours();
				}
			});
			expanded = true;
		}

		/**
		 * The node's neighbours are hidden, so prepare an action which will
		 * expand the node's neighbours when the user requests it.
		 */
		void setCollapsed() {
			setActionDescription("Expand Neighbours...");
			setActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					expandNeighbours();
				}
			});
			expanded = false;
		}

		/**
		 * add a node to this node's list of neighbours
		 */
		void addNeighbour(GraphControl.Node n, String key) {
			neighbours.put(key, n);
		}

		/**
		 * define the following method to properly clean up this node's
		 * neighbours on collapsing.
		 */
		abstract void collapseNeighbours();

		/**
		 * define the following method to perform the appropriate query to
		 * expand this node's neighbours
		 */
		abstract void expandNeighbours();

		/** an expanded node's neighbours are visible */
		boolean expanded;
		/** shared reference to the SQL connection */
		Statement stmt;
		/** list of neighbours for this node */
		Hashtable neighbours = new Hashtable();
		/** a reference back to the node which uses this class */
		GraphControl.Node node;
	}

	/**
	 * UserData class for company nodes.
	 */
	public class CompanyNodeData extends QueryNodeData {
		public CompanyNodeData(GraphControl.Node companyNode, Statement stmt, String epic) {
			this.stmt = stmt;
			this.epic = epic;
			this.node = companyNode;
			setCollapsed();
		}

		/**
		 * Elide the fund manager neighbours of this company that are not
		 * referenced by any other visible companies.
		 */
		void collapseNeighbours() {
			for (Enumeration e = neighbours.keys(); e.hasMoreElements();) {
				String fmcode = (String) e.nextElement();
				GraphControl.Node n = (GraphControl.Node) fmList.get(fmcode);
				// if there are no other references to this fund manager then
				// delete it
				// from the visible Wilma graph, remove it from the list of all
				// visible
				// fund manager nodes and remove it from this company node's
				// list of
				// neighbours
				if (n.getDegree() == 1) {
					fmList.remove(fmcode);
					n.delete();
					neighbours.remove(fmcode);
				}
			}
			// allow layout to be recomputed
			graphRoot.unfreeze();
			setCollapsed();
		}

		/**
		 * run a query to find and display the neighbours for this company node.
		 * if any neighbours are already visible as neighbours of another
		 * company then create an edge showing the link to the shared neighbour.
		 */
		void expandNeighbours() {
			try {
				ResultSet r = stmt.executeQuery("select fmcode, fund_man, shares*share_pric as val " + "from holders "
						+ "where epic = '" + epic + "' " + "order by val");
				int i = 0;
				while (r.next() && i++ < 10) {
					String fmcode = r.getString("fmcode");
					String fundman = r.getString("fund_man");
					GraphControl.Node n = (GraphControl.Node) fmList.get(fmcode);
					if (n == null) {
						n = graphRoot.addNode("DefaultNodeView");
						// fund managers are green
						n.setColour(0.0f, 0.8f, 0.0f);
						n.setLabel(fundman);
						n.setPosition(node.getPosition());
						FMNodeData data = new FMNodeData(n, stmt, fmcode);
						data.addNeighbour(node, epic);
						n.setUserData(data);
						fmList.put(fmcode, n);
					}
					if (neighbours.get(fmcode) == null) {
						neighbours.put(fmcode, n);
						GraphControl.Edge e = graphRoot.addEdge(n, node, "Plain Edge", 0.005f);
					}
				}
				graphRoot.unfreeze();
				setExpanded();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		/** UK stock market code for the Company */
		String epic;
	}

	/**
	 * NodeData for fund manager nodes, defining actions for expanding and
	 * collapsing the fund managers neighbouring company nodes.
	 */
	public class FMNodeData extends QueryNodeData {
		public FMNodeData(GraphControl.Node fmnode, Statement stmt, String fmcode) {
			this.stmt = stmt;
			this.fmcode = fmcode;
			this.node = fmnode;
			setCollapsed();
		}

		/**
		 * Elide the company neighbours of this company that are not referenced
		 * by any other visible fund managers.
		 */
		void collapseNeighbours() {
			for (Enumeration e = neighbours.keys(); e.hasMoreElements();) {
				String epic = (String) e.nextElement();
				GraphControl.Node n = (GraphControl.Node) companyList.get(epic);
				if (n.getDegree() == 1) {
					companyList.remove(epic);
					n.delete();
					neighbours.remove(epic);
				}
			}
			graphRoot.unfreeze();
			setCollapsed();
		}

		/**
		 * run a query to find and display the neighbours for this fundman node.
		 * if any neighbours are already visible as neighbours of another
		 * fundman then create an edge showing the link to the shared neighbour.
		 */
		void expandNeighbours() {
			try {
				ResultSet r = stmt.executeQuery("select epic, full_name, shares*share_pric as val " + "from holders "
						+ "where fmcode = " + fmcode + " " + "order by val");
				int i = 0;
				while (r.next() && i++ < 10) {
					String epic = r.getString("epic");
					String fullName = r.getString("full_name");
					GraphControl.Node n = (GraphControl.Node) companyList.get(epic);
					if (n == null) {
						n = graphRoot.addNode("DefaultNodeView");
						n.setLabel(fullName);
						n.setPosition(node.getPosition());
						CompanyNodeData data = new CompanyNodeData(n, stmt, epic);
						data.addNeighbour(node, fmcode);
						n.setUserData(data);
						companyList.put(epic, n);
					}
					if (neighbours.get(epic) == null) {
						neighbours.put(epic, n);
						GraphControl.Edge e = graphRoot.addEdge(node, n, "Plain Edge", 0.005f);
					}
				}
				graphRoot.unfreeze();
				setExpanded();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		/** the unique fund manager code for this node */
		String fmcode;
	}

	void ownershipQuery(String startFundman) {
		fmList.clear();
		companyList.clear();
		try {
			Connection con = DriverManager.getConnection(url, userName, password);
			Statement stmt = con.createStatement();
			ResultSet r = stmt.executeQuery("select fmcode, fund_man " + "from market " + "where fund_man like '%"
					+ startFundman + "%' ");
			r.next();
			String fmcode = r.getString("fmcode");
			String fundman = r.getString("fund_man");
			GraphControl.Node n = graphRoot.addNode("DefaultNodeView");
			n.setColour(0f, 0.8f, 0f);
			n.setLabel(fundman);
			n.setUserData(new FMNodeData(n, stmt, fmcode));
			fmList.put(fmcode, n);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// END OWNERSHIP VIEW
	// ========================================================================
	// BEGIN FUND MANAGER MOVEMENT VIEW
	void fmMovementQuery(String startDate, String endDate) {
		nodes.clear();
		String maxMinQueryString = "select max(val) as maxVal, min(val) as minVal "
				+ "from (select b.fund_man, b.sector, s.sector, "
				+ "      sum(b.shares*b.share_pric) + sum(s.shares*s.share_pric) as val " + "      from buy b, sell s "
				+ "      where b.fund_man = s.fund_man " + "      and b.notified between date('" + startDate + "') "
				+ "                         and date('" + endDate + "') " + "      and s.notified between date('"
				+ startDate + "') " + "                         and date('" + endDate + "')"
				+ "      group by b.fund_man, b.sector, s.sector) as subselect";
		String queryString = "select b.fund_man, b.sector as buy_sector, s.sector as sell_sector, "
				+ " b.sec_name as buy_sec_name, s.sec_name as sell_sec_name, "
				+ " sum(b.shares*b.share_pric) + sum(s.shares*s.share_pric) as value, " + " count(*) "
				+ "from buy b, sell s " + "where b.fund_man = s.fund_man " + "and b.notified between date('"
				+ startDate + "') and date('" + endDate + "') " + "and s.notified between date('" + startDate
				+ "') and date('" + endDate + "') " + "group by 1,2,3,4,5 ";
		try {
			Connection con = DriverManager.getConnection(url, userName, password);
			Statement stmt = con.createStatement();
			ResultSet r = stmt.executeQuery(maxMinQueryString);
			r.next();
			float minValue = r.getFloat("minVal");
			float maxValue = r.getFloat("maxVal");
			r = stmt.executeQuery(queryString);
			while (r.next()) {
				System.out.println(r.getString(1));
				String buySector = r.getString("buy_sector");
				String sellSector = r.getString("sell_sector");
				if (buySector.equals(sellSector)) {
					continue;
				}
				addNode(buySector, r.getString("buy_sec_name"));
				addNode(sellSector, r.getString("sell_sec_name"));
				float shareValue = r.getFloat("value");
				float weight = (shareValue - minValue) / (maxValue - minValue);
				addEdge(sellSector, buySector, weight);
				System.out.println("Buy Sector = " + buySector + ", Sell Sector = " + sellSector);
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		graphRoot.unfreeze();
	}

	// END FUND MANAGER MOVEMENT VIEW
	// ========================================================================
	// BEGIN FUND MANAGER MOVEMENT ACROSS TIME VIEW
	// we've defined a set of tables for company lookups at each time interval
	// and a set for looking up holding changes (diffs) for a particular time
	// This class just gives us a record us a reference to one of each for each
	// time period.
	class Table {
		String diffs;
		String company;

		Table(String d, String c) {
			diffs = d;
			company = c;
		}
	}

	// at one point I was drawing each time period separately. This class
	// defined a callback for when the graph for one time period was settled
	// so the next could be generated. Currently not used!
	class Client implements BalancedEventClient {
		Statement s;
		Vector t;

		Client(Statement s, Vector t) {
			this.s = s;
			this.t = t;
		}

		public void callback() {
			drawLevels(s, t);
		}
	}

	void fmMovementAcrossTimeQuery(String startDate, String endDate) {
		graphRoot.deleteAll();
		if (ColumnCluster.getColumnStyle() == ColumnCluster.DOTCOLUMNS) {
			graphRoot = graphRoot.addCluster();
			graphRoot.hide();
			org.wilmascope.dotlayout.DotLayout d = new org.wilmascope.dotlayout.DotLayout();
			graphRoot.setLayoutEngine(d);
			d.setXScale(Float.parseFloat(scaleField.getText()));
			d.setYScale(Float.parseFloat(scaleField.getText()));
		} else if (ColumnCluster.getColumnStyle() == ColumnCluster.FORCECOLUMNS) {
			((org.wilmascope.forcelayout.ForceLayout) graphRoot.getLayoutEngine()).setVelocityAttenuation(0.005f);
		}
		String queryString = "select diffs_table, 'company_'||to_char(to_date(company_table, 'YYYY-MM-DD'),'YYYYMMDD') as company_table "
				+ "from diffs_tables "
				+ "where startdate >= '"
				+ startDate
				+ "' "
				+ "  and enddate <= '"
				+ endDate
				+ "'";
		columns = new TreeMap();
		Vector tables = new Vector();
		level = 0;
		try {
			Connection connection = DriverManager.getConnection(url, userName, password);
			Statement stmt = connection.createStatement();
			ResultSet outer = stmt.executeQuery(queryString);
			while (outer.next()) {
				tables.add(new Table(outer.getString(1), outer.getString(2)));
			}
			// Client c = new Client(stmt,tables);
			// graphRoot.setBalancedEventClient(c);
			// c.callback();
			maxLevel = tables.size();
			while (drawLevels(stmt, tables)) {
			}
			;
			graphRoot.unfreeze();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			WilmaMain.showErrorDialog("SQL Error: " + queryString, e);
		}
	}

	void portfolioColumns(int style) {
		companyCluster = null;
		graphRoot.deleteAll();
		edgeThreshold = Float.parseFloat(edgeThresholdField.getText());
		columnThreshold = Integer.parseInt(columnThresholdField.getText());
		ColumnCluster.setColumnStyle(style);
		if (ColumnCluster.getColumnStyle() == ColumnCluster.DOTCOLUMNS) {
			// graphRoot = graphRoot.addCluster();
			// graphRoot.hide();
			org.wilmascope.dotlayout.DotLayout d = new org.wilmascope.dotlayout.DotLayout();
			graphRoot.setLayoutEngine(d);
			d.setXScale(Float.parseFloat(dotScaleField.getText()));
			d.setYScale(Float.parseFloat(dotScaleField.getText()));
		} else if (ColumnCluster.getColumnStyle() == ColumnCluster.FORCECOLUMNS) {
			org.wilmascope.forcelayout.ForceLayout f = new org.wilmascope.forcelayout.ForceLayout();
			graphRoot.setLayoutEngine(f);
			f.setVelocityAttenuation(0.001f);
			f.setFrictionCoefficient(90f);
			f.addForce(new Spring(0.1f));
			f.addForce(new Repulsion(5f, 100f));
			f.addForce(new Origin(8f));
			graphControl.setIterationsPerFrame(100);
		}
		columns = new TreeMap();
		Vector tables = new Vector();
		level = 0;
		HashMap secnamemap = new HashMap();
		try {
			String rec;
			BufferedReader in = new BufferedReader(new FileReader(org.wilmascope.global.GlobalConstants.getInstance()
					.getProperty("DefaultDataPath") + File.separator + "secnames"));
			{
				int i = 0;
				while ((rec = in.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(rec, ",");
					// System.out.println(rec);
					String secode = new String(st.nextToken());
					String secname = new String(st.nextToken());
					secnamemap.put(new Integer(i++), secname);
				}
			}
			in = new BufferedReader(new java.io.FileReader(org.wilmascope.global.GlobalConstants.getInstance()
					.getProperty("DefaultDataPath") + File.separator + "export.csv"));
			rec = in.readLine();
			StringTokenizer stl = new StringTokenizer(rec, ", ");
			int numofsectors = Integer.parseInt(stl.nextToken());
			int numofmonths = Integer.parseInt(stl.nextToken());
			long[][] count = new long[numofmonths][numofsectors];
			float[][] value = new float[numofmonths][numofsectors];
			maxLevel = numofmonths - 1;
			String[] strataNames = new String[numofmonths];
			for (int i = 0; i < numofmonths; i++) {
				strataNames[i] = new String("Month " + i);
			}
			graphControl.getRootCluster().setUserData(strataNames);
			count = new long[numofmonths][numofsectors];
			value = new float[numofmonths][numofsectors];
			for (int i = 0; i < numofmonths; i++) {
				rec = in.readLine();
				StringTokenizer st = new StringTokenizer(rec, ", ");
				for (int j = 0; j < numofsectors; j++) {
					count[i][j] = Long.parseLong(st.nextToken());
				}
			}
			for (int i = 0; i < numofmonths; i++) {
				rec = in.readLine();
				StringTokenizer st = new StringTokenizer(rec, ", ");
				for (int j = 0; j < numofsectors; j++) {
					value[i][j] = Float.parseFloat(st.nextToken());
				}
			}
			// find a suitable value for column scale
			// compile a list of the columnThreshold largest sectors in
			// displaySectors
			// only these will be used
			maxmc = 0;
			float[] vals = new float[numofsectors];
			for (int i = 0; i < numofmonths; i++) {
				for (int j = 0; j < numofsectors; j++) {
					float c = count[i][j];
					float v = value[i][j];
					float val = c * v;
					vals[j] += val;
					if (val > maxmc) {
						maxmc = val;
					}
				}
			}
			Hashtable sectorvals = new Hashtable();
			for (int i = 0; i < numofsectors; i++) {
				sectorvals.put(new Float(vals[i]), new Integer(i));
			}
			Arrays.sort(vals);
			for (int i = 0; i < numofsectors - columnThreshold; i++) {
				sectorvals.remove(new Float(vals[i]));
			}
			int[] displaySectors = new int[columnThreshold];
			{
				int i = 0;
				for (Enumeration e = sectorvals.elements(); e.hasMoreElements();) {
					displaySectors[i++] = ((Integer) e.nextElement()).intValue();
				}
			}
			in.close();
			for (int i = 0; i < numofmonths; i++) {
				decreased = new Vector();
				increased = new Vector();
				for (int j = 0; j < sectorvals.size(); j++) {
					long c = count[i][displaySectors[j]];
					float v = value[i][displaySectors[j]]; // the value in the
															// file is share
															// value
					int sec = displaySectors[j];
					System.out.println(sec + " " + (String) secnamemap.get(new Integer(sec)));
					addColumn((String) secnamemap.get(new Integer(sec)), v, c);
				}
				for (Iterator it = decreased.iterator(); it.hasNext();) {
					GraphControl.Node start = (GraphControl.Node) it.next();
					ColumnData sd = (ColumnData) start.getUserData();
					ColumnData sl = sd.last;
					for (Iterator jt = increased.iterator(); jt.hasNext();) {
						GraphControl.Node end = (GraphControl.Node) jt.next();
						ColumnData ed = (ColumnData) end.getUserData();
						ColumnData el = ed.last;
						float move = (ed.shareCount - el.shareCount) * el.shareValue + (sl.shareCount - sd.shareCount)
								* sl.shareValue;
						GraphControl.Edge edge;
						if (ColumnCluster.getColumnStyle() == ColumnCluster.DOTCOLUMNS) {
							edge = graphRoot.addEdge(start, end, "SplineTube", move / maxmc);
						} else {
							edge = graphRoot.addEdge(start, end, "Arrow", move / maxmc);
						}
						edge.setColour(0.8f * (float) level / (float) maxLevel,
								0.8f * (float) level / (float) maxLevel, 1f);
					}
				}
				level++;
				for (Iterator it = columns.keySet().iterator(); it.hasNext();) {
					String id = (String) it.next();
					ColumnCluster c = (ColumnCluster) columns.get(id);
					if (c.getNextLevel() < level) {
						System.out.println("id=" + id);
						float radius = c.getTopNode().getRadius();
						GraphControl.Node n = c.addNode(radius);
						n.setColour(0f, 0f, 0f);
					}
				}
			}
			float edgeMinRadius = Float.parseFloat(dotEdgeMinField.getText());
			float edgeMaxRadius = Float.parseFloat(dotEdgeMaxField.getText());
			GraphControl.Edge[] edges = graphRoot.getEdges();
			float minRadius = Float.MAX_VALUE, maxRadius = 0;
			for (int i = 0; i < edges.length; i++) {
				float r = ((EdgeView) edges[i].getView()).getRadius();
				if (r < minRadius) {
					minRadius = r;
				}
				if (r > maxRadius) {
					maxRadius = r;
				}
			}
			float scale = (edgeMaxRadius - edgeMinRadius) / (maxRadius - minRadius);
			for (int i = 0; i < edges.length; i++) {
				float r = ((EdgeView) edges[i].getView()).getRadius();
				r = (r - minRadius) * scale + edgeMinRadius;
				((EdgeView) edges[i].getView()).setRadius(r);
			}
			graphRoot.unfreeze();
		} catch (java.io.IOException e) {
			WilmaMain.showErrorDialog("IO Error", e);
		}
	}

	int maxLevel = 0;

	boolean drawLevels(Statement s, Vector tables) {
		if (tables.size() == 0)
			return false;
		Table t = (Table) tables.remove(0);
		nextLevel(s, t.diffs, t.company, Integer.parseInt(sectorField.getText()));
		// graphRoot.unfreeze();
		return true;
	}

	void nextLevel(Statement s, String diffsTable, String companyTable, int sector) {
		String queryString = new String(
				"select s.epic, b.epic, s.share_pric, b.share_pric, sum(s.new_shares * s.share_pric + b.new_shares * b.share_pric) as value, count(*) "
						+ "from "
						+ diffsTable
						+ " s, "
						+ diffsTable
						+ " b "
						+ "where s.fmcode = b.fmcode "
						+ "  and s.epic != b.epic "
						+ "  and s.holding_change = 'SELL' "
						+ "  and b.holding_change = 'BUY' "
						+ "  and s.sector = "
						+ sector
						+ " "
						+ "  and b.sector = s.sector " + "group by 1,2,3,4");
		try {
			ResultSet r = s.executeQuery(queryString);
			if (r.getFetchSize() == 0)
				return;
			System.out.println("commonlevel = " + level);
			while (r.next()) {
				String epic1 = r.getString(1);
				String epic2 = r.getString(2);
				float sharePrice1 = r.getFloat(3);
				float sharePrice2 = r.getFloat(4);
				float value = r.getFloat(5);
				int count = r.getInt(6);
				System.out.println("c1=" + epic1 + ",c2=" + epic2 + ",count=" + count);
				ColumnCluster a = addRelativeColumn(epic1, sharePrice1);
				ColumnCluster b = addRelativeColumn(epic2, sharePrice2);
				float minValue = 1000f;
				float maxValue = 100000f;
				float weight = (value - minValue) / (maxValue - minValue);
				addColumnEdge(a, b, weight, new EdgeOption(epic1, epic2, diffsTable, sector));
			}
			level++;
			for (Iterator i = columns.keySet().iterator(); i.hasNext();) {
				String id = (String) i.next();
				ColumnCluster c = (ColumnCluster) columns.get(id);
				if (c.getNextLevel() < level) {
					r = s.executeQuery("select share_pric from " + companyTable + " where epic = '" + id + "'");
					float radius = c.getTopNode().getRadius();
					if (r.next()) {
						radius = r.getFloat("share_pric");
					}
					GraphControl.Node n = c.addNode(radius);
					n.setColour(0.9f, 0.9f, 1f * (float) (level - 1) / (float) maxLevel);
				}
			}
		} catch (SQLException e) {
			WilmaMain.showErrorDialog("SQL Error: " + queryString, e);
		}
	}

	TreeMap columns;
	public static int level = 0;

	/**
	 * create a new column or if a column with that id already exists add a node
	 * to it. the radius of each level in the column will be relative to
	 * previous level and it will have a bottom radius of 1
	 */
	ColumnCluster addRelativeColumn(String id, float value) {
		ColumnCluster c = (ColumnCluster) columns.get(id);
		if (c == null) {
			c = new ColumnCluster(id, graphRoot, value, 1f, level, "Tube Node");
			columns.put(id, c);
		}
		if (c.getNextLevel() < level + 1) {
			GraphControl.Node n = c.addNode(value);
			n.setColour(0.9f, 0.9f, 1f * (float) level / (float) maxLevel);
		}
		return c;
	}

	Vector decreased, increased;

	/**
	 * create a new column or if a column with that id already exists add a node
	 * to it.
	 */
	class ColumnData {
		float shareValue;
		long shareCount;
		ColumnData last;
	}

	int columnThreshold = 10;
	float edgeThreshold = 0.05f;
	float maxmc = 0;
	GraphControl.Cluster companyCluster;

	ColumnCluster addColumn(String id, float shareValue, long count) {
		ColumnData d = new ColumnData();
		float nodeScale = Float.parseFloat(dotColumnScaleField.getText());
		d.shareValue = shareValue;
		System.out.println("Share value = " + shareValue + ", count=" + count);
		d.shareCount = count;
		float value = shareValue * count;
		ColumnCluster c = (ColumnCluster) columns.get(id);
		if (c == null) {
			StringTokenizer st = new StringTokenizer(id, " ");
			GraphControl.Cluster parent = graphRoot;
			int i = 0;
			String firstToken = st.nextToken();
			String[] idStrings = new String[st.countTokens()];
			if (firstToken.equals("subcluster")) {
				if (companyCluster == null) {
					companyCluster = graphRoot.addCluster("Tube Cluster");
					companyCluster.setProperty("LevelConstraint", "0");
					companyCluster.getNode().getLayout().resetProperties();
					org.wilmascope.forcelayout.ForceLayout f = (org.wilmascope.forcelayout.ForceLayout) companyCluster
							.getLayoutEngine();
					f.addForce(new Origin(8f));
					f.addForce(new Repulsion(1f, 5f));
					f.addForce(new Spring(5f));
				}
				parent = companyCluster;
			} else {
				idStrings = new String[st.countTokens() + 1];
				idStrings[i++] = firstToken;
			}
			while (st.hasMoreTokens()) {
				idStrings[i++] = st.nextToken();
			}
			c = new ColumnCluster(parent, nodeScale * value / maxmc, nodeScale * value / maxmc, level,
					"Column Cluster", "Tube Node");
			c.setLabel(idStrings);
			((ColumnLayout) c.getClusterFacade().getLayoutEngine()).setStrataSeparation(0.5f);
			columns.put(id, c);
		}
		// if(c.getNextLevel() < level + 1) {
		ColumnData l = d;
		GraphControl.Node ln = c.getTopNode();
		if (ln != null) {
			l = (ColumnData) ln.getUserData();
		}
		if (l == null) {
			l = d;
		}
		d.last = l;
		GraphControl.Node n = c.addVariableNode(nodeScale * value / maxmc);
		// colour the node according to change in share value:
		// white if no change
		// shade toward green if value increases, red if it decreases
		float diffShareValue = (d.shareValue - l.shareValue) / l.shareValue;
		float defaultGrey = 0.8f;
		if (diffShareValue > defaultGrey) {
			diffShareValue = defaultGrey;
		}
		if (diffShareValue < -defaultGrey) {
			diffShareValue = -defaultGrey;
		}
		if (diffShareValue > 0.01f) {
			n.setColour(defaultGrey - diffShareValue, defaultGrey, defaultGrey - diffShareValue);
		} else if (diffShareValue < -0.01f) {
			n.setColour(defaultGrey, defaultGrey + diffShareValue, defaultGrey + diffShareValue);
		} else {
			n.setColour(defaultGrey, defaultGrey, defaultGrey);
		}
		// n.setMass(value);
		n.setUserData(d);
		// if holding increases by more than edgeThreshold add to increased list
		// if it decreases by more than edgeThreshod add to decreased list
		if (edgeDiffByCountCheckBox.isSelected()) {
			diffShareValue = (float) (d.shareCount - l.shareCount) / (float) l.shareCount;
		}
		if (diffShareValue > edgeThreshold) {
			increased.add(n);
		} else if (diffShareValue < -edgeThreshold) {
			decreased.add(n);
		}
		return c;
	}

	void addColumnEdge(ColumnCluster from, ColumnCluster to, float weight, EdgeOption edgeOption) {
		float radius = 0.005f * (2 * weight + 1);
		GraphControl.Node start = from.getTopNode();
		GraphControl.Node end = to.getTopNode();
		GraphControl.Edge edge;
		if (ColumnCluster.getColumnStyle() == ColumnCluster.DOTCOLUMNS) {
			edge = graphRoot.addEdge(start, end, "SplineTube", radius);
		} else {
			edge = graphRoot.addEdge(start, end, "Arrow", radius);
		}
		edge.setWeight(weight);
		edge.setColour(0.9f, 0.9f, 1f * (float) level / (float) maxLevel);
		edge.setUserData(edgeOption);
	}

	class EdgeOption extends ElementData {
		EdgeOption(final String startEPIC, final String endEPIC, final String diffsTable, final int sector) {
			setActionDescription("Show Fund Manager details...");
			setActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					GraphControl.Edge edge = org.wilmascope.gui.EdgeOptionsMenu.getSelectedEdge();
					System.out.println(startEPIC + "->" + endEPIC);
					String queryString = new String(
							"select s.fund_man, sum(s.new_shares * s.share_pric + b.new_shares * b.share_pric) as value, count(*) "
									+ "from " + diffsTable + " s, " + diffsTable + " b " + "where s.fmcode = b.fmcode "
									+ "  and b.epic = '" + endEPIC + "'" + "  and s.epic = '" + startEPIC + "'"
									+ "  and s.holding_change = 'SELL' " + "  and b.holding_change = 'BUY' "
									+ "  and s.sector = " + sector + " " + "  and b.sector = s.sector " + "group by 1");
					try {
						Connection connection = DriverManager.getConnection(url, userName, password);
						Statement stmt = connection.createStatement();
						ResultSet r = stmt.executeQuery(queryString);
						if (r.getFetchSize() == 0)
							return;
						System.out.println("commonlevel = " + level);
						while (r.next()) {
							String fmcode = r.getString("fund_man");
							float value = r.getFloat("value");
							int count = r.getInt(3);
							System.out.println("fmcode=" + fmcode + ", value=" + value + ", count=" + count);
						}
					} catch (SQLException ex) {
						WilmaMain.showErrorDialog("SQL Error: " + queryString, ex);
					}
				}
			});
		}
	}

	void addNode(String id, String label) {
		if (!nodes.containsKey(id)) {
			GraphControl.Node n = graphRoot.addNode("LabelOnly");
			n.setLabel(label);
			nodes.put(id, n);
		}
	}

	void addEdge(String fromID, String toID, float weight) {
		float radius = 0.005f * (2 * weight + 1);
		GraphControl.Node start = (GraphControl.Node) nodes.get(fromID);
		GraphControl.Node end = (GraphControl.Node) nodes.get(toID);
		GraphControl.Edge edge = graphRoot.addEdge(start, end, "Arrow", radius);
		edge.setWeight(weight);
	}

	void addEdge(String fromID, String toID, float weight, String type) {
		float radius = 0.01f * (weight);
		GraphControl.Node start = (GraphControl.Node) nodes.get(fromID);
		GraphControl.Node end = (GraphControl.Node) nodes.get(toID);
		GraphControl.Edge edge = graphRoot.addEdge(start, end, type, radius);
		float defaultStiffness = ((org.wilmascope.forcelayout.EdgeForceLayout) edge.getEdge().getLayout())
				.getStiffness();
		((org.wilmascope.forcelayout.EdgeForceLayout) edge.getEdge().getLayout()).setStiffness(5f * (weight - 1f)
				* defaultStiffness);
		edge.setWeight(weight);
	}

	void dotColumnsRadioButton_actionPerformed(ActionEvent e) {
		ColumnCluster.setColumnStyle(ColumnCluster.DOTCOLUMNS);
	}

	void forceColumnRadioButton_actionPerformed(ActionEvent e) {
		ColumnCluster.setColumnStyle(ColumnCluster.FORCECOLUMNS);
	}

	void wormRadioButton_actionPerformed(ActionEvent e) {
		ColumnCluster.setColumnStyle(ColumnCluster.WORMS);
	}

	// END FUND MANAGER MOVEMENT ACCROSS TIME VIEW
	// ==========================================================================
	// BEGIN SECTOR VIEW
	void sectorQuery(String startDate, String endDate) {
		org.wilmascope.forcelayout.ForceLayout l = (org.wilmascope.forcelayout.ForceLayout) graphRoot.getLayoutEngine();
		l.getForce("Spring").setStrengthConstant(0.5f);
		l.getForce("Repulsion").setStrengthConstant(5f);
		// l.removeForce(l.getForce("Origin"));
		l.getForce("Origin").setStrengthConstant(4f);
		nodes.clear();
		String maxMinQueryString = "select max(weight) as max_weight, min(weight) as min_weight from ( "
				+ "select buy_sector, sell_sector, sum(weight) as weight " + "from sector_movement_200112 "
				+ "where start_date between '" + startDate + "' and '" + endDate + "' " + "group by 1,2 ) as foo";
		String queryString = "select buy_sector, sell_sector, " + "sum(weight) as weight "
				+ "from sector_movement_200112 " + "where start_date between '" + startDate + "' and '" + endDate
				+ "' " + "group by 1,2 " + "order by 3 desc";
		try {
			Connection con = DriverManager.getConnection(url, userName, password);
			Statement stmt = con.createStatement();
			ResultSet r = stmt.executeQuery(maxMinQueryString);
			r.next();
			float minWeight = r.getFloat("min_weight");
			float maxWeight = r.getFloat("max_weight");
			r = stmt.executeQuery(queryString);
			int i = 0;
			int visibleEdges = sectorEdgeSlider.getValue();
			boolean planar = planarSectorRadioButton.isSelected();
			while (r.next()) {
				String buySector = r.getString("buy_sector");
				String sellSector = r.getString("sell_sector");
				addSectorNode(buySector, planar, con);
				addSectorNode(sellSector, planar, con);
				float weight = r.getFloat("weight");
				weight = (weight - minWeight) / (maxWeight - minWeight) + 1f;
				if (i++ < visibleEdges) {
					addEdge(sellSector, buySector, weight, "Arrow");
				} else {
					addEdge(sellSector, buySector, weight, "NONE");
				}
				System.out.println("Buy Sector = " + buySector + ", Sell Sector = " + sellSector);
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println(queryString);
		}
		graphRoot.unfreeze();
	}

	/**
	 * add a sector node, looking up the short name and market capitalisation of
	 * the sector from a precomputed table
	 */
	void addSectorNode(String id, boolean planar, Connection con) {
		if (!nodes.containsKey(id)) {
			GraphControl.Node n = graphRoot.addNode();
			n.setUserData(new SectorNodeData(id));
			nodes.put(id, n);
			String query = "select case when short_sec is null then to_char(sector,'99') else short_sec end as name, "
					+ "cap_total from all_sectors where sector = " + id;
			try {
				Statement stmt = con.createStatement();
				ResultSet r = stmt.executeQuery(query);
				r.next();
				String label = r.getString(1);
				float cap = r.getFloat(2);
				if (planar) {
					n.setProperty("LevelConstraint", "0");
					n.getNode().getLayout().resetProperties();
				}
				n.setLabel(label);
				// I've hard coded the max and min market caps accross all
				// sectors
				// cos I'm lazy and this is just meant to be a demo.
				// should get these in another query
				cap = ((cap - 96f) / (256665f - 96f));
				// Colour from dark to light blue, the lighter the colour the
				// higher
				// the market cap
				n.setColour(cap, cap, 1f);
				// The bigger the node the larger the market cap
				n.setRadius((1 + cap) * n.getRadius());
				stmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				System.out.println(query);
			}
		}
	}

	/**
	 * defines a custom action for the node options menu for Sector nodes. The
	 * user can use this to initiate a fund manager movement query for the
	 * chosen sector.
	 */
	class SectorNodeData extends ElementData {
		String id;

		SectorNodeData(final String id) {
			this.id = id;
			setActionDescription("Zoom Sector...");
			setActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					thisframe.show();
					queryPane.setSelectedComponent(fmMovement2Panel);
					startDate2Field.setText(sectorStartField.getText());
					endDate2Field.setText(sectorEndField.getText());
					sectorField.setText(id);
				}
			});
		}
	}

	// END SECTOR VIEW
	// =========================================================================
	// global variables - DOH!
	/** the root cluster to which we will add all nodes and edges */
	public static GraphControl.Cluster graphRoot;
	// handy reference for use in anonymous inner classes
	QueryFrame thisframe = this;
	/** User name for connecting to SQL database */
	String userName = GlobalConstants.getInstance().getProperty("DatabaseUsername");
	/** password for connecting to SQL database */
	String password = GlobalConstants.getInstance().getProperty("DatabasePassword");
	/** location of the database */
	String url = GlobalConstants.getInstance().getProperty("DatabaseID");
	// =========================================================================
	// GUI objects, generated by JBuilder
	JPanel jPanel2 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JPanel sectorPanel = new JPanel();
	GridLayout gridLayout3 = new GridLayout();
	JLabel jLabel7 = new JLabel();
	JLabel jLabel8 = new JLabel();
	JTextField sectorStartField = new JTextField();
	JTextField sectorEndField = new JTextField();
	JLabel jLabel9 = new JLabel();
	JSlider sectorEdgeSlider = new JSlider();
	JRadioButton planarSectorRadioButton = new JRadioButton();
	JLabel jLabel2 = new JLabel();
	JTextField endDateField = new JTextField();
	JPanel jPanel1 = new JPanel();
	JButton okButton = new JButton();
	Hashtable nodes = new Hashtable();
	JTabbedPane queryPane = new JTabbedPane();
	JPanel fmMovementPanel = new JPanel();
	JLabel jLabel1 = new JLabel();
	GridLayout gridLayout1 = new GridLayout();
	JTextField startDateField = new JTextField();
	JPanel ownershipPanel = new JPanel();
	JButton cancelButton = new JButton();
	JLabel jLabel3 = new JLabel();
	JTextField fundmanField = new JTextField();
	JPanel fmMovement2Panel = new JPanel();
	JPanel fmMovement3Panel = new JPanel();
	JButton fmMovement3DotButton = new JButton();
	JButton fmMovement3ForceButton = new JButton();
	GridLayout gridLayout2 = new GridLayout();
	JLabel jLabel4 = new JLabel();
	JLabel jLabel5 = new JLabel();
	JTextField startDate2Field = new JTextField();
	JTextField endDate2Field = new JTextField();
	JLabel jLabel6 = new JLabel();
	JTextField sectorField = new JTextField();
	JTextField edgeThresholdField = new JTextField();
	JTextField dotScaleField = new JTextField();
	JTextField dotEdgeMinField = new JTextField();
	JTextField dotEdgeMaxField = new JTextField();
	JTextField dotColumnScaleField = new JTextField();
	JTextField columnThresholdField = new JTextField();
	JCheckBox edgeDiffByCountCheckBox = new JCheckBox("Edge threshold by count");
	JRadioButton wormRadioButton = new JRadioButton();
	JRadioButton dotColumnsRadioButton = new JRadioButton();
	JRadioButton forceColumnRadioButton = new JRadioButton();
	ButtonGroup styleButtonGroup = new ButtonGroup();
	private JButton scaleButton = new JButton();
	private Box box1;
	private JLabel jLabel10 = new JLabel();
	private JTextField scaleField = new JTextField();

	void scaleButton_actionPerformed(ActionEvent e) {
		// ColumnSizeControlsFrame f = new ColumnSizeControlsFrame(graphRoot);
		// f.pack();
		// f.show();
	}
}
