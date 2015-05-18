package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.model.result.AnalysisResult;

public class TODOListIdentifyerFacade {

	private List<TODOItem> list;

	private List<TODOIdentifyer> identifyers;

	public TODOListIdentifyerFacade() {

		identifyers = new ArrayList<TODOIdentifyer>();

		identifyers.add(new MoveJavaClassTODOIdentifyer());
		identifyers.add(new UniteComponentTODOIdentifyer());
		identifyers.add(new SplitComponentTODOIdentifyer());
		identifyers.add(new AdjustAbstractTODOIdentifyer());

	}

	public List<TODOItem> identify(AnalysisResult result) throws JDependException {

		list = new ArrayList<TODOItem>();

		TODOIdentifyInfo info = new TODOIdentifyInfo(result);

		for (TODOIdentifyer identifyer : identifyers) {
			long start = System.currentTimeMillis();
			list.addAll(identifyer.identify(info));
			LogUtil.getInstance(TODOListIdentifyerFacade.class).systemLog(
					"identifyMoveJavaClass [" + (System.currentTimeMillis() - start) + "]");
		}
		// 按Order排序
		Collections.sort(list);
		return list;
	}

}
