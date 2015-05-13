package jdepend.parse.impl;

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
	protected void readInfo(String info) {

		String[] infos;
		int pos;
		String calledPlace;
		String calledName;
		String calledPackageName;
		String calledMethod;
		String callType;
		int index;
		String fieldName;

		if (info.startsWith("invoke")) {
			infos = info.split("\\s+");
			if (infos.length > 1) {
				pos = infos[1].lastIndexOf('.');
				if (pos != -1) {
					callType = infos[0].substring(6);
					calledName = infos[1].substring(0, pos);
					calledMethod = infos[1].substring(pos + 1);
					calledPlace = method.getJavaClass().getPlace();
					// 得到包名
					index = calledName.lastIndexOf(".");
					if (index > 0) {
						calledPackageName = calledName.substring(0, index);
					} else {
						calledPackageName = JavaPackage.Default;
					}
					if (filter.accept(calledPackageName) && filter.acceptClass(calledName)) {
						InvokeItem item = new LocalInvokeItem(callType, calledPlace, calledName, calledMethod, infos[2]);
						method.addInvokeItem(item);
					}
				}
			}
		} else if (info.startsWith("getfield")) {
			fieldName = this.getFieldName(info);
			if (fieldName != null) {
				method.addReadField(fieldName);
			}
		} else if (info.startsWith("putfield")) {
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
				return infos[1].substring(pos + 1);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
