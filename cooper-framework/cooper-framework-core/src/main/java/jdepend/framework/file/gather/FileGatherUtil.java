package jdepend.framework.file.gather;

import java.io.File;
import java.util.List;

/**
 * 文件收集器接口
 * 
 * @author user
 *
 */
public interface FileGatherUtil {

	public TargetFiles gather();

	public void setAcceptFile(AcceptFile acceptFile);

	public void setDirectories(List<File> directories);

}