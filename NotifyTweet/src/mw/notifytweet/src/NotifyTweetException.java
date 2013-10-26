/*
 * © 2013 Margherita Works.
 * 
 */
package mw.notifytweet.src;

/**
 * NotifyTweetに強く由来するエラーを吐くときは,このメソッドが使われます.
 * 
 */
public class NotifyTweetException extends RuntimeException {
	/**
	 * コンストラクタ.
	 * 
	 * @param string
	 *            String:メッセージ
	 */
	public NotifyTweetException(String string) {
		super(string);
	}
}
