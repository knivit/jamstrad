package com.tsoft.jamstrad.gui.browser.components;

import com.tsoft.jamstrad.gui.components.ColoredTextArea;
import com.tsoft.jamstrad.program.AmstradProgram;

public abstract class ProgramSheet extends ColoredTextArea {

	private AmstradProgram program;

	protected ProgramSheet(AmstradProgram program, int maxItemsShowing, int maxWidth, int backgroundColorIndex) {
		super(maxItemsShowing);
		this.program = program;
		populateSheet(maxWidth, backgroundColorIndex);
	}

	protected abstract void populateSheet(int maxWidth, int backgroundColorIndex);

	public AmstradProgram getProgram() {
		return program;
	}

}