package mw.notifytweet.src.manager;

import java.awt.Color;
import java.awt.Font;

import mw.notifytweet.src.NotifyTweet;

import com.orekyuu.javatter.util.SaveData;

/**
 * 設定を管理するクラス.
 * @author Getaji
 *
 */
public enum NTConfigManager {
	INSTANCE;
	
	/** セーブデータ. */
    private SaveData saveData;

    /** プラグイン有効. */
    private boolean isEnable;
    /** 被お気に入り通知有効(1.6). */
    private boolean isEnableFav;
    /** 自分のアクションを通知有効(1.6). */
    private boolean isFromMe;
    /** ポップアップ表示位置. */
    private int popupPosition;
    /** 名前フォント. */
    private Font fontName;
    /** テキストフォント. */
    private Font fontText;
    /** ポップアップ表示時間. */
    private int popupTime;
    /** 新着音パス. */
    private String sePath;
    /** 被お気に入り音パス. */
    private String sePathFav;
    /** 新着音音量. */
    private float seVol;
    /** 被お気に入り音音量. */
    private float seVolFav;
    /** 新着音有効. */
    private boolean isEnableSE;
    /** 被お気に入り音有効. */
    private boolean isEnableSEFav;
    /** 通常背景色. */
    private Color colorNormal;
    /** リツイート背景色. */
    private Color colorRT;
    /** 被お気に入り背景色. */
    private Color colorFav;

    /**
     * デフォルトコンストラクタ.<br />
     * このメソッドは一度きり呼ばれます.
     */
    private NTConfigManager() {
        saveData = NotifyTweet.getInstance().getSaveDataNT();

        isEnable = saveData.getBoolean("enable");
        isEnableFav = saveData.getBoolean("enablefav");
        isFromMe = saveData.getBoolean("enableme");
        popupPosition = saveData.getInt("position");
        fontName = new Font(saveData.getString("fontname_n"),
                saveData.getInt("fontstyle_n"), saveData.getInt("fontsize_n"));
        fontText = new Font(saveData.getString("fontname"),
                saveData.getInt("fontstyle"), saveData.getInt("fontsize"));
        popupTime = saveData.getInt("popuptime");
        isEnableSE = saveData.getBoolean("enablese");
        isEnableSEFav = saveData.getBoolean("enablesef");
        sePath = saveData.getString("sepath");
        sePathFav = saveData.getString("sepathf");
        seVol = saveData.getFloat("sevol");
        seVolFav = saveData.getFloat("sevolf");

        int[] bufc = new int[3];
        int[] bufc2 = new int[3];
        int[] bufc3 = new int[3];
        String bufs = saveData.getString("colorn") + ":"
                + saveData.getString("colorrt") + ":"
                + saveData.getString("colorf");
        for (int i = 0; i < 3; ++i) {
            bufc[i] = Integer.valueOf(bufs.split(":")[0].split(",")[i]).
                    intValue();
            bufc2[i] = Integer.valueOf(bufs.split(":")[1].split(",")[i]).
                    intValue();
            bufc3[i] = Integer.valueOf(bufs.split(":")[2].split(",")[i]).
                    intValue();
        }
        colorNormal = new Color(bufc[0], bufc[1], bufc[2]);
        colorRT = new Color(bufc2[0], bufc2[1], bufc2[2]);
        colorFav = new Color(bufc3[0], bufc3[1], bufc3[2]);
    }

    /**
     * プラグインが有効状態を返す.
     * @return プラグインの有効状態.
     */
    public boolean isEnablePlugin() {
        return isEnable;
    }
    
    /**
     * プラグインの有効状態をセットする.
     * @param enable プラグインの有効状態
     */
    public void setEnablePlugin(boolean enable) {
    	isEnable = enable;
    }

    /**
     * 被お気に入り通知が有効状態を返す.
     * @return プラグインの有効状態.
     */
    public boolean isEnableFav() {
        return isEnableFav;
    }
    
    /**
     * 被お気に入り通知の有効状態をセットする.
     * @param enable プラグインの有効状態
     */
    public void setEnableFav(boolean enable) {
    	isEnableFav = enable;
    }

    /**
     * 自分のアクションの通知が有効状態を返す.
     * @return プラグインの有効状態.
     */
    public boolean isFromMe() {
        return isFromMe;
    }
    
    /**
     * 自分のアクションの通知の有効状態をセットする.
     * @param enable プラグインの有効状態
     */
    public void setFromMe(boolean enable) {
    	isFromMe = enable;
    }

