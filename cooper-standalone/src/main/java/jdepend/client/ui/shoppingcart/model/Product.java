package jdepend.client.ui.shoppingcart.model;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;

		return true;
	}
}
