package jdepend.parse.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.log.LogUtil;
import jdepend.metadata.TableInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class HibernateConfigParse implements ConfigParse {

	@Override
	public void parse(Document doc) {
		Map<String, List<TableInfo>> tables = new HashMap<String, List<TableInfo>>();
		List<TableInfo> tableInfos;
		TableInfo tableInfo;

		Element root = doc.getDocumentElement();
		String name;
		String table;
		NodeList tags = doc.getElementsByTagName("class");
		for (int i = 0; i < tags.getLength(); i++) {
			Element tag = (Element) tags.item(i);
			name = root.getAttribute("package") + "." + tag.getAttribute("name");
			table = tag.getAttribute("table");

			tableInfo = new TableInfo(table, TableInfo.Define);
			tableInfos = new ArrayList<TableInfo>();
			tableInfos.add(tableInfo);

			tables.put(name, tableInfos);

		}
		if (tables.size() > 0) {
			LogUtil.getInstance(HibernateConfigParse.class).systemLog("tables:" + tables);
			ConfigParseMgr.getInstance().addTables(TableInfoItem.ClassNameType, tables);
		}
	}
}
