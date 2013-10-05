/*
 * © 2013 Margherita Works.
 * 
 */
package mw.notifytweet.src.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import mw.notifytweet.src.EnumData;
import mw.notifytweet.src.NTMouseListener;
import mw.notifytweet.src.NotifyTweet;

/**
 * 新着ツイートポップアップの最上コンポーネント.<br />
 * このクラスは<code>java.awt.Window</code>を継承したものです.<br />
 * 表示位置の元になるポップアップ番号とピンの状態を保持します.
 *
 */
public class JPopup extends Window {

    /** ポップアップ番号. */
    private int index;

    /** ピンの状態. */
    private boolean pin = false;


    /** パネル. */
    private JPanel panel;

    /** メインアイコン. */
    private JLabel labelIconNormal;

    /** リツイート、ふぁぼアイコンラベル. */
    private JLabel labelIconMini;

    /** ピンアイコンラベル. */
    private JLabel labelIconPin;
    
    /** ピンが押下されているか. */
    private boolean isPinDown = false;

    /** ネームラベル. */
    private JLabel labelTextUser;

    /** テキストラベル. */
    private JLabel labelTextText;

    /**
     * コンストラクタ.
     * @param par0index int:ポップアップ番号
     */
    public JPopup(int par0index) {
        super(null);
        this.index = par0index;
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setBackground(new Color(0, 0, 0, 0));
        setBounds(
                EnumData.POPUP_LOC_X.getInt(),
                EnumData.POPUP_LOC_Y.getInt(),
                EnumData.POPUP_WIDTH.getInt(),
                EnumData.POPUP_HEIGHT.getInt());
        setFocusable(false);

        panel = new JPanel();
        panel.addMouseListener(new NTMouseListener(index));
        panel.setBorder(new LineBorder(Color.black, 1, false));
        panel.setBackground(Color.white);
        panel.setFocusable(false);
        panel.setLayout(null);

        labelIconNormal = new JLabel();
        labelIconNormal.setBounds(1, 0,
                EnumData.ICON_SIZE_NORMAL.getInt(),
                EnumData.ICON_SIZE_NORMAL.getInt());

        labelIconMini = new JLabel();
        int rtIconLoc = EnumData.ICON_SIZE_NORMAL.getInt()
                - EnumData.ICON_SIZE_MINI.getInt() - 1;
        labelIconMini.setBounds(rtIconLoc, rtIconLoc,
                EnumData.ICON_SIZE_MINI.getInt(),
                EnumData.ICON_SIZE_MINI.getInt());
        labelIconPin = new JLabel();
        labelIconPin.setBounds(
                EnumData.POPUP_WIDTH.getInt()
                    - (EnumData.ICON_SIZE_PIN.getInt() + 3),
                3,
                EnumData.ICON_SIZE_PIN.getInt(),
                EnumData.ICON_SIZE_PIN.getInt());
        labelIconPin.addMouseListener(new NTMouseListener(index));
        labelIconPin.setIcon(NotifyTweet.getInstance()
                .getResourceManager().getIconPinOff());

        labelTextUser = new JLabel("User");
        labelTextUser.setBounds(
                55, 0,
                EnumData.POPUP_WIDTH.getInt() - 70, 20);
        labelTextUser.setFont(NotifyTweet.getInstance()
                .getConfigManager().getFontName());

        labelTextText = new JLabel("Text");
        labelTextText.setBounds(55, 24, 290, 20);
        labelTextText.setFont(NotifyTweet.getInstance()
                .getConfigManager().getFontText());

        panel.add(labelIconMini);
        panel.add(labelIconNormal);
        panel.add(labelTextUser);
        panel.add(labelTextText);
        panel.add(labelIconPin);

        add(panel);
    }

    /**
     * ピンの状態を返します.<br />
     * ピンが刺さっている場合はtrue,そうでない場合はfalseを返します.
     * @return boolean:ピンの状態
     */
    public boolean isPin() {
        return pin;
    }

    public JLabel getPin() {
        return labelIconPin;
    }

    /**
     * ピンの状態を変更します.
     * ピンを指す場合はtrue,抜く場合はfalseを渡します.
     * @param ispin boolean:ピンを有効にする
     */
    public void setPin(boolean isPin) {
        pin = isPin;
    }

    /**
     * このポップアップのインデックスを返します.<br />
     * 番号の上限はポップアップの最大表示数です.
     * @return ポップアップ番号
     */
    public int getIndex() {
        return index;
    }

    /**
     * メインアイコンをセットします.
     * @param icon メインアイコン
     */
    public void setIconNormal(ImageIcon icon) {
        labelIconNormal.setIcon(icon);
    }

    /**
     * ミニアイコンをセットします.
     * @param icon ミニアイコン
     */
    public void setIconMini(ImageIcon icon) {
        labelIconMini.setIcon(icon);
    }

    /**
     * ユーザー名をセットします.
     * @param userName ユーザー名
     */
    public void setTextUser(String userName) {
        labelTextUser.setText(userName);
    }

    /**
     * テキストをセットします.
     * @param text テキスト
     */
    public void setTextText(String text) {
        labelTextText.setText(text);
    }

    /**
     * 背景色をセットします.
     * @param color 背景色
     */
    public void setPanelBackground(Color color) {
        panel.setBackground(color);
    }

    /**
     * ユーザー名のフォントをセットします.
     * @param font ユーザー名フォント
     */
    public void setFontUser(Font font) {
        labelTextUser.setFont(font);
    }

    /**
     * テキストのフォントをセットします.
     * @param font テキストフォント
     */
    public void setFontText(Font font) {
        labelTextText.setFont(font);
    }

    public void setIconPin(ImageIcon icon) {
        labelIconPin.setIcon(icon);
    }

    /**
     * ピンを右下にずらします.
     */
    public void onPinDown() {
    	if (!isPinDown) {
    		labelIconPin.setLocation(labelIconPin.getX() + 1,
    				labelIconPin.getY() + 1);
    		isPinDown = true;
    	}
    }

    /**
     * ずらしたピンを元に戻します.
     */
    public void onPinUp() {
    	if (isPinDown) {
    		labelIconPin.setLocation(labelIconPin.getX() - 1,
    				labelIconPin.getY() - 1);
    		isPinDown = false;
    	}
    }
}

