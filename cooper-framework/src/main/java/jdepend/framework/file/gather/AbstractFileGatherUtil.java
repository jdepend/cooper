package jdepend.framework.file.gather;

import java.io.File;
import java.util.List;

abstract class AbstractFileGatherUtil implements FileGatherUtil {

	private AcceptFile acceptFile;

	private List<File> directories;// 搜索的文件路径集合

	@Override
	public void setAcceptFile(AcceptFile acceptFile) {
		this.acceptFile = acceptFile;
	}

	protected boolean acceptFile(File file) {
		return acceptFile.acceptFile(file);
	}

	public List<File> getDirectories() {
		return directories;
	}
	
	@Override
	public void setDirectories(List<File> directories) {
		this.directories = directories;
	}

}
