package jdepend.parse.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.Const;
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
			if (code.length > 0) {
				ByteSequence stream = new ByteSequence(code);
				String info;

				try {
					while (stream.available() > 0) {
						int opcode = code[stream.getIndex()] & 0xff;
						info = Utility.codeToString(stream, obj.getConstantPool(), true);
						for (MethodReader reader : readers) {
							reader.readInfo(opcode, info);
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
}
