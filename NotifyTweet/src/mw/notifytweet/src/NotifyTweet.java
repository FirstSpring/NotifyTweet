/*
 * © 2013 Margherita Works.
 *
 */
package mw.notifytweet.src;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import mw.notifytweet.src.manager.AccountManager;
import mw.notifytweet.src.manager.NTConfigManager;
import mw.notifytweet.src.manager.PopupManager;
import mw.notifytweet.src.manager.ResourceManager;
import mw.notifytweet.src.manager.SaveData;

/**
 * Javatterプラグイン,NotifyTweetのメインクラスです.<br />
 * 初期処理を行い,共用インスタンスなどを保持します.
 * 
 * @author Getaji
 * 
 */
public class NotifyTweet implements ActionListener {
	/** メインクラスインスタンス. */
	private static NotifyTweet instance;

	/** セーブデータ . */
	private SaveData saveData;

	/** コンフィグマネージャー. */
	private NTConfigManager cfgManager;

	/** リソースマネージャー. */
	private ResourceManager resourceManager;

	/** ポップアップ管理. */
	private PopupManager popupManager;

	/** 通常新着音のクリップ. */
	private Clip clip;

	/** 被お気に入り新着音のクリップ. */
	private Clip clipFav;

	/** ロゴイメージ. */
	private static ImageIcon imageLogo;

	/** タスクトレイのアイコン */
	private TrayIcon icon;

	public static final String ACCOUNT = "a";
	public static final String CONNECT = "c";
	public static final String NOTIFY = "n";
	public static final String EXIT = "e";

	/**
	 * デフォルトコンストラクタ.
	 */
	public NotifyTweet() {
		instance = this;
		saveData = new SaveData("NotifyTweet", new File("."));
		saveData.load();
	}

	public static void main(String... args) {
		new NotifyTweet().init();
	}

	public void init() {
		/* ******** セーブデータのデフォルト値を設定 ******** */
		saveData = getSaveDataNT();

		saveData.setDefaultValue("enable", true);
		saveData.setDefaultValue("enablefav", true);
		saveData.setDefaultValue("enableme", true);
		saveData.setDefaultValue("position", 0);
		saveData.setDefaultValue("fontname_n", "Meiryo");
		saveData.setDefaultValue("fontstyle_n", Font.BOLD);
		saveData.setDefaultValue("fontsize_n", 12);
		saveData.setDefaultValue("fontname", "Meiryo");
		saveData.setDefaultValue("fontstyle", Font.PLAIN);
		saveData.setDefaultValue("fontsize", 12);
		saveData.setDefaultValue("popuptime", 5000);
		saveData.setDefaultValue("enablese", true);
		saveData.setDefaultValue("enablesef", true);
		saveData.setDefaultValue("sepath", "./ping.wav");
		saveData.setDefaultValue("sepathf", "./fav.wav");
		saveData.setDefaultValue("sevol", 0.5F);
		saveData.setDefaultValue("sevolf", 0.5F);
		saveData.setDefaultValue("colorn", "255,255,255");
		saveData.setDefaultValue("colorrt", "210,255,199");
		saveData.setDefaultValue("colorf", "255,249,191");

		/* ******** マネージャーをセット ******** */
		cfgManager = NTConfigManager.INSTANCE;
		resourceManager = ResourceManager.INSTANCE;
		popupManager = PopupManager.INSTANCE;

		/* ******** SE,Libraryを読み込み ******** */
		loadSE();

		icon = new TrayIcon(new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_RGB));
		PopupMenu menu = new PopupMenu();
		menu.addActionListener(this);
		icon.setPopupMenu(menu);
		MenuItem item;
		item = new MenuItem("アカウント設定");
		item.setActionCommand(ACCOUNT);
		menu.add(item);
		item = new MenuItem("再接続");
		item.setActionCommand(CONNECT);
		menu.add(item);
		item = new MenuItem("通知設定");
		item.setActionCommand(NOTIFY);
		menu.add(item);
		item = new MenuItem("終了");
		item.setActionCommand(EXIT);
		menu.add(item);
		try {
			SystemTray.getSystemTray().add(icon);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

		AccountManager.load();
		NTListener.getInstance().connect();
	}

