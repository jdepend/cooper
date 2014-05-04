package jdepend.parse.impl;

public class Constant {

	private byte _tag;

	private int _nameIndex;

	private int _typeIndex;

	private Object _value;

	Constant(byte tag, int nameIndex) {
		this(tag, nameIndex, -1);
	}

	Constant(byte tag, Object value) {
		this(tag, -1, -1);
		_value = value;
	}

	Constant(byte tag, int nameIndex, int typeIndex) {
		_tag = tag;
		_nameIndex = nameIndex;
		_typeIndex = typeIndex;
		_value = null;
	}

	public byte getTag() {
		return _tag;
	}

	public int getNameIndex() {
		return _nameIndex;
	}

	int getTypeIndex() {
		return _typeIndex;
	}

	public Object getValue() {
		return _value;
	}

	public String toString() {

		StringBuilder s = new StringBuilder("");

		s.append("tag: " + getTag());

		if (getNameIndex() > -1) {
			s.append(" nameIndex: " + getNameIndex());
		}

		if (getTypeIndex() > -1) {
			s.append(" typeIndex: " + getTypeIndex());
		}

		if (getValue() != null) {
			s.append(" value: " + getValue());
		}

		return s.toString();
	}
}
