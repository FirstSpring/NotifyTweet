/*
 * © 2013 Margherita Works.
 * 
 */
package mw.notifytweet.src;

import java.awt.Font;

/**
 * java.awt.Fontのラッパー.
 * ゲッター・セッターを持つ.
 * @author Getaji
 *
 */
public class NTFont {
	/** フォント名. */
    private String fontName;
    
    /** フォントスタイル. */
    private int fontStyle;
    
    /** フォントサイズ. */
    private int fontSize;
    
    /**
     * NTFontインスタンスを生成する.
     * @param par0Name フォント名
     * @param par1Style フォントスタイル(0~2の範囲で指定してください)
     * @param par2Size フォントサイズ(0以上の範囲で指定してください)
     */
    public NTFont(String par0Name, int par1Style, int par2Size) {
        if (par1Style > 2 || par1Style < 0) {
            throw new NotifyTweetException("フォントスタイルが不正です[" + par1Style + "]");
        } else if (par2Size < 0) {
            throw new NotifyTweetException("フォントサイズが不正です[" + par2Size + "]");
        }
        fontName = par0Name;
        fontStyle = par1Style;
        fontSize = par2Size;
    }
    
    /**
     * <code>java.awt.Font</code>からインスタンスを生成する.
     * @param par0Font フォント
     */
    public NTFont(Font par0Font) {
    	setFont(par0Font);
    }

    /**
     * フォント名を返す.
     * @return フォント名
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * フォントスタイルを取得する.
     * @return フォントスタイル
     */
    public int getFontStyle() {
        return fontStyle;
    }

    /**
     * フォントサイズを取得する.
     * @return フォントサイズ
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * 名前、スタイル、サイズをjava.awt.Fontの形式で取得する.
     * @return java.awt.Font
     */
    public Font getFont() {
        return new Font(fontName, fontStyle, fontSize);
    }

    /**
     * フォント名をセットする.
     * @param par0Name フォント名
     */
    public void setFontName(String par0Name) {
        fontName = par0Name;
    }

    /**
     * フォントスタイルをセットする.
     * @param par0Style フォントスタイル
     */
    public void setFontStyle(int par0Style) {
        if (par0Style > 2 || par0Style < 0) {
            throw new NotifyTweetException("フォントスタイルが不正です[" + par0Style + "]");
        }
        fontStyle = par0Style;
    }

    /**
     * フォントサイズをセットする.
     * @param par0Size フォントサイズ
     */
    public void setFontSize(int par0Size) {
        if (par0Size < 0) {
            throw new NotifyTweetException("フォントサイズが不正です[" + par0Size + "]");
        }
        fontSize = par0Size;
    }
    
    /**
     * フォント名,スタイル,サイズを一括設定する.
     * @param par0Name フォント名
     * @param par1Style フォントスタイル
     * @param par2Size フォントサイズ
     */
    public void setAll(String par0Name, int par1Style, int par2Size) {
        if (par1Style > 2 || par1Style < 0) {
            throw new NotifyTweetException("フォントスタイルが不正です[" + par1Style + "]");
        } else if (par2Size < 0) {
            throw new NotifyTweetException("フォントサイズが不正です[" + par2Size + "]");
        }
        fontName = par0Name;
        fontStyle = par1Style;
        fontSize = par2Size;
    }
    
    /**
     * NTFontの各値を引数から取ってセットする.
     * @param par0Font NTFont
     */
    public void setNTFont(NTFont par0Font) {
    	fontName = par0Font.getFontName();
    	fontStyle = par0Font.getFontStyle();
    	fontSize = par0Font.getFontSize();
    }
    
    /**
     * Fontの各値を引数から取ってセットする.
     * @param par0Font Font
     */
    public void setFont(Font par0Font) {
    	fontName = par0Font.getFamily();
    	fontStyle = par0Font.getStyle();
    	fontSize = par0Font.getSize();
    }
}
