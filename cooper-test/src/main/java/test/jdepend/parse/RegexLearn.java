package test.jdepend.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexLearn {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String metaRxp = "from\\w*\\b";
		String text = "select * from  tableA aa , tableD dd where * from (select * from tableB  where * from (select * from tableC))";
		Pattern pattern = Pattern.compile(metaRxp);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			System.out.println(matcher.start());
			System.out.println(matcher.group());
		}
	}

}
