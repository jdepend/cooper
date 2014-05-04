/**
 * @author star
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package org.wilmascope.light;

import java.util.Vector;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.PointLight;
import javax.media.j3d.SpotLight;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.wilmascope.global.GlobalConstants;

/**
 * LightManager is the main class to control the light sources. <br>
 * <br>
 * It maintain 4 vectors: <br>
 * 1 ambLightVector--contains all the ambient lights created. <br>
 * 2 dirLightVector--contains all the directioanl lights created. <br>
 * 3 pointLightVector--contains all the point lights created. <br>
 * 4 spotLightVector--contains all the spot lights created. <br>
 * <br>
 * Here is an example to create an object of it:
 * 
 * <pre>
 * lightManager = new LightManager(bg, bounds, &quot;WILMA_CONSTANTS.properties&quot;);
 * </pre>
 * 
 * <br>
 * parameter 1:the branch group the lights will be attached to <br>
 * parameter 2:the scheduling bounds of the lights <br>
 * parameter 3:the light properties file name. <br>
 * <br>
 * Before showing the lightFrame,first disable all mouse picking and behavior
 * capabilities. And then add a window Listener to the lightFrame, so that when
 * the frame is closed, the mouse picking and behavior capabilites are
 * reenabled:
 * 
 * <pre>
 * GraphCanvas graphCanvas = graphControl.getGraphCanvas();
 * graphControl.getGraphCanvas().setPickingEnabled(false);
 * graphControl.getGraphCanvas().getMouseRotate().setEnable(false);
 * graphControl.getGraphCanvas().getMouseTranslate().setEnable(false);
 * graphControl.getGraphCanvas().getMouseZoom().setEnable(false);
 * 
 * lightFrame = graphCanvas.getLightManager().getLightFrame();
 * 
 * lightFrame.addWindowListener(new WindowAdapter() {
 * 	public void windowClosing(WindowEvent e) {
 * 		graphControl.getGraphCanvas().setPickingEnabled(true);
 * 		graphControl.getGraphCanvas().getMouseRotate().setEnable(true);
 * 		graphControl.getGraphCanvas().getMouseTranslate().setEnable(true);
 * 		graphControl.getGraphCanvas().getMouseZoom().setEnable(true);
 * 	}
 * });
 * lightFrame.setVisible(true);
 * </pre>
 */
public class LightManager {
	// light vectors to hold different WilmaLight
	private Vector ambLightVector = new Vector();
	private Vector dirLightVector = new Vector();
	private Vector pointLightVector = new Vector();
	private Vector spotLightVector = new Vector();
	private GlobalConstants constants;
	private BranchGroup bg;
	private Bounds bounds;
	private LightFrame lightFrame;
	private String propertyFileName;
	private LightPropertiesSaver Saver;

	public LightManager(BranchGroup bg, Bounds bounds, String propertyFileName) {

		this.bg = bg;
		this.bounds = bounds;
		constants = GlobalConstants.getInstance();
		this.propertyFileName = propertyFileName;
		// read in the light properties
		addLights();

		Saver = new LightPropertiesSaver(this);

	}

