package jdepend.framework.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import jdepend.framework.context.JDependContext;

import org.apache.commons.beanutils.BeanUtils;

public abstract class PersistentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6606835123757981876L;

	private String name;

	private String tip;

	private String path;

	private transient boolean isExist;

	public PersistentBean() {

	}

	private PersistentBean(String path) {
		this.init(path);
	}

	public PersistentBean(String name, String tip, String path) {
		this(path);

		this.name = name;
		this.tip = tip;
	}

	protected void init(String path) {
		this.initPath(path);
		this.loadData();
	}

	protected void initPath(String path) {
		// 将数据保存到workspace中
		String workspacePath = JDependContext.getWorkspacePath();
		if (workspacePath != null) {
			if (path != null) {
				this.path = JDependContext.getWorkspacePath() + "\\" + path;
			} else {
				this.path = JDependContext.getWorkspacePath();
			}
		} else {
			this.path = path;
		}
	}

	protected void loadData() {

		File file = new File(getPersistentFileName());
		FileInputStream in = null;
		ObjectInputStream s = null;

		boolean serialVersionUIDFailure = false;
		try {
			if (file.exists()) {
				in = new FileInputStream(file);
				s = new ObjectInputStream(in);
				BeanUtils.copyProperties(this, s.readObject());
				this.isExist = true;
			} else {
				this.isExist = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			serialVersionUIDFailure = true;
		} finally {
			try {
				if (s != null)
					s.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (serialVersionUIDFailure) {
			file.delete();
		}
	}

	public boolean isExist() {
		return isExist;
	}

	public void save() throws IOException {

		FileOutputStream out = null;
		ObjectOutputStream s = null;

		try {
			File file = new File(getPersistentFileName());
			if (!file.exists()) {
				if (file.getParent() != null) {
					new File(file.getParent()).mkdirs();
				}
				file.createNewFile();
			}
			out = new FileOutputStream(file);
			s = new ObjectOutputStream(out);
			s.writeObject(this);
			this.isExist = true;
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private String getPersistentFileName() {
		if (this.path == null || this.path.length() == 0) {
			return this.getClass().getName();
		} else {
			return this.path + "/" + this.getClass().getName();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PersistentBean other = (PersistentBean) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getTip() {
		return tip;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
