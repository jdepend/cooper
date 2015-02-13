package jdepend.model.relationtype;

public class TableRelation extends BaseJavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4281750498692868958L;

	private String tableName;

	public TableRelation(float intensity) {
		super(JavaClassRelationTypeMgr.Table, intensity);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public TableRelation clone(String tableName) {
		TableRelation tableRelation = new TableRelation(this.getIntensity());
		tableRelation.setTableName(tableName);
		return tableRelation;
	}
	
	@Override
	public boolean canAbstraction() {
		return false;
	}

	@Override
	public String toString() {
		return "JavaClassRelationType[intensity=" + getIntensity() + ", name=" + getName() + ",tableName=" + tableName
				+ "]";
	}

}
