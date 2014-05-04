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

import java.awt.Color;
import java.awt.Font;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.ImageIcon;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.picking.PickTool;

/*
 * Title: WilmaToo Description: Sequel to the ever popular WilmaScope software
 * Copyright: Copyright (c) 2001 Company: WilmaScope.org @author Tim Dwyer
 * 
 * @version 1.0
 */
/**
 * GraphElementView defines the methods and interfaces required for Edge and
 * Node views that implement this to be registered as prototypes in the
 * {@link ViewManager}
 */
public abstract class GraphElementView extends org.wilmascope.patterns.Prototype implements
		org.wilmascope.graph.Viewable {
	public void initGraphElement() {
		pickingClients = new Vector();
		t3d = new Transform3D();
		tg = new TransformGroup();
		// All graph elements will need to be moved around at runtime
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_LOCAL_TO_VWORLD_READ);
		// Allow the graph element to be deleted
		bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		labelBranch = new BranchGroup();
		labelBranch.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		labelBranch.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		tg.addChild(labelBranch);
		setupDefaultMaterial();
		setupHighlightMaterial();
		init();
		draw();
		bg.addChild(tg);
		// bg.compile();
	}

	public void setTransparencyAttributes(TransparencyAttributes ta) {
		getAppearance().setTransparencyAttributes(ta);
	}

	/**
	 * Sets the default material for graph elements You must over-ride this
	 * abstract method stub with: setupDefaultAppearance(myDefaultMaterial);
	 */
	protected abstract void setupDefaultMaterial();

	/**
	 * You must over-ride this method with:
	 * setupHighlightAppearance(myHighLightedMaterial)
	 */
	protected abstract void setupHighlightMaterial();

	/**
	 * you must over-ride the following method to create the 3D Shapes which
	 * will represent your graph elements
	 */
	protected abstract void init();

	/**
	 * Make it go away, for the last time!
	 */
	public void delete() {
		bg.detach();
	}

	/**
	 * add a scene graph node to this graph element's transform group
	 */
	public void addTransformGroupChild(javax.media.j3d.Node node) {
		tg.addChild(node);
	}

	/**
	 * set the transform for this graph element's transform group
	 */
	protected void setTransformGroupTransform(Transform3D transform) {
		tg.setTransform(transform);
	}

	/**
	 * @return the transform group for this graph element
	 */
	public TransformGroup getTransformGroup() {
		return tg;
	}

	/**
	 * set up for a translation transform
	 */
	protected void setTranslation(Vector3f v) {
		t3d.setTranslation(new Vector3f(v));
		setTransformGroupTransform(t3d);
	}

	/**
	 * set up for a transform with scale and translation components
	 */
	protected void setResizeTranslateTransform(Vector3d scale, Vector3f translation) {
		t3d.setScale(scale);
		t3d.setTranslation(translation);
		setTransformGroupTransform(t3d);
	}

	/**
	 * set up for a full transform with scale, translation and rotation
	 * components
	 */
	protected void setFullTransform(Vector3d scale, Vector3f translation, AxisAngle4f rotation) {
		t3d.setScale(scale);
		t3d.setTranslation(translation);
		t3d.setRotation(rotation);
		setTransformGroupTransform(t3d);
	}

	/**
	 * set up for a full transform with scale, translation and rotation
	 * components
	 */
	protected void setFullTransform(Vector3d scale, Vector3f translation, Quat4f orientation) {
		t3d.setScale(scale);
		t3d.setTranslation(translation);
		t3d.setRotation(orientation);
		setTransformGroupTransform(t3d);
	}

	public void transform(Vector3f v) {
		t3d.transform(v);
	}

	public boolean picked(java.awt.event.MouseEvent e) {
		if (!pickable) {
			return false;
		}
		for (int i = 0; i < pickingClients.size(); i++) {
			PickingClient pickingClient = (PickingClient) pickingClients.get(i);
			pickingClient.callback(e);
		}
		return true;
	}

	protected void setupDefaultAppearance(Material defaultMaterial) {

		if (!defaultMaterial.isLive())
			defaultMaterial.setCapability(Material.ALLOW_COMPONENT_READ);
		appearance = new Appearance();
		appearance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		appearance.setCapability(Appearance.ALLOW_MATERIAL_READ);
		appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
		appearance.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL,
				PolygonAttributes.CULL_BACK, 0.01f, true));
		RenderingAttributes ra = new RenderingAttributes();
		ra.setDepthBufferEnable(true);
		ra.setDepthBufferWriteEnable(true);
		appearance.setRenderingAttributes(ra);
		this.defaultMaterial = defaultMaterial;
		appearance.setMaterial(defaultMaterial);
		/*
		 * LineAttributes la=new LineAttributes();
		 * la.setLineAntialiasingEnable(true); PointAttributes pa=new
		 * PointAttributes(); pa.setPointAntialiasingEnable(true);
		 * appearance.setLineAttributes(la); appearance.setPointAttributes(pa);
		 */
	}

	protected void setupHighlightAppearance(Material highlightMaterial) {
		this.highlightMaterial = highlightMaterial;
	}

	public Appearance getAppearance() {
		return appearance;
	}

	public void setColour(float red, float green, float blue) {
		setColour(new Color3f(red, green, blue));
	}

	Color3f diffuseColour;

	public void setColour(Color3f diffuse) {
		this.diffuseColour = diffuse;
		defaultColourSet = false;
		// Invisible elements such as LineNode can have null appearance
		if (appearance != null) {
			Color3f ambient = new Color3f(diffuse);
			ambient.scale(0.5f);
			Material material = new Material();
			material.setCapability(Material.ALLOW_COMPONENT_READ);
			material.setDiffuseColor(diffuse);
			material.setAmbientColor(ambient);
			appearance.setMaterial(material);
		}
	}

	public void setColour(java.awt.Color colour) {
		setColour(new Color3f(colour));
	}

	public Color3f getColor3f() {
		Color3f colour = new Color3f();
		if (diffuseColour == null) { // no user colour set, get it from the
										// appearance
			if (appearance != null && appearance.getMaterial() != null) {
				appearance.getMaterial().getDiffuseColor(colour);
			} else {
				colour = new Color3f(Color.gray);
			}
		} else {
			colour = diffuseColour;
		}
		return colour;
	}

	public java.awt.Color getColour() {
		Color3f colour = getColor3f();
		try {
			return colour.get();
		} catch (IllegalArgumentException e) {
			System.err.println("ERROR: Invalid colour:" + colour);
		}
		return null;
	}

	public java.awt.Color getDefaultColour() {
		Color3f colour = new Color3f();
		defaultMaterial.getDiffuseColor(colour);
		return colour.get();
	}

	public boolean isDefaultColour() {
		return defaultColourSet;
	}

	public void defaultColour() {
		defaultColourSet = true;
		appearance.setMaterial(defaultMaterial);
	}

	public void highlightColour() {
		appearance.setMaterial(highlightMaterial);
	}

	public void setLabel(String text) {
		this.labelText = text;
		showLabel(text);
	}

	/**
	 * sets a multiline label... this may be implemented in different ways by
	 * different subclass views... The default is just to show the first line
	 */
	public void setLabel(String[] labelLines) {
		this.labelText = labelLines[0];
		showLabel(labelLines[0]);
	}

	public String getLabel() {
		return labelText;
	}

	/**
	 * add a label to the element
	 * 
	 * @param String
	 *            the text of the label
	 * @param scale
	 *            factor to resize the text
	 * @param Point3f
	 *            origin position of the centre of the label relative to the
	 *            axis of the edge
	 * @param Vector3f
	 *            Translation vector to position the label origin relative to
	 *            centre of the edge
	 * @param Appearance
	 *            for the label
	 */
	// static Font3D f3d = new Font3D(new Font("Dummy", Font.PLAIN, 4),new
	// FontExtrusion());
	protected void addLabel(String text, double scale, Point3f originPosition, Vector3f vTranslation, Appearance apText) {
		if (text != null && !text.equals("null") && text.length() > 0) {
			/*
			 * // using Text3D Point3f centredOriginPosition = new
			 * Point3f(originPosition); centredOriginPosition.x +=
			 * -text.length()/2; Text3D txt = new Text3D(f3d, text,
			 * centredOriginPosition); OrientedShape3D textShape = new
			 * OrientedShape3D();
			 * textShape.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
			 * textShape.setRotationPoint(new Point3f(0,0,0));
			 * textShape.setGeometry(txt); textShape.setAppearance(apText);
			 */
			// Using text2D and a billboard
			Color3f c = null;
			if (constants.getProperty("LabelUseViewColour").equals("true")) {
				c = this.getColor3f();
			} else {
				c = constants.getColor3f("LabelColour");
			}
			Text2D text2D = new Text2D(text, c, "Dummy", 20, Font.PLAIN);
			TransformGroup textTG = new TransformGroup();
			textTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			textTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
			makePickable(text2D);
			textTG.addChild(text2D);
			BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
			Billboard billboard = new Billboard(textTG, Billboard.ROTATE_ABOUT_POINT, originPosition);
			billboard.setSchedulingBounds(bounds);
			TransformGroup l = new TransformGroup();
			Transform3D scaleTransform = new Transform3D();
			scaleTransform.setScale(scale);
			l.setTransform(scaleTransform);
			l.addChild(billboard);
			l.addChild(textTG);
			if (labelSubBranch != null) {
				labelSubBranch.detach();
			}
			labelSubBranch = new BranchGroup();
			labelSubBranch.setCapability(BranchGroup.ALLOW_DETACH);
			Transform3D translation = new Transform3D();
			translation.setTranslation(vTranslation);
			TransformGroup translateTG = new TransformGroup(translation);
			translateTG.addChild(l);
			labelSubBranch.addChild(translateTG);
			labelBranch.addChild(labelSubBranch);
		}
	}

	public void removeLabel() {
		labelSubBranch.detach();
	}

	protected void setLabel(TransformGroup label) {
		labelSubBranch = new BranchGroup();
		labelSubBranch.setCapability(BranchGroup.ALLOW_DETACH);
		labelSubBranch.addChild(label);
		labelBranch.addChild(labelSubBranch);
	}

	/**
	 * Add a visible Shape3D to this Graph Element. The shape will be made
	 * pickable, returning this GraphElement to the pick listener. Alternately
	 * add complex objects such as LODs yourself using addTransformGroupChild
	 * and then call makePickable on them.
	 */
	protected void addShape(Shape3D shape) {
		this.shape = shape;
		makePickable(shape);
		shape.setAppearance(getAppearance());
		addTransformGroupChild(shape);
	}

	/**
	 * Make a given shape a pick target for this GraphElement
	 */
	public void makePickable(Shape3D shape) {
		shape.setUserData(this);
		shape.setCapability(Shape3D.ENABLE_PICK_REPORTING);
		try {
			PickTool.setCapabilities(shape, PickTool.INTERSECT_FULL);
		} catch (javax.media.j3d.RestrictedAccessException e) {
			// System.out.println("Not setting bits on already setup shared
			// geometry");
		}
	}

	GraphCanvas graphCanvas;

	public void hide() {
		visible = false;
		bg.detach();
	}

	public GraphCanvas getGraphCanvas() {
		return graphCanvas;
	}

	public void show(GraphCanvas graphCanvas) {
		this.graphCanvas = graphCanvas;
		graphCanvas.addGraphElementView(this);
		visible = true;
	}

	/**
	 * Register a PickingClient whose {@link PickingClient#callback}method will
	 * be called when the element is clicked
	 */
	public void addPickingClient(PickingClient client) {
		pickingClients.add(client);
	}

	public void removePickingClient(PickingClient client) {
		pickingClients.remove(client);
	}

	public BranchGroup getBranchGroup() {
		return bg;
	}

	public abstract ImageIcon getIcon();

	public void setPickable(boolean pickable) {
		this.pickable = pickable;
	}

	public void setUserData(Object data) {
		this.userData = data;
	};

	public Object getUserData() {
		return userData;
	}

	protected void addLiveBranch(BranchGroup b) {
		labelBranch.addChild(b);
	}

	public Properties getProperties() {
		Properties p = new Properties();
		String labelText = getLabel();
		if (labelText != null) {
			p.setProperty("Label", labelText);
		}
		if (!visible) {
			p.setProperty("Visible", "false");
		}
		if (!defaultColourSet) {
			float[] rgb = getColour().getRGBColorComponents(null);
			p.setProperty("Colour", rgb[0] + " " + rgb[1] + " " + rgb[2]);
		}
		return p;
	}

	public void setProperties(Properties p) {
		String label = p.getProperty("Label");
		if (label != null) {
			setLabel(label);
		}
		String visible = p.getProperty("Visible");
		if (visible != null && visible.toLowerCase().equals("false")) {
			hide();
		}
		String colour = p.getProperty("Colour");
		if (colour != null) {
			StringTokenizer st = new StringTokenizer(colour);
			setColour(Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()),
					Float.parseFloat(st.nextToken()));
		}
	}

	/**
	 * @param init
	 * @param target
	 * @return AxisAngle for rotation of init vector to be oriented with target
	 *         vector
	 */
	public static AxisAngle4f getAxisAngle4f(Vector3f init, Vector3f target) {
		Vector3f norm = new Vector3f();
		norm.cross(init, target);
		return new AxisAngle4f(norm.x, norm.y, norm.z, init.angle(target));
	}

	protected abstract void showLabel(String text);

	// the branch group for the whole GraphElement
	private BranchGroup bg;
	// labelBranch is a BranchGroup below bg's TransformGroup with CHILDREN
	// write
	// and extend bits set
	private String labelText;
	private BranchGroup labelBranch;
	// labelSubBranch is the branch group containing any actual label, it is
	// replaced if the label changes. It is a child of labelBranch.
	private BranchGroup labelSubBranch;
	private Appearance appearance;
	// transform to position the element. set by draw method.
	protected Transform3D t3d;
	// Called to render the element at a new position
	private TransformGroup tg;
	private static org.wilmascope.global.GlobalConstants constants = org.wilmascope.global.GlobalConstants
			.getInstance();
	// The shape representing this element
	private Shape3D shape;
	private Vector pickingClients;
	private Material defaultMaterial;
	private boolean defaultColourSet = true;
	private Material highlightMaterial;
	private boolean pickable = true;
	private Object userData;
	private boolean visible = false;
}
