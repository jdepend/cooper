package jdepend.framework.file.gather;

import java.io.File;
import java.util.List;

public interface FileGatherUtil {

	public TargetFiles gather();

	public void setAcceptFile(AcceptFile acceptFile);

	public void setDirectories(List<File> directories);

}