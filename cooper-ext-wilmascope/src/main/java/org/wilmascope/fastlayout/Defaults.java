package org.wilmascope.fastlayout;

/**
 * Description: Defaults for the many parameters used in FastLayout layout
 * engine
 * 
 * @author James Cowling
 * @version 1.0
 */

public class Defaults {

	// how much to multiply the repulsion term by when calculating potential
	static final public int REPULSION = 1;

	// how much to multiply the attraction term by when calculating potential
	static final public int ATTRACTION = 2;

	// the size of a node footprint (in universe units) on the density matrix
	// the density will attenuate within this footprint
	static final public int NODE_FOOTPRINT = 1;

	// the maximum distance a node can jump during the boiling phase
	// this will decrease in subsequent phases
	// this factor would need to be tuned heavily for different sized graphs,
	// it'd be nice
	// to try to calculate it automatically eventually
	static final public float BOIL_JUMP = 1f;

	// the indexes in the 'universe' matrix are integral yet the node positions
	// are floats
	// this resolution defines how much to scale the floating point position
	// by before rounding to an int
	static final public int FIELD_RES = 3;

	// the number of times to run applyLayout before it is considered balanced
	// (fixed num as recommended by Eades)
	static final public int ITERATIONS = 200;

	// the fraction of the total iterations that are performed in the quenching
	// phase
	// ( < 1 - BOIL_LENGTH)
	static final public double QUENCH_LENGTH = 0.4;

	// the fraction of the total iterations that are performed in the boiling
	// phase
	// ( < 1 - QUENCH_LENGTH)
	static final public double BOIL_LENGTH = 0.4;

	// the frequency of barrier jumping during the boiling phase ( < 1)
	static final public double MAX_BARRIER_RATE = 0.25;

	// the frequency of barrier jumping at the end of the quenching phase ( < 1)
	static final public double MIN_BARRIER_RATE = 0.1;

	// the radius of the initial 'universe' containing the nodes
	// the universe will expand, via array-doubling, as necessary
	static final public int FIELD_RADIUS = 10;

	// whether graph centring is enabled
	static final public boolean CENTRE_FLAG = false;

	// whether colour coding by potential is enabled
	static final public boolean COLOUR_FLAG = false;

	// whether extra eye-candy is enabled
	static final public boolean EYE_CANDY_FLAG = false;

	// the fraction of the max jump (during the boiling phase) that applies
	// during the simmering phase
	static final public double SIMMER_RATE = 0.1;

	// indicates the min level the density should attenuate to at the edge of
	// the
	// node footprint
	static final public double MIN_DENSITY = 0.04;

	// specifies if the algorithm should be naive (and faster) in not updating
	// the density term
	// at a position with the density of the current node, before deciding if
	// the potential is less
	// at that point
	static final public boolean NAIVE = false;

	// specifies whether barrier jumping should be enabled in the boiling phase
	static final public boolean BOIL_BARRIER = true;

}
