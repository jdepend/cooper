package jdepend.framework.util;

public final class StringUtil {

	/**
	 * 
	 * 获取字符串的长度，如果有中文，则每个中文字符计为2位
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public static int length(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	public static int lastIndexOf(String sA, String sB, int num) {

		assert (num > 1);

		int weizhi = 0;
		for (int i = 1; i < num; i++) {
			if (weizhi == 0) {
				weizhi = sA.lastIndexOf(sB);
			}
			weizhi = sA.lastIndexOf(sB, weizhi - 1);
		}
		return weizhi;
	}

	/**
	 * 通配符算法。 可以匹配"*"和"?" 如a*b?d可以匹配aAAAbcd
	 * 
	 * @param pattern
	 *            匹配表达式
	 * @param str
	 *            匹配的字符串
	 * @return
	 */
	public static boolean match(String pattern, String str) {
		if (pattern == null || str == null)
			return false;

		for (String item : pattern.split("\\|")) {
			if (item.trim().length() != 0 && matchItem(item.trim(), str)) {
				return true;
			}
		}
		return false;
	}

	private static boolean matchItem(String pattern, String str) {

		boolean result = false;
		char c; // 当前要匹配的字符串
		boolean beforeStar = false; // 是否遇到通配符*
		int back_i = 0;// 回溯,当遇到通配符时,匹配不成功则回溯
		int back_j = 0;
		int i, j;
		for (i = 0, j = 0; i < str.length();) {
			if (pattern.length() <= j) {
				if (back_i != 0) {// 有通配符,但是匹配未成功,回溯
					beforeStar = true;
					i = back_i;
					j = back_j;
					back_i = 0;
					back_j = 0;
					continue;
				}
				break;
			}

			if ((c = pattern.charAt(j)) == '*') {
				if (j == pattern.length() - 1) {// 通配符已经在末尾,返回true
					result = true;
					break;
				}
				beforeStar = true;
				j++;
				continue;
			}

			if (beforeStar) {
				if (str.charAt(i) == c) {
					beforeStar = false;
					back_i = i + 1;
					back_j = j;
					j++;
				}
			} else {
				if (c != '?' && c != str.charAt(i)) {
					result = false;
					if (back_i != 0) {// 有通配符,但是匹配未成功,回溯
						beforeStar = true;
						i = back_i;
						j = back_j;
						back_i = 0;
						back_j = 0;
						continue;
					}
					break;
				}
				j++;
			}
			i++;
		}

		if (i == str.length() && j == pattern.length())// 全部遍历完毕
			result = true;
		return result;
	}

	public static void main(String[] args) {
		String test = "126";

		System.out.print(lastIndexOf(test, "3", 3));
	}

}
