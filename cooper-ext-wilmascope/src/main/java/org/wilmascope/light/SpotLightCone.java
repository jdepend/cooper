package org.wilmascope.light;

/**@author Christine
 *  
 */

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Sphere;

/**
 * This class creates a cone to represent a spot light's colour, position,
 * direction and spreadangle
 */
public class SpotLightCone extends TransformGroup {
	private double radius;
	private double hight = 0.15;
	private Cone cone;
	private Color3f eColor = new Color3f(0.0f, 1.0f, 0.0f);
	private Color3f sColor = new Color3f(1.0f, 1.0f, 1.0f);
	private Color3f objColor = new Color3f(0.0f, 0.0f, 0.0f);
	private Material m = new Material(objColor, eColor, objColor, sColor, 100.0f);
	private Appearance a = new Appearance();

	private Transform3D scaleMatrix = new Transform3D();
	private Transform3D tempZ = new Transform3D();
	private Transform3D tempY = new Transform3D();
	private Transform3D positionMatrix = new Transform3D();
	// TransformGroup to rotate around the z axis and y axis
	private TransformGroup ZGroup = new TransformGroup();
	private TransformGroup YGroup = new TransformGroup();
	// TransformGroup to scale in x and z axis
	private TransformGroup ScaleGroup = new TransformGroup();
	private Vector3f xAxis = new Vector3f(1, 0, 0);
	private Vector3f yAxis = new Vector3f(0, 1, 0);
	// transparent sphere
	private Sphere transparentSphere;
	private Material sphereMaterial = new Material();
	private TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.FASTEST,
			0.7f);
	private Appearance sphereAppearance = new Appearance();

	public SpotLightCone() {
		// sets the material of the cone
		m.setCapability(Material.ALLOW_COMPONENT_READ);
		m.setCapability(Material.ALLOW_COMPONENT_WRITE);
		m.setLightingEnable(true);
		a.setMaterial(m);
		a.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		a.setCapability(Appearance.ALLOW_MATERIAL_READ);
		radius = hight * (float) Math.tan(Math.PI / 6);
		cone = new Cone((float) radius, (float) hight, a);
		cone.setCapability(Geometry.ALLOW_INTERSECT);
		Transform3D t = new Transform3D();
		t.rotX(Math.PI);
		t.setTranslation(new Vector3f(0f, 0.075f, 0f));
		TransformGroup transCone = new TransformGroup(t);

		// transparent sphere
		transparencyAttributes.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
		sphereMaterial.setDiffuseColor(eColor);
		sphereMaterial.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		sphereMaterial.setCapability(Appearance.ALLOW_MATERIAL_READ);
		sphereAppearance.setMaterial(sphereMaterial);
		sphereAppearance.setTransparencyAttributes(transparencyAttributes);
		sphereAppearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
		transparentSphere = new Sphere(0.16f, sphereAppearance);
		Transform3D t1 = new Transform3D();
		t1.setTranslation(new Vector3f(0f, 0.08f, 0f));
		TransformGroup transSphere = new TransformGroup(t1);
		transSphere.addChild(transparentSphere);

		transCone.addChild(cone);
		ScaleGroup.addChild(transCone);
		ZGroup.addChild(ScaleGroup);
		ZGroup.addChild(transSphere);
		YGroup.addChild(ZGroup);
		this.addChild(YGroup);

		YGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		YGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ZGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		ZGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ScaleGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		ScaleGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.setTransform(positionMatrix);
		this.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

	}

	/**
	 * Sets the colour of the cone
	 * 
	 * @param color
	 *            colour of the spot light
	 */
	public void setColor(Color3f color) {
		m.setEmissiveColor(color);
		sphereMaterial.setDiffuseColor(color);
	}

	/**
	 * Sets the spreadAngle of the cone
	 * 
	 * @param spreadAngle
	 *            spreadangle of the spot light
	 */
	public void setSpreadAngle(float spreadAngle) {
		// calculate the new radius of the cone
		double newRadius = hight * Math.tan(spreadAngle / 2);
		// calculate the amount to scale in x and z axis
		double scale = newRadius / radius;
		// scale in x and z direction
		scaleMatrix.setScale(new Vector3d(scale, 1d, scale));
		ScaleGroup.setTransform(scaleMatrix);

	}

	/**
	 * sets the position of the cone
	 * 
	 * @param position
	 *            the position of the spotlight
	 */
	public void setPosition(Point3f position) {
		positionMatrix.setTranslation(new Vector3f(position.x, position.y, position.z));
		this.setTransform(positionMatrix);
	}

	/**
	 * Sets the direction of the cone
	 * 
	 * @param direction
	 *            the direction or the spot light
	 */
	public void setDirection(Vector3f direction) {

		// angle between the y axis and the direction vector
		float FI;
		// angle between the x axis and the direction vector
		float Theta;
		FI = -direction.angle(yAxis);
		Theta = -xAxis.angle(new Vector3f(direction.x, 0, direction.z));
		if (direction.z < 0)
			Theta = -Theta;

		tempZ.rotZ(FI);
		ZGroup.setTransform(tempZ);
		tempY.rotY(Theta);
		YGroup.setTransform(tempY);

	}

	public void setTransparency(float tranparency) {
		transparencyAttributes.setTransparency(tranparency);
	}

}
