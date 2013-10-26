package mw.notifytweet.src.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mw.notifytweet.src.NTConfigView;
import mw.notifytweet.src.NTFont;
import say.swing.JFontChooser;

/**
 * フォント選択ダイアログです.<br />
 * このコンポーネントはユーザー名とテキストのフォントを選択するのに用いられます.
 * 
 */
public class DialogFontSelect extends JDialog implements ActionListener {
	/** OK,Cancelボタン. */
	private JButton buttonOK, buttonCancel;
	/** フォント. */
	private NTFont font;
	/** フォントチョーサー. */
	private JFontChooser fontChooser;
	/** フォントスタイル名の配列. */
	private final String[] fontStyle = { "標準", "太字", "斜体", "太字&斜体" };
	/** コンフィグのラベルを格納する. */
	@Deprecated
	private JLabel label;
	/** 名前とテキストを区別する. */
	private boolean isName;

	/**
	 * コンストラクタ.
	 * 
	 * @param par0Parent
	 *            JDialog:親
	 * @param par1Font
	 *            Font:デフォルトフォント
	 * @param par2Label
	 *            JLabel:忘れた
	 * @param name
	 *            boolean:忘れた
	 */
	public DialogFontSelect(NTFont par1Font, JLabel par2Label, boolean name) {
		font = par1Font;
		label = par2Label;
		isName = name;
		setAlwaysOnTop(true);
		setLayout(new BorderLayout());
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(400, 400);
		setTitle("フォントを選択してください");

		fontChooser = new JFontChooser();
		fontChooser.setSelectedFont(font.getFont());
		add(fontChooser, "Center");

		JPanel ynPanel = new JPanel();
		buttonOK = new JButton("OK");
		buttonCancel = new JButton("キャンセル");
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		ynPanel.add(buttonOK);
		ynPanel.add(buttonCancel);
		add(ynPanel, "Last");

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource().equals(buttonOK)) {
			font.setFont(fontChooser.getSelectedFont());
			label.setText(font.getFontName() + " / "
					+ fontStyle[font.getFontStyle()] + " / "
					+ font.getFontSize() + "pt");
			if (isName) {
				NTConfigView.getPreViewName().setFont(
						fontChooser.getSelectedFont());
			} else {
				NTConfigView.getPreViewText().setFont(
						fontChooser.getSelectedFont());
			}
			setVisible(false);
		}
		if (ev.getSource().equals(buttonCancel)) {
			setVisible(false);
		}
	}

}
