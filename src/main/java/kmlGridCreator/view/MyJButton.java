package main.java.kmlGridCreator.view;

import javax.swing.JButton;

/**
 * this class has the same functionality as a normal JButton except the fact
 * that it handles the button-texts itself
 */
class MyJButton extends JButton {

	private String enabledText, disabledText, enabledToolTipText, disabledToolTipText;

	MyJButton(boolean enabled, String enabledText, String disabledText, String enabledToolTipText,
			String disabledToolTipText) {
		super();
		this.enabledText = enabledText;
		this.disabledText = disabledText;
		this.enabledToolTipText = enabledToolTipText;
		this.disabledToolTipText = disabledToolTipText;

		this.setEnabled(enabled);

		// if (enabled) {
		// this.setText(enabledText);
		// this.setToolTipText(enabledToolTipText);
		// } else {
		// this.setText(disabledText);
		// this.setToolTipText(disabledToolTipText);
		// }

		this.addChangeListener(x -> {
			this.refreshButtonTexts();
		});
		refreshButtonTexts();
	}

	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
	}

	private void refreshButtonTexts() {
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
	}

	public String getDisabledText() {
		return disabledText;
	}

	public void setDisabledText(String disabledText) {
		this.disabledText = disabledText;
	}

	public String getEnabledToolTipText() {
		return enabledToolTipText;
	}

	public void setEnabledToolTipText(String enabledToolTipText) {
		this.enabledToolTipText = enabledToolTipText;
	}

	public String getDisabledToolTipText() {
		return disabledToolTipText;
	}

	public void setDisabledToolTipText(String disabledToolTipText) {
		this.disabledToolTipText = disabledToolTipText;
	}
}
