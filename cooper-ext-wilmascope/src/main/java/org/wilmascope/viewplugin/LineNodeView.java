package org.wilmascope.viewplugin;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.vecmath.Color3f;

import org.wilmascope.view.NodeView;
import org.wilmascope.view.Renderer2D;

/**
 * Basically the following is an invisible dummy node... might replace it with
 * an OpenGL dot
 */
public class LineNodeView extends NodeView {

	public LineNodeView() {
		setTypeName("LineNode");
	}

	protected void setupHighlightMaterial() {
	}

	protected void setupDefaultMaterial() {
	}

	protected void init() {
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("dot.png"));
	}

	public void draw2D(Renderer2D r, Graphics2D g, float transparency) {
		Color3f c = getColor3f();
		g.setColor(c.get());
		r.fillCircle(g, getNode().getPosition(), 0.001f);
	}
}
