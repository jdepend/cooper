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

/*
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular Wilma graph drawing engine
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaOrg
 * @author Tim Dwyer
 * @version 1.0
 */
/** Class for basic Graph animation behavior
 */
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupOnElapsedFrames;

public class GraphBehavior extends Behavior {
	WakeupOnElapsedFrames w = new WakeupOnElapsedFrames(0);
	// WakeupOnElapsedTime w = new WakeupOnElapsedTime(10);
	private BehaviorClient client;

	public GraphBehavior(BehaviorClient client) {
		this.client = client;
	}

	/**
	 * Initialize the behavior of the GraphNode
	 */
	public void initialize() {
		wakeupOn(w);
	}

	/**
	 * When the behavior triggers animation this is called
	 * 
	 * @param criteria
	 *            Enumeration of the criteria for the call
	 */
	public void processStimulus(Enumeration criteria) {
		client.callback();
		wakeupOn(w);
	}
}
