/*
 * © 2013 Margherita Works.
 * 
 */
package mw.notifytweet.src;

/**
 * 定数.
 *
 */
public enum EnumData {
    /** ポップアップの横幅. */
    POPUP_WIDTH(350),

    /** ポップアップの縦幅. */
    POPUP_HEIGHT(50),

    /** ポップアップのX方向の間隔. */
    POPUP_SPACE_X(5),

    /** ポップアップのY方向の間隔. */
    POPUP_SPACE_Y(5),

    /** ポップアップのX方向の基準位置. */
    POPUP_LOC_X(5),

    /** ポップアップのY方向の基準位置. */
    POPUP_LOC_Y(5),


    /** 通常アイコンサイズ. */
    ICON_SIZE_NORMAL(50),

    /** RT、ふぁぼアイコンサイズ. */
    ICON_SIZE_MINI(24),

    /** ピンアイコンサイズ. */
    ICON_SIZE_PIN(16),


    /** コンフィグダイアログの横幅. */
    CFG_WIDTH(420),

    /** コンフィグダイアログの縦幅. */
    CFG_HEIGHT(590),

    /** 左上. */
    TOP_LEFT(0),

    /** 右上. */
    TOP_RIGHT(1),

    /** 右下. */
    BOTTOM_RIGHT(2),

    /** 左下. */
    BOTTOM_LEFT(3),

    /** ポップアップの数. */
    POPUP_QUA(10),
    
    /** 音量加算量. */
    PLUS_VOL(500);

    /** 値. */
    private int num;

    /**
     * コンストラクタ.
     * @param i あたい.
     */
    private EnumData(int i) {
        num = i;
    }
    /**
     * 値をintで返す.
     * @return 値
     */
    public int getInt() {
        return num;
    }

    /**
     * 2つのEnumDataを加算してintで返す.
     * @param par1 値1
     * @param par2 値2
     * @return par1 + par2
     */
    public static int plus(EnumData par1, EnumData par2) {
        return par1.getInt() + par2.getInt();
    }
}
