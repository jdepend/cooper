package org.wilmascope.view;

/**
 * @author dwyer
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public interface SizeAdjustableNodeView {
	public void setEndRadii(final float bottomRadius, final float topRadius);

	/**
	 * If shape is BOX the: x dimension = the node's radius y dimension = depth
	 */
	public float getDepth();

	public float getBottomRadius();

	public float getTopRadius();

	public int getShape();

	final static int BOX = 0;
	final static int DISC = 1;
}
