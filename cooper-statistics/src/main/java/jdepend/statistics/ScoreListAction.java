package jdepend.statistics;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.AsynAction;
import jdepend.framework.ui.JDependFrame;

public abstract class ScoreListAction extends AsynAction {

	protected ScoreCollection scoreCollection;

	public ScoreListAction(JDependFrame frame, String name) {
		super(frame, name);
		this.scoreCollection = ScoreCollection.getInstance();
	}

	@Override
	protected int getProcess() throws JDependException {
		return scoreCollection.getScoreInfos().size();
	}

}
