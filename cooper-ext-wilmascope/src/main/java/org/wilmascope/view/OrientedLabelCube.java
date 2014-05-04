/*
 *	@(#)LabelCube.java 1.5 00/02/10 13:13:49
 *
 * Copyright (c) 1996-2000 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
package org.wilmascope.view;

import javax.media.j3d.Appearance;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;

public class OrientedLabelCube extends OrientedShape3D {

	private static final float[] verts = {
			// Front Face
			1.1f, -1.0f, 1.0f, 1.1f, 1.0f, 1.0f, -1.1f, 1.0f, 1.0f, -1.1f, -1.0f, 1.0f,
	/*
	 * // Back Face -1.1f, -1.0f, -1.0f, -1.1f, 1.0f, -1.0f, 1.1f, 1.0f, -1.0f,
	 * 1.1f, -1.0f, -1.0f, // Right Face 1.1f, -1.0f, -1.0f, 1.1f, 1.0f, -1.0f,
	 * 1.1f, 1.0f, 1.0f, 1.1f, -1.0f, 1.0f, // Left Face -1.1f, -1.0f, 1.0f,
	 * -1.1f, 1.0f, 1.0f, -1.1f, 1.0f, -1.0f, -1.1f, -1.0f, -1.0f, // Top Face
	 * 1.1f, 1.0f, 1.0f, 1.1f, 1.0f, -1.0f, -1.1f, 1.0f, -1.0f, -1.1f, 1.0f,
	 * 1.0f, // Bottom Face -1.1f, -1.0f, 1.0f, -1.1f, -1.0f, -1.0f, 1.1f,
	 * -1.0f, -1.0f, 1.1f, -1.0f, 1.0f,
	 */
	};

	private static final float[] normals = {
			// Front Face
			0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
	/*
	 * // Back Face 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f,
	 * 0.0f, 0.0f, -1.0f, // Right Face 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
	 * 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // Left Face -1.0f, 0.0f, 0.0f,
	 * -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, // Top Face
	 * 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
	 * // Bottom Face 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
	 * 0.0f, -1.0f, 0.0f,
	 */
	};
	private static final Point2f[] textureCoords = { new Point2f(1.0f, 0.0f), new Point2f(1.0f, 1.0f),
			new Point2f(0.0f, 1.0f), new Point2f(0.0f, 0.0f) };

	public OrientedLabelCube(Appearance appearance, float size, float widthScale) {
		super();
		quadArray.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
		quadArray.setCapability(QuadArray.ALLOW_TEXCOORD_WRITE);
		quadArray.setCapability(QuadArray.ALLOW_NORMAL_WRITE);
		generateGeometry(size, widthScale);
		setGeometry(quadArray);
		setAppearance(appearance);
		setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
		setRotationPoint(new Point3f(0, 0, 0));
		setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
	}

	public void generateGeometry(float size, float widthScale) {
		float scaledVerts[] = new float[verts.length];
		for (int i = 0; i < verts.length; i++) {
			scaledVerts[i] = verts[i] * size * ((i % 3 == 0) ? widthScale : 1);
		}
		for (int i = 0; i < 4; i++) {
			quadArray.setTextureCoordinate(0, i, new TexCoord2f(textureCoords[i % 4]));
		}
		quadArray.setCoordinates(0, scaledVerts);
		quadArray.setNormals(0, normals);
	}

	QuadArray quadArray = new QuadArray(4, QuadArray.COORDINATES | QuadArray.NORMALS | QuadArray.TEXTURE_COORDINATE_2);
}
