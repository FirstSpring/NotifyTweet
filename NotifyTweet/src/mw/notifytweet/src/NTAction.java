package mw.notifytweet.src;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import mw.notifytweet.src.manager.AccountManager;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * イベントリスナのアクションを実際に実行するクラス.
 * 
 * @author Getaji
 * 
 */
public enum NTAction {

	/** インスタンス. */
	INSTANCE;

	/** 通常ツイート. */
	public static final short TW_NORMAL = 0;

	/** リツイート. */
	public static final short TW_RT = 1;

	/** 被お気に入り. */
	public static final short TW_FAV = 2;

	/** 値の加算. */
	public static final boolean PLUS = true;

	/** 値の減算. */
	public static final boolean MINUS = false;

	/** 不可視コンストラクタ. */
	private NTAction() {
	}

	/**
	 * 効果音を選択する.
	 * 
	 * @param parent
	 *            親コンポーネント
	 * @param path
	 *            表示時のカレントパス
	 * @param title
	 *            ダイアログのタイトル
	 * @return 選択したファイルのパス
	 */
	public String selectSE(Component parent, String path, String title) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("*.wav", "wav"));
		fileChooser.setCurrentDirectory(new File(path));
		fileChooser.setDialogTitle(title);

		if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile().getAbsolutePath();
		}
		return path;
	}

	/**
	 * ポップアップをプレビュー表示する.
	 */
	public void previewPopup() {
		for (int i = 0; i < EnumData.POPUP_QUA.getInt(); i++) {
			if (!NotifyTweet.getInstance().getPopupManager().getVisible(i)) {
				// カウンター番号と同じポップアップが非表示状態なら
				User user = null;
				URL url = null;
				try {
					// アイコンを設定する
					user = AccountManager.getInstance().getTwitter()
							.showUser(AccountManager.getInstance().selected);
					url = new URL(user.getProfileImageURL());
				} catch (TwitterException e1) {
					System.out
							.println("[NTAction.previewPopup]ユーザー情報を取得できませんでした");
					return;
				} catch (MalformedURLException e) {
					System.out
							.println("[NTAction.previewPopup]ユーザーアイコンを取得できませんでした");
					return;
				}
				NotifyTweet
						.getInstance()
						.getPopupManager()
						.setVisible(
								i,
								url,
								user.getScreenName(),
								user.getName(),
								"この設定ではこのように表示されます",
								NotifyTweet.getInstance().getConfigManager()
										.getColorNormal(), true);
				break;
			}
		}
		if ((!NotifyTweet.getInstance().getClipNormal().isActive())
				&& (NotifyTweet.getInstance().getConfigManager()
						.isEnableNormalSE())) {
			// 新着音が鳴ってない && 有効なら
			NotifyTweet.getInstance().getClipNormal().stop();
			NotifyTweet.getInstance().getClipNormal()
					.setMicrosecondPosition(0L);
			NotifyTweet.getInstance().getClipNormal().start();
		}
	}

	/**
	 * 背景色を選択する.
	 * 
	 * @param color
	 *            現在の色
	 * @param prevPanel
	 *            プレビューパネル
	 * @return 選択した色(キャンセルした場合はcolorがそのまま返る)
	 */
	public Color selectColor(Color color, JPanel prevPanel) {
		Color bufc = JColorChooser.showDialog(null, "通常色", color);
		if (bufc != null) {
			// カラーチョーサーが色を持ってきたら
			if (prevPanel.getBackground() == color) {
				// パネルの色とラベルの色が同じなら
				prevPanel.setBackground(bufc);
			}
			return bufc;
		}
		return color;
	}

	/**
	 * 浮動小数点値の加算/減算をする.
	 * 
	 * @param nowVal
	 *            現在の値
	 * @param isPlus
	 *            加算か減算か. 可読性の確保のためにNTAction.PLUS / MINUSを用いるとよい
	 * @param changeVal
	 *            変化させる量
	 * @return 変化させた値
	 */
	public String pmValue(BigDecimal nowVal, boolean isPlus,
			BigDecimal changeVal) {
		if (isPlus) {
			// 加算
			return nowVal.add(changeVal).toString();
		} else {
			// 減算
			return nowVal.subtract(changeVal).toString();
		}
	}
}
