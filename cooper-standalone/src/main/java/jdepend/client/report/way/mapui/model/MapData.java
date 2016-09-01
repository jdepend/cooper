package jdepend.client.report.way.mapui.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.model.Element;
import jdepend.model.Relation;
import jdepend.client.report.way.mapui.layout.specifiedposition.CommandSpecifiedPosition;
import jdepend.client.report.way.mapui.layout.specifiedposition.SpecifiedNodePosition;
import jdepend.client.report.way.mapui.layout.specifiedposition.SpecifiedPositionMgr;

public class MapData {

	private String group;

	private String command;

	private Collection<Element> elements;

	private Map<String, Element> elementForNames;

	private Collection<Relation> relations;

	public MapData(String group, String command, Collection<Relation> relations) {
		super();
		this.group = group;
		this.command = command;
		this.relations = relations;

		elements = Relation.calElements(relations);
		elementForNames = new HashMap<String, Element>();
		for (Element element : elements) {
			elementForNames.put(element.getName(), element);
		}
	}

	public Element getTheElement(String name) {
		return this.elementForNames.get(name);
	}

	public Collection<Relation> getRelations() {
		return relations;
	}

	public Collection<Element> getElements() {
		return elements;
	}

	public String getGroup() {
		return group;
	}

	public String getCommand() {
		return command;
	}

	public boolean isHaveSpecifiedPosition() {
		if (group != null && command != null) {
			CommandSpecifiedPosition commandSpecifiedPosition = SpecifiedPositionMgr.getInstance()
					.getTheCommandSpecifiedPosition(group, command);
			if (commandSpecifiedPosition != null
					&& commandSpecifiedPosition.getNodePositions().size() == elements.size()) {
				return true;
			}
		}
		return false;
	}

	public double getX(String elementName) {
		CommandSpecifiedPosition commandSpecifiedPosition = SpecifiedPositionMgr.getInstance()
				.getTheCommandSpecifiedPosition(group, command);

		for (SpecifiedNodePosition specifiedNodePosition : commandSpecifiedPosition.getNodePositions()) {
			if (specifiedNodePosition.getName().equals(elementName)) {
				return specifiedNodePosition.getX();
			}
		}
		return 0D;

	}

	public double getY(String elementName) {
		CommandSpecifiedPosition commandSpecifiedPosition = SpecifiedPositionMgr.getInstance()
				.getTheCommandSpecifiedPosition(group, command);

		for (SpecifiedNodePosition specifiedNodePosition : commandSpecifiedPosition.getNodePositions()) {
			if (specifiedNodePosition.getName().equals(elementName)) {
				return specifiedNodePosition.getY();
			}
		}
		return 0D;
	}

}
