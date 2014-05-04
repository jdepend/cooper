package org.wilmascope.dotparser;

import java.awt.Point;
import java.util.StringTokenizer;
import java.util.Vector;

public abstract class EdgeClient {
	public class ArrowPosition {
		public ArrowPosition(int curveIndex, boolean arrowAtStart, Point position) {
			this.curveIndex = curveIndex;
			this.arrowAtStart = arrowAtStart;
			this.position = position;
		}

		public int curveIndex;
		public boolean arrowAtStart;
		public Point position;
	}

	public EdgeClient(NodeClient start, NodeClient end) {
		this.start = start;
		this.end = end;
	}

	public abstract void setCurves(java.util.Vector curves);

	public void setCurves(String curvesString) {
		Vector curves = new Vector();
		for (StringTokenizer st = new StringTokenizer(curvesString, ";"); st.hasMoreTokens();) {
			addCurve(curves, st.nextToken());
		}
		setCurves(curves);
	}

	private void addCurve(Vector curves, String curve) {
		Vector pnts = new Vector();
		for (StringTokenizer st = new StringTokenizer(curve, " "); st.hasMoreTokens();) {
			for (StringTokenizer pt = new StringTokenizer(st.nextToken(), ","); pt.hasMoreTokens();) {
				String s = pt.nextToken();
				if (s.equals("e") || s.equals("s")) {
					addArrow(curves.size(), s.equals("s") ? true : false, point(pt.nextToken(), pt.nextToken()));
				} else {
					pnts.add(point(s, pt.nextToken()));
				}
			}
		}
		curves.add(pnts);
	}

	private Point point(String a, String b) {
		return new Point(Integer.parseInt(a), Integer.parseInt(b));
	}

	public void addArrow(int curveIndex, boolean arrowAtStart, Point position) {
		arrowPositions.add(new ArrowPosition(curveIndex, arrowAtStart, position));
	}

	public Vector getArrowPositions() {
		return arrowPositions;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public NodeClient start, end;
	public String layer;
	public Vector arrowPositions = new Vector();
}
