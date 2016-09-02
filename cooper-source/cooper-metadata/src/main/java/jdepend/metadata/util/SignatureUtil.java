package jdepend.metadata.util;

import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.Signature;

public final class SignatureUtil {

	/**
	 * 获得范型中的类型
	 * 
	 * @param obj
	 * @return
	 */
	public static String getSignature(FieldOrMethod obj) {
		for (Attribute attribute : obj.getAttributes()) {
			if (attribute instanceof Signature) {
				return ((Signature) attribute).getSignature();
			}
		}
		return obj.getSignature();
	}

}
