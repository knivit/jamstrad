package com.tsoft.jamstrad.gui.browser.components;

public class ProgramCloseMenuItem extends ProgramMenuItem {

	public ProgramCloseMenuItem(ProgramMenu menu) {
		super(menu, "Close");
	}

	@Override
	public void execute() {
		getBrowser().closeModalWindow();
	}

}