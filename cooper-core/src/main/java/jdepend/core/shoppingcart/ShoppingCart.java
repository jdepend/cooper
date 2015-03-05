package jdepend.core.shoppingcart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.result.AnalysisResult;

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

	public void addProduct(AnalysisResult result) throws JDependException {
		if (this.products.size() < 9) {
			Product product = new Product(result);
			if (!this.products.contains(product)) {
				this.products.add(product);
			} else {
				throw new JDependException("该分析结果已加入购物车");
			}
		} else {
			throw new JDependException("购物车中最多只能添加10个分析结果");
		}
	}

	public Product getTheProduct(Date createDate) {
		for (Product product : this.products) {
			if (product.getCreateDate().equals(createDate)) {
				return product;
			}
		}
		return null;
	}

	public boolean isEmpty() {
		return this.products.size() == 0;
	}

	public void clear() {
		this.products = new ArrayList<Product>();
	}
}
