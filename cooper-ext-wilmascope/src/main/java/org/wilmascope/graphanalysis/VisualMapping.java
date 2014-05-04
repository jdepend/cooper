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
package org.wilmascope.graphanalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JPanel;

import org.wilmascope.control.GraphControl.Cluster;

public abstract class VisualMapping {
	/**
	 * Comment for <code>analysis</code>
	 */
	String attributeName;

	JPanel controls = new JPanel();

	public VisualMapping(String attribute) {
		attributeName = attribute;
	}

	public static final String propertyKey = "VisualMapping";

	public String getAttributeName() {
		return attributeName;
	}

	public void storeMapping(Cluster c, String analysisProperty) {
		String existingMapping = c.getProperties().getProperty("VisualMapping");
		if (existingMapping != null) {
			c.getProperties().setProperty(propertyKey, existingMapping + "," + analysisProperty + "-" + attributeName);
		} else {
			c.getProperties().setProperty(propertyKey, analysisProperty + "-" + attributeName);
		}
	}

	public static void removeStoredMapping(Cluster c, String analysisProperty) {
		String existingMapping = c.getProperties().getProperty("VisualMapping");
		String newMapping = null;
		StringTokenizer st = new StringTokenizer(existingMapping, ",");
		while (st.hasMoreTokens()) {
			String mapping = st.nextToken();
			String[] m = mapping.split("-");
			if (!m[0].equals(analysisProperty)) {
				if (newMapping == null) {
					newMapping = mapping;
				} else {
					newMapping = newMapping + "," + mapping;
				}
			}
		}
		c.getProperties().setProperty("VisualMapping", newMapping);
	}

	public static List<String> getClusterMappings(Cluster c) {
		String mappings = c.getProperties().getProperty(VisualMapping.propertyKey);
		ArrayList<String> mappingList = new ArrayList<String>();
		if (mappings != null) {
			StringTokenizer st = new StringTokenizer(mappings, ",");
			while (st.hasMoreTokens()) {
				mappingList.add(st.nextToken());
			}
		}
		return mappingList;
	}

	public abstract void apply(Cluster c, String analysisProperty);

	public JPanel getControls() {
		return controls;
	}
}
