package jdepend.report.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.util.MathUtil;
import jdepend.model.Element;
import jdepend.model.Relation;

public final class IntensitySizeCalculator {

	public static final int RelationIntensityLevel = 4;

	public static final int RelationIntensityStep = 2;

	public static final int ElementIntensityLevel = 4;

	public static final int ElementIntensityStep = 6;

	public static Map<Element, Integer> calElementSize(Collection<Element> elements) {

		List<Float> elementBalances = new ArrayList<Float>();
		for (Element element : elements) {
			elementBalances.add(element.getBalance());
		}
		Collections.sort(elementBalances);

		Map<Element, Integer> rtn = new HashMap<Element, Integer>();

		for (Element element : elements) {
			rtn.put(element, calElementSize(element.getBalance(), elementBalances));
		}

		return rtn;

	}

	public static Map<Relation, Integer> calRelationSize(Collection<Relation> relations) {

		List<Float> relationIntensitys = new ArrayList<Float>();
		for (Relation relation : relations) {
			relationIntensitys.add(relation.getIntensity());
		}
		Collections.sort(relationIntensitys);

		Map<Relation, Integer> rtn = new HashMap<Relation, Integer>();

		for (Relation relation : relations) {
			rtn.put(relation, calEdgeSize(relation.getIntensity(), relationIntensitys));
		}

		return rtn;

	}

	private static int calElementSize(Float current, List<Float> intensitys) {
		int size = 1;
		for (Float intensity : intensitys) {
			if (MathUtil.compare(current, intensity) == 0) {
				if (intensitys.size() <= ElementIntensityLevel) {
					return size + 11;
				} else {
					return size / (intensitys.size() / ElementIntensityLevel + 1) + 12;
				}
			} else {
				size += ElementIntensityStep;
			}
		}

		return 0;
	}

	private static int calEdgeSize(Float current, List<Float> intensitys) {
		int index = 1;
		for (Float intensity : intensitys) {
			if (MathUtil.compare(current, intensity) == 0) {
				if (intensitys.size() <= RelationIntensityLevel) {
					return index;
				} else {
					return index / (intensitys.size() / RelationIntensityLevel + 1) + RelationIntensityStep;

				}
			} else {
				index += RelationIntensityStep;
			}
		}

		return 0;
	}

}
