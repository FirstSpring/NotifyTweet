package mw.notifytweet.src.manager;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

import mw.notifytweet.src.EnumData;
import mw.notifytweet.src.NTTimer;
import mw.notifytweet.src.NotifyTweet;
import mw.notifytweet.src.NotifyTweetException;
import mw.notifytweet.src.component.JPopup;
import twitter4j.Status;
import twitter4j.User;

/**
 * ポップアップを管理するクラス.
 * 
 * @author TAISEI
 * 
 */
public enum PopupManager {
	INSTANCE;

	/** ポップアップ本体. */
	private JPopup[] popups;

	/**
	 * コンストラクタ.
	 */
	private PopupManager() {
		int qua = EnumData.POPUP_QUA.getInt();
		popups = new JPopup[qua];
		for (int i = 0; i < qua; i++) {
			popups[i] = new JPopup(i);
		}
	}

	/**
	 * ポップアップの表示状態を変更する.
	 * 
	 * @param index
	 *            int:ポップアップ番号
	 * @param visible
	 *            boolean:表示する
	 * @param status
	 *            Status:ステータス
	 */
	public final void setVisible(int index, boolean visible, Status status) {
		if (visible) {
			popups[index].setLocation(getX(), getY(index));
			try {
				if (status.isRetweet()) {
					popups[index].setIconNormal(IconCache.getInstance()
							.getIcon(
									new URL(status.getRetweetedStatus()
											.getUser().getProfileImageURL())));
					popups[index].setIconMini(new ImageIcon(ImageIO
							.read(new URL(status.getUser()
									.getMiniProfileImageURL()))));
					popups[index].setTextUser("@"
							+ status.getRetweetedStatus().getUser()
									.getScreenName() + " / "
							+ status.getRetweetedStatus().getUser().getName()
							+ " :");
					popups[index].setTextText(status.getRetweetedStatus()
							.getText());
					popups[index].setPanelBackground(NotifyTweet.getInstance()
							.getConfigManager().getColorRT());
				} else {
					popups[index].setIconNormal(IconCache.getInstance()
							.getIcon(
									new URL(status.getUser()
											.getProfileImageURL())));
					popups[index].setIconMini(null);
					popups[index].setTextUser("@"
							+ status.getUser().getScreenName() + " / "
							+ status.getUser().getName() + " : ");
					popups[index].setTextText(status.getText());
					popups[index].setPanelBackground(NotifyTweet.getInstance()
							.getConfigManager().getColorNormal());
				}
			} catch (IOException e) {
				throw new NotifyTweetException("アイコン画像を取得できません");
			}

			NTTimer nttimer = new NTTimer(index);
			Timer timer = new Timer(NotifyTweet.getInstance()
					.getConfigManager().getTimePopup(), nttimer);
			nttimer.setTimer(timer);
			timer.start();
		}
		try {
			popups[index].setVisible(visible);
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new NotifyTweetException("存在しないダイアログ番号が指定されました");
		}
	}

	/**
	 * ポップアップの表示状態を変更する（ふぁぼ用）.
	 * 
	 * @param index
	 *            int:ポップアップ番号
	 * @param visible
	 *            boolean:表示する
	 * @param from
	 *            User:ふぁぼったユーザー
	 * @param to
	 *            User:ふぁぼられたユーザー
	 * @param status
	 *            Status:ステータス
	 */
	public final void setVisibleOnFav(int index, boolean visible, User from,
			User to, Status status) {
		if (visible) {
			popups[index].setLocation(getX(), getY(index));
			try {
				popups[index].setIconNormal(IconCache.getInstance().getIcon(
						new URL(to.getProfileImageURL())));
				popups[index].setIconMini(new ImageIcon(ImageIO.read(new URL(
						from.getMiniProfileImageURL()))));
				popups[index].setTextUser("@" + to.getScreenName() + " /"
						+ to.getName() + " :");
				popups[index].setTextText(status.getText());
				popups[index].setPanelBackground(NotifyTweet.getInstance()
						.getConfigManager().getColorFav());
			} catch (IOException e) {
				throw new NotifyTweetException("アイコン画像を取得できません");
			}

			NTTimer nttimer = new NTTimer(index);
			Timer timer = new Timer(NotifyTweet.getInstance()
					.getConfigManager().getTimePopup(), nttimer);
			nttimer.setTimer(timer);
			timer.start();
		}
		try {
			popups[index].setVisible(visible);
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new NotifyTweetException("存在しないダイアログ番号が指定されました");
		}
	}

