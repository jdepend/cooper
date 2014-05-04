/*
 * The following source code is part of the WilmaScope 3D Graph Drawing Engine
 * which is distributed under the terms of the GNU Lesser General Public License
 * (LGPL - http://www.gnu.org/copyleft/lesser.html).
 *
 * As usual we distribute it with no warranties and anything you chose to do
 * with it you do at your own risk.
 *
 * Copyright for this work is retained by Tim Dwyer and the WilmaScope organisation
 * (www.wilmascope.org) however it may be used or modified to work as part of
 * other software subject to the terms of the LGPL.  I only ask that you cite
 * WilmaScope as an influence and inform us (tgdwyer@yahoo.com)
 * if you do anything really cool with it.
 *
 * The WilmaScope software source repository is hosted by Source Forge:
 * www.sourceforge.net/projects/wilma
 *
 * -- Tim Dwyer, 2001
 */

package org.wilmascope.view;

/**
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular Wilma graph drawing engine
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaOrg
 * @author Tim Dwyer
 * @version 1.0
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.vecmath.Color3f;

public class Colours {
	private static org.wilmascope.global.GlobalConstants constants = org.wilmascope.global.GlobalConstants
			.getInstance();

	public static class Material extends javax.media.j3d.Material {
		String name;
		Color awtColor;
		ImageIcon icon;

		Material(String name, Color awtColor, Color3f diffuseColor, Color3f ambientColor) {
			super();
			this.name = name;
			this.awtColor = awtColor;
			setCapability(ALLOW_COMPONENT_READ);
			setDiffuseColor(diffuseColor);
			setAmbientColor(ambientColor);
			setShininess(constants.getFloatValue("DefaultShininess"));
			Image image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
			Graphics graphics = image.getGraphics();
			graphics.setColor(awtColor);
			graphics.fill3DRect(0, 0, 20, 20, true);
			icon = new ImageIcon(image, name);
		}

		public Color getAwtColor() {
			return awtColor;
		}

		public String toString() {
			return name;
		}

		public ImageIcon getIcon() {
			return icon;
		}
	}

	public static Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
	public static Color3f ambientRed = new Color3f(0.4f, 0.0f, 0.0f);
	public static Color3f yellow = new Color3f(255f / 255f, 255f / 255f, 0f);
	public static Color3f ambientYellow = new Color3f(255f / 510f, 255f / 510f, 0f);
	public static Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
	public static Color3f ambientGreen = new Color3f(0.0f, 0.4f, 0.0f);
	public static Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);
	public static Color3f ambientBlue = new Color3f(0.0f, 0.0f, 0.4f);
	public static Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
	public static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
	public static Color3f beige = new Color3f(211f / 255f, 199f / 255f, 182f / 255f);
	public static Color3f greyBlueC3f = new Color3f(44f / 255f, 87f / 255f, 120f / 255f);
	public static Color3f greyBlueAmbC3f = new Color3f(34f / 255f, 68f / 255f, 94f / 255f);
	public static Color greyBlueAwt = new Color(44f / 255f, 87f / 255f, 120f / 255f);
	public static Color beigeAwt = new Color(211f / 255f, 199f / 255f, 182f / 255f);
	public static Color3f ambientBeige = new Color3f(120f / 255f, 100f / 255f, 73f / 255f);
	public static Color3f pink = new Color3f(218f / 255f, 90f / 255f, 215f / 255f);
	public static Color3f ambientPink = new Color3f(151f / 255f, 34f / 255f, 148f / 255f);
	public static Color pastelPinkAwt = new Color(231f / 255f, 169f / 255f, 190f / 255f);
	public static Color3f pastelPinkC3f = new Color3f(231f / 255f, 169f / 255f, 190f / 255f);
	public static Color3f pastelPinkAmbC3f = new Color3f(167f / 255f, 48f / 255f, 89f / 255f);
	public static Color pastelBlueAwt = new Color(12f / 255f, 226f / 255f, 250f / 255f);
	public static Color3f pastelBlueC3f = new Color3f(12f / 255f, 226f / 255f, 250f / 255f);
	public static Color3f pastelBlueAmbC3f = new Color3f(3f / 255f, 123f / 255f, 137f / 255f);
	public static Color pastelGreenAwt = new Color(91f / 255f, 223f / 255f, 145f / 255f);
	public static Color3f pastelGreenC3f = new Color3f(91f / 255f, 223f / 255f, 145f / 255f);
	public static Color3f pastelGreenAmbC3f = new Color3f(22f / 255f, 116f / 255f, 60f / 255f);
	public static Color pastelYellowAwt = new Color(255f / 255f, 255f / 255f, 128f / 255f);
	public static Color3f pastelYellowC3f = new Color3f(255f / 255f, 255f / 255f, 128f / 255f);
	public static Color3f pastelYellowAmbC3f = new Color3f(174f / 255f, 174f / 255f, 0f / 255f);
	public static Material redMaterial = new Material("Red", Color.red, red, ambientRed);
	public static Material yellowMaterial = new Material("Yellow", Color.yellow, yellow, ambientYellow);
	public static Material blueMaterial = new Material("Blue", Color.blue, blue, ambientBlue);
	public static Material pinkMaterial = new Material("Pink", Color.pink, pink, ambientPink);
	public static Material greenMaterial = new Material("Green", Color.green, green, ambientGreen);
	public static Material whiteMaterial = new Material("White", Color.white, white, white);
	public static Material defaultMaterial = new Material("default", beigeAwt, beige, ambientBeige);
	public static Material momentIntervalMaterial = new Material("moment-interval", pastelPinkAwt, pastelPinkC3f,
			pastelPinkAmbC3f);
	public static Material thingMaterial = new Material("thing", pastelGreenAwt, pastelGreenC3f, pastelGreenAmbC3f);
	public static Material roleMaterial = new Material("role", pastelYellowAwt, pastelYellowC3f, pastelYellowAmbC3f);
	public static Material descriptionMaterial = new Material("description", pastelBlueAwt, pastelBlueC3f,
			pastelBlueAmbC3f);
	public static Material greyBlueMaterial = new Material("greyBlue", greyBlueAwt, greyBlueC3f, greyBlueAmbC3f);
	public static Material blackMaterial = new Material("black", Color.black, black, black);

	public static class UnknownMaterialException extends Exception {
		public UnknownMaterialException(String name) {
			super(name);
		}
	}

	public static Material getMaterial(String name) throws UnknownMaterialException {
		if (name.equals(redMaterial.toString()))
			return redMaterial;
		else if (name.equals(blueMaterial.toString()))
			return blueMaterial;
		else if (name.equals(pinkMaterial.toString()))
			return pinkMaterial;
		else if (name.equals(greenMaterial.toString()))
			return greenMaterial;
		else if (name.equals(whiteMaterial.toString()))
			return whiteMaterial;
		else if (name.equals(momentIntervalMaterial.toString()))
			return momentIntervalMaterial;
		else if (name.equals(thingMaterial.toString()))
			return thingMaterial;
		else if (name.equals(roleMaterial.toString()))
			return roleMaterial;
		else if (name.equals(descriptionMaterial.toString()))
			return descriptionMaterial;
		else if (name.equals(defaultMaterial.toString()))
			return defaultMaterial;
		else
			throw new UnknownMaterialException(name);
	}
}
