package org.wilmascope.light;

/** @author Christine
 *
 * 
 */

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;

/**
 * This class enables using mouse to change the direction of the directional
 * light
 */
public class DirLightMouseCtrl extends MouseBehavior {
	private DirectionalLight dirLight;
	private Arrow arrow;
	private Vector3f direction = new Vector3f();
	private Transform3D temp1 = new Transform3D();
	private Transform3D temp2 = new Transform3D();
	// for the arrow translate
	private Matrix4d transMatrix = new Matrix4d();
	private Transform3D oldMatrix = new Transform3D();

	private Color3f color = new Color3f();

	// for the pick and mouse event
	protected MouseEvent mevent;
	private boolean buttonPressed;

	private WakeupCriterion[] mouseEvents;
	private WakeupOr mouseCriterion;
	private DirectionalLightPanel dirPane;

	public DirLightMouseCtrl(DirectionalLightPanel panel) {
		super(new TransformGroup());
		dirPane = panel;

	}

	/**
	 * Initializes the wake up event
	 */
	public void initialize() {
		mouseEvents = new WakeupCriterion[3];
		mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
		mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
		mouseEvents[2] = new WakeupOnAWTEvent(MouseEvent.MOUSE_CLICKED);
		mouseCriterion = new WakeupOr(mouseEvents);
		wakeupOn(mouseCriterion);

	}

	/**
	 * This method processes the mouse event,making the directional light and
	 * the corrisponding arrow changing with the mouse movement.
	 */
	public void processStimulus(Enumeration criteria) {
		WakeupCriterion wakeup;
		AWTEvent[] event;
		int id, buttonMask;
		double dx, dy;
		double angleX, angleY;
		boolean shiftDown;

		while (criteria.hasMoreElements()) {
			wakeup = (WakeupCriterion) criteria.nextElement();
			event = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
			for (int i = 0; i < event.length; i++) {
				id = event[i].getID();
				mevent = (MouseEvent) event[i];
				buttonMask = ((MouseEvent) event[i]).getModifiers();
				x = mevent.getX();
				y = mevent.getY();
				shiftDown = ((MouseEvent) event[i]).isShiftDown();
				if ((id == MouseEvent.MOUSE_PRESSED) || (id == MouseEvent.MOUSE_CLICKED)) {
					x_last = x;
					y_last = y;
				}
				if ((buttonMask == MouseEvent.BUTTON1_MASK) && (id == MouseEvent.MOUSE_DRAGGED)) {
					// rotate with the x and y axis
					// move x direction to rotate around the y axis
					// move y direction to rotate around the x axis
					dx = x - x_last;
					angleY = dx * 0.03;
					dy = -(y - y_last);
					angleX = dy * 0.03;
					x_last = x;
					y_last = y;
					temp1.rotY(angleY);
					temp2.rotX(angleX);
					temp2.mul(temp1);
					dirLight.getDirection(direction);
					temp2.transform(direction);
					dirLight.setDirection(direction);
					arrow.setDirection(direction);
					dirPane.xDir.setText("" + direction.x);
					dirPane.yDir.setText("" + direction.y);
					dirPane.zDir.setText("" + direction.z);

				}
				if ((buttonMask == MouseEvent.BUTTON3_MASK) && (id == MouseEvent.MOUSE_DRAGGED)) {
					dx = x - x_last;
					dy = -(y - y_last);
					dx = dx * 0.005;
					dy = dy * 0.005;
					x_last = x;
					y_last = y;
					if ((Math.abs(dx) > 0.5) || (Math.abs(dy) > 0.5))
						continue;
					arrow.getTransform(oldMatrix);
					oldMatrix.get(transMatrix);
					transMatrix.m03 += dx;
					transMatrix.m13 += dy;
					oldMatrix.set(transMatrix);
					arrow.setTransform(oldMatrix);
				}

			}
			wakeupOn(mouseCriterion);
		}
	}

	public void setArrow(Arrow arrow) {
		this.arrow = arrow;

	}

	public void setLight(DirectionalLight light) {
		this.dirLight = light;

	}
}
