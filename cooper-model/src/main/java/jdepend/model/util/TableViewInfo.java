package jdepend.model.util;

public class TableViewInfo implements Comparable<TableViewInfo> {

	public String name;
	public Integer count;
	public String type;
	public String javaClass;
	public String Component;

	public TableViewInfo(String name, String type, String javaClass, String Component) {
		super();
		this.name = name;
		this.type = type;
		this.javaClass = javaClass;
		this.Component = Component;
	}

	@Override
	public int compareTo(TableViewInfo o) {
		int rtn;
		rtn = o.count.compareTo(this.count);
		if (rtn != 0) {
			return rtn;
		} else {
			rtn = this.name.compareTo(o.name);
			if (rtn != 0) {
				return rtn;
			} else {
				rtn = this.type.compareTo(o.type);
				if (rtn != 0) {
					return rtn;
				} else {
					rtn = this.Component.compareTo(o.Component);
					if (rtn != 0) {
						return rtn;
					} else {
						rtn = this.javaClass.compareTo(o.javaClass);
						if (rtn != 0) {
							return rtn;
						} else {
							return 0;
						}
					}
				}
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((javaClass == null) ? 0 : javaClass.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		TableViewInfo other = (TableViewInfo) obj;

		if (javaClass == null) {
			if (other.javaClass != null)
				return false;
		} else if (!javaClass.equals(other.javaClass))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