    /**
     * ポップアップの表示位置を返す.
     * @return ポップアップの表示位置.
     * @see mw.notifytweet.src.EnumData
     */
    public int getPopupPosition() {
        return popupPosition;
    }
    
    /**
     * ポップアップの表示位置をセットする.
     * @param position ポップアップ表示位置
     * @see mw.notifytweet.src.EnumData
     */
    public void setPopupPosition(int position) {
    	popupPosition = position;
    }

    /**
     * 名前のフォントを返す.
     * @return 名前フォント
     */
    public Font getFontName() {
        return fontName;
    }
    
    /**
     * 名前のフォントをセットする.
     * @param font 名前フォント
     */
    public void setFontName(Font font) {
    	fontName = font;
    }

    /**
     * テキストのフォントを返す.
     * @return テキストフォント
     */
    public Font getFontText() {
        return fontText;
    }
    
    /**
     * テキストのフォントをセットする.
     * @param font テキストフォント
     */
    public void setFontText(Font font) {
    	fontText = font;
    }

    /**
     * ポップアップの表示時間をミリ秒で返す.
     * @return ポップアップ表示時間(ミリ秒)
     */
    public int getTimePopup() {
        return popupTime;
    }
    
    /**
     * ポップアップの表示時間をセットする.
     * @param time ポップアップ表示時間(ミリ秒)
     */
    public void setTimePopup(int time) {
    	popupTime = time;
    }

    /**
     * 通常新着音のファイルパスを返す.
     * @return 通常新着音パス
     */
    public String getPathNormalSE() {
        return sePath;
    }
    
    /**
     * 通常新着音のファイルパスをセットする.
     * @param path 通常新着音パス
     */
    public void setPathNormalSE(String path) {
    	sePath = path;
    }

    /**
     * 被お気に入り音のファイルパスを返す.
     * @return 被お気に入り音パス
     */
    public String getPathFavSE() {
        return sePathFav;
    }
    
    /**
     * 被お気に入り音のファイルパスをセットする.
     * @param path 被お気に入り音パス
     */
    public void setPathFavSE(String path) {
    	sePathFav = path;
    }

    /**
     * 通常新着音の音量を返す.
     * @return 通常新着音音量
     */
    public float getVolNotmalSE() {
        return seVol;
    }
    
    /**
     * 通常新着音の音量をセットする.
     * @param volume 通常新着音音量
     */
    public void setVolNormalSE(float volume) {
    	seVol = volume;
    }

    /**
     * 被お気に入り音の音量を返す.
     * @return 被お気に入り音音量
     */
    public float getVolFavSE() {
        return seVolFav;
    }
    
    /**
     * 被お気に入り音の音量をセットする.
     * @param volume 被お気に入り音音量
     */
    public void setVolFavSE(float volume) {
    	seVolFav = volume;
    }

    /**
     * 通常新着音の有効状態を返す.
     * @return 通常新着音の状態
     */
    public boolean isEnableNormalSE() {
        return isEnableSE;
    }
    
    /**
     * 通常新着音の有効状態をセットする.
     * @param enable 通常新着音の状態
     */
    public void setEnableNormalSE(boolean enable) {
    	isEnableSE = enable;
    }

    /**
     * 被お気に入り音の有効状態を返す.
     * @return 被お気に入り音の状態
     */
    public boolean isEnableFavSE() {
        return isEnableSEFav;
    }
    
    /**
     * 被お気に入り音の有効状態をセットする.
     * @param enable 被お気に入り音の状態
     */
    public void setEnableFavSE(boolean enable) {
    	isEnableSEFav = enable;
    }

    /**
     * 通常背景色を返す.
     * @return 通常背景色
     */
    public Color getColorNormal() {
        return colorNormal;
    }
    
    /**
     * 通常背景色をセットする.
     * @param color 通常背景色
     */
    public void setColorNormal(Color color) {
    	colorNormal = color;
    }

    /**
     * リツイート背景色を返す.
     * @return リツイート背景色
     */
    public Color getColorRT() {
        return colorRT;
    }
    
    /**
     * リツイート背景色をセットする.
     * @param color リツイート背景色
     */
    public void setColorRT(Color color) {
    	colorRT = color;
    }

    /**
     * 被お気に入り背景色を返す.
     * @return 被お気に入り背景色
     */
    public Color getColorFav() {
        return colorFav;
    }
    
    /**
     * 被お気に入り背景色をセットする.
     * @param color 被お気に入り背景色
     */
    public void setColorFav(Color color) {
    	colorFav = color;
    }
}
