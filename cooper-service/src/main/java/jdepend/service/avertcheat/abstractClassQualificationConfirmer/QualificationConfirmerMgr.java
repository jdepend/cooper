package jdepend.service.avertcheat.abstractClassQualificationConfirmer;

public class QualificationConfirmerMgr {

	private static QualificationConfirmerMgr mgr = new QualificationConfirmerMgr();

	private QualificationConfirmer confirmer = new DefaultQualificationConfirmer();

	private QualificationConfirmerMgr() {
	}

	public static QualificationConfirmerMgr getIntance() {
		return mgr;
	}

	public void registQualificationConfirmer(QualificationConfirmer confirmer) {
		this.confirmer = confirmer;
	}

	public QualificationConfirmer getConfirmer() {
		return confirmer;
	}

}
