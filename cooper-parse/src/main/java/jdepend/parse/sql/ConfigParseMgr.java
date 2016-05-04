package jdepend.parse.sql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jdepend.framework.exception.JDependException;
import jdepend.framework.file.TargetFileInfo;
import jdepend.framework.log.LogUtil;
import jdepend.metadata.TableInfo;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 配置文件解析器
 * 
 * @author wangdg
 * 
 */
public final class ConfigParseMgr {

	private static ConfigParseMgr inst = new ConfigParseMgr();

	private Map<String, ConfigParse> parses = new HashMap<String, ConfigParse>();

	private String currentXMLType;

	private String IBATIS_SQL_2_0 = "IBATIS_SQL_2_0";
	private String IBATIS_Mapper_3_0 = "IBATIS_Mapper_3_0";

	private String Hibernate = "Hibernate";

	private ThreadLocal<TableInfoCollection> tables = new ThreadLocal<TableInfoCollection>();

	private ConfigParseMgr() {
		this.parses.put(IBATIS_SQL_2_0, new IBATIS20SQLConfigParse());
		this.parses.put(IBATIS_Mapper_3_0, new IBATIS30MapperConfigParse());
	}

	public static ConfigParseMgr getInstance() {
		return inst;
	}

	public void parse(Map<String, List<TargetFileInfo>> configs) throws JDependException {

		try {
			DocumentBuilder builder = getDocumentBuilder();
			InputStream is = null;
			Document doc = null;
			int configCount = 0;
			for (String place : configs.keySet()) {
				for (TargetFileInfo config : configs.get(place)) {
					try {
						is = new ByteArrayInputStream(config.getContent());
						doc = builder.parse(is);
						if (currentXMLType != null && this.parses.get(currentXMLType) != null) {
							this.parses.get(currentXMLType).parse(doc);
						} else if (currentXMLType != null && this.parses.get(currentXMLType) == null) {
							LogUtil.getInstance(ConfigParseMgr.class).systemWarning("未配置" + currentXMLType + "XML解析器。");
						}
						configCount++;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (is != null) {
							try {
								is.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			LogUtil.getInstance(ConfigParseMgr.class).systemWarning("分析了" + configCount + "个XML文件。");

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new JDependException(e);
		}
	}

	public TableInfoCollection getTables() {
		if (tables.get() == null) {
			this.tables.set(new TableInfoCollection());
		}
		return tables.get();
	}

	public Map<String, List<TableInfo>> getTheTables(String type) {

		Map<String, List<TableInfo>> theTableInfos = new HashMap<String, List<TableInfo>>();

		for (TableInfoItem item : this.getTables().getTableInfos()) {
			if (item.getType().equals(type)) {
				theTableInfos.put(item.getName(), item.getTableInfos());
			}
		}

		return theTableInfos;

	}

	/**
	 * 增加表信息
	 * 
	 * @param type
	 *            类型
	 * @param tbs
	 *            表信息
	 */
	public void addTables(String type, Map<String, List<TableInfo>> tbs) {
		if (this.tables.get() == null) {
			this.tables.set(new TableInfoCollection());
		}
		for (String name : tbs.keySet()) {
			this.tables.get().addItem(name, type, tbs.get(name));
		}
	}

	private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				if (publicId != null) {
					if (publicId.equalsIgnoreCase("-//ibatis.apache.org//DTD SQL Map 2.0//EN")) {
						currentXMLType = IBATIS_SQL_2_0;
					} else if (publicId.equalsIgnoreCase("-//iBATIS.com//DTD SQL Map 2.0//EN")) {
						currentXMLType = IBATIS_SQL_2_0;
					} else if (publicId.equalsIgnoreCase("-//ibatis.apache.org//DTD Mapper 3.0//EN")) {
						currentXMLType = IBATIS_Mapper_3_0;
					} else if (publicId.equalsIgnoreCase("-//Hibernate/Hibernate Mapping DTD 3.0//EN")) {
						currentXMLType = Hibernate;
					} else if (publicId.equalsIgnoreCase("-//Hibernate/Hibernate Mapping DTD//EN")) {
						currentXMLType = Hibernate;
					} else {
						LogUtil.getInstance(ConfigParseMgr.class).systemWarning("未处理" + publicId + "说明的XML。");
						currentXMLType = null;
					}
				} else {
					LogUtil.getInstance(ConfigParseMgr.class).systemWarning("未处理不明publicId说明的XML。");
					currentXMLType = null;
				}
				return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));

			}
		});
		return builder;
	}
}
