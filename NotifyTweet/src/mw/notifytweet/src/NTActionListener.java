package mw.notifytweet.src;

import java.awt.Component;

/**
 * NTButton用アクションリスナ.<br />
 * 将来このクラスはActionListenerの実装に伴い廃止される予定です.
 * 
 * @author Getaji
 * 
 */
public interface NTActionListener {
	/**
	 * NTButtonがクリックされた時に呼ばれます.
	 * 
	 * @param source
	 *            発生元コンポーネント
	 */
	void ntAction(Component source);
}
