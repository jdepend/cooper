package org.wilmascope.rotation;

/**
 * @author Christine
 *
 */
import java.util.Enumeration;

import javax.media.j3d.Interpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

/**
 * AutoRotation is a java3D Interpolator object used to continously rotate the
 * scene graph. <blockquote>
 * 
 * <pre>
 * rotator = new AutoRotation(transformGroup);
 * rotator.setSchedulingBounds(bounds);
 * bg.addChild(rotator);
 * </pre>
 * 
 * </blockquote> The above code will add the AutoRotation object to the
 * branchgroup group.
 */
public class AutoRotation extends Interpolator {
	private TransformGroup target;

	private Transform3D targetMatrix = new Transform3D();

	private Transform3D deltaRotationX = new Transform3D();

	private Transform3D deltaRotationY = new Transform3D();

	private Transform3D deltaRotationZ = new Transform3D();

	private double dAngleX = 0, dAngleY = 0;

	private long elapsedTime = 25;

	private WakeupOnElapsedTime criterion;

	private Matrix4d mat = new Matrix4d();

	/**
	 * @param target
	 *            The TransformGroup AutoRotation will manipulate
	 */

	public AutoRotation(TransformGroup target) {
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
	 * Rotates the scene graph with a small angle each time wakes up
	 */
	public void processStimulus(Enumeration e) {

		target.getTransform(targetMatrix);
		// save old matrix
		targetMatrix.get(mat);
		targetMatrix.setTranslation(new Vector3d(0.0, 0.0, 0.0));

		deltaRotationY.rotY(dAngleY);
		deltaRotationX.rotX(dAngleX);
		targetMatrix.mul(deltaRotationX, targetMatrix);
		targetMatrix.mul(deltaRotationY, targetMatrix);

		// Set old translation back
		Vector3d translation = new Vector3d(mat.m03, mat.m13, mat.m23);
		targetMatrix.setTranslation(translation);
		target.setTransform(targetMatrix);
		this.wakeupOn(criterion);

	}

	/**
	 * Sets up the rotation parameters
	 * 
	 * @param duration
	 *            The time between mouse pressed and mouse released
	 * @param angleX
	 *            During the mouse drag, how much angle has been rotated around
	 *            the x axis
	 * @param angleY
	 *            During the mouse drag, how much angle has been rotated around
	 *            the y axis
	 */

	public void setRotParams(long duration, double angleX, double angleY) {

		if (duration != 0) {
			dAngleX = angleX * elapsedTime / 3 / duration;
			dAngleY = angleY * elapsedTime / 3 / duration;
		} else
			dAngleX = dAngleY = 0;

	}

}
