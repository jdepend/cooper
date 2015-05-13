package jdepend.util.analyzer.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileUtil;
import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class KeywordSearch extends AbstractAnalyzer {

	private static final long serialVersionUID = 8489227155048814797L;

	private List<Keyword> keywords;

	public transient static final String DEFAULT_PROPERTY_DIR = "analyzerData";

	public transient static final String DEFAULT_PROPERTY_FILE = "KeyWords.txt";

	private static final String KeywordFile = JDependContext.getWorkspacePath() + "\\" + DEFAULT_PROPERTY_DIR + "\\"
			+ DEFAULT_PROPERTY_FILE;

	public KeywordSearch() {
		super("关键字搜索", Analyzer.Attention, "搜索含有关键字的分析单元");
	}

	@Override
	public void init() throws JDependException {

		if (!FileUtil.exists(KeywordFile)) {
			FileUtil.createFile(KeywordFile);
		}

		Properties keywordInfos = new Properties();
		try {
			FileUtil.readFileContentKeyValue(KeywordFile, keywordInfos);
		} catch (JDependException e) {
			throw new JDependException("读取配置文件[" + KeywordFile + "]失败");
		}

		this.keywords = new ArrayList<Keyword>();

		for (Object obj : keywordInfos.keySet()) {
			String key = (String) obj;
			Keyword keyword = new Keyword();
			keyword.content = key;
			keyword.type = keywordInfos.getProperty(key);

			this.keywords.add(keyword);
		}

		Collections.sort(this.keywords);

	}

	@Override
	public void release() throws JDependException {
		FileUtil.deleteFile(KeywordFile);
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		List<KeywordUnit> keywordUnits = new ArrayList<KeywordUnit>();
		KeywordUnit keywordUnit;
		boolean exist;

		Collection<Component> units = result.getComponents();

		for (Keyword keyword : keywords) {
			exist = false;
			for (JDependUnit unit : units) {
				if (this.match(unit.getName(), keyword.content)) {
					keywordUnit = new KeywordUnit(unit, keyword);
					if (!keywordUnits.contains(keywordUnit)) {
						keywordUnits.add(keywordUnit);
						exist = true;
					}
				}
			}
			if (!exist && units.size() > 0) {
				for (JavaPackage javaPackage : result.getJavaPackages()) {
					if (this.match(javaPackage.getName(), keyword.content)) {
						keywordUnit = new KeywordUnit(javaPackage, keyword);
						if (!keywordUnits.contains(keywordUnit)) {
							keywordUnits.add(keywordUnit);
							exist = true;
						}
					}
				}
				for (JavaClassUnit javaClass : result.getClasses()) {
					if (this.match(javaClass.getName(), keyword.content)) {
						keywordUnit = new KeywordUnit(javaClass, keyword);
						if (!keywordUnits.contains(keywordUnit)) {
							keywordUnits.add(keywordUnit);
							exist = true;
						}
					}
				}
			}
		}

		for (Keyword keyword : keywords) {
			exist = false;
			for (KeywordUnit ku : keywordUnits) {
				if (keyword.equals(ku.keyword)) {
					exist = true;

					if (ku.unit instanceof JavaClassUnit) {
						this.printTable("分析单元", ((JavaClassUnit) ku.unit).getName());
						this.printTable("单元类型", "类");
					} else if (ku.unit instanceof JavaPackage) {
						this.printTable("分析单元", ((JavaPackage) ku.unit).getName());
						this.printTable("单元类型", "包");
					} else {
						this.printTable("分析单元", ((Component) ku.unit).getName());
						this.printTable("单元类型", "组件");
					}
					this.printTable("关键字类型", keyword.type);
					this.printTable("关键字", keyword.content);
				}
			}
			if (!exist) {
				this.printTable("分析单元", "");
				this.printTable("单元类型", "");
				this.printTable("关键字类型", keyword.type);
				this.printTable("关键字", keyword.content);
			}
		}

	}

	private boolean match(String current, String key) {
		return current.toUpperCase().indexOf(key.toUpperCase()) != -1;
		// for (String segment : current.split("\\.")) {
		// if (segment.equalsIgnoreCase(key)) {
		// return true;
		// }
		// }
		// return false;
	}

	public String[] getKeyword() {
		String[] info = new String[keywords.size()];
		int i = 0;
		for (Keyword keyword : keywords) {
			info[i++] = keyword.content + "=" + keyword.type;
		}
		return info;
	}

	public void setKeyword(String[] keywordInfo) throws JDependException {
		StringBuilder info = new StringBuilder();
		for (int i = 0; i < keywordInfo.length; i++) {
			info.append(keywordInfo[i]);
			info.append("\n");
		}
		FileUtil.saveFileContent(KeywordFile, info);
		this.init();

	}

	class Keyword implements Serializable, Comparable<Keyword> {

		public String content;
		public String type;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((content == null) ? 0 : content.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Keyword other = (Keyword) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (content == null) {
				if (other.content != null)
					return false;
			} else if (!content.equals(other.content))
				return false;
			return true;
		}

		private KeywordSearch getOuterType() {
			return KeywordSearch.this;
		}

		@Override
		public int compareTo(Keyword o) {
			return this.type.compareTo(o.type);
		}

	}

	class KeywordUnit implements Serializable {

		public Object unit;
		public Keyword keyword;

		public KeywordUnit(Object unit, Keyword keyword) {
			super();
			this.unit = unit;
			this.keyword = keyword;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
			result = prime * result + ((unit == null) ? 0 : unit.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			KeywordUnit other = (KeywordUnit) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (keyword == null) {
				if (other.keyword != null)
					return false;
			} else if (!keyword.equals(other.keyword))
				return false;
			if (unit == null) {
				if (other.unit != null)
					return false;
			} else if (!unit.equals(other.unit))
				return false;
			return true;
		}

		private KeywordSearch getOuterType() {
			return KeywordSearch.this;
		}
	}
}