	/**
	 * 2つの新着音をロードします.<br />
	 * <b>WARNING:このメソッドは1度きりしか呼ばれてはなりません</b>
	 */
	private void loadSE() {
		/* ******** 通常新着音読み込み ******** */
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(
					cfgManager.getPathNormalSE()));
			DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(ais);
			FloatControl control = (FloatControl) clip
					.getControl(FloatControl.Type.MASTER_GAIN);
			control.setValue((float) Math.log10(cfgManager.getVolNotmalSE()) * 20.0F);

		} catch (FileNotFoundException e) {
			/* **** ファイルが存在しない **** */
			JOptionPane.showMessageDialog(null, "指定された通常新着音ファイルが存在しません。\n"
					+ "同梱のNotifyTweetフォルダをpluginsフォルダに入れるか\n"
					+ "正確なファイルパスを指定してください。", "NotifyTweet",
					JOptionPane.ERROR_MESSAGE);

		} catch (UnsupportedAudioFileException e) {
			/* **** ファイル形式がサポートされていない **** */
			JOptionPane.showMessageDialog(null, "指定された通常新着音ファイルはサポートされていません。",
					"NotifyTweet", JOptionPane.ERROR_MESSAGE);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		/* ******** 被お気に入り音読み込み ******** */
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(
					cfgManager.getPathFavSE()));
			DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
			clipFav = (Clip) AudioSystem.getLine(info);
			clipFav.open(ais);
			FloatControl control = (FloatControl) clipFav
					.getControl(FloatControl.Type.MASTER_GAIN);
			control.setValue((float) Math.log10(cfgManager.getVolFavSE()) * 20.0F);

		} catch (FileNotFoundException e) {
			/* **** ファイルが存在しない **** */
			JOptionPane.showMessageDialog(null, "指定された被お気に入り音ファイルが存在しません。\n"
					+ "同梱のNotifyTweetフォルダをpluginsフォルダに入れるか\n"
					+ "正確なファイルパスを指定してください。", "NotifyTweet",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();

		} catch (UnsupportedAudioFileException e) {
			/* **** ファイル形式がサポートされていない **** */
			JOptionPane.showMessageDialog(null,
					"指定された被お気に入り音ファイルはサポートされていません。", "NotifyTweet",
					JOptionPane.ERROR_MESSAGE);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * NotifyTweetのインスタンスを返します.
	 * 
	 * @return NotifyTweetのインスタンス
	 */
	public static NotifyTweet getInstance() {
		return instance;
	}

	/**
	 * ポップアップマネージャーを返します.
	 * 
	 * @return ポップアップマネージャーのインスタンス
	 */
	public PopupManager getPopupManager() {
		return popupManager;
	}

	/**
	 * 読み込み後のセーブデータを返します.
	 * 
	 * @return セーブデータ
	 */
	public SaveData getSaveDataNT() {
		return saveData;
	}

	/**
	 * 設定マネージャーを返します.
	 * 
	 * @return 設定マネージャー
	 */
	public NTConfigManager getConfigManager() {
		return cfgManager;
	}

	/**
	 * リソースマネージャーを返します.
	 * 
	 * @return リソースマネージャー
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	/**
	 * 通常新着音のクリップを返します.
	 * 
	 * @return クリップ
	 */
	public Clip getClipNormal() {
		return clip;
	}

	/**
	 * 被お気に入り新着音のクリップを返します.
	 * 
	 * @return クリップ
	 */
	public Clip getClipFav() {
		return clipFav;
	}

	/**
	 * ロゴイメージを返します.
	 * 
	 * @return ロゴイメージ
	 */
	public ImageIcon getIconLogo() {
		return imageLogo;
	}

	public TrayIcon getTrayIcon() {
		return icon;
	}

	public void openConfig(Component c) {
		JFrame frame = new JFrame();
		frame.add(c);
		frame.setTitle("NotifyTweet");
		frame.setSize(EnumData.CFG_WIDTH.getInt(), EnumData.CFG_HEIGHT.getInt());
		frame.setResizable(false);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(ACCOUNT)) {
			openConfig(new NTAccountView().getComponent());
		}
		if (cmd.equals(CONNECT)) {
			NTListener.getInstance().connect();
		}
		if (cmd.equals(NOTIFY)) {
			openConfig(new NTConfigView().getComponent());
		}
		if (cmd.equals(EXIT)) {
			System.exit(0);
		}
	}

}
