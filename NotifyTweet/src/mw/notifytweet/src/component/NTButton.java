package mw.notifytweet.src.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import mw.notifytweet.src.NTActionListener;
import mw.notifytweet.src.Utility;

public class NTButton extends JLabel implements MouseListener {

	private List<NTActionListener> actionListenerList = new ArrayList<NTActionListener>();
	private static final Color COLOR_OFF = new Color(215, 215, 215);
	private static final Color COLOR_ON = new Color(190, 190, 190);
	float a = 1.12F;

	public NTButton(String text, int width) {
		super(text, JLabel.CENTER);
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.GRAY));
		addMouseListener(this);
		setOpaque(true);
		this.setPreferredSize(new Dimension(width, 30));
	}

	public void addActionListener(NTActionListener listener) {
		actionListenerList.add(listener);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		setBackground(COLOR_ON);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setBackground(COLOR_OFF);
		if (Utility.isInBox(this, e.getPoint())) {
			for (NTActionListener listener : actionListenerList) {
				listener.ntAction(this);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setBackground(COLOR_OFF);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setBackground(Color.WHITE);
	}
}
