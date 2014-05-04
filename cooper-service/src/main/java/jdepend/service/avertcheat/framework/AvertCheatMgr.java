package jdepend.service.avertcheat.framework;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import jdepend.model.util.ClassSearchUtil;

public final class AvertCheatMgr {

	private static AvertCheatMgr inst = new AvertCheatMgr();

	private List<AvertCheat> avertCheats;

	public AvertCheatMgr() {
		this.init();
	}

	public static AvertCheatMgr getInstance() {
		return inst;
	}

	private void init() {

		avertCheats = new ArrayList<AvertCheat>();
		List<String> avertCheatNames = ClassSearchUtil.getInstance().getSubClassNames(AvertCheat.class.getName());
		for (String avertCheatName : avertCheatNames) {
			try {
				Class avertCheatClass = Class.forName(avertCheatName);
				if (!avertCheatClass.isInterface() && !Modifier.isAbstract(avertCheatClass.getModifiers())) {
					AvertCheat avertCheat = (AvertCheat) avertCheatClass.newInstance();

					if (!avertCheats.contains(avertCheat)) {
						avertCheats.add(avertCheat);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public List<AvertCheat> getAvertCheats() {
		return this.avertCheats;
	}
}
