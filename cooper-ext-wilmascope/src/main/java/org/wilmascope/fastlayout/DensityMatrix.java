package org.wilmascope.fastlayout;

import javax.vecmath.Point3f;

import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeList;

/**
 * Description: Class abstracting the storing of the node densities for
 * FastLayout.
 * 
 * @author James Cwling
 * @version 1.2
 */

public class DensityMatrix {

	// pity java doesn't have pointers...
	// this redundancy here still outweighs the disadvantage of having the
	// overhead of a vector of vectors (of vectors)
	double[][] matrix2D;
	double[][][] matrix3D;

	// matrices storing the node footprint template for 2 & 3D respectively
	double[][] footprint2D;
	double[][][] footprint3D;

	// the indexes in the 'universe' matrix are integral yet the node positions
	// are floats
	// this resolution defines how much to scale the floating point position
	// by before rounding to an int
	int resolution = Defaults.FIELD_RES;

	// the radius of the node footprint
	int footRadius = resolution * Defaults.NODE_FOOTPRINT;

	// if the matrix is 3D, as opposed to 2D
	boolean threeD;

	// the min level the density should attenuate to at the edge of the node
	// footprint
	double cutoff = Defaults.MIN_DENSITY;

	// radii before expansion has occurred
	int initWidth, initHeight, initDepth = 0;

	public DensityMatrix(int width, boolean threeD) {
		this.threeD = threeD;
		initWidth = width;
		initHeight = width;
		initDepth = width;
		if (threeD)
			matrix3D = new double[initWidth][initHeight][initDepth];
		else
			matrix2D = new double[initWidth][initHeight];
		calcFootprint();
	}

	public void setFootprint(int footprint) {
		footRadius = resolution * footprint;
		calcFootprint();
	}

	public void setRes(int res) {
		footRadius = (footRadius / resolution) * res;
		resolution = res;
		calcFootprint();
	}

	public void setRadius(int val) {
		initWidth = initHeight = initDepth = val;
		reset();
	}

	private void calcFootprint() {

		double attenuateScale = cutoff * footRadius * footRadius;

		if (threeD) {
			footprint3D = new double[2 * footRadius + 1][2 * footRadius + 1][2 * footRadius + 1];

			for (int i = 0; i < footprint3D.length; i++) {
				for (int j = 0; j < footprint3D[i].length; j++) {
					for (int k = 0; k < footprint3D[i][j].length; k++) {
						int radSquared = distSquared(footRadius, footRadius, footRadius, i, j, k);
						if (radSquared <= footRadius * footRadius * footRadius + 1) { // could
																						// get
																						// rid
																						// of
																						// this
																						// -
																						// just
																						// restricts
																						// density
																						// to
																						// within
																						// circular
																						// footprint
							if (radSquared == 0)
								radSquared = 1;
							footprint3D[i][j][k] = attenuateScale / radSquared;
						} else
							footprint3D[i][j][k] = 0;
					}
				}
			}
		} else {
			footprint2D = new double[2 * footRadius + 1][2 * footRadius + 1];

			for (int i = 0; i < footprint2D.length; i++) {
				for (int j = 0; j < footprint2D[i].length; j++) {
					int radSquared = distSquared(footRadius, footRadius, i, j);
					if (radSquared <= footRadius * footRadius + 1) { // could
																		// get
																		// rid
																		// of
																		// this
																		// -
																		// just
																		// restricts
																		// density
																		// to
																		// within
																		// circular
																		// footprint
						if (radSquared == 0)
							radSquared = 1;
						footprint2D[i][j] = attenuateScale / radSquared;
					} else
						footprint2D[i][j] = 0;
				}
			}
		}
	}

