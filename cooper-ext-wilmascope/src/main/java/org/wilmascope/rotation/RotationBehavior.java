package org.wilmascope.rotation;

/**
 * @author Christine
 *
 */
import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Matrix4d;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;

/**
 * RotationBehavior is a Java3D behavior object that lets users control the
 * rotation direction and speed via a mouse. <blockquote>
 * 
 * <pre>
 * RotationBehavior rotationBehavior = new RotationBehavior(bg, transformGroup, bounds);
 * rotationBehavior.setSchedulingBounds(bounds);
 * bg.addChild(rotationBehavior);
 * </pre>
 * 
 * </blockquote> The above code will add the rotation behavior to the
 * branchgroup group.
 */
public class RotationBehavior extends MouseBehavior {
	private AutoRotation rotator;

	private Transform3D temp = new Transform3D();

	private Transform3D trans = new Transform3D();

	private long timePressed, timeReleased, timeElapse, duration;

	private double angleX, angleY, angleZ;

	private int x_start, y_start, x_end, y_end;

	private BranchGroup bg;

	private Bounds bounds;

	private BranchGroup rbg = new BranchGroup();

	private boolean rotateZ = false;

	private Matrix4d mat = new Matrix4d();

	/**
	 * When initialize, Rotation Behavior will create an {@link AutoRotation}
	 * object to rotate the scene graph continuously.
	 * 
	 * @param bg
	 *            The branch group the AutoRotation object will be attached
	 * @param transformGroup
	 *            The transform group RotationBehavior controls
	 * @param bounds
	 *            The scheduling bounds of the AutoRotation Object
	 */
	public RotationBehavior(BranchGroup bg, TransformGroup transformGroup, Bounds bounds) {
		super(transformGroup);
		this.bg = bg;
		// create an AutoRotation Object so that after the left mouse button is
		// released, the scene graph
		// will rotate continuously
		rotator = new AutoRotation(transformGroup);
		rotator.setSchedulingBounds(bounds);
		rotator.setEnable(false);
		rbg.addChild(rotator);
		bg.addChild(rbg);
	}

	/**
	 * Initializes the wake up events
	 */
	public void initialize() {
		mouseEvents = new WakeupCriterion[2];
		mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
		mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
		mouseCriterion = new WakeupOr(mouseEvents);
		wakeupOn(mouseCriterion);
	}

	/**
	 * causes a gentle left to right rotation
	 */
	public void defaultRotate() {
		rotator.setEnable(true);
		rotator.setRotParams(1, 0, 0.003);
	}

	/**
	 * Processes the mouse events
	 */
	public void processStimulus(Enumeration criteria) {
		WakeupCriterion wakeup;
		AWTEvent[] event;
		int id, buttonMask;
		double dx = 0, dy = 0, dz = 0, dAngle = 0;

		while (criteria.hasMoreElements()) {
			wakeup = (WakeupCriterion) criteria.nextElement();
			event = ((WakeupOnAWTEvent) wakeup).getAWTEvent();

			for (int i = 0; i < event.length; i++) {
				id = event[i].getID();
				buttonMask = ((MouseEvent) event[i]).getModifiers();
				x = ((MouseEvent) event[i]).getX();
				y = ((MouseEvent) event[i]).getY();
				if ((id == MouseEvent.MOUSE_PRESSED) && (buttonMask == MouseEvent.BUTTON1_MASK)) {
					// disable the rotator when left mouse button is pressed
					// so that the rotation
					// will stop
					rotator.setEnable(false);
					x_start = x;
					y_start = y;
					// remember the button pressed time
					timePressed = System.currentTimeMillis();
				}
				if ((id == MouseEvent.MOUSE_RELEASED) && (buttonMask == MouseEvent.BUTTON1_MASK)) {

					y_end = y;
					x_end = x;
					// calculate the time between the mouse pressed and the
					// mouse released
					timeReleased = System.currentTimeMillis();
					timeElapse = timeReleased - timePressed;
					// calcuate the how much angle has been rotated
					angleY = (x_end - x_start) * 0.03;
					angleX = (y_end - y_start) * 0.03;

					rotator.setRotParams(timeElapse, angleX, angleY);

					if ((x_start != x_end) || (y_start != y_end))
						rotator.setEnable(true);

				}

			}
			wakeupOn(mouseCriterion);
		}
	}

	/**
	 * Returns the AutoRotation Object
	 */
	public AutoRotation getRotator() {
		return rotator;
	}
}
