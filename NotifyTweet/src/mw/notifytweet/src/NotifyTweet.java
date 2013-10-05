/*
 * © 2013 Margherita Works.
 *
 */
package mw.notifytweet.src;

import java.awt.Desktop;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import mw.notifytweet.src.manager.NTConfigManager;
import mw.notifytweet.src.manager.PopupManager;
import mw.notifytweet.src.manager.ResourceManager;

import com.orekyuu.javatter.main.Main;
import com.orekyuu.javatter.plugin.JavatterPlugin;
import com.orekyuu.javatter.plugin.JavatterPluginLoader;
import com.orekyuu.javatter.util.ImageManager;
import com.orekyuu.javatter.util.SaveData;
import com.orekyuu.javatter.view.IJavatterTab;
import com.orekyuu.javatter.view.MainWindowView;

/**
 * Javatterプラグイン,NotifyTweetのメインクラスです.<br />
 * 初期処理を行い,共用インスタンスなどを保持します.
 * @author Getaji
 *
 */
public class NotifyTweet extends JavatterPlugin {
    /** メインクラスインスタンス. */
    private static NotifyTweet instance;

    /** セーブデータ .*/
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

    /**
     * デフォルトコンストラクタ.
     */
    public NotifyTweet() {
        instance = this;
    }

    @Override
    public String getPluginName() {
        return "NotifyTweet";
    }

    @Override
    public String getVersion() {
        return "1.6build" + getBuildNumber();
    }

    /**
     * Build number.
     * @return build number
     */
    public int getBuildNumber() {
        return 7;
    }

    @Override
    public void init() {

        /* ******** UserStreamリスナを登録 ******** */
        addUserStreamListener(NTListener.getInstance());

        /* ******** セーブデータのデフォルト値を設定 ******** */
        saveData = getSaveData();

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
        saveData.setDefaultValue("sepath", "./plugins/NotifyTweet/ping.wav");
        saveData.setDefaultValue("sepathf", "./plugins/NotifyTweet/fav.wav");
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
        setLib();

        /* ******** 更新チェック ******** */
        Thread thread = new Thread() {
            @Override
            public void run() {
                String url = Utility.updateCheck();
                System.out.println(url);
                if (!url.equals("UNKNOWN")) {
                    int numDialog = JOptionPane.showConfirmDialog(
                            getMainWindowView().getMainFrame(),
                            "NotifyTweet最新版が見つかりました\nダウンロードページを開きますか？",
                            "NotifyTweet",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    switch (numDialog) {
                    case JOptionPane.OK_OPTION:
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        };
        thread.start();
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
            control.setValue((float) Math.log10(cfgManager.getVolNotmalSE())
                    * 20.0F);

        } catch (FileNotFoundException e) {
            /* **** ファイルが存在しない **** */
            JOptionPane.showMessageDialog(this.getMainView().getMainFrame(),
                    "指定された通常新着音ファイルが存在しません。\n"
                    + "同梱のNotifyTweetフォルダをpluginsフォルダに入れるか\n"
                    + "正確なファイルパスを指定してください。",
                    "NotifyTweet", JOptionPane.ERROR_MESSAGE);

        } catch (UnsupportedAudioFileException e) {
            /* **** ファイル形式がサポートされていない **** */
            JOptionPane.showMessageDialog(this.getMainView().getMainFrame(),
                    "指定された通常新着音ファイルはサポートされていません。",
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
            control.setValue((float) Math.log10(cfgManager.getVolFavSE())
                    * 20.0F);

        } catch (FileNotFoundException e) {
            /* **** ファイルが存在しない **** */
            JOptionPane.showMessageDialog(this.getMainView().getMainFrame(),
                    "指定された被お気に入り音ファイルが存在しません。\n"
                    + "同梱のNotifyTweetフォルダをpluginsフォルダに入れるか\n"
                    + "正確なファイルパスを指定してください。",
                    "NotifyTweet", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();

        } catch (UnsupportedAudioFileException e) {
            /* **** ファイル形式がサポートされていない **** */
            JOptionPane.showMessageDialog(this.getMainView().getMainFrame(),
                    "指定された被お気に入り音ファイルはサポートされていません。",
                    "NotifyTweet", JOptionPane.ERROR_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * ライブラリをロードします.<br />
     * <b>WARNING:このメソッドは1度きりしか呼ばれてはなりません</b>
     */
    private void setLib() {
        /* ******** JFontChooserライブラリ読み込み ******** */
        File file = new File("./plugins/NotifyTweet/jfontchooser-1.0.5.jar");
        try {
            JavatterPluginLoader.addLibrary(file, Main.class.getClassLoader());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (!file.exists()) {
            /* **** ファイルが存在しない **** */
            JOptionPane.showMessageDialog(this.getMainView().getMainFrame(),
                    "必要なライブラリが見つかりません。\n"
                    + "同梱のNotifyTweetフォルダをpluginsフォルダに入れてください。",
                    "NotifyTweet", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            new say.swing.JFontChooser();

        } catch (NoClassDefFoundError e) {
            /* **** ファイルが破損している **** */
            JOptionPane.showMessageDialog(this.getMainView().getMainFrame(),
                    "ライブラリが破損しています。\n"
                    + "同梱のNotifyTweetフォルダを./plugins/NotifyTweetフォルダに"
                    + "上書きしてください。",
                    "NotifyTweet", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    protected IJavatterTab getPluginConfigViewObserver() {
        return new NTConfigView();
    }

    /**
     * NotifyTweetのインスタンスを返します.
     * @return NotifyTweetのインスタンス
     */
    public static NotifyTweet getInstance() {
        return instance;
    }

    /**
     * ポップアップマネージャーを返します.
     * @return ポップアップマネージャーのインスタンス
     */
    public PopupManager getPopupManager() {
        return popupManager;
    }

    /**
     * 読み込み後のセーブデータを返します.
     * @return セーブデータ
     */
    public SaveData getSaveDataNT() {
        return saveData;
    }

    /**
     * 設定マネージャーを返します.
     * @return 設定マネージャー
     */
    public NTConfigManager getConfigManager() {
        return cfgManager;
    }

    /**
     * リソースマネージャーを返します.
     * @return リソースマネージャー
     */
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    /**
     * 通常新着音のクリップを返します.
     * @return クリップ
     */
    public Clip getClipNormal() {
        return clip;
    }

    /**
     * 被お気に入り新着音のクリップを返します.
     * @return クリップ
     */
    public Clip getClipFav() {
        return clipFav;
    }

    /**
     * ロゴイメージを返します.
     * @return ロゴイメージ
     */
    public ImageIcon getIconLogo() {
        return imageLogo;
    }

    /**
     * メインウィンドウビューを返します.
     * @return メインウィンドウビュー
     */
    public MainWindowView getMainWindowView() {
        return getMainView();
    }

    /**
     * ステータスバーのステータスをセットします.
     * @param apply 成功ステータスか
     * @param status 表示するステータス文
     */
    public void setStatus(boolean apply, String status) {
        ImageIcon icon;
        if (apply) {
            icon = new ImageIcon(
                    ImageManager.getInstance().getImage(ImageManager.STATUS_NORMAL));
        } else {
            icon = new ImageIcon(
                    ImageManager.getInstance().getImage(ImageManager.STATUS_ERROR));
        }
        getMainView().setStatus(icon, status);
    }
}