	private static int distSquared(int x1, int y1, int x2, int y2) {
		return ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	private static int distSquared(int x1, int y1, int z1, int x2, int y2, int z2) {
		return ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));
	}

	public void setZero() {
		if (threeD)
			for (int i = 0; i < matrix3D.length; i++)
				for (int j = 0; j < matrix3D[i].length; j++)
					for (int k = 0; k < matrix3D[i][j].length; k++)
						matrix3D[i][j][k] = 0;
		else
			for (int i = 0; i < matrix2D.length; i++)
				for (int j = 0; j < matrix2D[i].length; j++)
					matrix2D[i][j] = 0;
	}

	public double get(Point3f pos) {
		if (threeD) {
			int x = Math.round(pos.x * resolution) + matrix3D.length / 2;
			int y = Math.round(pos.y * resolution) + matrix3D[0].length / 2;
			int z = Math.round(pos.z * resolution) + matrix3D[0][0].length / 2;

			if (outOfBounds(x, y, z))
				return 0;
			else
				return matrix3D[x][y][z];
		} else {
			int x = Math.round(pos.x * resolution) + matrix2D.length / 2;
			int y = Math.round(pos.y * resolution) + matrix2D[0].length / 2;
			if (outOfBounds(x, y))
				return 0;
			else
				return matrix2D[x][y];
		}
	}

	// returns the 'standard' density in the middle of a single node
	public double getStandard() {
		return cutoff * footRadius * footRadius;
	}

	public void set(Point3f pos, float mass) {

		// quicker just to have one check and a bit of code duplication:
		if (threeD) {
			int x = Math.round(pos.x * resolution) + matrix3D.length / 2;
			int y = Math.round(pos.y * resolution) + matrix3D[0].length / 2;
			int z = Math.round(pos.z * resolution) + matrix3D[0][0].length / 2;

			// check if either of two opposite corners are out of bounds
			while (outOfBounds(x - footRadius - 1, y - footRadius - 1, z - footRadius - 1)
					|| outOfBounds(x + footRadius + 1, y + footRadius + 1, z + footRadius + 1)) {
				System.err.println("Out of bounds attempt at: (" + x + ", " + y + ", " + z + ") with radius "
						+ footRadius);
				System.err.println("Field dimensions: (" + matrix3D.length + ", " + matrix3D[0].length + ", "
						+ matrix3D[0][0].length + ")");
				expand();
				x = Math.round(pos.x * resolution) + matrix3D.length / 2;
				y = Math.round(pos.y * resolution) + matrix3D[0].length / 2;
				z = Math.round(pos.z * resolution) + matrix3D[0][0].length / 2;
			}

			for (int i = 0; i < footprint3D.length; i++)
				for (int j = 0; j < footprint3D[i].length; j++)
					for (int k = 0; k < footprint3D[i][j].length; k++)
						place(x + i - footRadius, y + j - footRadius, z + k - footRadius, mass * footprint3D[i][j][k]);

		} else {
			int x = Math.round(pos.x * resolution) + matrix2D.length / 2;
			int y = Math.round(pos.y * resolution) + matrix2D[0].length / 2;

			// check if either of two opposite corners are out of bounds
			while (outOfBounds(x - footRadius - 1, y - footRadius - 1)
					|| outOfBounds(x + footRadius + 1, y + footRadius + 1)) {
				System.err.println("Out of bounds attempt at: (" + x + ", " + y + ") with radius " + footRadius);
				System.err.println("Field dimensions: (" + matrix2D.length + ", " + matrix2D[0].length + ")");
				expand();
				x = Math.round(pos.x * resolution) + matrix2D.length / 2;
				y = Math.round(pos.y * resolution) + matrix2D[0].length / 2;
			}

			for (int i = 0; i < footprint2D.length; i++)
				for (int j = 0; j < footprint2D[i].length; j++)
					place(x + i - footRadius, y + j - footRadius, mass * footprint2D[i][j]);
		}
	}

	private void place(int x, int y, double amount) {
		matrix2D[x][y] += amount;
	}

	private void place(int x, int y, int z, double amount) {
		matrix3D[x][y][z] += amount;
	}

	public void set(NodeList nodes) {
		for (Node n : nodes) {
			set(n);
		}
	}

	public void set(Node node) {
		set(node.getPosition(), node.getMass());
	}

	public void update(Point3f oldPos, Point3f newPos, Node node) {
		set(oldPos, -node.getMass());
		set(newPos, node.getMass());
	}

	private boolean outOfBounds(int x, int y) {
		return (x < 0 || y < 0 || x >= matrix2D.length || y >= matrix2D[0].length);
	}

	private boolean outOfBounds(int x, int y, int z) {
		return (x < 0 || y < 0 || z < 0 || x >= matrix3D.length || y >= matrix3D[0].length || z >= matrix3D[0][0].length);
	}

	private boolean outOfBounds(float x, float y) {
		return outOfBounds(Math.round(x), Math.round(y));
	}

	private boolean outOfBounds(float x, float y, float z) {
		return outOfBounds(Math.round(x), Math.round(y), Math.round(z));
	}

	public void expand() {
		// this will ruin time constraints if it happens too frequently
		//
		// could be made to expand only in the required direction
		//
		System.err.println("Expanding universe...");
		if (threeD) {
			double[][][] newMatrix = new double[2 * matrix3D.length][2 * matrix3D.length][2 * matrix3D.length];
			for (int i = 0; i < matrix3D.length; i++)
				for (int j = 0; j < matrix3D[i].length; j++)
					for (int k = 0; k < matrix3D[i][j].length; k++)
						newMatrix[i + matrix3D.length / 2][j + matrix3D.length / 2][k + matrix3D.length / 2] = matrix3D[i][j][k];
			matrix3D = newMatrix;
		} else {
			double[][] newMatrix = new double[2 * matrix2D.length][2 * matrix2D.length];
			for (int i = 0; i < matrix2D.length; i++)
				for (int j = 0; j < matrix2D[i].length; j++)
					newMatrix[i + matrix2D.length / 2][j + matrix2D.length / 2] = matrix2D[i][j];
			matrix2D = newMatrix;
		}
	}

	public void reset() {
		if (threeD)
			matrix3D = new double[initWidth][initHeight][initDepth];
		else
			matrix2D = new double[initWidth][initHeight];
	}

}
