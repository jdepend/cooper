package jdepend.util.refactor;

public final class RefactorToolFactory {

	public static RefactorTool createTool() {
		return new DefaultRefactorTool();
	}
}
