package jdepend.framework.util;

import java.text.NumberFormat;

public class MetricsFormat {

	public static Float toFormattedScore(Float f) {
		if (f == null) {
			return toFormattedScore(0F);
		} else {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(3);
			nf.setMinimumFractionDigits(1);
			return new Float(nf.format(f));
		}
	}

	public static String toFormattedPercent(Float f) {
		if (f == null) {
			return toFormattedPercent(0F);
		} else {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(1);
			nf.setMinimumFractionDigits(1);
			return nf.format(toFormattedScore(f) * 100) + "%";
		}
	}

	public static Float toFormattedMetrics(Float f) {
		if (f == null) {
			return null;
		} else {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(3);
			nf.setMinimumFractionDigits(1);
			return new Float(nf.format(f));
		}
	}

	public static Float toFormattedMetrics(Double f) {
		if (f == null) {
			return null;
		} else {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(3);
			nf.setMinimumFractionDigits(1);
			return new Float(nf.format(f));
		}
	}

}
