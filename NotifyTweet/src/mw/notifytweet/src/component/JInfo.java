package mw.notifytweet.src.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mw.notifytweet.src.NTActionListener;
import mw.notifytweet.src.NotifyTweet;
import mw.notifytweet.src.manager.ResourceManager;

/**
 * NotifyTweetについてのダイアログ.
 * 
 * @author Getaji
 * 
 */
public class JInfo extends JDialog implements NTActionListener {
	/** ロゴアイコン. */
	private JLabel iconLogo;

	/** メインテキストラベル. */
	private JLabel textMain;

	/** 状態ラベル. */
	private JLabel textState;

	/** 更新確認ボタン. */
	private NTButton buttonUpdateCheck;

	/** OKボタン. */
	private NTButton buttonOK;

	/**
	 * コンストラクタ.
	 */
	public JInfo() {
		setSize(500, 250);
		setTitle("NotifyTweetについて");
		setLayout(new BorderLayout());
		((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
		((JPanel) getContentPane()).setBackground(Color.LIGHT_GRAY);

		iconLogo = new JLabel(ResourceManager.INSTANCE.getIconLogo());
		iconLogo.setBounds(5, 5, 128, 128);
		iconLogo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent ev) {
				try {
					Desktop.getDesktop()
							.browse(new URI(
									"http://www1221uj.sakura.ne.jp/bbs/viewtopic.php?t=4"));
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (URISyntaxException ex) {
					ex.printStackTrace();
				}
			}
		});
		iconLogo.setCursor(new Cursor(Cursor.HAND_CURSOR));
		textMain = new JLabel("<html>NotifyTweet v" + 1 + "<br>"
				+ "© 2013 Margherita Works / Getaji<br>"
				+ "NotifyTweetは新着ツイート/リツイート/被お気に入りを通知するプラグインです。");
		textMain.setFont(new Font("MS Pゴシック", 0, 12));
		textMain.setBounds(140, 15, 300, 100);
		textState = new JLabel("", JLabel.CENTER);
		textState.setBounds(5, 120, 460, 20);
		textState.setFont(new Font("Meiryo UI", Font.BOLD, 12));
		buttonUpdateCheck = new NTButton("更新を確認する", 160);
		buttonUpdateCheck.setBounds(40, 150, 160, 30);
		buttonUpdateCheck.addActionListener(this);
		buttonOK = new NTButton("OK", 160);
		buttonOK.setBounds(270, 150, 160, 30);
		buttonOK.addActionListener(this);

		JPanel panelMain = new JPanel();
		panelMain.setLayout(null);
		panelMain.add(iconLogo);
		panelMain.add(textMain);
		panelMain.add(textState);
		panelMain.add(buttonUpdateCheck);
		panelMain.add(buttonOK);

		add(panelMain, "Center");
	}

	/**
	 * ダイアログを表示.
	 */
	public void showDialog() {
		setModal(true);
		setVisible(true);
	}

	@Override
	public void ntAction(Component source) {
		if (source.equals(buttonUpdateCheck)) {
			// hoge
		} else if (source.equals(buttonOK)) {
			setVisible(false);
		}
	}
}
