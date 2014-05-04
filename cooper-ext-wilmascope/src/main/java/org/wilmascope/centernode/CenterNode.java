package org.wilmascope.centernode;

/**
 * @author star
 * 
 */
import java.util.Enumeration;

import javax.media.j3d.Interpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3f;

/**
 * Class CenterNode translates the selected node to the center of the screen and
 * zooms it to an appropriate size.
 */
public class CenterNode extends Interpolator {
	private long elapsedTime = 25;
	private Transform3D targetMatrix = new Transform3D();
	private long durationX = 500;
	private long durationZ = 500;
	private long timerX = 0, timerZ = 0;
	private WakeupOnElapsedTime criterion;
	private TransformGroup target;
	private Matrix4d mat = new Matrix4d();
	private Vector3f trans = new Vector3f();
	private float dx, dy, dz;

	/**
	 * @param target
	 *            The TransformGroup class Center Node manipulates
	 */
	public CenterNode(TransformGroup target) {
		this.target = target;
	}

	/**
	 * Initializes the wake up event:wake up every 25 miliseconds
	 */
	public void initialize() {

		criterion = new WakeupOnElapsedTime(elapsedTime);
		wakeupOn(criterion);
	}

	/**
	 * Translate the whole scene graph a little every time wakes up
	 */
	public void processStimulus(Enumeration e) {
		target.getTransform(targetMatrix);
		targetMatrix.get(mat);
		// first move in the xoy plane
		if (timerX < durationX) {
			mat.m03 += dx;
			mat.m13 += dy;
			timerX += 25;
		}
		// then zoom
		else {
			if (timerZ < durationZ) {
				mat.m23 += dz;
				timerZ += 25;
			}
		}
		targetMatrix.set(mat);
		target.setTransform(targetMatrix);
		if ((timerX >= durationX) && (timerZ >= durationZ)) {
			timerX = timerZ = 0;
			wakeupOn(criterion);
			this.setEnable(false);
		} else
			wakeupOn(criterion);

	}

	/**
	 * Sets the origin position to the specified position
	 */
	public void setOriginPosition(Vector3f position) {
		target.getTransform(targetMatrix);
		targetMatrix.get(trans);
		dx = -(position.x + trans.x) * elapsedTime / durationX;
		dy = -(position.y + trans.y) * elapsedTime / durationX;
		dz = -((position.z + trans.z) - 1f) * elapsedTime / durationZ;
	}

	/**
	 * Sets the origin position to the specified position
	 * 
	 * @param position
	 *            target origin
	 * @param width
	 *            of the object that needs to fit in the viewport
	 */
	public void setOriginPosition(Vector3f position, float width) {
		target.getTransform(targetMatrix);
		targetMatrix.get(trans);
		dx = -(position.x + trans.x) * elapsedTime / durationX;
		dy = -(position.y + trans.y) * elapsedTime / durationX;
		dz = -((position.z + trans.z) + 2 * width) * elapsedTime / durationZ;
	}

}
