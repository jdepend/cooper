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
 * 
 * Adapted from code from sun --- see terms below below
 */
/*
 *	@(#)OffScreenCanvas3D.java 1.4 00/02/10 13:14:24
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

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Screen3D;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * An offscreen canvas with a print method that writes out a screen capture
 * image to a jpg file
 * 
 * @author dwyer
 */
class OffScreenCanvas3D extends Canvas3D {
	boolean printing = false;
	String path;

	/**
	 * You should create the canvas and attach to your view when you set that
	 * view up
	 * 
	 * @param gconfig
	 *            use the same graphics config as your universe
	 */
	public OffScreenCanvas3D(GraphicsConfiguration gconfig) {
		super(gconfig, true);
	}

	/**
	 * Prepare the offscreen canvas buffer to be written out to a file
	 * 
	 * @param path
	 *            jpg path name, either with or without .jpg extension
	 * @param dim
	 *            dimensions of the image file to write
	 * @param toWait
	 *            forces program to wait till screen is rendered
	 */
	public void print(String path, Dimension dim, boolean toWait) {
		this.path = path;
		if (!toWait)
			printing = true;
		BufferedImage bImage = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
		ImageComponent2D buffer = new ImageComponent2D(ImageComponent.FORMAT_RGB, bImage);
		buffer.setCapability(ImageComponent2D.ALLOW_IMAGE_READ);
		this.setSize(new Dimension(dim.width, dim.height));
		// set up screen dimensions and aspect ratio
		Screen3D s = this.getScreen3D();
		s.setSize(new Dimension(dim.width, dim.height));
		s.setPhysicalScreenHeight(0.0254 / 90.0 * dim.height);
		s.setPhysicalScreenWidth(0.0254 / 90.0 * dim.width);
		this.setOffScreenBuffer(buffer);
		this.renderOffScreenBuffer();
		if (toWait) {
			this.waitForOffScreenRendering();
			drawOffScreenBuffer();
		}
	}

	public void postSwap() {
		if (printing) {
			super.postSwap();
			drawOffScreenBuffer();
			printing = false;
		}
	}

	void drawOffScreenBuffer() {
		BufferedImage bImage = this.getOffScreenBuffer().getImage();
		ImageComponent2D newImageComponent = new ImageComponent2D(ImageComponent.FORMAT_RGBA, bImage);
		// write that to disk....
		if (!path.endsWith(".jpg")) {
			path = path + ".jpg";
		}
		try {
			FileOutputStream out = new FileOutputStream(path);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bImage);
			param.setQuality(0.9f, false); // 90% qualith JPEG
			encoder.setJPEGEncodeParam(param);
			encoder.encode(bImage);
			out.close();
			System.out.println("Wrote File: " + path);
		} catch (IOException e) {
			System.out.println("I/O exception!");
		}
	}
}
