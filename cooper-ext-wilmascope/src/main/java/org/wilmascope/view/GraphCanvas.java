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

/*
 * 
 * GraphCanvas.java
 * 
 * 
 * 
 * Created on 16 April 2000, 17:06
 *  
 */
/**
 * @author $Author: tgdwyer $
 * 
 * @version $Version:$
 *  
 */
import java.awt.Dimension;
import java.net.URL;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ExponentialFog;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.Screen3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.centernode.CenterNode;
import org.wilmascope.light.LightManager;
import org.wilmascope.rotation.RotationBehavior;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class GraphCanvas extends Canvas3D {
	protected GraphPickBehavior pb;

	protected TransformGroup transformGroup;

	protected TransformGroup stretchTransformGroup;

	protected TransformGroup rotationTransformGroup;

	private BranchGroup bg;

	private Background background;

	private ExponentialFog fog;

	private ViewConstants constants;

	private Bounds bounds;

	private SimpleUniverse universe;

	private LightManager lightManager;

	private MouseRotate MouseRotate;

	private MouseTranslate MouseTranslate;

	private MouseWheelZoom MouseZoom;

	private Alpha rotationAlpha;

	private RotationBehavior rotationBehavior;

	private boolean toggled;

	private CenterNode centerNode;

	/** Creates new GraphScene */
	public GraphCanvas(int xsize, int ysize) { // Create the root of the branch
		// graph
		super(SimpleUniverse.getPreferredConfiguration());
		constants = ViewConstants.getInstance();
		setSize(xsize, ysize);
		setLocation(5, 5);
		bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.setCapability(BranchGroup.ENABLE_PICK_REPORTING);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		bounds = new BoundingSphere(new Point3d(0, 0, 0), 10000);
		transformGroup = new TransformGroup();
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		transformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		transformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		transformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		transformGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		transformGroup.setCapability(TransformGroup.ALLOW_LOCAL_TO_VWORLD_READ);
		rotationTransformGroup = new TransformGroup();
		rotationTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotationTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		rotationTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		rotationTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		rotationTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		rotationTransformGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		rotationTransformGroup.setCapability(TransformGroup.ALLOW_LOCAL_TO_VWORLD_READ);
		Transform3D yAxis = new Transform3D();
		// rotator.setEnable(false);
		rotationBehavior = new RotationBehavior(bg, transformGroup, bounds);
		rotationBehavior.setSchedulingBounds(bounds);
		rotationBehavior.setEnable(false);
		toggled = false;
		bg.addChild(rotationBehavior);
		Transform3D stretch = new Transform3D();
		stretch.setScale(new Vector3d(1, 1, 1));
		transformGroup.setTransform(stretch);
		// Set up the background
		background = new Background(constants.getColor3f("BackgroundColour"));
		background.setApplicationBounds(bounds);
		background.setCapability(Background.ALLOW_COLOR_WRITE);
		background.setCapability(Background.ALLOW_COLOR_READ);
		background.setCapability(Background.ALLOW_IMAGE_WRITE);
		transformGroup.addChild(background);
		// Set up fog
		fog = new ExponentialFog();
		fog.setCapability(ExponentialFog.ALLOW_DENSITY_WRITE);
		fog.setInfluencingBounds(bounds);
		fog.setDensity(constants.getFloatValue("FogDensity"));
		fog.setColor(constants.getColor3f("FogColour"));
		bg.addChild(fog);
		pb = new GraphPickBehavior(bg, (Canvas3D) this, bounds, PickCanvas.GEOMETRY);
		pb.setSchedulingBounds(bounds);
		transformGroup.addChild(pb);
		// when the lightManager was initialized, it will read in the light
		// configuration in the
		// specified property file.
		lightManager = new LightManager(bg, bounds, "WILMA_CONSTANTS.properties");
		addMouseRotators(bounds);
		bg.addChild(transformGroup);
		// attach the centerNode behavior to the branch group
		centerNode = new CenterNode(transformGroup);
		centerNode.setSchedulingBounds(bounds);
		centerNode.setEnable(false);
		bg.addChild(centerNode);
		// setBackgroundTexture("org/wilmascope/images/sky.jpg");

	}

	public void setSphericalBackgroundTexture(String imagePath) {
		// set up sky background
		BranchGroup backGeoBranch = new BranchGroup();
		Sphere sphereObj = new Sphere(1.0f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_NORMALS_INWARD
				| Sphere.GENERATE_TEXTURE_COORDS, 45);
		Appearance backgroundApp = sphereObj.getAppearance();
		backGeoBranch.addChild(sphereObj);
		background.setGeometry(backGeoBranch);
		TextureLoader tex = new TextureLoader(imagePath, new String("RGB"), this);
		if (tex != null)
			backgroundApp.setTexture(tex.getTexture());
		else
			System.err.println("Couldn't load background texture!");
	}

	public void setBackgroundTexture(String imagePath) {
		TextureLoader myLoader = new TextureLoader(imagePath, this);
		ImageComponent2D myImage = myLoader.getImage();
		background.setImage(myImage);
	}

	public void setBackgroundTexture(URL url) {
		TextureLoader myLoader = new TextureLoader(url, this);
		ImageComponent2D myImage = myLoader.getImage();
		background.setImage(myImage);
	}

	public Bounds getBoundingSphere() {
		return bounds;
	}

	public void toggleRotator() {
		if (toggled == false) {
			rotationBehavior.setEnable(true);
			rotationBehavior.defaultRotate();
			toggled = true;
		} else {
			rotationBehavior.setEnable(false);
			rotationBehavior.getRotator().setEnable(false);
			toggled = false;
		}
	}

	public void createUniverse() {
		// bg.compile();
		// Create a universe with the Java3D universe utility.
		universe = new SimpleUniverse(this);
		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		universe.getViewingPlatform().setNominalViewingTransform();
		universe.getViewer().getView().setTransparencySortingPolicy(View.TRANSPARENCY_SORT_NONE);
		universe.getViewer().getView().setDepthBufferFreezeTransparent(false);
		setStereoSeparation(0.006);
		this.getView().setBackClipDistance(100);
		this.getView().setFrontClipDistance(0.1);
		universe.addBranchGraph(bg);
	}

	public void setStereoSeparation(double separation) {
		PhysicalBody b = this.getView().getPhysicalBody();
		b.setLeftEyePosition(new Point3d(-separation / 2.0, 0.0, 0.0));
		b.setRightEyePosition(new Point3d(separation / 2.0, 0.0, 0.0));
	}

	public void setAntialiasingEnabled(boolean enabled) {
		if (getSceneAntialiasingAvailable()) {
			universe.getViewer().getView().setSceneAntialiasingEnable(enabled);
		}
	}

	public void setParallelProjection(boolean enabled) {
		View v = getView();
		if (enabled == true) {
			v.setProjectionPolicy(View.PARALLEL_PROJECTION);
			v.setScreenScalePolicy(View.SCALE_EXPLICIT);
			v.setScreenScale(0.1d);
		} else {
			v.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
		}
	}

	public void setScale(double scale) {
		getView().setScreenScale(scale);
	}

	public void setScale(Vector3d scale) {
		Transform3D stretch = new Transform3D();
		stretch.setScale(scale);
		transformGroup.setTransform(stretch);
	}

	public Behavior addPerFrameBehavior(BehaviorClient client) {
		GraphBehavior gb = new GraphBehavior(client);
		gb.setSchedulingBounds(bounds);
		bg.addChild(gb);
		return gb;
	}

	private void addMouseRotators(Bounds bounds) {
		MouseRotate = new MouseRotate();
		MouseRotate.setTransformGroup(transformGroup);
		MouseRotate.setSchedulingBounds(bounds);
		bg.addChild(MouseRotate);
		MouseTranslate = new MouseTranslate();
		MouseTranslate.setTransformGroup(transformGroup);
		MouseTranslate.setSchedulingBounds(bounds);
		bg.addChild(MouseTranslate);
		MouseZoom = new MouseWheelZoom();
		MouseZoom.setTransformGroup(transformGroup);
		MouseZoom.setSchedulingBounds(bounds);
		transformGroup.addChild(MouseZoom);

	}

	public MouseTranslate getMouseTranslate() {
		return MouseTranslate;
	}

	public MouseRotate getMouseRotate() {
		return MouseRotate;
	}

	public MouseWheelZoom getMouseZoom() {
		return MouseZoom;
	}

	public void addGraphElementView(GraphElementView view) {
		BranchGroup b = view.getBranchGroup();
		if (!b.isLive()) {
			// b.compile();
			transformGroup.addChild(b);
		}
	}

	public TransformGroup getTransformGroup() {
		return transformGroup;
	}

	public TransformGroup getRotationGroup() {
		return rotationTransformGroup;
	}

	public void behaviorWakeup() {
	}

	public BranchGroup getBranchGroup() {
		return bg;
	}

	public void reorient() {
		javax.media.j3d.Transform3D reorient = new javax.media.j3d.Transform3D();
		transformGroup.setTransform(reorient);
	}

	public void reorient(Vector3f position) {
		centerNode.setOriginPosition(position);
		centerNode.setEnable(true);
	}

	/**
	 * Moves the graph (animated movement) such that position is centred and an
	 * object of the specified width fits into the view.
	 * 
	 * @param position
	 *            new origin
	 * @param width
	 *            of object at position to fit into view frustum
	 */
	public void reorient(Vector3f position, float width) {
		centerNode.setOriginPosition(position, width);
		centerNode.setEnable(true);
	}

	public void reorient(javax.media.j3d.Transform3D reorientTransform) {
		transformGroup.setTransform(reorientTransform);
	}

	public void setBackgroundColor(Color3f c) {
		background.setColor(c);
	}

	public void setBackgroundColor(java.awt.Color c) {
		background.setColor(new Color3f(c));
	}

	public java.awt.Color getBackgroundColor() {
		Color3f c = new Color3f();
		background.getColor(c);
		return c.get();
	}

	public float getFogDensity() {
		return fog.getDensity();
	}

	public void setFogDensity(float d) {
		fog.setDensity(d);
	}

	public void setRootPickingClient(PickingClient client) {
		pb.setRootPickingClient(client);
	}

	public void setPickingEnabled(boolean enabled) {
		pb.setEnable(enabled);
	}

	// method related to light source
	public LightManager getLightManager() {
		return lightManager;
	}

	public RotationBehavior getRotationBehavior() {
		return rotationBehavior;
	}

	public void writeJPEG(String path, float scale) {
		OffScreenCanvas3D offScreenCanvas = new OffScreenCanvas3D(SimpleUniverse.getPreferredConfiguration());
		// set the offscreen to match the onscreen
		Screen3D sOn = getScreen3D();
		Screen3D sOff = offScreenCanvas.getScreen3D();
		sOff.setSize(sOn.getSize());
		sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth());
		sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight());
		// setAntialiasingEnabled(true);
		Dimension dim = getSize();
		dim.width *= scale;
		dim.height *= scale;
		// attach the same view to the offscreen canvas and render an extra
		// frame
		// to make sure it's ready
		View view = universe.getViewer().getView();
		view.addCanvas3D(offScreenCanvas);
		view.stopView();
		view.renderOnce();
		view.startView();
		offScreenCanvas.print(path, dim, true);
		// setAntialiasingEnabled(false);
		view.removeCanvas3D(offScreenCanvas);
	}
}
