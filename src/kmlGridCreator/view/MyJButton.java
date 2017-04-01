package kmlGridCreator.view;

import javax.swing.JButton;

public class MyJButton extends JButton {

	private String enabledText, disabledText, enabledToolTipText, disabledToolTipText;

	public MyJButton(boolean enabled, String enabledText, String disabledText, String enabledToolTipText, String disabledToolTipText) {
		super();
		this.enabledText = enabledText;
		this.disabledText = disabledText;
		this.enabledToolTipText = enabledToolTipText;
		this.disabledToolTipText = disabledToolTipText;

		this.setEnabled(enabled);

		if (enabled) {
			this.setText(enabledText);
			this.setToolTipText(enabledToolTipText);
		} else {
			this.setText(disabledText);
			this.setToolTipText(disabledToolTipText);
		}
	}

	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		this.refreshButtonTexts();
	}

	public void refreshButtonTexts() {
		if (this.isEnabled()) {
			this.setText(enabledText);
			this.setToolTipText(enabledToolTipText);
		} else {
			this.setText(disabledText);
			this.setToolTipText(disabledToolTipText);
		}
	}

	// =================================================================
	public String getEnabledText() {
		return enabledText;
	}

	public void setEnabledText(String enabledText) {
		this.enabledText = enabledText;
		refreshButtonTexts();
	}

	public String getDisabledText() {
		return disabledText;
	}

	public void setDisabledText(String disabledText) {
		this.disabledText = disabledText;
		refreshButtonTexts();
	}

	public String getEnabledToolTipText() {
		return enabledToolTipText;
	}

	public void setEnabledToolTipText(String enabledToolTipText) {
		this.enabledToolTipText = enabledToolTipText;
		refreshButtonTexts();
	}

	public String getDisabledToolTipText() {
		return disabledToolTipText;
	}

	public void setDisabledToolTipText(String disabledToolTipText) {
		this.disabledToolTipText = disabledToolTipText;
		refreshButtonTexts();
	}

}
