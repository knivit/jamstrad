package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.pc.AmstradPcFrame;

public class WindowCenterOnScreenAction extends AmstradPcAction {

	public WindowCenterOnScreenAction(AmstradPc amstradPc) {
		this(amstradPc, "Center on screen");
	}

	public WindowCenterOnScreenAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		AmstradPcFrame frame = getAmstradPc().getFrame();
		if (frame != null)
			frame.centerOnScreen();
	}

}