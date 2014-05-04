package org.wilmascope.light;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.PointLight;
import javax.media.j3d.SpotLight;
import javax.media.j3d.Transform3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * @author Christine
 *
 */
/**
 * This class saves the light configuration to the .properties file
 */
public class LightPropertiesSaver {
	private LineNumberReader lr;
	private String fileName;
	private File inFile;
	private File temp;
	private FileReader in;
	private FileWriter out;

	private Color3f colour = new Color3f();
	private Point3f position = new Point3f();
	private Vector3f direction = new Vector3f();
	private Point3f attenuation = new Point3f();

	private float SpreadAngle;
	private float Concentration;
	private LightManager lightManager;

	public LightPropertiesSaver(LightManager lightManager) {
		this.lightManager = lightManager;
		inFile = new File(lightManager.getPropertyFileName());
		temp = new File("temp.properties");
	}

	/**
	 * Saves lights parameters to file WILMA_CONSTANTS.properties
	 */
	public void saveToWilmaCons() {

		String line = new String();
		String title = new String();
		try {
			in = new FileReader(inFile);
			out = new FileWriter(temp);
			lr = new LineNumberReader(in);
			line = lr.readLine();

			while (line != null) {
				if (line.length() >= 5)
					title = line.substring(0, 5);
				else
					title = line;
				if (title.compareTo("Light") != 0) {
					out.write(line);
					out.write("\r\n");
				}
				line = lr.readLine();

			}
			saveAmbientLight();
			saveDirectionalLight();
			savePointLight();
			saveSpotLight();
			in.close();
			out.close();

			in = new FileReader("temp.properties");
			out = new FileWriter("WILMA_CONSTANTS.properties");
			lr = new LineNumberReader(in);
			line = lr.readLine();

			while (line != null) {
				out.write(line);
				line = lr.readLine();
				out.write("\r\n");
			}
			in.close();
			out.close();
			temp.delete();

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

	/**
	 * Saves lights configuration to file other than WILMA_CONSTANTS.properties
	 */
	public void saveToNewFile(String name) {

		try {
			out = new FileWriter(name);
			saveAmbientLight();
			saveDirectionalLight();
			savePointLight();
			saveSpotLight();
			out.close();
		} catch (IOException e) {
			System.err.println("Error saving the light configuration in file:" + lightManager.getPropertyFileName());
		}
	}

	/**
	 * Saves ambient lights parameters
	 */
	private void saveAmbientLight() throws IOException {
		try {
			for (int i = 0; i < lightManager.getAmbLightVector().size(); i++) {
				WilmaLight w = (WilmaLight) lightManager.getAmbLightVector().get(i);
				AmbientLight l = (AmbientLight) w.getLight();
				l.getColor(colour);
				out.write("Light" + i + "AmbientColourR=" + colour.x + "\r\n");
				out.write("Light" + i + "AmbientColourG=" + colour.y + "\r\n");
				out.write("Light" + i + "AmbientColourB=" + colour.z + "\r\n");
				if (l.getEnable() == true)
					out.write("Light" + i + "AmbientEnable=" + "true" + "\r\n");
				else
					out.write("Light" + i + "AmbientEnable=" + "false" + "\r\n");
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Saves directional lights parameters
	 */
	private void saveDirectionalLight() throws IOException {
		Vector3f position = new Vector3f();
		Transform3D trans = new Transform3D();
		for (int i = 0; i < lightManager.getDirLightVector().size(); i++) {
			WilmaLight w = (WilmaLight) lightManager.getDirLightVector().get(i);
			DirectionalLight l = (DirectionalLight) w.getLight();

			// colour
			l.getColor(colour);
			out.write("Light" + i + "DirectionalColourR=" + colour.x + "\r\n");
			out.write("Light" + i + "DirectionalColourG=" + colour.y + "\r\n");
			out.write("Light" + i + "DirectionalColourB=" + colour.z + "\r\n");
			// direction
			l.getDirection(direction);
			out.write("Light" + i + "DirectionalVectorX=" + direction.x + "\r\n");
			out.write("Light" + i + "DirectionalVectorY=" + direction.y + "\r\n");
			out.write("Light" + i + "DirectionalVectorZ=" + direction.z + "\r\n");

			if (l.getEnable() == true)
				out.write("Light" + i + "DirectionalEnable=" + "true" + "\r\n");
			else
				out.write("Light" + i + "DirectionalEnable=" + "false" + "\r\n");

			w.getArrow().getTransform(trans);
			trans.get(position);
			// remember the position of the arrows on the screen, the positions
			// have no
			// effects to the directional lights
			out.write("Light" + i + "DirectionalPositionX=" + position.x + "\r\n");
			out.write("Light" + i + "DirectionalPositionY=" + position.y + "\r\n");
			out.write("Light" + i + "DirectionalPositionZ=" + position.z + "\r\n");
		}
	}

	/**
	 * Saves point lights parameters
	 */
	private void savePointLight() throws IOException {
		for (int i = 0; i < lightManager.getPointLightVector().size(); i++) {
			WilmaLight w = (WilmaLight) lightManager.getPointLightVector().get(i);
			PointLight l = (PointLight) w.getLight();
			// colour
			l.getColor(colour);
			out.write("Light" + i + "PointColourR=" + colour.x + "\r\n");
			out.write("Light" + i + "PointColourG=" + colour.y + "\r\n");
			out.write("Light" + i + "PointColourB=" + colour.z + "\r\n");
			// position
			l.getPosition(position);
			out.write("Light" + i + "PointPositionX=" + position.x + "\r\n");
			out.write("Light" + i + "PointPositionY=" + position.y + "\r\n");
			out.write("Light" + i + "PointPositionZ=" + position.z + "\r\n");

			// attenuation
			l.getAttenuation(attenuation);
			out.write("Light" + i + "PointAttenuationConstant=" + attenuation.x + "\r\n");
			out.write("Light" + i + "PointAttenuationLinear=" + attenuation.y + "\r\n");
			out.write("Light" + i + "PointAttenuationQuadratic=" + attenuation.z + "\r\n");
			if (l.getEnable() == true)
				out.write("Light" + i + "PointEnable=" + "true" + "\r\n");
			else
				out.write("Light" + i + "PointEnable=" + "false" + "\r\n");
		}
	}

	/**
	 * Saves spot lights parameters
	 */
	private void saveSpotLight() throws IOException {

		for (int i = 0; i < lightManager.getSpotLightVector().size(); i++) {
			WilmaLight w = (WilmaLight) lightManager.getSpotLightVector().get(i);
			SpotLight l = (SpotLight) w.getLight();
			// colour
			l.getColor(colour);
			out.write("Light" + i + "SpotColourR=" + colour.x + "\r\n");
			out.write("Light" + i + "SpotColourG=" + colour.y + "\r\n");
			out.write("Light" + i + "SpotColourB=" + colour.z + "\r\n");
			// position
			l.getPosition(position);
			out.write("Light" + i + "SpotPositionX=" + position.x + "\r\n");
			out.write("Light" + i + "SpotPositionY=" + position.y + "\r\n");
			out.write("Light" + i + "SpotPositionZ=" + position.z + "\r\n");
			// direction
			l.getDirection(direction);
			out.write("Light" + i + "SpotDirectionVectorX=" + direction.x + "\r\n");
			out.write("Light" + i + "SpotDirectionVectorY=" + direction.y + "\r\n");
			out.write("Light" + i + "SpotDirectionVectorZ=" + direction.z + "\r\n");

			// attenuation
			l.getAttenuation(attenuation);
			out.write("Light" + i + "SpotAttenuationConstant=" + attenuation.x + "\r\n");
			out.write("Light" + i + "SpotAttenuationLinear=" + attenuation.y + "\r\n");
			out.write("Light" + i + "SpotAttenuationQuadratic=" + attenuation.z + "\r\n");
			// others
			out.write("Light" + i + "SpotSpreadAngle=" + l.getSpreadAngle() + "\r\n");
			out.write("Light" + i + "SpotConcentration=" + l.getConcentration() + "\r\n");
			if (l.getEnable() == true)
				out.write("Light" + i + "SpotEnable=" + "true" + "\r\n");
			else
				out.write("Light" + i + "SpotEnable=" + "false" + "\r\n");
		}
	}

}