	// read the light configuration from .properties file and attach lights to
	// the scene graph
	private void addLights() {
		// Set up ambient light source

		for (int i = 0;; i++) {
			if (constants.getProperty("Light" + i + "AmbientColourR") == null)
				break;
			System.err.println("Adding Ambient Light " + i);
			AmbientLight ambientLight = new AmbientLight();
			ambientLight.setCapability(AmbientLight.ALLOW_COLOR_READ);
			ambientLight.setCapability(AmbientLight.ALLOW_COLOR_WRITE);
			ambientLight.setCapability(AmbientLight.ALLOW_STATE_WRITE);
			ambientLight.setCapability(AmbientLight.ALLOW_STATE_READ);
			ambientLight.setInfluencingBounds(bounds);
			ambientLight.setColor(constants.getColor3f("Light" + i + "AmbientColour"));
			if (constants.getProperty("Light" + i + "AmbientEnable").equals("false"))
				ambientLight.setEnable(false);
			BranchGroup lightGroup1 = new BranchGroup();
			lightGroup1.setCapability(BranchGroup.ALLOW_DETACH);

			lightGroup1.addChild(ambientLight);
			bg.addChild(lightGroup1);
			WilmaLight w1 = new WilmaLight();
			w1.setLight(ambientLight);
			w1.setBranchGroup(lightGroup1);
			ambLightVector.add(w1);
		}
		// Set up directional light
		Transform3D trans = new Transform3D();
		for (int i = 0;; i++) {
			if (constants.getProperty("Light" + i + "DirectionalColourR") == null) {
				break;
			}
			System.err.println("Adding Directional Light " + i);
			DirectionalLight dirLight = new DirectionalLight();
			dirLight.setInfluencingBounds(bounds);
			dirLight.setCapability(DirectionalLight.ALLOW_DIRECTION_READ);
			dirLight.setCapability(DirectionalLight.ALLOW_DIRECTION_WRITE);
			dirLight.setCapability(DirectionalLight.ALLOW_COLOR_READ);
			dirLight.setCapability(DirectionalLight.ALLOW_COLOR_WRITE);
			dirLight.setCapability(DirectionalLight.ALLOW_STATE_WRITE);
			dirLight.setCapability(DirectionalLight.ALLOW_STATE_READ);
			Vector3f direction = constants.getVector3f("Light" + i + "DirectionalVector");
			dirLight.setDirection(direction);
			dirLight.setColor(constants.getColor3f("Light" + i + "DirectionalColour"));
			if (constants.getProperty("Light" + i + "DirectionalEnable").equals("false"))
				dirLight.setEnable(false);
			BranchGroup lightGroup2 = new BranchGroup();
			lightGroup2.setCapability(BranchGroup.ALLOW_DETACH);
			lightGroup2.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			lightGroup2.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
			lightGroup2.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
			lightGroup2.addChild(dirLight);
			bg.addChild(lightGroup2);
			WilmaLight w2 = new WilmaLight();
			w2.setLight(dirLight);
			w2.setBranchGroup(lightGroup2);
			Vector3f position = constants.getVector3f("Light" + i + "DirectionalPosition");
			w2.getArrow().getTransform(trans);
			trans.set(position);
			w2.getArrow().setTransform(trans);
			dirLightVector.add(w2);
		}
		// Add point light sources for all that have a colour defined in
		// the constants file
		for (int i = 0;; i++) {
			if (constants.getProperty("Light" + i + "PointColourR") == null)
				break;
			System.err.println("Adding Point Light " + i);
			PointLight pl = new PointLight(constants.getColor3f("Light" + i + "PointColour"), new Point3f(
					constants.getVector3f("Light" + i + "PointPosition")), new Point3f(constants.getFloatValue("Light"
					+ i + "PointAttenuationConstant"), constants.getFloatValue("Light" + i + "PointAttenuationLinear"),
					constants.getFloatValue("Light" + i + "PointAttenuationQuadratic")));
			if (constants.getProperty("Light" + i + "PointEnable").equals("false"))
				pl.setEnable(false);
			pl.setInfluencingBounds(bounds);
			pl.setCapability(PointLight.ALLOW_POSITION_READ);
			pl.setCapability(PointLight.ALLOW_POSITION_WRITE);
			pl.setCapability(PointLight.ALLOW_COLOR_READ);
			pl.setCapability(PointLight.ALLOW_COLOR_WRITE);
			pl.setCapability(PointLight.ALLOW_ATTENUATION_READ);
			pl.setCapability(PointLight.ALLOW_ATTENUATION_WRITE);
			pl.setCapability(PointLight.ALLOW_STATE_WRITE);
			pl.setCapability(PointLight.ALLOW_STATE_READ);

			BranchGroup lightGroup3 = new BranchGroup();
			lightGroup3.setCapability(BranchGroup.ALLOW_DETACH);
			lightGroup3.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			lightGroup3.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
			lightGroup3.addChild(pl);
			bg.addChild(lightGroup3);
			WilmaLight w3 = new WilmaLight();
			w3.setLight(pl);
			w3.setBranchGroup(lightGroup3);
			pointLightVector.add(w3);
		}
		for (int i = 0;; i++) {
			if (constants.getProperty("Light" + i + "SpotColourR") == null)
				break;
			System.err.println("Adding Spot Light " + i);
			SpotLight sl = new SpotLight();
			sl.setColor(constants.getColor3f("Light" + i + "SpotColour"));
			sl.setPosition(new Point3f(constants.getVector3f("Light" + i + "SpotPosition")));
			sl.setAttenuation(new Point3f(constants.getFloatValue("Light" + i + "SpotAttenuationConstant"), constants
					.getFloatValue("Light" + i + "SpotAttenuationLinear"), constants.getFloatValue("Light" + i
					+ "SpotAttenuationQuadratic")));

			sl.setSpreadAngle(constants.getFloatValue("Light" + i + "SpotSpreadAngle"));
			sl.setConcentration(constants.getFloatValue("Light" + i + "SpotConcentration"));
			sl.setDirection(constants.getVector3f("Light" + i + "SpotDirectionVector"));
			if (constants.getProperty("Light" + i + "SpotEnable").equals("false"))
				sl.setEnable(false);

			sl.setInfluencingBounds(bounds);

			sl.setCapability(SpotLight.ALLOW_POSITION_READ);
			sl.setCapability(SpotLight.ALLOW_POSITION_WRITE);
			sl.setCapability(SpotLight.ALLOW_COLOR_READ);
			sl.setCapability(SpotLight.ALLOW_COLOR_WRITE);
			sl.setCapability(SpotLight.ALLOW_ATTENUATION_READ);
			sl.setCapability(SpotLight.ALLOW_ATTENUATION_WRITE);
			sl.setCapability(SpotLight.ALLOW_STATE_WRITE);
			sl.setCapability(SpotLight.ALLOW_STATE_READ);
			sl.setCapability(SpotLight.ALLOW_SPREAD_ANGLE_READ);
			sl.setCapability(SpotLight.ALLOW_SPREAD_ANGLE_WRITE);
			sl.setCapability(SpotLight.ALLOW_CONCENTRATION_READ);
			sl.setCapability(SpotLight.ALLOW_CONCENTRATION_WRITE);
			sl.setCapability(SpotLight.ALLOW_DIRECTION_READ);
			sl.setCapability(SpotLight.ALLOW_DIRECTION_WRITE);

			BranchGroup lightGroup4 = new BranchGroup();
			lightGroup4.setCapability(BranchGroup.ALLOW_DETACH);
			lightGroup4.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			lightGroup4.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
			lightGroup4.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
			lightGroup4.addChild(sl);
			bg.addChild(lightGroup4);
			WilmaLight w4 = new WilmaLight();
			w4.setLight(sl);
			w4.setBranchGroup(lightGroup4);
			spotLightVector.add(w4);
		}
	}

