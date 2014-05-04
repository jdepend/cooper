package jdepend.report;

/**
 * ReportRenderFactory
 * 
 * @author <b>Abner</b>
 * 
 */
public interface ReportCreatorFactory {

	public ReportCreator create(String group, String command);

}
