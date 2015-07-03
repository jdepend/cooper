package jdepend.model.area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private AnalysisResult result;

	public AreaCreatorChain(AnalysisResult result) {
		super();
		this.result = result;
		this.creators = new ArrayList<AreaCreator>();

		Map<String, AreaCreator> allCreators = this.getAllCreators();
		List<String> accordings = result.getRunningContext().getProfileFacade().getAreaComponentProfile()
				.getAccordings();
		for (String according : accordings) {
			this.creators.add(allCreators.get(according));
		}
	}

	private Map<String, AreaCreator> getAllCreators() {
		Map<String, AreaCreator> allCreators = new HashMap<String, AreaCreator>();

		AreaCreator areaCreator;

		areaCreator = new AreaCreatorWithComponentLayer();
		allCreators.put(areaCreator.getName(), areaCreator);

		areaCreator = new AreaCreatorWithInstability();
		allCreators.put(areaCreator.getName(), areaCreator);

		return allCreators;

	}

	public List<AreaComponent> create() {

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
