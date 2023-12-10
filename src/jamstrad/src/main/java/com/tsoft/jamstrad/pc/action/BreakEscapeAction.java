package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;

import com.tsoft.jamstrad.pc.AmstradPc;

public class BreakEscapeAction extends AmstradPcAction {

	public BreakEscapeAction(AmstradPc amstradPc) {
		this(amstradPc, "Break ESC");
	}

	public BreakEscapeAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		runInSeparateThread(new Runnable() {

			@Override
			public void run() {
				getAmstradPc().getKeyboard().breakEscape();
			}
		});
	}

}