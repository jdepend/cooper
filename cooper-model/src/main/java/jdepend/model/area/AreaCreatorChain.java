package jdepend.model.area;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.AreaComponent;
import jdepend.model.result.AnalysisResult;

/**
 * 组件区域创建器
 * 
 * @author user
 * 
 */
public class AreaCreatorChain {

	private List<AreaCreator> creators;

	public AreaCreatorChain() {
		super();
		this.creators = new ArrayList<AreaCreator>();

		this.creators.add(new AreaCreatorWithComponentLayer());
		// this.creators.add(new AreaCreatorWithPathInfo());
		this.creators.add(new AreaCreatorWithInstability());
	}

	public List<AreaComponent> create(AnalysisResult result) {

		int maxCoverCounts = 0;
		int coverCounts = 0;
		int index = 0;
		int theIndex = 0;
		int componentCount = result.getComponents().size();

		for (AreaCreator creator : this.creators) {
			coverCounts = creator.coverCount(result);
			if (coverCounts == componentCount) {
				theIndex = index;
				break;
			} else if (maxCoverCounts < coverCounts) {
				maxCoverCounts = coverCounts;
				theIndex = index;
			}
			index++;
		}
		AreaCreator creator = this.creators.get(theIndex);
		if (creator.areaCount() < 2) {
			return new ArrayList<AreaComponent>();
		} else {
			return creator.create();
		}
	}

}
