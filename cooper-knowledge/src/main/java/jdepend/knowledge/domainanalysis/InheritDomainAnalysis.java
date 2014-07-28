package jdepend.knowledge.domainanalysis;

import java.util.ArrayList;
import java.util.List;

import jdepend.knowledge.AbstractDomainAnalysis;
import jdepend.knowledge.AdviseInfo;
import jdepend.knowledge.StructureCategory;
import jdepend.model.result.AnalysisResult;

public final class InheritDomainAnalysis extends AbstractDomainAnalysis {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3528310465287350860L;
	private final static String DAO = "DAO";
	private final static String ALL = "ALL";

	private int daoTreeMaxDeep = 4;// DAO继承树最大深度

	private int allTreeMinDeep = 3;// 整个继承关系中的最小深度

	public InheritDomainAnalysis() {
		super("继承关系领域分析", "用于对继承树结构进行建议");
	}

	protected AdviseInfo doAdvise(String name, AnalysisResult result) {

		if (name.equalsIgnoreCase(DAO)) {
			Integer[] treeData = createTreeData(result);
			int width = treeData[1];
			int deep = treeData[2];
			if (deep > daoTreeMaxDeep) {
				return new AdviseInfo("DAO继承树深度太大，1、建议通过抽取Util类提取公共代码来避免过深的继承树【优先使用组合而非继承】；2、DAO中可能涉及业务逻辑造成继承树较深，建议对DAO进行分析，移出业务逻辑。");
			}

		} else if (name.equalsIgnoreCase(ALL)) {
			List<Integer[]> treeDatas = createTreeDatas(result);
			boolean isExistDeep = false;
			for (Integer[] treeData : treeDatas) {
				if (treeData[2] > allTreeMinDeep) {
					isExistDeep = true;
					break;
				}
			}
			if (!isExistDeep) {
				return new AdviseInfo("目标代码中整个继承树全部为浅层继承【继承深度小于4】，可能业务分析得不够，或结构过于简单，建议对复杂业务涉及的继承关系进行分析。");
			}
		}
		return null;
	}

	private Integer[] createTreeData(AnalysisResult result) {
		Integer[] treeData = new Integer[0];
		return treeData;
	}

	private List<Integer[]> createTreeDatas(AnalysisResult result) {
		List<Integer[]> treeDatas = new ArrayList<Integer[]>();
		return treeDatas;
	}

	public int getDaoTreeMaxDeep() {
		return daoTreeMaxDeep;
	}

	public void setDaoTreeMaxDeep(int daoTreeMaxDeep) {
		this.daoTreeMaxDeep = daoTreeMaxDeep;
	}

	public int getAllTreeMinDeep() {
		return allTreeMinDeep;
	}

	public void setAllTreeMinDeep(int allTreeMinDeep) {
		this.allTreeMinDeep = allTreeMinDeep;
	}

	@Override
	public StructureCategory getStructureCategory() {
		return StructureCategory.Inherit;
	}

}
