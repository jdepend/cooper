package jdepend.parse.impl;

import org.apache.bcel.Const;

import jdepend.metadata.InvokeItem;
import jdepend.metadata.JavaPackage;
import jdepend.metadata.LocalInvokeItem;
import jdepend.metadata.Method;

/**
 * 普通方法读取器
 * 
 * @author user
 * 
 */
public class GeneralMethodReader extends MethodReader {

	private PackageFilter filter;

	public GeneralMethodReader(Method method, PackageFilter filter) {
		super(method);
		this.filter = filter;
	}

	@Override
	protected void readInfo(int opcode, String info) {

		String[] infos;
		int pos;
		int pos2;

		String calledPlace;
		String calledName;
		String calledPackageName;
		String calledMethod;
		String calledMethodSignature;

		String callType;
		int index;
		String fieldName;

		if (opcode == Const.INVOKEVIRTUAL || opcode == Const.INVOKESPECIAL || opcode == Const.INVOKENONVIRTUAL
				|| opcode == Const.INVOKESTATIC || opcode == Const.INVOKEINTERFACE || opcode == Const.INVOKEDYNAMIC) {
			infos = info.split("\\s+");
			if (infos.length > 1) {
				pos = infos[1].lastIndexOf('.');
				if (pos != -1) {
					callType = infos[0].substring(6);
					calledName = infos[1].substring(0, pos);
					pos2 = infos[1].indexOf(':');
					calledMethod = infos[1].substring(pos + 1, pos2);
					calledMethodSignature = infos[1].substring(pos2 + 1);
					calledPlace = method.getJavaClass().getPlace();
					// 得到包名
					index = calledName.lastIndexOf('.');
					if (index > 0) {
						calledPackageName = calledName.substring(0, index);
					} else {
						calledPackageName = JavaPackage.Default;
					}
					if (filter.accept(calledPackageName) && filter.acceptClass(calledName)) {
						InvokeItem item = new LocalInvokeItem(callType, calledPlace, calledName, calledMethod,
								calledMethodSignature);
						method.addInvokeItem(item);
					}
				}
			}
		} else if (opcode == Const.GETFIELD) {
			fieldName = this.getFieldName(info);
			if (fieldName != null) {
				method.addReadField(fieldName);
			}
		} else if (opcode == Const.PUTFIELD) {
			fieldName = this.getFieldName(info);
			if (fieldName != null) {
				method.addWriteField(fieldName);
			}
		}

	}

	private String getFieldName(String info) {
		String[] infos = info.split("\\s+");
		if (infos.length > 1) {
			int pos = infos[1].lastIndexOf('.');
			if (pos != -1) {
				return infos[1].substring(pos + 1, infos[1].indexOf(':'));
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
