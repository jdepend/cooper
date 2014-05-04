package org.wilmascope.light;

/**
 * @author star
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.PointLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;

/**
 * This class enables using mouse to control the position of the point light
 */
public class PointLightMouseCtrl extends MouseBehavior {
	private Transform3D temp = new Transform3D();
	private Transform3D oldPos = new Transform3D();
	private Matrix4d transMatrix = new Matrix4d();
	private PointLight pointLight;
	private PointLightSphere sphere;
	private Point3f position = new Point3f();
	// for the pick and mouse event
	private boolean buttonPressed;
	private PointLightPanel pointPane;
	private Color3f color = new Color3f();

	public PointLightMouseCtrl(PointLightPanel pointPane) {
		super(new TransformGroup());
		this.pointPane = pointPane;

	}

	/**
	 * Initialize the wake up event
	 */
	public void initialize() {
		mouseEvents = new WakeupCriterion[3];
		mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
		mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
		mouseEvents[2] = new WakeupOnAWTEvent(MouseEvent.MOUSE_CLICKED);
		mouseCriterion = new WakeupOr(mouseEvents);
		wakeupOn(mouseCriterion);
		x = y = 0;
		x_last = y_last = 0;
	}

	/**
	 * Processes each mouse event,making the point light and corrisponding
	 * sphere moving with the mouse movement
	 */
	// called by Java 3D when appropriate stimulus occurs
	public void processStimulus(Enumeration criteria) {
		WakeupCriterion wakeup;
		AWTEvent[] event;
		int id, buttonMask;
		double dx = 0, dy = 0, dz = 0;

		while (criteria.hasMoreElements()) {
			wakeup = (WakeupCriterion) criteria.nextElement();
			// if(wakeup instanceof WakeupOnAWTEvent){
			event = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
			for (int i = 0; i < event.length; i++) {
				id = event[i].getID();
				buttonMask = ((MouseEvent) event[i]).getModifiers();
				x = ((MouseEvent) event[i]).getX();
				y = ((MouseEvent) event[i]).getY();
				if (id == MouseEvent.MOUSE_PRESSED || (id == MouseEvent.MOUSE_CLICKED)) {
					x_last = x;
					y_last = y;
				}

				if ((buttonMask == MouseEvent.BUTTON1_MASK) && (id == MouseEvent.MOUSE_DRAGGED) && (sphere != null)) {
					// left button to change to x, y position
					dx = x - x_last;
					dy = -(y - y_last);
					dx = dx * 0.005;
					dy = dy * 0.005;
					x_last = x;
					y_last = y;

					if ((Math.abs(dx) > 0.3) || (Math.abs(dy) > 0.3))
						continue;
					sphere.getTransform(oldPos);
					oldPos.get(transMatrix);
					transMatrix.m03 += dx;
					transMatrix.m13 += dy;
					oldPos.set(transMatrix);
					sphere.setTransform(oldPos);

					pointLight.getPosition(position);
					position.x += dx;
					position.y += dy;
					pointLight.setPosition(position);

					pointPane.xPos.setText("" + position.x);
					pointPane.yPos.setText("" + position.y);
					pointPane.zPos.setText("" + position.z);

				}
				if ((buttonMask == MouseEvent.BUTTON3_MASK) && (id == MouseEvent.MOUSE_DRAGGED) && (sphere != null)) {
					// right button to zoom
					x = ((MouseEvent) event[i]).getX();
					dz = (x - x_last) * 0.015;

					x_last = x;
					y_last = y;
					if (Math.abs(dz) > 0.5)
						continue;
					sphere.getTransform(oldPos);
					oldPos.get(transMatrix);
					transMatrix.m23 += dz;
					oldPos.set(transMatrix);
					sphere.setTransform(oldPos);

					pointLight.getPosition(position);
					position.z += dz;
					pointLight.setPosition(position);

					pointPane.xPos.setText("" + position.x);
					pointPane.yPos.setText("" + position.y);
					pointPane.zPos.setText("" + position.z);

				}

			}
		}
		wakeupOn(mouseCriterion);
	}

	public void setSphere(PointLightSphere sphere) {
		this.sphere = sphere;
	}

	public void setLight(PointLight light) {
		this.pointLight = light;
	}
}
