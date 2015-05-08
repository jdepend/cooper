/* 
 * @author Hinex
 * @date 2015-5-8 13:15:10
 */

package jdepend.util.analyzer.element.helper;

public enum ServiceOrVOType {
	
	SERVICE("Service"), VO("VO"), UNSURE("不确定");
	
	private String value;
	
	private ServiceOrVOType(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}

}
