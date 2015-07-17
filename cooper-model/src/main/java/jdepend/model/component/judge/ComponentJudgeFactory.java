package jdepend.model.component.judge;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.ComponentException;

public class ComponentJudgeFactory {

	private JudgeConfigure conf;

	public ComponentJudgeFactory(String group, String command) throws ComponentException {
		super();
		this.conf = new ComponentJudgeRepo(group, command).getConfigure();
	}

	public List<ComponentJudge> getJudges() {

		List<ComponentJudge> judges = new ArrayList<ComponentJudge>();

		if (conf.applyChildren) {
			ChildrenComponentJudge childrenComponentJudge = new ChildrenComponentJudge();
			for (String key : conf.childrenKeys) {
				childrenComponentJudge.addChildrenKey(key);
			}
			judges.add(childrenComponentJudge);
		}

		if (conf.applyLayer) {
			judges.add(new LayerComponentJudge(conf.layer));
		}

		if (judges.size() == 0) {
			judges.add(new WisdomLayerComponentJudge());
		}

		return judges;

	}

}
