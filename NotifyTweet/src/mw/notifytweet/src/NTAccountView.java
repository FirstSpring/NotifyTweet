package mw.notifytweet.src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import mw.notifytweet.src.component.NTButton;
import mw.notifytweet.src.manager.AccountManager;
import twitter4j.TwitterException;

public class NTAccountView implements NTActionListener {
	JPanel panel;

	NTButton buttonOK;
	NTButton buttonCancel;
	NTButton buttonAuth;

	JList<String> accountList;

	public Component getComponent() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(200, 200, 200));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(mainPanel, "Center");

		JPanel ynPanel = new JPanel();
		buttonOK = new NTButton("OK", 100);
		buttonCancel = new NTButton("キャンセル", 100);
		buttonAuth = new NTButton("アカウント認証", 100);
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		buttonAuth.addActionListener(this);
		ynPanel.add(buttonOK);
		ynPanel.add(buttonCancel);
		ynPanel.add(buttonAuth);
		accountList = new JList<>(AccountManager.getInstance().getAccounts());
		mainPanel.add(accountList, "Center");
		panel.add(ynPanel, "Last");
		return panel;
	}

	@Override
	public void ntAction(Component source) {
		if (source == buttonOK) {
			String acc = accountList.getSelectedValue();
			if (acc != null) {
				AccountManager.getInstance().selected = acc;
				AccountManager.save();
				NTListener.getInstance().connect();
			}
		}
		if (source != buttonAuth) {
			JFrame f = (JFrame) SwingUtilities.getWindowAncestor(panel);
			f.setVisible(false);
		} else {
			try {
				JDialog d = new JDialog();
				d.setSize(420, 200);
				d.setResizable(false);
				d.setModalityType(ModalityType.APPLICATION_MODAL);
				d.add(new NTAuthView().getComponent());
				d.setVisible(true);
			} catch (TwitterException e) {
				JOptionPane.showMessageDialog(
						SwingUtilities.getWindowAncestor(panel), "通信に失敗しました");
			}

			accountList.setListData(AccountManager.getInstance().getAccounts());
		}

	}
}
