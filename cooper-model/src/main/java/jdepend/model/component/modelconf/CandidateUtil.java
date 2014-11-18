package jdepend.model.component.modelconf;

public class CandidateUtil {

	private final static String IDDecollator = "#";

	public final static String getId(String place, String name) {
		return place + IDDecollator + name;
	}

	public final static String getId(Candidate candidate) {
		return getId(candidate.getPlace(), candidate.getName());
	}

	public final static String getPlace(String id) {
		return id.substring(0, id.indexOf(IDDecollator));
	}

	public final static String getName(String id) {
		return id.substring(id.indexOf(IDDecollator) + 1);
	}

}
