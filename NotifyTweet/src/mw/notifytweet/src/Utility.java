package mw.notifytweet.src;

import java.awt.Component;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 汎用的な処理をまとめたクラスです.
 * @author Getaji
 *
 */
public class Utility {
	
	/** ぷらいべっつ. */
	private Utility() { }
	
	/**
	 * マウスポインタがコンポーネント内に入っているか判定します.
	 * @param source コンポーネント
	 * @param point ポインタ
	 * @return 入っているか
	 */
	public static boolean isInBox(Component source, Point point) {
		return 0 <= point.x
				&& 0 <= point.y
				&& point.x <= source.getWidth()
				&& point.y <= source.getHeight();
	}
	
	/**
	 * NotifyTweetの更新をチェックします.
	 * @return 更新が見つかった場合はDL先URL,最新版or見つからなかった場合UNKNOWNを返す
	 */
	public static String updateCheck() {
		HttpURLConnection http = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		final String unknown = "UNKNOWN";
		try {
			URL url = new URL(
					"https://dl.dropboxusercontent.com/u/23669096/JavatterPlugin/NotifyTweet/ver_nt.txt");
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.connect();

			isr = new InputStreamReader(http.getInputStream(), "UTF-8");
			br = new BufferedReader(isr);
			String newVer = br.readLine();
			int newbm = Integer.parseInt(br.readLine());
			if (newbm <= NotifyTweet.getInstance().getBuildNumber()) {
				return unknown;
			} else {
				return br.readLine();
			}
		} catch (IOException e) {
			NotifyTweet.getInstance().setStatus(false, "[NotifyTweet]更新の取得に失敗しました");
			return unknown;
		}
	}
}
