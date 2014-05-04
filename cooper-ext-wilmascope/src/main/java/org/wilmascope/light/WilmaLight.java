/**
 * @author Christine
 */
package org.wilmascope.light;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.PointLight;
import javax.media.j3d.SpotLight;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * The class serves as a memory for the light and it's parent branch group. The
 * light's parent branchgroup cannot be returned using java3d's method once the
 * light is turned 'live'. It's necessary to create a reference for the light's
 * parent branchgroup so that the light can be deleted after it is turned
 * 'live'.
 */
public class WilmaLight {
	private Light light;
	private BranchGroup lightBranchGroup;
	// branchGroup for the 3D Shapes
	private BranchGroup objGroup = new BranchGroup();
	// 3D Shapes
	private Arrow arrow;
	private SpotLightCone cone;
	private PointLightSphere sphere;

	public WilmaLight() {
		objGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		objGroup.setCapability(BranchGroup.ALLOW_DETACH);

	}

	public Light getLight() {
		return light;
	}

	/**
	 * @return The parent BranchGroup
	 */
	public BranchGroup getBranchGroup() {
		return lightBranchGroup;
	}

	/**
	 * Registers the light and create a 3D Shape to represent the light
	 */
	public void setLight(Light l) {
		light = l;

		if (l instanceof DirectionalLight) {
			Vector3f direction = new Vector3f();
			((DirectionalLight) l).getDirection(direction);
			Color3f color = new Color3f();
			((DirectionalLight) l).getColor(color);
			arrow = new Arrow();
			arrow.setDirection(direction);
			arrow.setColor(color);
			objGroup.addChild(arrow);

		}
		if ((l instanceof PointLight) && !(l instanceof SpotLight)) {
			Point3f position = new Point3f();
			((PointLight) l).getPosition(position);
			Color3f color = new Color3f();
			((PointLight) l).getColor(color);
			sphere = new PointLightSphere();
			sphere.setPosition(position);
			sphere.setColor(color);
			objGroup.addChild(sphere);

		}
		if (l instanceof SpotLight) {
			Point3f position = new Point3f();
			((SpotLight) l).getPosition(position);
			Color3f color = new Color3f();
			((SpotLight) l).getColor(color);
			Vector3f direction = new Vector3f();
			((SpotLight) l).getDirection(direction);
			float spreadAngle = ((SpotLight) l).getSpreadAngle();
			cone = new SpotLightCone();
			cone.setPosition(position);
			cone.setColor(color);
			cone.setDirection(direction);
			cone.setSpreadAngle(spreadAngle);
			objGroup.addChild(cone);

		}

	}

	/**
	 * Registers the light's parent BranchGroup
	 */
	public void setBranchGroup(BranchGroup b) {
		lightBranchGroup = b;
	}

	/**
	 * @return The Sphere that represents the point light
	 */
	public PointLightSphere getSphere() {
		return sphere;
	}

	/**
	 * @return The Cone that represents the spot light
	 */
	public SpotLightCone getCone() {
		return cone;
	}

	/**
	 * Detaches the 3D shape
	 */
	public void deleteObj() {
		objGroup.detach();
	}

	/**
	 * @return The branchgroup the 3D shape attached
	 */
	public BranchGroup getObjGroup() {
		return objGroup;
	}

	/**
	 * @return the arrow that represent the directional light
	 */
	public Arrow getArrow() {
		return arrow;
	}
}
