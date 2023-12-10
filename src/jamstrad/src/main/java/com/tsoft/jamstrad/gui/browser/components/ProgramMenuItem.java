package com.tsoft.jamstrad.gui.browser.components;

import com.tsoft.jamstrad.gui.browser.ProgramBrowserDisplaySource;
import com.tsoft.jamstrad.gui.components.ScrollableItem;
import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.program.AmstradProgram;

public abstract class ProgramMenuItem implements ScrollableItem {

	private ProgramMenu menu;

	private String label;

	protected ProgramMenuItem(ProgramMenu menu, String label) {
		this.menu = menu;
		this.label = label;
	}

	public abstract void execute();

	public boolean isEnabled() {
		return true;
	}

	public int getLabelColor() {
		return 22;
	}

	public int getFocusBackgroundColor() {
		return 9;
	}

	protected AmstradPc getAmstradPc() {
		return getBrowser().getAmstradPc();
	}

	protected ProgramBrowserDisplaySource getBrowser() {
		return getMenu().getBrowser();
	}

	public AmstradProgram getProgram() {
		return getMenu().getProgram();
	}

	public ProgramMenu getMenu() {
		return menu;
	}

	public String getLabel() {
		return label;
	}

}