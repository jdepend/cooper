package jdepend.report.ui;

import java.awt.Color;
import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLJDependUtil {

	public JComponent createResult(StringBuilder strResult) {

		JTree tree = getTree(strResult);

		return new JScrollPane(tree);
	}

	private JTree getTree(StringBuilder strResult) {

		JTree tree = new JTree(getTreeModel(strResult));

		// Steal the default icons from the default renderer...
		DefaultTreeCellRenderer rend1 = new DefaultTreeCellRenderer();
		IconAndTipRenderer rend2 = new IconAndTipRenderer(rend1.getOpenIcon(), rend1.getClosedIcon(),
				rend1.getLeafIcon());
		tree.setCellRenderer(rend2);
		// TODO Auto-generated method stub
		return tree;
	}

	private TreeModel getTreeModel(StringBuilder strResult) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		XMLIconTreeHandler handler = new XMLIconTreeHandler();
		SAXParser saxParser;
		ByteArrayInputStream stream = null;
		try {

			saxParser = factory.newSAXParser();
			stream = new ByteArrayInputStream(strResult.toString().getBytes("UTF-8"));
			saxParser.parse(stream, handler);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return new DefaultTreeModel(handler.getRoot());
	}

	public class XMLIconTreeHandler extends DefaultHandler {

		private DefaultMutableTreeNode root, currentNode;

		public DefaultMutableTreeNode getRoot() {
			return root;
		}

		public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException {
			String eName = lName;
			if ("".equals(eName))
				eName = qName;
			ITag t = new ITag(eName, attrs);
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(t);
			if (currentNode == null) {
				root = newNode;
			} else {
				currentNode.add(newNode);
			}
			currentNode = newNode;
		}

		public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
			currentNode = (DefaultMutableTreeNode) currentNode.getParent();
		}

		public void characters(char buf[], int offset, int len) throws SAXException {
			String s = new String(buf, offset, len).trim();
			((ITag) currentNode.getUserObject()).addData(s);
		}
	}

	public class ITag implements IconAndTipCarrier {

		private String name;

		private String data;

		private Map<String, String> attr = new LinkedHashMap();

		private Icon icon;

		private String tipText;

		public ITag(String n, Attributes a) {
			name = n;
			for (int i = 0; i < a.getLength(); i++) {
				String aname = a.getQName(i);
				String value = a.getValue(i);
				attr.put(aname, value);
				if (aname.equals("icon")) {
					tipText = value;
					icon = new ImageIcon(value);
				}
			}
		}

		public String getName() {
			return name;
		}

		public Map getAttributes() {
			return attr;
		}

		public void setData(String d) {
			data = d;
		}

		public String getData() {
			return data;
		}

		public String getToolTipText() {
			return tipText;
		}

		public Icon getIcon() {
			return icon;
		}

		public void addData(String d) {
			if (data == null) {
				setData(d);
			} else {
				data += d;
			}
		}

		public String getAttributesAsString() {
			StringBuilder buf = new StringBuilder(256);
			Iterator it = this.attr.keySet().iterator();
			String key;
			while (it.hasNext()) {
				key = (String) it.next();
				buf.append("  ");
				buf.append(key);
				buf.append("=\"");
				buf.append(this.attr.get(key));
				buf.append("\"");
			}
			return buf.toString();
		}

		public String toString() {

			StringBuilder info = new StringBuilder(50);

			info.append(name);
			String a = getAttributesAsString();
			if (a != null && a.length() != 0) {
				info.append(": " + a);
			}
			if (data != null && data.length() != 0) {
				info.append(" (" + data + ")");
			}
			return info.toString();
		}
	}

	public interface IconAndTipCarrier {

		public Icon getIcon();

		public String getToolTipText();
	}

	public class IconAndTipRenderer extends JLabel implements TreeCellRenderer {
		Color backColor = new Color(0xFF, 0xCC, 0xFF);

		Icon openIcon, closedIcon, leafIcon;

		String tipText = "";

		public IconAndTipRenderer(Icon open, Icon closed, Icon leaf) {
			openIcon = open;
			closedIcon = closed;
			leafIcon = leaf;
			setBackground(backColor);
			setForeground(Color.black);
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			setText(value.toString());
			if (selected) {
				setOpaque(true);
			} else {
				setOpaque(false);
			}

			IconAndTipCarrier itc = null;
			if (value instanceof DefaultMutableTreeNode) {
				Object uo = ((DefaultMutableTreeNode) value).getUserObject();
				if (uo instanceof IconAndTipCarrier) {
					itc = (IconAndTipCarrier) uo;
				}
			} else if (value instanceof IconAndTipCarrier) {
				itc = (IconAndTipCarrier) value;
			}
			if ((itc != null) && (itc.getIcon() != null)) {
				setIcon(itc.getIcon());
				tipText = itc.getToolTipText();
			} else {
				tipText = " ";
				if (expanded) {
					setIcon(openIcon);
				} else if (leaf) {
					setIcon(leafIcon);
				} else {
					setIcon(closedIcon);
				}
			}
			return this;
		}

		public String getToolTipText() {
			return tipText;
		}

	}
}
