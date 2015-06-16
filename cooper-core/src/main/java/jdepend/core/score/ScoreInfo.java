package jdepend.core.score;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScoreInfo implements Serializable, Comparable<ScoreInfo> {

	public String id;

	public String group;

	public String command;

	public Integer lc;
	
	public Integer componentCount;

	public Integer relationCount;
	
	public Float cohesion;

	public Float coupling;

	public Float score;

	public Float distance;

	public Float balance;

	public Float relation;

	public Float encapsulation;

	public Date createDate;

	public Date getCreateDate() {
		return new Date(createDate.getTime()) {
			public String toString() {
				return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(this);
			}
		};
	}

	@Override
	public int compareTo(ScoreInfo o) {
		return o.score.compareTo(this.score);
	}

}
