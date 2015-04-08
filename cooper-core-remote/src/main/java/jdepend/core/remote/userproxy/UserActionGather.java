package jdepend.core.remote.userproxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jdepend.core.remote.session.RemoteSessionProxy;
import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.domain.PersistentBean;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogItem;
import jdepend.framework.log.BusiLogListener;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.LogUtil;
import jdepend.framework.log.Operation;
import jdepend.service.remote.user.UserActionItem;

/**
 * 前端用户行为收集
 * 
 * @author wangdg
 * 
 */
public final class UserActionGather extends PersistentBean implements BusiLogListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8598403950808582282L;

	private Date collectEndTime;

	private boolean isNeedSave = false;

	public final static long collectTimeDiff = 1000 * 60 * 60;

	private static UserActionGather gather;

	private static boolean noConditionGather = true;

	private UserActionGather() {
		super("用户信息前端收集器", "用户信息前端收集器", PropertyConfigurator.DEFAULT_PROPERTY_DIR);
		// 定时触发收集动作
		(new Timer()).schedule(new GatherTask(this), 20000, 600000);
	}

	public static UserActionGather getInstance() {
		if (gather == null) {
			gather = new UserActionGather();
		}
		return gather;
	}

	@Override
	public void onBusiLog(String id, String userName, Operation operation) {
		this.gather();
	}

	public void gather() {
		long currentTime = System.currentTimeMillis();
		if (noConditionGather || this.collectEndTime == null
				|| currentTime - this.collectEndTime.getTime() >= collectTimeDiff) {
			try {
				if (RemoteSessionProxy.getInstance().isValid()) {
					// 收集指定时间段的用户行为信息
					List<BusiLogItem> items;
					if (this.collectEndTime == null) {
						items = BusiLogUtil.getInstance().getAllLogItems();
					} else {
						items = BusiLogUtil.getInstance().getLogItems(this.collectEndTime);
					}
					if (items != null && items.size() > 0) {
						List<UserActionItem> actionItems = new ArrayList<UserActionItem>();
						UserActionItem actionItem;
						String ip = InetAddress.getLocalHost().getHostAddress();
						for (BusiLogItem item : items) {
							actionItem = new UserActionItem(item.username, item.operation, ip, item.createdate);
							actionItems.add(actionItem);
						}
						// 发送用户行为信息
						UserRemoteServiceProxy.getInstance().getUserRemoteService().uploadUserAction(actionItems);
					}
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

	public boolean isNeedSave() {
		return isNeedSave;
	}

	@Override
	public void save() throws IOException {
		if (isNeedSave()) {
			super.save();
			LogUtil.getInstance(UserActionGather.class).systemLog("保存用户行为收集器。。。");
		}
	}

	class GatherTask extends TimerTask {

		private UserActionGather gather;

		public GatherTask(UserActionGather gather) {
			this.gather = gather;
		}

		@Override
		public void run() {
			gather.gather();
		}
	}
}
