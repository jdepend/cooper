package jdepend.framework.util;

import java.text.NumberFormat;

public class MetricsFormat {

	private static NumberFormat nf;

	private static NumberFormat nf1;

	static {
		nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(3);
		nf.setMinimumFractionDigits(1);

		nf1 = NumberFormat.getInstance();
		nf1.setGroupingUsed(false);
		nf1.setMaximumFractionDigits(1);
		nf1.setMinimumFractionDigits(1);
	}

	public static Float toFormattedScore(Float f) {
		if (f == null) {
			return toFormattedScore(0F);
		} else {
			return new Float(nf.format(f));
		}
	}

	public static String toFormattedPercent(Float f) {
		if (f == null) {
			return toFormattedPercent(0F);
		} else {
			return nf1.format(toFormattedScore(f) * 100) + "%";
		}
	}

	public static Float toFormattedMetrics(Float f) {
		if (f == null) {
			return null;
		} else {
			return new Float(nf.format(f));
		}
	}

	public static Float toFormattedMetrics(Double f) {
		if (f == null) {
			return null;
		} else {
			return new Float(nf.format(f));
		}
	}

}
