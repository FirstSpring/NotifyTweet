package mw.notifytweet.src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import mw.notifytweet.src.component.NTButton;
import mw.notifytweet.src.manager.AccountManager;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class NTAuthView implements NTActionListener {

	JPanel panel;
	JTextField pin;
	Twitter t;
	RequestToken rt;

	public Component getComponent() throws TwitterException {
		t = new TwitterFactory().getInstance();
		t.setOAuthConsumer(AccountManager.KEY, AccountManager.SECRET);
		rt = t.getOAuthRequestToken();

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(200, 200, 200));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(mainPanel, "Center");

		JTextField url = new JTextField(rt.getAuthorizationURL(), 35);
		mainPanel.add(url);
		JLabel label = new JLabel(
				"<html>上のURLをブラウザにコピペして認証し<br>出力される7桁の数字を下のボックスに入力してください");
		mainPanel.add(label);
		pin = new JTextField("", 5);
		mainPanel.add(pin);
		JPanel buttonPanel = new JPanel();
		NTButton button = new NTButton("OK", 50);
		button.addActionListener(this);
		buttonPanel.add(button);
		panel.add(buttonPanel, "Last");
		return panel;
	}

	@Override
	public void ntAction(Component source) {
		try {
			AccessToken token = t.getOAuthAccessToken(rt, pin.getText());
			AccountManager.getInstance().put(token.getScreenName(), token);
			AccountManager.save();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					SwingUtilities.getWindowAncestor(panel), "何かがおかしいです");
		}
		SwingUtilities.getWindowAncestor(source).setVisible(false);
	}

}