	/**
	 * @return A LightFrame instance
	 */
	public LightFrame getLightFrame() {
		lightFrame = new LightFrame(this);
		lightFrame.setSize(370, 480);
		lightFrame.setTitle("LightFrame");
		return lightFrame;
	}

	/**
	 * Saves the light configuration to the property file
	 */
	public void saveLight() {
		propertyFileName = "WILMA_CONSTANTS.properties";
		Saver.saveToWilmaCons();
	}

	/**
	 * @return The AmbientLight Vector
	 */
	public Vector getAmbLightVector() {
		return ambLightVector;
	}

	/**
	 * @return The DirectionalLight Vector
	 */
	public Vector getDirLightVector() {
		return dirLightVector;
	}

	/**
	 * @return The PointLight Vector
	 */
	public Vector getPointLightVector() {
		return pointLightVector;
	}

	/**
	 * @return The SpotLight Vector
	 */
	public Vector getSpotLightVector() {
		return spotLightVector;
	}

	/**
	 * @return The branch group that the lights attach
	 */
	public BranchGroup getBranchGroup() {
		return bg;
	}

	/**
	 * @return The scheduling bounds of the lights
	 */
	public Bounds getBoundingSphere() {
		return bounds;
	}

	/**
	 * @return The file name of light configuration file
	 */
	public String getPropertyFileName() {
		return propertyFileName;
	}

