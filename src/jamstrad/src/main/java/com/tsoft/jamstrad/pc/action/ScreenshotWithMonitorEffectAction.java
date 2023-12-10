package com.tsoft.jamstrad.pc.action;

import java.awt.event.KeyEvent;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.pc.keyboard.AmstradKeyboardEvent;

public class ScreenshotWithMonitorEffectAction extends ScreenshotAction {

	public ScreenshotWithMonitorEffectAction(AmstradPc amstradPc) {
		this(amstradPc, "Capture monitor image...");
	}

	public ScreenshotWithMonitorEffectAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name);
	}

	@Override
	protected boolean invokeOn(AmstradKeyboardEvent keyEvent) {
		return keyEvent.isKeyPressed() && keyEvent.getKeyCode() == KeyEvent.VK_I && keyEvent.isControlDown()
				&& keyEvent.isShiftDown();
	}

	@Override
	protected boolean includeMonitorEffect() {
		return true;
	}

}