package jdepend.framework.file.gather;

public class FileGatherUtilFactory {

	public FileGatherUtil createFileGatherUtil() {
		return new ConcurrentFileGatherUtil();
	}

}
