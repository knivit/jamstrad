package com.tsoft.jamstrad.gui.browser.components;

public class ProgramReturnMenuItem extends ProgramMenuItem {

	public ProgramReturnMenuItem(ProgramMenu menu) {
		super(menu, "Return");
	}

	@Override
	public void execute() {
		getBrowser().close();
	}

}