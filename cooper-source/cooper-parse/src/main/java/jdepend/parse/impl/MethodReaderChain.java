package jdepend.parse.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.util.ByteSequence;

public class MethodReaderChain {

	private List<MethodReader> readers = new ArrayList<MethodReader>();

	public void addReader(MethodReader reader) {
		readers.add(reader);
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
					for (MethodReader reader : readers) {
						reader.readInfo(info);
					}
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

}
