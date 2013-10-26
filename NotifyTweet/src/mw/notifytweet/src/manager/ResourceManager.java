package mw.notifytweet.src.manager;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import mw.notifytweet.src.NotifyTweet;

/**
 * リソースを管理するクラス.
 * 
 * @author Getaji
 * 
 */
public enum ResourceManager {
	/** インスタンス. */
	INSTANCE;

	/** ピンONアイコン. */
	private ImageIcon iconPinOn;

	/** ピンOFFアイコン. */
	private ImageIcon iconPinOff;

	/** ロゴアイコン. */
	private ImageIcon iconLogo;

	/**
	 * デフォルトコンストラクタ.
	 */
	private ResourceManager() {
		try {
			iconPinOn = new ImageIcon(ImageIO.read(getClass()
					.getResourceAsStream("/resource/pin16_red.png")));
			iconPinOff = new ImageIcon(ImageIO.read(getClass()
					.getResourceAsStream("/resource/pin16_red_n.png")));
			iconLogo = new ImageIcon(ImageIO.read(NotifyTweet.class
					.getResourceAsStream("/resource/logo.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public ImageIcon getIconPinOn() {
		return iconPinOn;
	}

	public ImageIcon getIconPinOff() {
		return iconPinOff;
	}

	public ImageIcon getIconLogo() {
		return iconLogo;
	}
}
