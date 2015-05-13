package jdepend.parse.impl;

import java.io.IOException;

import jdepend.metadata.Method;

import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.util.ByteSequence;

/**
 * 方法內容读取器
 * 
 * @author user
 * 
 */
public abstract class MethodReader {

	protected Method method;

	public MethodReader(Method method) {
		super();
		this.method = method;
	}

	public final void read(org.apache.bcel.classfile.Method obj) {
		Code codeType = obj.getCode();
		if (codeType != null) {
			byte[] code = codeType.getCode();
			ByteSequence stream = new ByteSequence(code);
			String info;

			try {
				while (stream.available() > 0) {
					info = Utility.codeToString(stream, obj.getConstantPool(), true);
					readInfo(info);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected abstract void readInfo(String info);

}
