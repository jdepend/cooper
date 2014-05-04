package org.wilmascope.graphmodifiers.plugin;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.wilmascope.control.GraphControl;

/**
 * Methods for restructuring a graph. At the moment this class just provides a
 * simple kMeans clustering algorithm.
 * 
 * @author dwyer
 * @version 1.0
 */

public class KMeans {
	public KMeans() {
		random = org.wilmascope.global.RandomGenerator.getRandom();
	}

	public Vector[] kMeansClustering(GraphControl.Cluster cf, int k, int n) {
		if (k < n) {
			throw new IllegalArgumentException("k must be >= n");
		}
		if (n < 1) {
			throw new IllegalArgumentException("n must be >= 1");
		}
		Vector[] clusters = kMeansClustering(cf, k);
		Vector[] largestClusters = new Vector[n];
		int[] sizes = new int[k];
		for (int i = 0; i < k; i++) {
			sizes[i] = clusters[i].size();
		}
		Arrays.sort(sizes);
		int limit = sizes[k - n];
		for (int i = 0, j = 0; i < k && j < n; i++) {
			if (clusters[i].size() >= limit) {
				largestClusters[j++] = clusters[i];
			}
		}
		return largestClusters;
	}

	public Vector[] kMeansClustering(GraphControl.Cluster cf, int k) {
		Vector nodes = new Vector(Arrays.asList(cf.getNodes()));
		if (k > nodes.size()) {
			throw new IllegalArgumentException("k > number nodes in cluster");
		}
		Vector[] clusters = new Vector[k];
		Point3f[] clusterBarycenters = new Point3f[k];

		GraphControl.Node n;
		Point3f p;
		// randomly select starting nodes for clusters
		for (int i = 0; i < k; i++) {
			int r = random.nextInt(nodes.size());
			n = (GraphControl.Node) nodes.remove(r);
			clusters[i] = new Vector();
			clusters[i].add(n);
			clusterBarycenters[i] = n.getPosition();
		}

		// add nodes to their closest clusters
		float newClusterDistance = Float.MAX_VALUE, d;
		for (int j = 0; j < nodes.size(); j++) {
			n = (GraphControl.Node) nodes.get(j);
			p = n.getPosition();
			int newCluster = 0;
			for (int i = 0; i < k; i++) {
				d = distanceSquared(clusterBarycenters[i], p);
				if (d < newClusterDistance) {
					newCluster = i;
					newClusterDistance = d;
				}
			}
			clusters[newCluster].add(n);
		}
		// update the barycenters of the clusters
		for (int i = 0; i < k; i++) {
			clusterBarycenters[i] = getBarycenter(clusters[i]);
		}
		int moved = 0;
		do {
			moved = improveKClusters(k, clusters, clusterBarycenters);
		} while (moved > 0);

		return clusters;
	}

	private float distanceSquared(Point3f u, Point3f v) {
		Vector3f vec = new Vector3f();
		vec.sub(u, v);
		return vec.lengthSquared();
	}

	private int improveKClusters(int k, Vector[] clusters, Point3f[] clusterBarycenters) {
		GraphControl.Node n;
		Point3f p;
		int moved = 0, newCluster;
		float oldDistance, newDistance;
		for (int i = 0; i < k; i++) {
			for (int h = 0; h < clusters[i].size(); h++) {
				n = (GraphControl.Node) clusters[i].get(h);
				p = n.getPosition();
				oldDistance = distanceSquared(p, clusterBarycenters[i]);
				newCluster = -1;
				for (int j = 0; j < k; j++) {
					if (i == j)
						continue;
					newDistance = distanceSquared(p, clusterBarycenters[j]);
					if (newDistance < oldDistance) {
						oldDistance = newDistance;
						newCluster = j;
					}
				}
				if (newCluster >= 0) {
					clusters[i].remove(n);
					clusters[newCluster].add(n);
					moved++;
				}
			}
		}
		// update the barycenters of the clusters
		for (int i = 0; i < k; i++) {
			clusterBarycenters[i] = getBarycenter(clusters[i]);
		}
		return moved;
	}

	private Point3f getBarycenter(Vector nodes) {
		Point3f barycenter = new Point3f();
		for (Iterator i = nodes.iterator(); i.hasNext();) {
			barycenter.add(((GraphControl.Node) i.next()).getPosition());
		}
		barycenter.scale(1f / (float) nodes.size());
		return barycenter;
	}

	Random random;
}
