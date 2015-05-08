/* 
 * @author Hinex
 * @date 2015-5-8 13:12:15
 */

package jdepend.util.analyzer.element.helper;

import static jdepend.util.analyzer.element.helper.ServiceOrVOType.*;

public enum ServiceOrVO {
	
	INIT(UNSURE, false, ""),
	
	ONLY_CONSTRUCTION(VO, true, "1"),
	
	SUB_NO_BIZ_METHOD(VO, true, "26"),
	
	SUB_STATE_SUPER_STATE(VO, true, "27"),
	
	SUB_STATE_NOT_PR(SERVICE, false, "29"),
	
	SUB_STATE_IS_PR(VO, true, "210"),
	
	SUPER_NO_BIZ_METHOD(VO, true, "36"),
	
	SUPER_STATE_TWICE(VO, true, "37"),
	
	SUPER_STATE_NOT_PR(SERVICE, false, "39"),
	
	SUPER_STATE_IS_PR(VO, true, "310"),
	
	UNSURE_SERVICE(SERVICE, false, "4"),
	
	UNSURE_NO_BIZ_METHOD(VO, true, "56"),
	
	UNSURE_SUPER_STATE(VO, true, "57"),
	
	UNSURE_NOT_PR(SERVICE, false, "59"),
	
	UNSURE_IS_PR(VO, true, "510");
	
	private ServiceOrVOType type;
	
	private boolean ensureVO;
	
	private String index;
	
	private ServiceOrVO(ServiceOrVOType type, boolean ensureVO, String index) {
		this.type = type;
		this.ensureVO = ensureVO;
		this.index = index;
	}

	public ServiceOrVOType getType() {
		return type;
	}

	public boolean isEnsureVO() {
		return ensureVO;
	}

	public String getIndex() {
		return index;
	}
	
}
