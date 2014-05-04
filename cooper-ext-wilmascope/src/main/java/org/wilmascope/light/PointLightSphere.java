package org.wilmascope.light;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * @author star
 *
 */
/**
 * This class creates a sphere to represent the PointLight's position and colour
 */

public class PointLightSphere extends TransformGroup {
	private Color3f eColor = new Color3f(0.0f, 1.0f, 0.0f);
	private Color3f sColor = new Color3f(1.0f, 1.0f, 1.0f);
	private Color3f objColor = new Color3f(0.0f, 0.0f, 0.0f);
	private Material m = new Material(objColor, eColor, objColor, sColor, 100.0f);
	private Appearance a = new Appearance();

	private Transform3D translate = new Transform3D();
	private Sphere transparentSphere = new Sphere(0.03f);
	private Material sphereMaterial = new Material();
	private TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.FASTEST,
			0.7f);
	private Appearance sphereAppearance = new Appearance();
	private Sphere lightSphere;

	public PointLightSphere() {

		m.setCapability(Material.ALLOW_COMPONENT_READ);
		m.setCapability(Material.ALLOW_COMPONENT_WRITE);
		m.setLightingEnable(true);
		a.setMaterial(m);
		a.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		a.setCapability(Appearance.ALLOW_MATERIAL_READ);
		lightSphere = new Sphere(0.02f, a);

		transparencyAttributes.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
		sphereMaterial.setDiffuseColor(eColor);
		sphereMaterial.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		sphereMaterial.setCapability(Appearance.ALLOW_MATERIAL_READ);
		sphereAppearance.setMaterial(sphereMaterial);
		sphereAppearance.setTransparencyAttributes(transparencyAttributes);
		sphereAppearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
		transparentSphere = new Sphere(0.16f, sphereAppearance);

		this.addChild(lightSphere);
		this.addChild(transparentSphere);

		this.setTransform(translate);
		this.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	}

	/**
	 * Sets the position of the sphere
	 * 
	 */
	public void setPosition(Point3f position) {
		translate.setTranslation(new Vector3f(position.x, position.y, position.z));
		this.setTransform(translate);
	}

	/**
	 * Sets the color of the sphere
	 */
	public void setColor(Color3f color) {
		m.setEmissiveColor(color);
		sphereMaterial.setDiffuseColor(color);
	}

	public void setTransparency(float tranparency) {
		transparencyAttributes.setTransparency(tranparency);
	}

}
