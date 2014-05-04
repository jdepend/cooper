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
package org.wilmascope.layoutregistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import org.wilmascope.control.WilmaMain;
import org.wilmascope.global.GlobalConstants;
import org.wilmascope.graph.LayoutEngine;

/*
 * Title: WilmaToo Description: Sequel to the ever popular WilmaScope software
 * Copyright: Copyright (c) 2001 Company: WilmaScope.org @author Tim Dwyer
 * 
 * @version 1.0
 */

/**
 * This class provides a manager or registry of all the available layout
 * engines. This class implements the Singleton design pattern (Gamma et al.)
 * such that there can only ever be one instance in the system and a reference
 * to that instance can be obtained by calling the static {link #getInstance()}
 * method from anywhere.
 */
public class LayoutManager {
	LayoutEngine defaultLayout;

	public static LayoutManager getInstance() {
		return instance;
	}

	private LayoutManager() {
		layoutEngines = new Hashtable<String, LayoutEngine>();
		load();
		try {
			defaultLayout = createLayout(GlobalConstants.getInstance().getProperty("DefaultLayout"));
		} catch (UnknownLayoutTypeException e) {
			WilmaMain.showErrorDialog("DefaultLayout specified in WILMA_CONSTANTS.properties file is unknown!", e);
		}
	}

	public class UnknownLayoutTypeException extends Exception {
		public UnknownLayoutTypeException(String layoutType) {
			super("No known layout type: " + layoutType);
		}
	}

	public LayoutEngine createLayout(String layoutType) throws UnknownLayoutTypeException {
		LayoutEngine prototype = (LayoutEngine) layoutEngines.get(layoutType);
		if (prototype == null) {
			throw (new UnknownLayoutTypeException(layoutType));
		}
		try {
			return (LayoutEngine) prototype.getClass().newInstance();
		} catch (InstantiationException e) {
			WilmaMain.showErrorDialog("Couldn't instantiate LayoutEngine (InstantiationException): " + layoutType, e);
		} catch (IllegalAccessException e) {
			WilmaMain.showErrorDialog("Couldn't instantiate LayoutEngine (IllegalAccessException): " + layoutType, e);
		}
		return null;
	}

	public void addPrototypeLayout(LayoutEngine prototype) {
		layoutEngines.put(prototype.getName(), prototype);
	}

	public Collection getAvailableLayoutEngines() {
		return layoutEngines.values();
	}

	private Hashtable<String, LayoutEngine> layoutEngines;

	private static final LayoutManager instance = new LayoutManager();

	public LayoutEngine[] getAll() {
		LayoutEngine[] all = new LayoutEngine[layoutEngines.size()];
		int i = 0;
		for (Enumeration e = layoutEngines.elements(); e.hasMoreElements();) {
			all[i++] = (LayoutEngine) e.nextElement();
		}
		return all;
	}

	/**
	 * If you create a layout engine then add the fully qualified class path to
	 * the LayoutPlugins field in WILMA_CONSTANTS.properties
	 */
	public void load() {
		try {
			String plugins = GlobalConstants.getInstance().getProperty("LayoutPlugins");
			List<String> classNames = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(plugins, ",");
			while (st.hasMoreElements()) {
				String el = (String) st.nextElement();
				classNames.add(el);
			}
			String[] list = new String[classNames.size()];
			list = (String[]) classNames.toArray(list);
			for (int i = 0; i < list.length; i++) {
				System.out.println("Loading plugin file: " + list[i]);
				String name = list[i];
				Class c = Class.forName(name);
				LayoutEngine eng = (LayoutEngine) c.newInstance();
				addPrototypeLayout(eng);
			}
		} catch (Exception e) {
			WilmaMain
					.showErrorDialog(
							"WARNING: Couldn't load plugins... check that you've listed the plugin classnames in the properties file",
							e);
		}
	}

	/**
	 * @return an array of type names
	 */
	public String[] getTypeList() {
		String[] typeList = new String[layoutEngines.size()];
		int i = 0;
		for (Enumeration e = layoutEngines.keys(); e.hasMoreElements();) {
			typeList[i++] = (String) e.nextElement();
		}
		return typeList;
	}
}
