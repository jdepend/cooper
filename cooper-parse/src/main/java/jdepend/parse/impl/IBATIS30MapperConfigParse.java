package jdepend.parse.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.log.LogUtil;
import jdepend.metadata.TableInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class IBATIS30MapperConfigParse implements ConfigParse {

	@Override
	public void parse(Document doc) {

		Map<String, List<TableInfo>> tables = new HashMap<String, List<TableInfo>>();

		Element root = doc.getDocumentElement();
		String namespace = root.getAttribute("namespace");
		LogUtil.getInstance(IBATIS30MapperConfigParse.class).systemLog("namespace(JavaClassName):" + namespace);
		List<String> tagNames = new ArrayList<String>();
		tagNames.add("insert");
		tagNames.add("update");
		tagNames.add("delete");
		tagNames.add("select");
		for (String tagName : tagNames) {
			NodeList tags = doc.getElementsByTagName(tagName);
			for (int i = 0; i < tags.getLength(); i++) {
				Element tag = (Element) tags.item(i);
				String sql = tag.getTextContent();
				tables.put(namespace, SqlParserUtil.parserSql(sql));
			}
		}
		LogUtil.getInstance(IBATIS30MapperConfigParse.class).systemLog("tables:" + tables);
		ConfigParseMgr.getInstance().addTables(TableInfoItem.ClassNameType, tables);

	}

}
