/*
 * © 2013 Margherita Works.
 *
 */
package mw.notifytweet.src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import mw.notifytweet.src.component.DialogFontSelect;
import mw.notifytweet.src.component.JInfo;
import mw.notifytweet.src.component.NTButton;
import mw.notifytweet.src.manager.AccountManager;
import mw.notifytweet.src.manager.IconCache;
import mw.notifytweet.src.manager.NTConfigManager;
import mw.notifytweet.src.manager.SaveData;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * 設定画面を管理するクラスです.
 * 
 */
public class NTConfigView implements ActionListener, MouseListener,
		MouseWheelListener, NTActionListener {

	/** トップレベルパネル. */
	private JPanel panel;

	/** プラグイン有効/無効. */
	private JCheckBox checkIsUse;

	/** 被お気に入り通知有効/無効. */
	private JCheckBox checkIsUseFav;

	/** 自分のアクションの通知有効/無効. */
	private JCheckBox checkFromMe;

	/** ポップアップ表示位置. */
	private JLabel[] labelPos = new JLabel[4];

	/** 選択位置. */
	private int selectPos;

	/** ポップアップ表示時間. */
	private JSpinner spinnerPopupTime;

	/** 名前フォント情報. */
	private JLabel labelFontName;

	/** テキストフォント情報. */
	private JLabel labelFontText;

	/** 通常新着音有効/無効. */
	private JCheckBox checkEnableSE;

	/** 通常新着音ファイルパス. */
	private JTextField textSEPath;

	/** 通常新着音音量. */
	private JTextField textSEVol;

	/** 被お気に入り音有効/無効. */
	private JCheckBox checkEnableSEF;

	/** 被お気に入り音ファイルパス. */
	private JTextField textSEPathF;

	/** 被お気に入り音音量. */
	private JTextField textSEVolF;

	/** プレビューパネル. */
	private static JPanel panelView;

	/** OKボタン. */
	private NTButton buttonOK;

	/** キャンセルボタン. */
	private NTButton buttonCancel;

	/** 適用ボタン. */
	private NTButton buttonSet;

	/** インフォボタン. */
	private NTButton buttonAbout;

	/** フォント名. */
	private final String[] fontStyle = { "標準", "太字", "斜体", "太字&斜体" };

	/** <code>java.awt.Font</code>のラッパー. */
	private static NTFont fontname, fonttext;

	/** プレビューラベル群. */
	private static JLabel labelIcon, previewName, previewText, previewPin;

	/** ピンが下がっているか. */
	private boolean isPinDown = false;

	/** 背景色ラベル群. */
	private JLabel labelColorN, labelColorRT, labelColorFav;

	/** オフボーダー. */
	private static LineBorder borderOff = new LineBorder(Color.LIGHT_GRAY);

	/** オンボーダー. */
	private static LineBorder borderOn = new LineBorder(Color.DARK_GRAY);

	public Component getComponent() {
		NTConfigManager cfg = NotifyTweet.getInstance().getConfigManager();

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(200, 200, 200));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainPanel.setFont(new Font("Meiryo", Font.PLAIN, 12));
		mainPanel.setBorder(new EmptyBorder(0, 5, 0, 5));

		JPanel panelIsUse = new JPanel();
		panelIsUse.setLayout(new BoxLayout(panelIsUse, BoxLayout.Y_AXIS));
		panelIsUse.setPreferredSize(new Dimension(360, 90));
		checkIsUse = new JCheckBox("ツイート/リツイートを通知する");
		checkIsUse.setSelected(cfg.isEnablePlugin());
		checkIsUse.addActionListener(this);
		checkIsUseFav = new JCheckBox("被お気に入り（ふぁぼられ）を通知する");
		checkIsUseFav.setSelected(cfg.isEnableFav());
		checkIsUseFav.addActionListener(this);
		checkFromMe = new JCheckBox("自分のアクションを通知する");
		checkFromMe.setSelected(cfg.isFromMe());
		panelIsUse.add(checkIsUse);
		panelIsUse.add(checkIsUseFav);
		panelIsUse.add(checkFromMe);
		mainPanel.add(panelIsUse);

		// ▼位置
		JPanel positionPanel = new JPanel();
		positionPanel.setBorder(new TitledBorder(new LineBorder(
				Color.LIGHT_GRAY), "位置", TitledBorder.LEFT, TitledBorder.TOP));
		positionPanel.setPreferredSize(new Dimension(230, 72));
		positionPanel.setLayout(null);
		labelPos[0] = new JLabel("左上", JLabel.CENTER);
		labelPos[0].setBounds(10, 20, 100, 20);
		labelPos[0].setBackground(Color.WHITE);
		labelPos[0].setOpaque(true);
		labelPos[0].setBorder(borderOff);
		labelPos[0].addMouseListener(this);

		labelPos[1] = new JLabel("右上", JLabel.CENTER);
		labelPos[1].setBounds(120, 20, 100, 20);
		labelPos[1].setBackground(Color.WHITE);
		labelPos[1].setOpaque(true);
		labelPos[1].setBorder(borderOff);
		labelPos[1].addMouseListener(this);

		labelPos[2] = new JLabel("右下", JLabel.CENTER);
		labelPos[2].setBounds(120, 45, 100, 20);
		labelPos[2].setBackground(Color.WHITE);
		labelPos[2].setOpaque(true);
		labelPos[2].setBorder(borderOff);
		labelPos[2].addMouseListener(this);

		labelPos[3] = new JLabel("左下", JLabel.CENTER);
		labelPos[3].setBounds(10, 45, 100, 20);
		labelPos[3].setBackground(Color.WHITE);
		labelPos[3].setOpaque(true);
		labelPos[3].setBorder(borderOff);
		labelPos[3].addMouseListener(this);

		int pos = cfg.getPopupPosition();
		labelPos[pos].setBorder(borderOn);
		selectPos = pos;
		positionPanel.add(labelPos[0]);
		positionPanel.add(labelPos[1]);
		positionPanel.add(labelPos[2]);
		positionPanel.add(labelPos[3]);
		mainPanel.add(positionPanel);

		// ▼表示時間
		JPanel popupTimePanel = new JPanel();
		popupTimePanel.setBorder(new TitledBorder(new LineBorder(
				Color.LIGHT_GRAY), "表示時間(ミリ秒)", TitledBorder.LEFT,
				TitledBorder.TOP));
		popupTimePanel.setPreferredSize(new Dimension(130, 72));
		spinnerPopupTime = new JSpinner();
		spinnerPopupTime.setPreferredSize(new Dimension(70, 32));
		spinnerPopupTime.setValue(Integer.valueOf(cfg.getTimePopup()));
		spinnerPopupTime.addMouseWheelListener(this);
		popupTimePanel.add(spinnerPopupTime);
		mainPanel.add(popupTimePanel);

		// フォント
		JPanel panelFont = new JPanel();
		panelFont.setLayout(new GridLayout(2, 1));
		panelFont.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY),
				"フォント", TitledBorder.LEFT, TitledBorder.TOP));
		panelFont.setAlignmentY(0.0F);
		panelFont.setPreferredSize(new Dimension(365, 110));
		Dimension dimButton = new Dimension(100, 30);
		// ▼名前フォント
		JPanel fontNamePanel = new JPanel();
		fontNamePanel.setLayout(new BoxLayout(fontNamePanel, BoxLayout.X_AXIS));
		NTButton buttonFontName = new NTButton("ユーザー名", 100);
		labelFontName = new JLabel();
		buttonFontName.setMaximumSize(dimButton);
		fontname = new NTFont(cfg.getFontName());
		buttonFontName.addActionListener(new NTActionListener() {
			public void ntAction(Component source) {
				new DialogFontSelect(fontname, labelFontName, true);
			}
		});
		labelFontName.setText(fontname.getFontName() + " / "
				+ fontStyle[fontname.getFontStyle()] + " / "
				+ fontname.getFontSize() + "pt");
		fontNamePanel.add(Box.createRigidArea(new Dimension(5, 10)));
		fontNamePanel.add(buttonFontName);
		fontNamePanel.add(Box.createRigidArea(new Dimension(5, 10)));
		fontNamePanel.add(labelFontName);
		panelFont.add(fontNamePanel);

		// ▼本文フォント
		JPanel fontTextPanel = new JPanel();
		fontTextPanel.setLayout(new BoxLayout(fontTextPanel, BoxLayout.X_AXIS));
		NTButton buttonFontText = new NTButton("テキスト", 100);
		labelFontText = new JLabel();
		fonttext = new NTFont(cfg.getFontText());
		buttonFontText.setMaximumSize(dimButton);
		buttonFontText.addActionListener(new NTActionListener() {
			@Override
			public void ntAction(Component source) {
				new DialogFontSelect(fonttext, labelFontText, false);
			}
		});
		labelFontText.setText(fonttext.getFontName() + " / "
				+ fontStyle[fonttext.getFontStyle()] + " / "
				+ fonttext.getFontSize() + "pt");
		labelFontText.repaint();
		fontTextPanel.add(Box.createRigidArea(new Dimension(5, 10)));
		fontTextPanel.add(buttonFontText);
		fontTextPanel.add(Box.createRigidArea(new Dimension(5, 10)));
		fontTextPanel.add(labelFontText);
		panelFont.add(fontTextPanel);
		mainPanel.add(panelFont);

		JPanel sePanel = new JPanel();
		sePanel.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY),
				"新着音", TitledBorder.LEFT, TitledBorder.TOP));
		sePanel.setPreferredSize(new Dimension(365, 110));
		sePanel.setLayout(new GridLayout(2, 1));

		JPanel sePanelN = new JPanel();
		sePanelN.setLayout(new BoxLayout(sePanelN, BoxLayout.X_AXIS));
		checkEnableSE = new JCheckBox("通常　");
		textSEPath = new JTextField(cfg.getPathNormalSE());
		textSEPath.addMouseListener(this);
		textSEPath.setMaximumSize(new Dimension(240, 30));
		JLabel labelSEVol = new JLabel("音量(1.0 ≧ )");
		textSEVol = new JTextField(String.valueOf(cfg.getVolNotmalSE()));
		textSEVol.addMouseWheelListener(this);
		textSEVol.setMaximumSize(new Dimension(30, 30));
		checkEnableSE.setSelected(cfg.isEnableNormalSE());
		sePanelN.add(Box.createRigidArea(new Dimension(5, 10)));
		sePanelN.add(checkEnableSE);
		sePanelN.add(Box.createRigidArea(new Dimension(5, 10)));
		sePanelN.add(textSEPath);
		sePanelN.add(Box.createRigidArea(new Dimension(5, 10)));
		sePanelN.add(labelSEVol);
		sePanelN.add(Box.createRigidArea(new Dimension(5, 10)));
		sePanelN.add(textSEVol);
		mainPanel.add(sePanelN);

		mainPanel.add(Box.createRigidArea(new Dimension(100, 5)));

		JPanel sePanelF = new JPanel();
		sePanelF.setPreferredSize(new Dimension(375, 65));
		sePanelF.setLayout(new BoxLayout(sePanelF, BoxLayout.X_AXIS));
		checkEnableSEF = new JCheckBox("ふぁぼ");
		textSEPathF = new JTextField(cfg.getPathFavSE());
		textSEPathF.addMouseListener(this);
		textSEPathF.setMaximumSize(new Dimension(240, 30));
		JLabel labelSEVolF = new JLabel("音量(1.0 ≧ )");
		textSEVolF = new JTextField(String.valueOf(cfg.getVolFavSE()));
		textSEVolF.addMouseWheelListener(this);
		textSEVolF.setMaximumSize(new Dimension(30, 30));
		checkEnableSEF.setSelected(cfg.isEnableFavSE());
		sePanelF.add(Box.createRigidArea(new Dimension(5, 10)));
		sePanelF.add(checkEnableSEF);
		sePanelF.add(Box.createRigidArea(new Dimension(5, 10)));
		sePanelF.add(textSEPathF);
		sePanelF.add(Box.createRigidArea(new Dimension(5, 10)));
		sePanelF.add(labelSEVolF);
		sePanelF.add(Box.createRigidArea(new Dimension(5, 10)));
		sePanelF.add(textSEVolF);
		sePanel.add(sePanelN);
		sePanel.add(sePanelF);
		mainPanel.add(sePanel);

		mainPanel.add(Box.createRigidArea(new Dimension(100, 3)));

		// プレビュー
		panelView = new JPanel();
		panelView.setBorder(new LineBorder(Color.black, 1));
		panelView.setBackground(Color.white);
		panelView.setPreferredSize(new Dimension(350, 50));
		panelView.setLayout(null);
		panelView.setOpaque(true);
		labelIcon = new JLabel();
		labelIcon.setBounds(1, 0, 50, 50);
		labelIcon.addMouseListener(this);
		labelIcon.setToolTipText("実際にポップアップを表示させます。\n設定が適用された状態でないと反映されません。");
		User user = null;
		boolean isLoadedUser = true;
		try {
			user = AccountManager.getInstance().getTwitter()
					.showUser(AccountManager.getInstance().selected);
			String url = user.getProfileImageURL();
			labelIcon.setIcon(IconCache.getInstance().getIcon(new URL(url)));
		} catch (TwitterException e1) {
			e1.printStackTrace();
			isLoadedUser = false;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			isLoadedUser = false;
		}
		if (isLoadedUser) {
			previewName = new JLabel("@" + user.getScreenName() + " / "
					+ user.getName() + " :");
			previewName.setBounds(55, 0, 295, 20);
			previewName.setFont(fontname.getFont());
			previewText = new JLabel("この設定ではこのように表示されます");
			previewText.setBounds(55, 24, 295, 20);
			previewText.setFont(fonttext.getFont());
			previewPin = new JLabel(NotifyTweet.getInstance()
					.getResourceManager().getIconPinOff());
			previewPin.setBounds(331, 3, 16, 16);
			previewPin.addMouseListener(this);
			labelColorN = new JLabel();
			labelColorN.setBackground(cfg.getColorNormal());
			labelColorN.setBorder(borderOff);
			labelColorN.setOpaque(true);
			labelColorN.setBounds(290, 30, 16, 16);
			labelColorN.addMouseListener(this);
			labelColorRT = new JLabel();
			labelColorRT.setBackground(cfg.getColorRT());
			labelColorRT.setBorder(new LineBorder(Color.GRAY));
			labelColorRT.setOpaque(true);
			labelColorRT.setBounds(310, 30, 16, 16);
			labelColorRT.addMouseListener(this);
			labelColorFav = new JLabel();
			labelColorFav.setBackground(cfg.getColorFav());
			labelColorFav.setBorder(new LineBorder(Color.GRAY));
			labelColorFav.setOpaque(true);
			labelColorFav.setBounds(330, 30, 16, 16);
			labelColorFav.addMouseListener(this);
			panelView.add(labelIcon);
			panelView.add(previewName);
			panelView.add(previewText);
			panelView.add(previewPin);
			panelView.add(labelColorN);
			panelView.add(labelColorRT);
			panelView.add(labelColorFav);
			mainPanel.add(panelView);
		}

		panel.add(mainPanel, "Center");

		JPanel lastPanel = new JPanel();

		JPanel ynPanel = new JPanel();
		buttonOK = new NTButton("OK", 50);
		buttonCancel = new NTButton("キャンセル", 100);
		buttonSet = new NTButton("適用", 50);
		buttonAbout = new NTButton("NotifyTweetについて", 140);
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		buttonSet.addActionListener(this);
		buttonAbout.addActionListener(this);
		ynPanel.add(buttonOK);
		ynPanel.add(buttonCancel);
		ynPanel.add(buttonSet);
		ynPanel.add(buttonAbout);
		lastPanel.add(ynPanel);

		panel.add(lastPanel, "Last");

		return panel;
	}

	/**
	 * プレビューにフォントを設定して再描画します.
	 */
	public static void repaint() {
		previewName.setFont(fontname.getFont());
		previewText.setFont(fonttext.getFont());
		previewName.repaint();
		previewText.repaint();
	}

	@Override
	public void actionPerformed(final ActionEvent ev) {
	}

	public static JLabel getPreViewName() {
		return previewName;
	}

	public static JLabel getPreViewText() {
		return previewText;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getSource().equals(textSEPath)) {
			// 通常新着音選択
			String path = NTAction.INSTANCE.selectSE(panel,
					textSEPath.getText(), "通常新着音");

			textSEPath.setText(path);

		} else if (arg0.getSource().equals(textSEPathF)) {
			// RT、ふぁぼ新着音選択
			String path = NTAction.INSTANCE.selectSE(panel,
					textSEPathF.getText(), "被お気に入り音");

			textSEPathF.setText(path);

		} else if (arg0.getSource().equals(labelIcon)) {
			// プレビュー
			NTAction.INSTANCE.previewPopup();

		} else if (arg0.getSource().equals(labelColorN)
				&& SwingUtilities.isLeftMouseButton(arg0)) {
			// 色1を選択
			labelColorN.setBackground(NTAction.INSTANCE.selectColor(
					labelColorN.getBackground(), panelView));

		} else if (arg0.getSource().equals(labelColorRT)
				&& SwingUtilities.isLeftMouseButton(arg0)) {
			// 色2を選択
			labelColorRT.setBackground(NTAction.INSTANCE.selectColor(
					labelColorRT.getBackground(), panelView));

		} else if (arg0.getSource().equals(labelColorFav)
				&& SwingUtilities.isLeftMouseButton(arg0)) {
			// 色3を選択
			labelColorFav.setBackground(NTAction.INSTANCE.selectColor(
					labelColorFav.getBackground(), panelView));

		} else if (arg0.getSource().equals(labelColorN)
				&& SwingUtilities.isRightMouseButton(arg0)
				&& Utility.isInBox(arg0.getComponent(), arg0.getPoint())) {
			// 色1に設定
			panelView.setBackground(labelColorN.getBackground());
		} else if (arg0.getSource().equals(labelColorRT)
				&& SwingUtilities.isRightMouseButton(arg0)
				&& Utility.isInBox(arg0.getComponent(), arg0.getPoint())) {
			// 色2に設定
			panelView.setBackground(labelColorRT.getBackground());
		} else if (arg0.getSource().equals(labelColorFav)
				&& SwingUtilities.isRightMouseButton(arg0)
				&& Utility.isInBox(arg0.getComponent(), arg0.getPoint())) {
			// 色3に設定
			panelView.setBackground(labelColorFav.getBackground());
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if (arg0.getSource().equals(labelIcon)) {
			// アイコンにボーダー
			labelIcon.setBorder(borderOff);
		} else if (arg0.getSource().equals(previewPin)) {
			// ピンをON
			previewPin.setIcon(NotifyTweet.getInstance().getResourceManager()
					.getIconPinOn());
		} else if (arg0.getSource().equals(labelColorN)) {
			// 色0にボーダー
			labelColorN.setBorder(borderOn);
		} else if (arg0.getSource().equals(labelColorRT)) {
			// 色1にボーダー
			labelColorRT.setBorder(borderOn);
		} else if (arg0.getSource().equals(labelColorFav)) {
			// 色2にボーダー
			labelColorFav.setBorder(borderOn);
		} else if (arg0.getSource().equals(labelPos[0])) {
			// 左上選択
			labelPos[0].setBackground(Color.LIGHT_GRAY);
		} else if (arg0.getSource().equals(labelPos[1])) {
			// 右上選択
			labelPos[1].setBackground(Color.LIGHT_GRAY);
		} else if (arg0.getSource().equals(labelPos[2])) {
			// 右下選択
			labelPos[2].setBackground(Color.LIGHT_GRAY);
		} else if (arg0.getSource().equals(labelPos[3])) {
			// 左下選択
			labelPos[3].setBackground(Color.LIGHT_GRAY);
		}

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if (arg0.getSource().equals(labelIcon)) {
			// アイコンからボーダー削除
			labelIcon.setBorder(null);
		} else if (arg0.getSource().equals(previewPin)) {
			// ピンをOFF
			previewPin.setIcon(NotifyTweet.getInstance().getResourceManager()
					.getIconPinOff());
		} else if (arg0.getSource().equals(labelColorN)) {
			// 色0にボーダー
			labelColorN.setBorder(borderOff);
		} else if (arg0.getSource().equals(labelColorRT)) {
			// 色1にボーダー
			labelColorRT.setBorder(borderOff);
		} else if (arg0.getSource().equals(labelColorFav)) {
			// 色2にボーダー
			labelColorFav.setBorder(borderOff);
		} else if (arg0.getSource().equals(labelPos[0])) {
			// 左上選択
			labelPos[0].setBackground(Color.WHITE);
		} else if (arg0.getSource().equals(labelPos[1])) {
			// 右上選択
			labelPos[1].setBackground(Color.WHITE);
		} else if (arg0.getSource().equals(labelPos[2])) {
			// 右下選択
			labelPos[2].setBackground(Color.WHITE);
		} else if (arg0.getSource().equals(labelPos[3])) {
			// 左下選択
			labelPos[3].setBackground(Color.WHITE);
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (arg0.getSource().equals(previewPin) && !isPinDown) {
			previewPin
					.setLocation(previewPin.getX() + 1, previewPin.getY() + 1);
			isPinDown = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (!Utility.isInBox(arg0.getComponent(), arg0.getPoint())) {
			return;
		}
		if (arg0.getSource().equals(labelPos[0])) {
			// 左上選択
			labelPos[selectPos].setBorder(borderOff);
			labelPos[0].setBorder(borderOn);
			selectPos = 0;
		} else if (arg0.getSource().equals(labelPos[1])) {
			// 右上選択
			labelPos[selectPos].setBorder(borderOff);
			labelPos[1].setBorder(borderOn);
			selectPos = 1;
		} else if (arg0.getSource().equals(labelPos[2])) {
			// 右下選択
			labelPos[selectPos].setBorder(borderOff);
			labelPos[2].setBorder(borderOn);
			selectPos = 2;
		} else if (arg0.getSource().equals(labelPos[3])) {
			// 左下選択
			labelPos[selectPos].setBorder(borderOff);
			labelPos[3].setBorder(borderOn);
			selectPos = 3;
		} else if (arg0.getSource().equals(previewPin) && isPinDown) {
			previewPin
					.setLocation(previewPin.getX() - 1, previewPin.getY() - 1);
			isPinDown = false;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO MouseWheelMoved
		if (arg0.getSource().equals(textSEVol)) {
			// 通常新着音
			BigDecimal now = new BigDecimal(textSEVol.getText());
			BigDecimal pm = new BigDecimal("0.1");
			if (arg0.getPreciseWheelRotation() < 0
					&& Float.valueOf(textSEVol.getText()) < 1) {
				// ホイール上
				textSEVol.setText(now.add(pm).toString());
			} else if (arg0.getPreciseWheelRotation() > 0
					&& Float.valueOf(textSEVol.getText()) > 0) {
				// ホイール下
				textSEVol.setText(now.subtract(pm).toString());
			}
		} else if (arg0.getSource().equals(textSEVolF)) {
			// 被お気に入り新着音
			BigDecimal now = new BigDecimal(textSEVolF.getText());
			BigDecimal pm = new BigDecimal("0.1");
			if (arg0.getPreciseWheelRotation() < 0
					&& Float.valueOf(textSEVolF.getText()) < BigDecimal.ONE
							.intValue()) {
				// ホイール上
				textSEVolF.setText(now.add(pm).toString());
			} else if (arg0.getPreciseWheelRotation() > 0
					&& Float.valueOf(textSEVolF.getText()) > BigDecimal.ZERO
							.intValue()) {
				// ホイール下
				textSEVolF.setText(now.subtract(pm).toString());
			}
		} else if (arg0.getSource().equals(spinnerPopupTime)) {
			// ポップアップ表示時間
			if (arg0.getPreciseWheelRotation() < 0) {
				// ホイール上
				spinnerPopupTime.setValue((Integer) spinnerPopupTime.getValue()
						+ EnumData.PLUS_VOL.getInt());
			} else if (arg0.getPreciseWheelRotation() > 0
					&& (Integer) spinnerPopupTime.getValue() > 0) {
				// ホイール下
				spinnerPopupTime.setValue((Integer) spinnerPopupTime.getValue()
						- EnumData.PLUS_VOL.getInt());
			}
		}

	}

	@Override
	public void ntAction(final Component source) {

		if (source.equals(buttonOK) || source.equals(buttonSet)) {

			Thread thread = new Thread() {
				NTConfigManager cfg = NotifyTweet.getInstance()
						.getConfigManager();
				SaveData saveData = NotifyTweet.getInstance().getSaveDataNT();
				JFrame f = (JFrame) SwingUtilities.getWindowAncestor(panel);

				public void run() {
					previewText.setText("プラグインの有効状態を保存中...");
					saveData.setBoolean("enable", checkIsUse.isSelected());
					cfg.setEnablePlugin(checkIsUse.isSelected());

					previewText.setText("被お気に入り通知の有効状態を保存中...");
					saveData.setBoolean("enablefav", checkIsUseFav.isSelected());
					cfg.setEnableFav(checkIsUseFav.isSelected());

					previewText.setText("自分のアクションの通知の有効状態を保存中...");
					saveData.setBoolean("enableme", checkFromMe.isSelected());
					cfg.setFromMe(checkFromMe.isSelected());

					previewText.setText("ポップアップの表示位置を保存中...");
					saveData.setInt("position", selectPos);
					cfg.setPopupPosition(selectPos);

					previewText.setText("フォントを保存中...");
					saveData.setString("fontname_n", fontname.getFontName());
					saveData.setInt("fontstyle_n", fontname.getFontStyle());
					saveData.setInt("fontsize_n", fontname.getFontSize());
					saveData.setString("fontname", fonttext.getFontName());
					saveData.setInt("fontstyle", fonttext.getFontStyle());
					saveData.setInt("fontsize", fonttext.getFontSize());
					cfg.setFontName(fontname.getFont());
					cfg.setFontText(fonttext.getFont());

					previewText.setText("ポップアップの表示時間を保存中...");
					try {
						saveData.setInt("popuptime",
								((Integer) spinnerPopupTime.getValue())
										.intValue());
						cfg.setTimePopup(((Integer) spinnerPopupTime.getValue())
								.intValue());
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(f, "表示時間の値が不正です", "エラー",
								0);
						return;
					}

					previewText.setText("通常新着音を保存中...");
					saveData.setBoolean("enablese", checkEnableSE.isSelected());
					cfg.setEnableNormalSE(checkEnableSE.isSelected());

					saveData.setString("sepath", textSEPath.getText());
					cfg.setPathNormalSE(textSEPath.getText());
					try {
						saveData.setFloat("sevol",
								Float.valueOf(textSEVol.getText()).floatValue());
						cfg.setVolNormalSE(Float.valueOf(textSEVol.getText())
								.floatValue());
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(f, "通常音音量の値が不正です", "エラー",
								0);
						return;
					}

					previewText.setText("被お気に入り音を保存中...");
					saveData.setBoolean("enablesef",
							checkEnableSEF.isSelected());
					cfg.setEnableFavSE(checkEnableSEF.isSelected());

					saveData.setString("sepathf", textSEPathF.getText());
					cfg.setPathFavSE(textSEPathF.getText());
					try {
						saveData.setFloat("sevolf",
								Float.valueOf(textSEVolF.getText())
										.floatValue());
						cfg.setVolFavSE(Float.valueOf(textSEVolF.getText())
								.floatValue());
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(f, "ふぁぼ音音量の値が不正です",
								"エラー", 0);
						return;
					}

					previewText.setText("フォントを再設定中...");
					NotifyTweet.getInstance().getPopupManager().resetFont();

					previewText.setText("通常新着音を再設定中...");
					try {
						AudioInputStream ais = AudioSystem
								.getAudioInputStream(new File(cfg
										.getPathNormalSE()));
						NotifyTweet.getInstance().getClipNormal().close();
						NotifyTweet.getInstance().getClipNormal().open(ais);
						FloatControl control = (FloatControl) NotifyTweet
								.getInstance().getClipNormal()
								.getControl(FloatControl.Type.MASTER_GAIN);
						control.setValue((float) Math.log10(cfg
								.getVolNotmalSE()) * 20.0F);
					} catch (UnsupportedAudioFileException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					}

					previewText.setText("被お気に入り音を再設定中...");
					try {
						AudioInputStream ais = AudioSystem
								.getAudioInputStream(new File(cfg
										.getPathFavSE()));
						NotifyTweet.getInstance().getClipFav().close();
						NotifyTweet.getInstance().getClipFav().open(ais);
						FloatControl control = (FloatControl) NotifyTweet
								.getInstance().getClipFav()
								.getControl(FloatControl.Type.MASTER_GAIN);
						control.setValue((float) Math.log10(cfg.getVolFavSE()) * 20.0F);
					} catch (UnsupportedAudioFileException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					}

					previewText.setText("ポップアップの背景色を保存中...");
					saveData.setString("colorn", labelColorN.getBackground()
							.toString().replaceAll("[^\\d,]", ""));
					saveData.setString("colorrt", labelColorRT.getBackground()
							.toString().replaceAll("[^\\d,]", ""));
					saveData.setString("colorf", labelColorFav.getBackground()
							.toString().replaceAll("[^\\d,]", ""));
					cfg.setColorNormal(labelColorN.getBackground());
					cfg.setColorRT(labelColorRT.getBackground());
					cfg.setColorFav(labelColorFav.getBackground());

					previewText.setText("この設定ではこのように表示されます");
					if (source.equals(buttonOK)) {
						f.setVisible(false);
					}
				}
			};
			thread.start();
		} else if (source.equals(buttonCancel)) {
			JFrame f = (JFrame) SwingUtilities.getWindowAncestor(panel);
			f.setVisible(false);
		} else if (source.equals(buttonAbout)) {
			new JInfo().showDialog();
		}
	}
}
