package org.wilmascope.dotlayout;

/**
 * <p>Description: </p>
 * <p>$Id: Spline.java,v 1.3 2003/06/23 07:58:59 tgdwyer Exp $ </p>
 * <p>@author </p>
 * <p>@version $Revision: 1.3 $</p>
 *  unascribed
 *
 */
import java.awt.Rectangle;
import java.util.Vector;

public interface Spline {
	public void setScale(float xScale, float yScale);

	public void setCurves(float xScale, float yScale, int x0, int y0, int x1, int y1, Vector curves,
			Vector arrowPositions);

	public Vector getCurves();

	public Vector getArrowPositions();

	public float getXScale();

	public float getYScale();

	public Rectangle getBounds();

	public void copyCurves(Spline original);
}
