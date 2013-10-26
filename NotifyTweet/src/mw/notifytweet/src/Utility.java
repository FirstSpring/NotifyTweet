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
 * 
 * @author Getaji
 * 
 */
public class Utility {

	/** ぷらいべっつ. */
	private Utility() {
	}

	/**
	 * マウスポインタがコンポーネント内に入っているか判定します.
	 * 
	 * @param source
	 *            コンポーネント
	 * @param point
	 *            ポインタ
	 * @return 入っているか
	 */
	public static boolean isInBox(Component source, Point point) {
		return 0 <= point.x && 0 <= point.y && point.x <= source.getWidth()
				&& point.y <= source.getHeight();
	}

}
