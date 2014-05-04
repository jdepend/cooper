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

import java.io.FileNotFoundException;
import java.net.URL;

import javax.media.j3d.Appearance;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;

import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.view.Colours;
import org.wilmascope.view.NodeView;
import org.wilmascope.view.View2D;

import com.sun.j3d.loaders.Scene;

/**
 * A box shaped node. Labels are texture mapped onto the face.
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class StarNodeView extends NodeView implements View2D {

	public StarNodeView() {
		setTypeName("Star Node");
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
		String fileName = "star.WRL";
		VrmlLoader l = new VrmlLoader();
		Scene s = null;
		URL url = null;

		Transform3D scale = new Transform3D();
		scale.setScale(0.2);

		TransformGroup objTrans = new TransformGroup(scale);

		try {
			url = org.wilmascope.images.Images.class.getResource(fileName);
			if (url == null)
				throw new FileNotFoundException();
			s = l.load(url);
			Shape3D shape = (Shape3D) s.getNamedObjects().get("Star01_SHAPE");
			objTrans.addChild(shape);
			addTransformGroupChild(objTrans);
			makePickable(shape);
		} catch (FileNotFoundException e) {
			WilmaMain.showErrorDialog("VRML File: " + url.getPath() + ", not found when creating node view", e);
		}
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("starnode.png"));
	}

}
