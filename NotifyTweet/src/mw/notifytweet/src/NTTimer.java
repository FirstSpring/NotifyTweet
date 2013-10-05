package mw.notifytweet.src;

import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class NTTimer implements ActionListener {
    private int index;
    private Timer timer;
    public NTTimer(int index) {
        this.index = index;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        /* ダイアログがピン止めされてたら抜ける */
        if (NotifyTweet.getInstance().getPopupManager().isPinDialog(index)) {return;}

        /* ダイアログの中にマウスが入ってたら抜ける */
        Point pointerM = MouseInfo.getPointerInfo().getLocation();
        Point pointerD = null;
        try {
            pointerD = NotifyTweet.getInstance().getPopupManager().getPopups()[index].getLocationOnScreen();
            if (pointerM.x > pointerD.x
                    && pointerM.x < pointerD.x + EnumData.POPUP_WIDTH.getInt()
                    && pointerM.y > pointerD.y
                    && pointerM.y < pointerD.y + EnumData.POPUP_HEIGHT.getInt()) {
                return;
        }
        } catch (IllegalComponentStateException e) {
        } catch (NullPointerException e) {
        	// ヽ(╹◡╹)ノ
        }

        NotifyTweet.getInstance().getPopupManager().setVisible(index, false, null);
        timer.stop();
    }

}
