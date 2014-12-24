package jdepend.framework.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
	
	public final static int ThreadCount = 4;
	private final static int awaitTerminationTimeOut = 100;
	
	public static ExecutorService getPool(){
		return Executors.newFixedThreadPool(ThreadPool.ThreadCount);
	}
	
	public static void awaitTermination(ExecutorService pool){
		pool.shutdown();
		try {
			boolean loop = true;
			do { // 等待所有任务完成
				loop = !pool.awaitTermination(ThreadPool.awaitTerminationTimeOut, TimeUnit.MILLISECONDS);
			} while (loop);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

}
