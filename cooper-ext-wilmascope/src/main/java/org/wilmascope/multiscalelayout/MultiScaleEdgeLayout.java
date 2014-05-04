package org.wilmascope.multiscalelayout;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * $Id: MultiScaleEdgeLayout.java,v 1.1 2002/08/09 02:20:20 tgdwyer Exp $
 * </p>
 * <p>
 * @author
 * </p>
 * <p>
 * @version $Revision: 1.1 $
 * </p>
 * unascribed
 * 
 */

public class MultiScaleEdgeLayout extends org.wilmascope.graph.EdgeLayout {
	MultiScaleNodeLayout u;
	MultiScaleNodeLayout v;

	MultiScaleEdgeLayout(MultiScaleNodeLayout u, MultiScaleNodeLayout v) {
		this.u = u;
		this.v = v;
		if (!u.neighbours.contains(v)) {
			u.neighbours.add(v);
		} else {
			System.out.println("MultiEdge");
		}
		if (!v.neighbours.contains(u)) {
			v.neighbours.add(u);
		} else {
			System.out.println("MultiEdge");
		}
	}

	public boolean equals(Object o) {
		if (!(o instanceof MultiScaleEdgeLayout) || o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		MultiScaleEdgeLayout other = (MultiScaleEdgeLayout) o;
		if (other.u == this.u && other.v == this.v || other.v == this.u && other.u == this.v) {
			return true;
		}
		return false;
	}
}
