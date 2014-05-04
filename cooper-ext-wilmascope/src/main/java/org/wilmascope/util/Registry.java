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
package org.wilmascope.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import org.wilmascope.control.WilmaMain;
import org.wilmascope.global.GlobalConstants;

/**
 * Provides a registry of plugins.
 * 
 * @author dwyer
 * 
 */
public class Registry<T extends Plugin> {
	/**
	 * Builds up the table of available generators and sets the default based on
	 * the "DefaultGenerator" field in the WILMA_CONSTANTS.properties file.
	 * 
	 * @throws UnknownTypeException
	 *             if the default generator
	 */
	protected Registry(String defaultPluginPropertyKey, String pluginListKey) {
		plugins = new Hashtable<String, T>();
		load(pluginListKey);
		try {
			defaultPlugin = getPlugin(GlobalConstants.getInstance().getProperty(defaultPluginPropertyKey));
		} catch (UnknownTypeException e) {
			WilmaMain.showErrorDialog(defaultPluginPropertyKey
					+ " specified in WILMA_CONSTANTS.properties file is unknown!", e);
		}
	}

	/**
	 * If you create a new generator then add the fully qualified class path to
	 * the GeneratorPlugins field in WILMA_CONSTANTS.properties
	 */
	public void load(String pluginListKey) {
		try {
			String plugins = GlobalConstants.getInstance().getProperty(pluginListKey);
			List classNames = new ArrayList();
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
				T gen = (T) c.newInstance();
				addPlugin(gen);
			}
		} catch (Exception e) {
			WilmaMain
					.showErrorDialog(
							"WARNING: Couldn't load plugins... check that you've listed the plugin classnames in the properties file",
							e);
		}
	}

	public String[] getTypeList() {
		return plugins.keySet().toArray(new String[plugins.size()]);
	}

	public T getPlugin(String type) throws UnknownTypeException {
		T g = plugins.get(type);
		if (g == null) {
			throw new UnknownTypeException(type);
		}
		return g;
	}

	public T getDefault() {
		return defaultPlugin;
	}

	public Collection<T> getPlugins() {
		return plugins.values();
	}

	public void addPlugin(T p) {
		plugins.put(p.getName(), p);
	}

	Hashtable<String, T> plugins;

	T defaultPlugin;
}
