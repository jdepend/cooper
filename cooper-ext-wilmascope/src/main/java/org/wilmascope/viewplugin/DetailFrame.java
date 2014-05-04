/*
 * The following source code is part of the WilmaScope 3D Graph Drawing Engine
 * which is distributed under the terms of the GNU Lesser General Public License
 * (LGPL - http://www.gnu.org/copyleft/lesser.html).
 *
 * As usual we distribute it with no warranties and anything you chose to do
 * with it you do at your own risk.
 *
 * Copyright for this work is retained by Tim Dwyer and the WilmaScope organisation
 * (www.wilmascope.org) however it may be used or modified to work as part of
 * other software subject to the terms of the LGPL.  I only ask that you cite
 * WilmaScope as an influence and inform us (tgdwyer@yahoo.com)
 * if you do anything really cool with it.
 *
 * The WilmaScope software source repository is hosted by Source Forge:
 * www.sourceforge.net/projects/wilma
 *
 * -- Tim Dwyer, 2001
 */

package org.wilmascope.viewplugin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.wilmascope.gui.HtmlPane;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class DetailFrame extends JFrame {
	JPanel controlsPanel;
	JButton editButton, closeButton;
	HtmlPane html;
	String text;
	DefaultNodeData nodeData;

	public DetailFrame(DefaultNodeData nodeData, String helpFileName) {
		super("Node Details");
		html = new HtmlPane(helpFileName);
		this.text = helpFileName;
		this.nodeData = nodeData;
		init();
	}

	public DetailFrame(DefaultNodeData nodeData, String type, String text) {
		super("Node Details");
		html = new HtmlPane(type, text);
		this.text = text;
		this.nodeData = nodeData;
		init();
	}

	private void init() {
		setBounds(200, 25, 400, 400);
		getContentPane().setLayout(new BorderLayout());
		controlsPanel = new JPanel();
		getContentPane().add(BorderLayout.CENTER, html);
		getContentPane().add(BorderLayout.NORTH, controlsPanel);
		controlsPanel.add(editButton = new JButton("Edit Text"));
		controlsPanel.add(closeButton = new JButton("Close"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeButtonActionPerformed();
			}
		});
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editButtonActionPerformed();
			}
		});
		controlsPanel.setMaximumSize(new Dimension(12345, 50));
		pack();
		setSize(400, 400);
	}

	public void closeButtonActionPerformed() {
		this.dispose();
	}

	public void editButtonActionPerformed() {
		final JFrame editFrame = new JFrame();
		editFrame.getContentPane().setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane();
		final JEditorPane editPane = new JEditorPane("text", text);
		scrollPane.getViewport().add(editPane);
		editFrame.getContentPane().add(BorderLayout.CENTER, scrollPane);
		JPanel okPanel = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text = editPane.getText();
				editFrame.dispose();
				html.setText(text);
				nodeData.setData(text);
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editFrame.dispose();
			}
		});
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		editFrame.getContentPane().add(BorderLayout.NORTH, okPanel);
		editFrame.pack();
		editFrame.setSize(400, 400);
		editFrame.show();
	}
}
