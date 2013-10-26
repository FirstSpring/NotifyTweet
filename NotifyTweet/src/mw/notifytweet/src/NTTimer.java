package mw.notifytweet.src;

import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * ポップアップを一定時間後に非表示にするためのクラスです.
 * 
 * @author Getaji
 * 
 */
public class NTTimer implements ActionListener {
	/** ポップアップインデックス. */
	private int index;

	/** タイマー. */
	private Timer timer;

	/**
	 * ポップアップインデックスを登録.
	 * 
	 * @param index
	 *            ポップアップインデックス
	 */
	public NTTimer(int index) {
		this.index = index;
	}

	/**
	 * Timerを登録.
	 * 
	 * @param timer
	 *            タイマー
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		/* ダイアログがピン止めされてたら抜ける */
		if (NotifyTweet.getInstance().getPopupManager().isPinDialog(index)) {
			return;
		}

		/* ダイアログの中にマウスが入ってたら抜ける */
		Point pointerM = MouseInfo.getPointerInfo().getLocation();
		Point pointerD = null;
		try {
			pointerD = NotifyTweet.getInstance().getPopupManager().getPopups()[index]
					.getLocationOnScreen();
			if (pointerM.x > pointerD.x
					&& pointerM.x < pointerD.x + EnumData.POPUP_WIDTH.getInt()
					&& pointerM.y > pointerD.y
					&& pointerM.y < pointerD.y + EnumData.POPUP_HEIGHT.getInt()) {
				return;
			}
		} catch (IllegalComponentStateException e) {
			throw new NotifyTweetException("存在しないポップアップインデックス");
		} catch (NullPointerException e) {
			// ヽ(╹◡╹)ノ
		}

		NotifyTweet.getInstance().getPopupManager()
				.setVisible(index, false, null);
		timer.stop();
	}

}
