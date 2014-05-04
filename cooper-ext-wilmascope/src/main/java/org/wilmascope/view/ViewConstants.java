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

package org.wilmascope.view;

import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.vecmath.Vector3f;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class ViewConstants extends org.wilmascope.global.AbstractConstants {
	private static final ResourceBundle WILMA_CONSTANTS_PROPERTIES = PropertyResourceBundle
			.getBundle("WILMA_CONSTANTS");

	private static ViewConstants instance = null;

	public static ViewConstants getInstance() {
		if (instance == null) {
			instance = new ViewConstants(getDefaultProperties(), WILMA_CONSTANTS_PROPERTIES);
		}
		return instance;
	}

	private ViewConstants(Properties defaultProperties, ResourceBundle bundle) {
		super(defaultProperties, bundle);
	}

	protected static Properties getDefaultProperties() {
		Properties d = new Properties();
		d.setProperty(
				"Plugins",
				"org.wilmascope.viewplugin.DefaultNodeView,org.wilmascope.viewplugin.DefaultEdgeView,org.wilmascope.viewplugin.DefaultClusterView");
		d.setProperty("FogDensity", "0.2");
		d.setProperty("FogColourR", "0.5");
		d.setProperty("FogColourG", "0.5");
		d.setProperty("FogColourB", "0.5");
		d.setProperty("BackgroundColourR", "0");
		d.setProperty("BackgroundColourG", "0");
		d.setProperty("BackgroundColourB", "0");
		d.setProperty("AmbientLightColourR", "1");
		d.setProperty("AmbientLightColourG", "1");
		d.setProperty("AmbientLightColourB", "1");
		d.setProperty("DirectionalLightVectorX", "-1");
		d.setProperty("DirectionalLightVectorY", "-1");
		d.setProperty("DirectionalLightVectorZ", "-1");
		d.setProperty("DirectionalLightColourR", "1");
		d.setProperty("DirectionalLightColourG", "1");
		d.setProperty("DirectionalLightColourB", "1");
		d.setProperty("SharedGeometry", "0");
		d.setProperty("LabelUseViewColour", "false");
		d.setProperty("LabelColourR", "1");
		d.setProperty("LabelColourG", "0.5");
		d.setProperty("LabelColourB", "0.5");
		/*
		 * d.setProperty("PointLight1ColourR","0");
		 * d.setProperty("PointLight1ColourG","0");
		 * d.setProperty("PointLight1ColourB","0");
		 * d.setProperty("PointLight1PositionX","0");
		 * d.setProperty("PointLight1PositionY","0");
		 * d.setProperty("PointLight1PositionZ","0");
		 * d.setProperty("PointLight1AttenuationConstant","0");
		 * d.setProperty("PointLight1AttenuationLinear","0");
		 * d.setProperty("PointLight1AttenuationQuadratic","0");
		 */
		return d;
	}

	public static org.wilmascope.global.GlobalConstants gc = org.wilmascope.global.GlobalConstants.getInstance();
	public static final Vector3f vX = gc.vX;
	public static final Vector3f vY = gc.vY;
	public static final Vector3f vZ = gc.vZ;
	public static final Vector3f vZero = gc.vZero;
}
