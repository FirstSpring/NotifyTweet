/*
 * © 2013 Margherita Works.
 * 
 */
package mw.notifytweet.src;

import java.awt.TrayIcon;

import mw.notifytweet.src.manager.AccountManager;
import mw.notifytweet.src.manager.NTConfigManager;
import twitter4j.ConnectionLifeCycleListener;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserStreamAdapter;
import twitter4j.auth.AccessToken;

/**
 * 新着ステータス,被お気に入りイベントを処理するクラスです.<br />
 * UserStreamControllerを継承しActionListenerを実装しています.
 * 
 */
public class NTListener extends UserStreamAdapter implements
		ConnectionLifeCycleListener {

	/** インスタンス. */
	private static NTListener instance = new NTListener();

	private TwitterStream stream;

	/**
	 * コンストラクタ.
	 */
	private NTListener() {
		// TODO 初期処理
	}

	/**
	 * 新着ステータス.
	 * 
	 * @param status
	 *            Status:ステータス
	 */
	public void onStatus(Status status) {
		NTConfigManager cfg = NotifyTweet.getInstance().getConfigManager();
		if (cfg.isEnablePlugin()) {
			if (!cfg.isFromMe()) {
				if (AccountManager.getInstance().selected.equals(status
						.getUser().getId())) {
					return;
				}
			}
			for (int i = 0; i < EnumData.POPUP_QUA.getInt(); i++) {
				if (!NotifyTweet.getInstance().getPopupManager().getVisible(i)) {
					NotifyTweet.getInstance().getPopupManager()
							.setVisible(i, true, status);
					break;
				}
			}
			if ((!NotifyTweet.getInstance().getClipNormal().isActive())
					&& (NotifyTweet.getInstance().getConfigManager()
							.isEnableNormalSE())) {
				NotifyTweet.getInstance().getClipNormal().stop();
				NotifyTweet.getInstance().getClipNormal()
						.setMicrosecondPosition(0L);
				NotifyTweet.getInstance().getClipNormal().start();
			}
		}
	}

	@Override
	public void onFavorite(User source, User target, Status status) {
		NTConfigManager cfg = NotifyTweet.getInstance().getConfigManager();
		if (cfg.isEnableFav()) {
			if (!cfg.isFromMe()) {
				if (AccountManager.getInstance().selected.equals(status
						.getUser().getId())) {
					return;
				}
			}
			for (int i = 0; i < EnumData.POPUP_QUA.getInt(); i++) {
				if (!NotifyTweet.getInstance().getPopupManager().getVisible(i)) {
					NotifyTweet.getInstance().getPopupManager()
							.setVisibleOnFav(i, true, source, target, status);
					break;
				}
			}
			if ((!NotifyTweet.getInstance().getClipFav().isActive())
					&& (NotifyTweet.getInstance().getConfigManager()
							.isEnableNormalSE())) {
				NotifyTweet.getInstance().getClipFav().stop();
				NotifyTweet.getInstance().getClipFav()
						.setMicrosecondPosition(0L);
				NotifyTweet.getInstance().getClipFav().start();
			}
		}
	}

	/**
	 * インスタンスを返す.
	 * 
	 * @return インスタンス
	 */
	public static NTListener getInstance() {
		return instance;
	}

	public void connect() {
		if (stream != null) {
			stream.shutdown();
			stream = null;
		}
		AccountManager m = AccountManager.getInstance();
		AccessToken token = m.get(m.selected);
		if (token != null) {
			stream = new TwitterStreamFactory().getInstance();
			stream.setOAuthConsumer(AccountManager.KEY, AccountManager.SECRET);
			stream.setOAuthAccessToken(token);
			stream.addListener(NTListener.getInstance());
			stream.addConnectionLifeCycleListener(this);
			stream.user();
		}
	}

	@Override
	public void onCleanUp() {

	}

	@Override
	public void onConnect() {
		NotifyTweet
				.getInstance()
				.getTrayIcon()
				.displayMessage("NotifyTweet", "接続しました",
						TrayIcon.MessageType.INFO);
		NotifyTweet.getInstance().getClipNormal().stop();
		NotifyTweet.getInstance().getClipNormal().setMicrosecondPosition(0L);
		NotifyTweet.getInstance().getClipNormal().start();
	}

	@Override
	public void onDisconnect() {

	}
}
