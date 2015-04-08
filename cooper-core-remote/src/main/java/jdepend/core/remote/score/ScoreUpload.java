package jdepend.core.remote.score;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jdepend.core.framework.serverconf.ServerConfigurator;
import jdepend.core.local.score.ScoreInfo;
import jdepend.core.local.score.ScoreRepository;
import jdepend.core.remote.session.RemoteSessionProxy;
import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.domain.PersistentBean;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.service.remote.score.ScoreDTO;
import jdepend.service.remote.score.ScoreRemoteService;

/**
 * 分数上传
 * 
 * @author wangdg
 * 
 */
public class ScoreUpload extends PersistentBean {

	private static final long serialVersionUID = -7772443906780252903L;

	private static final String DEFAULT_SCORE_SERVICE = "rmi://localhost/ScoreRemoteService";

	private Date collectEndTime;

	private boolean isNeedSave = false;

	public final static long collectTimeDiff = 1000 * 60 * 60;
	// public final static long collectTimeDiff = 0;

	private static ScoreUpload uploader;

	private transient Timer timer = new Timer();

	public static ScoreUpload getInstance() {
		if (uploader == null) {
			uploader = new ScoreUpload();
		}
		return uploader;
	}

	public ScoreUpload() {
		super("分数前端收集器", "分数前端收集器", PropertyConfigurator.DEFAULT_PROPERTY_DIR);
	}

	public void start() {
		// 定时触发分数传递任务
		timer.schedule(new UploadScoreTask(this), 20000, 600000);
	}

	public void stop() {
		timer.cancel();
	}

	public void upload() {
		long currentTime = System.currentTimeMillis();
		if (this.collectEndTime == null || currentTime - this.collectEndTime.getTime() >= collectTimeDiff) {
			try {
				// 收集指定时间段的分数信息
				List<ScoreInfo> items = new ArrayList<ScoreInfo>();

				if (this.collectEndTime == null) {
					items = ScoreRepository.getScoreList();
				} else {
					items = ScoreRepository.getScoreList(this.collectEndTime);
				}
				if (items != null && items.size() > 0) {
					List<ScoreDTO> uploadItems = new ArrayList<ScoreDTO>();
					ScoreDTO uploadItem;
					String ip = InetAddress.getLocalHost().getHostAddress();
					String user = null;
					if (RemoteSessionProxy.getInstance().isValid()) {
						user = RemoteSessionProxy.getInstance().getUserName();
					} else {
						user = InetAddress.getLocalHost().getHostName();
					}
					for (ScoreInfo item : items) {
						uploadItem = new ScoreDTO(ip, user, item.group, item.command, item.lc, item.score, item.d,
								item.balance, item.relation, item.relation, item.createDate);
						uploadItems.add(uploadItem);
					}
					// 发送分数信息
					this.getScoreRemoteService().uploadScore(uploadItems);
				}
				// 重置collectEndTime
				if (this.collectEndTime == null) {
					this.collectEndTime = new Date(currentTime);
				} else {
					this.collectEndTime.setTime(currentTime);
				}
				isNeedSave = true;
			} catch (JDependException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public Date getCollectEndTime() {
		return collectEndTime;
	}

	public void setCollectEndTime(Date collectEndTime) {
		this.collectEndTime = collectEndTime;
	}

	public void setNeedSave(boolean isNeedSave) {
		this.isNeedSave = isNeedSave;
	}

	public boolean isNeedSave() {
		return isNeedSave;
	}

	@Override
	public void save() throws IOException {
		if (isNeedSave()) {
			super.save();
			LogUtil.getInstance(ScoreUpload.class).systemLog("保存分数收集器。。。");
		}
	}

	public ScoreRemoteService getScoreRemoteService() throws JDependException {
		try {
			return (ScoreRemoteService) Naming.lookup(getScoreRemoteServiceURL());
		} catch (ConnectException e) {
			e.printStackTrace();
			throw new JDependException("连接服务器失败！", e);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new JDependException("URL地址错误！", e);
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new JDependException("远程服务失败！", e);
		} catch (NotBoundException e) {
			e.printStackTrace();
			throw new JDependException("没有查询到绑定服务！", e);
		}

	}

	private String getScoreRemoteServiceURL() {
		String scoreRemoteServiceURL = (new ServerConfigurator()).getScoreRemoteServiceURL();
		if (scoreRemoteServiceURL == null) {
			scoreRemoteServiceURL = DEFAULT_SCORE_SERVICE;
		}
		return scoreRemoteServiceURL;
	}

	class UploadScoreTask extends TimerTask {

		private ScoreUpload uploader;

		public UploadScoreTask(ScoreUpload uploader) {
			this.uploader = uploader;
		}

		@Override
		public void run() {
			uploader.upload();
		}
	}

}