	/**
	 * creates an ambient light with default values and attach it to the scene
	 * graph
	 */
	public void createAmbLight() {
		BranchGroup lightGroup = new BranchGroup();
		lightGroup.setCapability(BranchGroup.ALLOW_DETACH);
		lightGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		WilmaLight w = new WilmaLight();

		AmbientLight newAmbLight = new AmbientLight();
		newAmbLight.setCapability(AmbientLight.ALLOW_COLOR_READ);
		newAmbLight.setCapability(AmbientLight.ALLOW_COLOR_WRITE);
		newAmbLight.setCapability(AmbientLight.ALLOW_STATE_WRITE);
		newAmbLight.setCapability(AmbientLight.ALLOW_STATE_READ);
		newAmbLight.setInfluencingBounds(bounds);
		// add to the branch group
		lightGroup.addChild(newAmbLight);
		bg.addChild(lightGroup);
		// save the light's parent branchgroup for future use
		w.setLight(newAmbLight);
		w.setBranchGroup(lightGroup);
		ambLightVector.add(w);
	}

	/**
	 * creates a directional light with default values and attach it to the
	 * scene graph
	 */
	public void createDirLight() {
		BranchGroup lightGroup = new BranchGroup();
		lightGroup.setCapability(BranchGroup.ALLOW_DETACH);
		lightGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		lightGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		WilmaLight w = new WilmaLight();

		DirectionalLight newDirLight = new DirectionalLight();
		newDirLight.setCapability(DirectionalLight.ALLOW_DIRECTION_READ);
		newDirLight.setCapability(DirectionalLight.ALLOW_DIRECTION_WRITE);
		newDirLight.setCapability(DirectionalLight.ALLOW_COLOR_READ);
		newDirLight.setCapability(DirectionalLight.ALLOW_COLOR_WRITE);
		newDirLight.setCapability(DirectionalLight.ALLOW_STATE_WRITE);
		newDirLight.setCapability(DirectionalLight.ALLOW_STATE_READ);
		newDirLight.setCapability(DirectionalLight.ALLOW_PICKABLE_READ);
		newDirLight.setInfluencingBounds(bounds);

		// add to the branch group
		lightGroup.addChild(newDirLight);
		bg.addChild(lightGroup);
		w.setLight(newDirLight);
		w.setBranchGroup(lightGroup);
		dirLightVector.add(w);
	}

	/**
	 * creates a point light with default values and attach it to the scene
	 * graph
	 */
	public void createPointLight() {
		BranchGroup lightGroup = new BranchGroup();
		lightGroup.setCapability(BranchGroup.ALLOW_DETACH);
		lightGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		WilmaLight w = new WilmaLight();

		PointLight newPointLight = new PointLight();
		newPointLight.setCapability(PointLight.ALLOW_POSITION_READ);
		newPointLight.setCapability(PointLight.ALLOW_POSITION_WRITE);
		newPointLight.setCapability(PointLight.ALLOW_COLOR_READ);
		newPointLight.setCapability(PointLight.ALLOW_COLOR_WRITE);
		newPointLight.setCapability(PointLight.ALLOW_ATTENUATION_READ);
		newPointLight.setCapability(PointLight.ALLOW_ATTENUATION_WRITE);
		newPointLight.setCapability(PointLight.ALLOW_STATE_WRITE);
		newPointLight.setCapability(PointLight.ALLOW_STATE_READ);
		newPointLight.setInfluencingBounds(bounds);

		// add to the branch group
		lightGroup.addChild(newPointLight);
		bg.addChild(lightGroup);
		w.setLight(newPointLight);
		w.setBranchGroup(lightGroup);
		pointLightVector.add(w);
	}

