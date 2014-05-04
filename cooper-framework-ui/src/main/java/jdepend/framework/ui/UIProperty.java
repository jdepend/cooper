package jdepend.framework.ui;

import java.awt.Font;

public final class UIProperty {

	public static Font TEXTFONT = new Font("DialogInput", Font.PLAIN, 14);

	public static void setSize(int size) {
		TEXTFONT = new Font("DialogInput", Font.PLAIN, size);
	}

}
