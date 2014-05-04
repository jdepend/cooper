package org.wilmascope.gui;

/**
 * The following came from one of the Sun examples... I can't recall which one but it's been butchered pretty badly since then, still you should probably read the standard sun disclaimers before you try to use it in your own project:
 *
 * Copyright (c) 1998, 1999 by Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.

 */
import java.awt.Container;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

public class HtmlPane extends JScrollPane implements HyperlinkListener {
	JEditorPane html;

	public HtmlPane(String helpFileName) {
		try {
			File f = new File(helpFileName);
			String s = f.getAbsolutePath();
			s = "file:" + s;
			URL url = new URL(s);
			html = new JEditorPane(s);
			html.setEditable(false);
			html.addHyperlinkListener(this);

			JViewport vp = getViewport();
			vp.add(html);
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e);
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
	}

	public void setText(String text) {
		html.setText(text);
	}

	public void init() {
		html.addHyperlinkListener(this);
		JViewport vp = getViewport();
		html.setSize(400, 400);
		vp.add(html);
		vp.updateUI();
	}

	public HtmlPane(String type, String text) {
		html = new JEditorPane(type, text);
		html.addHyperlinkListener(this);
		html.setEditable(false);
		init();
	}

	/**
	 * Notification of a change relative to a hyperlink.
	 */
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			linkActivated(e.getURL());
		}
	}

	/**
	 * Follows the reference in an link. The given url is the requested
	 * reference. By default this calls <a href="#setPage">setPage</a>, and if
	 * an exception is thrown the original previous document is restored and a
	 * beep sounded. If an attempt was made to follow a link, but it represented
	 * a malformed url, this method will be called with a null argument.
	 * 
	 * @param u
	 *            the URL to follow
	 */
	protected void linkActivated(URL u) {
		Cursor c = html.getCursor();
		Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		html.setCursor(waitCursor);
		SwingUtilities.invokeLater(new PageLoader(u, c));
	}

	/**
	 * temporary class that loads synchronously (although later than the request
	 * so that a cursor change can be done).
	 */
	class PageLoader implements Runnable {

		PageLoader(URL u, Cursor c) {
			url = u;
			cursor = c;
		}

		public void run() {
			if (url == null) {
				// restore the original cursor
				html.setCursor(cursor);

				// PENDING(prinz) remove this hack when
				// automatic validation is activated.
				Container parent = html.getParent();
				parent.repaint();
			} else {
				Document doc = html.getDocument();
				try {
					html.setPage(url);
				} catch (IOException ioe) {
					html.setDocument(doc);
					getToolkit().beep();
				} finally {
					// schedule the cursor to revert after
					// the paint has happended.
					url = null;
					SwingUtilities.invokeLater(this);
				}
			}
		}

		URL url;
		Cursor cursor;
	}

}
