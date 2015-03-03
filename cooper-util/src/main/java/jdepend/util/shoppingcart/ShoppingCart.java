package jdepend.util.shoppingcart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.util.refactor.AdjustHistory;

public class ShoppingCart {

	private List<Product> products = new ArrayList<Product>();

	private static ShoppingCart inst = new ShoppingCart();

	private ShoppingCart() {
	}

	public static ShoppingCart getInstance() {
		return inst;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void addProduct(AnalysisResult result) {
		this.products.add(new Product(result));
	}

	public Product getTheProduct(Date createDate) {
		for (Product product : this.products) {
			if (product.getCreateDate().equals(createDate)) {
				return product;
			}
		}
		return null;
	}

	public void compare(Date date1, Date date2) {
		AdjustHistory.getInstance().clear();
		AnalysisResult result1 = this.getTheProduct(date1).getResult();
		JDependUnitMgr.getInstance().setResult(result1);
		AdjustHistory.getInstance().addMemento();
		AnalysisResult result2 = this.getTheProduct(date2).getResult();
		JDependUnitMgr.getInstance().setResult(result2);
	}

	public boolean isEmpty() {
		return this.products.size() == 0;
	}

	public void clear() {
		this.products = new ArrayList<Product>();
	}
}
