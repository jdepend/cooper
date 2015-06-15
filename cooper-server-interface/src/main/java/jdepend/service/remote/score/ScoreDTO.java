package jdepend.service.remote.score;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScoreDTO implements Serializable, Comparable<ScoreDTO> {

	private static final long serialVersionUID = -2965218447852638525L;

	public String ip;

	public String user;

	public String group;

	public String command;

	public Integer lc;
	
	public Integer componentCount;

	public Integer relationCount;
	
	public Float cohesion;

	public Float coupling;

	public Float score;

	public Float d;

	public Float balance;

	public Float encapsulation;

	public Float relation;

	public Date createDate;

	// 内部使用的字段

	public String id;

	public Date uploadDate;

	public ScoreDTO() {
		super();
	}

	public ScoreDTO(String ip, String user, String group, String command, Integer lc, Float score, Float d,
			Float balance, Float encapsulation, Float relation, Date createDate) {
		super();
		this.ip = ip;
		this.user = user;
		this.group = group;
		this.command = command;
		this.lc = lc;
		this.score = score;
		this.d = d;
		this.balance = balance;
		this.encapsulation = encapsulation;
		this.relation = relation;
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return new Date(createDate.getTime()) {
			public String toString() {
				return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(this);
			}
		};
	}

	public Date getUploadDate() {
		return new Date(uploadDate.getTime()) {
			public String toString() {
				return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(this);
			}
		};
	}

	@Override
	public int compareTo(ScoreDTO o) {
		return o.score.compareTo(this.score);
	}
}