	/**
	 * creates a spot light with default values and attach it to the scene graph
	 */
	public void createSpotLight() {
		BranchGroup lightGroup = new BranchGroup();
		lightGroup.setCapability(BranchGroup.ALLOW_DETACH);
		lightGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		WilmaLight w = new WilmaLight();

		SpotLight newSpotLight = new SpotLight();
		newSpotLight.setSpreadAngle((float) Math.PI / 6);
		// set up the capability
		newSpotLight.setCapability(SpotLight.ALLOW_DIRECTION_READ);
		newSpotLight.setCapability(SpotLight.ALLOW_DIRECTION_WRITE);
		newSpotLight.setCapability(SpotLight.ALLOW_COLOR_READ);
		newSpotLight.setCapability(SpotLight.ALLOW_COLOR_WRITE);
		newSpotLight.setCapability(SpotLight.ALLOW_POSITION_READ);
		newSpotLight.setCapability(SpotLight.ALLOW_POSITION_WRITE);
		newSpotLight.setCapability(SpotLight.ALLOW_ATTENUATION_READ);
		newSpotLight.setCapability(SpotLight.ALLOW_ATTENUATION_WRITE);
		newSpotLight.setCapability(SpotLight.ALLOW_SPREAD_ANGLE_READ);
		newSpotLight.setCapability(SpotLight.ALLOW_SPREAD_ANGLE_WRITE);
		newSpotLight.setCapability(SpotLight.ALLOW_CONCENTRATION_READ);
		newSpotLight.setCapability(SpotLight.ALLOW_CONCENTRATION_WRITE);
		newSpotLight.setCapability(SpotLight.ALLOW_STATE_WRITE);
		newSpotLight.setCapability(SpotLight.ALLOW_STATE_READ);
		newSpotLight.setInfluencingBounds(bounds);

		// add to the branch group
		lightGroup.addChild(newSpotLight);
		bg.addChild(lightGroup);
		w.setLight(newSpotLight);
		w.setBranchGroup(lightGroup);
		spotLightVector.add(w);
	}

	/**
	 * deletes the ambient light from the scene graph
	 * 
	 * @param index
	 *            index in the ambient light vector(ambLightVector)
	 */
	public void deleteAmbLight(int index) {

		// get the selected light
		WilmaLight w = (WilmaLight) ambLightVector.get(index);
		// remove it from the light vector
		ambLightVector.remove(index);
		// detach the light from the scene graph
		w.getBranchGroup().detach();
	}

	/**
	 * deletes the directional light from the scene graph
	 * 
	 * @param index
	 *            index in the directional light vector(dirLightVector)
	 */
	public void deleteDirLight(int index) {
		WilmaLight w = (WilmaLight) dirLightVector.get(index);
		dirLightVector.remove(index);
		w.getBranchGroup().detach();
	}

	/**
	 * deletes the point light from the scene graph
	 * 
	 * @param index
	 *            index in the point light vector(pointLightVector)
	 */
	public void deletePointLight(int index) {
		WilmaLight w = (WilmaLight) pointLightVector.get(index);
		pointLightVector.remove(index);
		w.getBranchGroup().detach();
	}

	/**
	 * deletes the spot light from the scene graph
	 * 
	 * @param index
	 *            index in the spot light vector(spotLightVector)
	 */
	public void deleteSpotLight(int index) {
		WilmaLight w = (WilmaLight) spotLightVector.get(index);
		spotLightVector.remove(index);
		w.getBranchGroup().detach();
	}

	/**
	 * loads the light configuration from the specified property file
	 */
	public void loadFile(String propertyFileName) {
		int i;
		WilmaLight w;
		this.propertyFileName = propertyFileName;
		constants = GlobalConstants.getInstance();
		// detach all the lights
		for (i = 0; i < ambLightVector.size(); i++) {
			w = (WilmaLight) ambLightVector.get(i);
			w.getBranchGroup().detach();
		}
		for (i = 0; i < dirLightVector.size(); i++) {
			w = (WilmaLight) dirLightVector.get(i);
			w.getBranchGroup().detach();
		}
		for (i = 0; i < pointLightVector.size(); i++) {
			w = (WilmaLight) pointLightVector.get(i);
			w.getBranchGroup().detach();
		}
		for (i = 0; i < spotLightVector.size(); i++) {
			w = (WilmaLight) spotLightVector.get(i);
			w.getBranchGroup().detach();
		}
		// clear the light vectors
		ambLightVector.clear();
		dirLightVector.clear();
		pointLightVector.clear();
		spotLightVector.clear();
		// read the light configuration from the specified file
		addLights();

	}

	/**
	 * Saves the light configuration to a new file
	 */
	public void saveToNewFile(String name) {
		Saver.saveToNewFile(name);
		this.propertyFileName = name;
	}
}
