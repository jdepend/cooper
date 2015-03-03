package jdepend.util.shoppingcart;

import java.util.Date;

import jdepend.framework.util.DateUtil;
import jdepend.model.result.AnalysisResult;

public class Product {

	private Date createDate;

	private AnalysisResult result;

	public Product(AnalysisResult result) {
		this.result = result;

		this.createDate = DateUtil.getSysDate();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public AnalysisResult getResult() {
		return result;
	}

}
