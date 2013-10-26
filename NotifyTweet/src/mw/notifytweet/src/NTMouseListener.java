package mw.notifytweet.src;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

/**
 * マウスリスナ.<br />
 * 主にポップアップのマウスイベントに使われる.
 * 
 * @author Getaji
 * 
 */
public class NTMouseListener implements MouseListener {

	/** インデックス. */
	private int index;

	/**
	 * コンストラクタ.
	 * 
	 * @param i
	 *            インデックス
	 */
	public NTMouseListener(int i) {
		index = i;
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		if (ev.getSource().equals(
				NotifyTweet.getInstance().getPopupManager()
						.getPinFromIndex(index))) {
			// ピンラベル
			if (!NotifyTweet.getInstance().getPopupManager().getPopups()[index]
					.isPin()) {
				// ピンが有効だったら
				NotifyTweet.getInstance().getPopupManager().setPin(index, true);
			} else {
				// ピンが無効だったら
				NotifyTweet.getInstance().getPopupManager()
						.setPin(index, false);
			}
		} else if (ev.getSource().equals(
				NotifyTweet.getInstance().getPopupManager().getPopups()[index]
						.getComponent(0))
				&& !NotifyTweet.getInstance().getPopupManager().getPopups()[index]
						.isPin()) {
			// ポップアップ
			NotifyTweet.getInstance().getPopupManager().getPopups()[index]
					.setVisible(false);
			NotifyTweet.getInstance().getPopupManager().setPin(index, false);
		}
	}

	@Override
	public void mouseEntered(MouseEvent ev) {
		if (ev.getSource().equals(
				NotifyTweet.getInstance().getPopupManager()
						.getPinFromIndex(index))) {
			// ピン
			((JLabel) ev.getSource()).setIcon(NotifyTweet.getInstance()
					.getResourceManager().getIconPinOn());
		}
	}

	@Override
	public void mouseExited(MouseEvent ev) {
		if (ev.getSource().equals(
				NotifyTweet.getInstance().getPopupManager()
						.getPinFromIndex(index))
				&& !NotifyTweet.getInstance().getPopupManager()
						.isPinDialog(index)) {
			// ピン
			((JLabel) ev.getSource()).setIcon(NotifyTweet.getInstance()
					.getResourceManager().getIconPinOff());
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (arg0.getSource().equals(
				NotifyTweet.getInstance().getPopupManager()
						.getPinFromIndex(index))) {
			NotifyTweet.getInstance().getPopupManager().getPopups()[index]
					.onPinDown();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (arg0.getSource().equals(
				NotifyTweet.getInstance().getPopupManager()
						.getPinFromIndex(index))) {
			NotifyTweet.getInstance().getPopupManager().getPopups()[index]
					.onPinUp();
		}
	}

}
