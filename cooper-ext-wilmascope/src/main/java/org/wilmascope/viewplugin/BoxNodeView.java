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

package org.wilmascope.viewplugin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.media.j3d.Appearance;
import javax.swing.ImageIcon;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.columnlayout.NodeColumnLayout;
import org.wilmascope.graph.Node;
import org.wilmascope.view.Colours;
import org.wilmascope.view.NodeView;
import org.wilmascope.view.Renderer2D;
import org.wilmascope.view.SizeAdjustableNodeView;
import org.wilmascope.view.View2D;

import com.sun.j3d.utils.geometry.Box;

/**
 * A box shaped node. Labels are texture mapped onto the face.
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class BoxNodeView extends NodeView implements SizeAdjustableNodeView, View2D {
	public BoxNodeView() {
		setTypeName("Box Node");
	}

	protected void setupDefaultMaterial() {
		setupDefaultAppearance(Colours.defaultMaterial);
		getAppearance().setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		getAppearance().setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
	}

	protected void setupHighlightMaterial() {
		setupHighlightAppearance(Colours.yellowMaterial);
	}

	protected void init() {
		// make the main box that will carry the texture mapped label
		box = new Box(1.0f, 1.0f, 1.0f, getAppearance());
		makePickable(box.getShape(Box.TOP));
		makePickable(box.getShape(Box.BOTTOM));
		makePickable(box.getShape(Box.BACK));
		makePickable(box.getShape(Box.FRONT));
		makePickable(box.getShape(Box.LEFT));
		makePickable(box.getShape(Box.RIGHT));
		addTransformGroupChild(box);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("cube.png"));
	}

	public void draw() {
		try {
			Node n = getNode();
			float height = ((NodeColumnLayout) n.getLayout()).getHeight();
			Vector3f v = new Vector3f(n.getPosition());
			float nr = getRadius() / 4.0f;
			float cr = ((ColumnClusterView) n.getOwner().getView()).getMaxRadius() / 4.0f;
			v.x -= (cr - nr);
			float depth = getDepth();
			if (nr == 0) {
				depth = 0;
			}
			setResizeTranslateTransform(new Vector3d(nr, depth, height / 2.0), v);
		} catch (NullPointerException e) {
			System.out.println("WARNING: Null pointer in ColumnClusterView.draw()");
		}
	}

	public void draw2D(Renderer2D r, Graphics2D g, float transparency) {
		Node n = getNode();
		float nr = getRadius() / 4.0f;
		float cr = ((ColumnClusterView) n.getOwner().getView()).getMaxRadius() / 4.0f;
		Point3f p = new Point3f(n.getPosition());
		g.setStroke(new BasicStroke(1f));
		g.setColor(new Color(0, 0, 0, transparency));
		r.drawRect(g, p, cr, getDepth());
		p.x -= (cr - nr);
		r.fillRect(g, p, nr, getDepth());
	}

	Box box;

	/**
	 * @see org.wilmascope.view.SizeAdjustableNodeView#getBottomRadius()
	 */
	public float getBottomRadius() {
		return getRadius();
	}

	/**
	 * @see org.wilmascope.view.SizeAdjustableNodeView#getTopRadius()
	 */
	public float getTopRadius() {
		return getRadius();
	}

	/**
	 * @see org.wilmascope.view.SizeAdjustableNodeView#setEndRadii(float, float)
	 */
	public void setEndRadii(float bottomRadius, float topRadius) {
		setRadius(topRadius / 100f);
	}

	public int getShape() {
		return BOX;
	}

	public float getDepth() {
		return 1f / 4f;
	}
}