	/**
	 * プレビュー用のポップアップ可視設定.
	 * 
	 * @param index
	 *            ポップアップ番号
	 * @param url
	 *            アイコンURL
	 * @param name
	 *            ユーザー名
	 * @param names
	 *            表示名
	 * @param text
	 *            テキスト
	 * @param background
	 *            背景色
	 * @param visible
	 *            可視性
	 */
	public final void setVisible(int index, URL url, String name, String names,
			String text, Color background, boolean visible) {
		if (visible) {
			popups[index].setLocation(getX(), getY(index));
			popups[index].setIconNormal(IconCache.getInstance().getIcon(url));
			popups[index].setIconMini(null);
			popups[index].setTextUser("@" + name + " / " + names + " :");
			popups[index].setTextText(text);
			popups[index].setPanelBackground(background);

			NTTimer nttimer = new NTTimer(index);
			Timer timer = new Timer(NotifyTweet.getInstance()
					.getConfigManager().getTimePopup(), nttimer);
			nttimer.setTimer(timer);
			timer.setRepeats(false);
			timer.restart();
		}
		try {
			popups[index].setVisible(visible);
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new NotifyTweetException("存在しないダイアログ番号が指定されました");
		}
	}

	/**
	 * ポップアップが表示されているかを取得.
	 * 
	 * @param index
	 *            ポップアップ番号
	 * @return boolean:ポップアップが表示されている
	 */
	public boolean getVisible(int index) {
		return popups[index].isVisible();
	}

	/**
	 * ポップアップのY方向の表示座標を取得.
	 * 
	 * @return int:表示座標
	 */
	private int getX() {
		int x = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds().width;
		int pos = NotifyTweet.getInstance().getConfigManager()
				.getPopupPosition();

		if ((pos == EnumData.TOP_LEFT.getInt())
				|| (pos == EnumData.BOTTOM_LEFT.getInt())) {
			return EnumData.POPUP_SPACE_X.getInt();
		}

		if ((pos == EnumData.TOP_RIGHT.getInt())
				|| (pos == EnumData.BOTTOM_RIGHT.getInt())) {
			return x
					- (EnumData.POPUP_WIDTH.getInt() + EnumData.POPUP_SPACE_X
							.getInt());
		}
		return EnumData.POPUP_SPACE_X.getInt();
	}

	/**
	 * ポップアップのX方向の表示座標を取得.
	 * 
	 * @param index
	 *            ポップアップ番号
	 * @return int:表示座標
	 */
	private int getY(int index) {
		int y = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds().height;
		int pos = NotifyTweet.getInstance().getConfigManager()
				.getPopupPosition();

		if ((pos == EnumData.TOP_LEFT.getInt())
				|| (pos == EnumData.TOP_RIGHT.getInt())) {
			/* ポップアップが上のとき */
			return (EnumData
					.plus(EnumData.POPUP_HEIGHT, EnumData.POPUP_SPACE_Y))
					* index + EnumData.POPUP_SPACE_Y.getInt();
		}

		/*  */
		if ((pos == EnumData.BOTTOM_RIGHT.getInt())
				|| (pos == EnumData.BOTTOM_LEFT.getInt())) {
			/* ポップアップが下のとき */
			return y
					- ((index * EnumData.POPUP_HEIGHT.getInt()) + (EnumData.POPUP_HEIGHT
							.getInt() + EnumData.POPUP_SPACE_Y.getInt() + index
							* EnumData.POPUP_SPACE_Y.getInt()));
		}
		return EnumData.POPUP_SPACE_Y.getInt();
	}

	/**
	 * フォントを再設定.
	 */
	public void resetFont() {
		for (int i = 0; i < EnumData.POPUP_QUA.getInt(); ++i) {
			popups[i].setFontUser(NotifyTweet.getInstance().getConfigManager()
					.getFontName());
			popups[i].setFontText(NotifyTweet.getInstance().getConfigManager()
					.getFontText());
		}
	}

	/**
	 * ポップアップがピン留めされているかを返す.
	 * 
	 * @param i
	 *            ポップアップ番号
	 * @return boolean:ピン留めされている
	 */
	public boolean isPinDialog(int i) {
		return popups[i].isPin();
	}

	/**
	 * ポップアップのピン留め状態をセットする.
	 * 
	 * @param i
	 *            int:ポップアップ番号
	 * @param pin
	 *            boolean:ピン留めする
	 */
	public void setPin(int i, boolean pin) {
		popups[i].setPin(pin);
		if (pin) {
			popups[i].setIconPin(NotifyTweet.getInstance().getResourceManager()
					.getIconPinOn());
		} else {
			popups[i].setIconPin(NotifyTweet.getInstance().getResourceManager()
					.getIconPinOff());
		}
	}

	/**
	 * ポップアップウィンドウを返します.
	 * 
	 * @return ポップアップウィンドウ
	 */
	public JPopup[] getPopups() {
		return popups;
	}

	/**
	 * ポップアップ番号からピンラベルを返す.
	 * 
	 * @param index
	 *            ポップアップ番号
	 * @return ピンラベル
	 */
	public JLabel getPinFromIndex(int index) {
		return popups[index].getPin();
	}
}
