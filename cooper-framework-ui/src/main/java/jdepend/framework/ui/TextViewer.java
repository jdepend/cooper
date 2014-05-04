package jdepend.framework.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import jdepend.framework.exception.JDependException;

public class TextViewer extends JEditorPane {

	MyEditorKit kit = new MyEditorKit();

	private JScrollPane scrollPane;

	public TextViewer() {
		this.setFont(UIProperty.TEXTFONT);
		this.setEditorKitForContentType("text/java", kit);
		this.setContentType("text/java");

		this.setCaretPosition(0);

		this.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
			}
		});

		this.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == 70 || e.getKeyCode() == 70) && e.isControlDown()) {
					if (scrollPane != null) {
						SearchDialog d = new SearchDialog(TextViewer.this);
						d.setModal(true);
						d.setLocation(250, 100);
						d.setVisible(true);
					}
				}

			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
	}

	public void search(String text) throws JDependException {

		if (this.scrollPane == null) {
			throw new JDependException("scrollPane 为空，不支持search");
		}

		int start = this.getText().indexOf(text);
		if (start != -1) {
			Point p = new Point();
			p.setLocation(0, start);
			scrollPane.getViewport().setViewPosition(p);
		}
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
}

class SearchDialog extends JDialog {

	private JTextField search = new JTextField();

	private TextViewer viewer;

	public SearchDialog(TextViewer viewer) {

		setTitle("搜索");

		this.viewer = viewer;

		setResizable(false);

		getContentPane().setLayout(new BorderLayout());
		setSize(400, 100);

		this.add(BorderLayout.CENTER, search);

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createSearchButton());

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	private JButton createSearchButton() {

		JButton button = new JButton("搜索");

		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					viewer.search(search.getText());
					dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
					java.awt.Component source = (java.awt.Component) e.getSource();
					JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}
}

class MyEditorKit extends DefaultEditorKit {
	public MyEditorKit() {
		super();
	}

	public ViewFactory getViewFactory() {
		return new MyViewFactory();
	}
}

class MyViewFactory implements ViewFactory {
	public MyViewFactory() {
	}

	public View create(Element element) {
		return new MyEditorView(element);
	}
}

class MyEditorView extends PlainView {
	public MyEditorView(Element element) {
		super(element);
	}

	protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException {
		Document doc = getDocument();
		Segment segment = new Segment(), token = new Segment();
		int index = 0, count = p1 - p0;
		char c = '\u0000';

		doc.getText(p0, count, segment);
		for (int i = 0; i < count; i++) {
			if (Character.isLetter(c = segment.array[segment.offset + i])) {
				index = i;
				while (++i < count && Character.isLetter(segment.array[segment.offset + i]))
					;
				doc.getText(p0 + index, (i--) - index, token);
				if (KeyWord.isKeyWord(token)) {
					g.setFont(KEYWORDFONT);
					g.setColor(KEYWORDCOLOR);
				} else {
					g.setFont(UIProperty.TEXTFONT);
					g.setColor(TEXTCOLOR);
				}
				x = Utilities.drawTabbedText(token, x, y, g, this, p0 + index);
				continue;
			} else if (c == '/') {
				index = i;
				if (++i < count && segment.array[segment.offset + i] == '/') {
					doc.getText(p0 + index, count - index, token);
					g.setFont(COMMENTFONT);
					g.setColor(COMMENTCOLOR);
					x = Utilities.drawTabbedText(token, x, y, g, this, p0 + index);
					break;
				}
				doc.getText(p0 + index, 1, token);
				g.setFont(UIProperty.TEXTFONT);
				g.setColor(TEXTCOLOR);
				x = Utilities.drawTabbedText(token, x, y, g, this, p0 + index);
				i--;
				continue;
			} else if (c == '\'' || c == '\"') {
				index = i;
				char ch = '\u0000';
				while (++i < count) {
					if ((ch = segment.array[segment.offset + i]) == '\\') {
						i++;
						continue;
					} else if (ch == c)
						break;
				}
				if (i >= count)
					i = count - 1;
				doc.getText(p0 + index, i - index + 1, token);
				g.setFont(STRINGFONT);
				g.setColor(STRINGCOLOR);
				x = Utilities.drawTabbedText(token, x, y, g, this, p0 + index);
				continue;
			} else {
				index = i;
				while (++i < count && !Character.isLetter((c = segment.array[segment.offset + i])) && c != '/'
						&& c != '\'' && c != '\"')
					;
				doc.getText(p0 + index, (i--) - index, token);
				g.setFont(UIProperty.TEXTFONT);
				g.setColor(TEXTCOLOR);
				x = Utilities.drawTabbedText(token, x, y, g, this, p0 + index);
			}
		}
		return x;
	}

	protected int drawSelectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException {
		g.setFont(UIProperty.TEXTFONT);
		g.setColor(TEXTCOLOR);
		return super.drawSelectedText(g, x, y, p0, p1);
	}

	public static Color TEXTCOLOR = Color.black;
	public static Font KEYWORDFONT = new Font(UIProperty.TEXTFONT.getFontName(), Font.BOLD,
			UIProperty.TEXTFONT.getSize());
	public static Color KEYWORDCOLOR = new Color(0, 0, 128);
	public static Font COMMENTFONT = UIProperty.TEXTFONT;
	public static Color COMMENTCOLOR = new Color(192, 192, 192);
	public static Font STRINGFONT = UIProperty.TEXTFONT;
	public static Color STRINGCOLOR = new Color(255, 0, 0);
}

class KeyWord {
	public KeyWord() {
	}

	public static boolean isKeyWord(Segment seg) {
		boolean isKey = false;
		for (int i = 0; !isKey && i < KEYWORDS.length; i++)
			if (seg.count == KEYWORDS[i].length()) {
				isKey = true;
				for (int j = 0; isKey && j < seg.count; j++)
					if (seg.array[seg.offset + j] != KEYWORDS[i].charAt(j))
						isKey = false;

			}
		return isKey;
	}

	public static final String[] KEYWORDS = { "abstract", "boolean", "break", "byte", "case", "catch", "char", "class",
			"const", "continue", "default", "do", "double", "else", "extends", "final", "finally", "float", "for",
			"goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package",
			"private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch",
			"synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "true",
			"false",

			"JDependUnitName", "Stats", "isInner", "Summary", "TotalClasses", "ConcreteClasses", "AbstractClasses",
			"TotalLineCount", "Ca", "Ce", "A", "I", "D", "DependsUpon", "UsedBy", "DependencyCycles", "Name", "CC",
			"LC", "AC", "V", "OO", "Classes", "Coupling", "Cohesion", "Balance",

			"Attribute", "fields", "methods",

			"ERROR", "LOG" };
}
