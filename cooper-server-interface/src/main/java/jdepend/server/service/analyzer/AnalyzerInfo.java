package jdepend.server.service.analyzer;

public final class AnalyzerInfo extends AnalyzerSummaryDTO {

	private byte[] defaultData;

	private byte[] def;

	public byte[] getDefaultData() {
		return defaultData;
	}

	public void setDefaultData(byte[] defaultData) {
		this.defaultData = defaultData;
	}

	public byte[] getDef() {
		return def;
	}

	public void setDef(byte[] def) {
		this.def = def;
	}
}
