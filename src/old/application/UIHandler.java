package old.application;

public class UIHandler {

	private BaseUI userInterface;

	public UIHandler(BaseUI userInterface) {
		this.userInterface = userInterface;
	}

	public BaseUI getUserInterface() {
		return userInterface;
	}

}
