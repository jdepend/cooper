package jdepend.parse.impl;

import java.io.IOException;

import jdepend.model.Method;

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

			if (this.method.getJavaClass().getName()
					.equals("com.neusoft.saca.snap.engine.api.internal.NotificationController")
					&& this.method
							.getInfo()
							.equals("public java.util.Map createNotification(com.neusoft.saca.snap.engine.api.internal.dto.NotificationDto notificationDto) [Signature((Lcom/neusoft/saca/snap/engine/api/internal/dto/NotificationDto;)Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)] [RuntimeVisibleAnnotations] [RuntimeVisibleParameterAnnotations]")) {
				System.out.print("");
			}

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
