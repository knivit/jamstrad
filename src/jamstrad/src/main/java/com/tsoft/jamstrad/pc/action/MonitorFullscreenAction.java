package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.pc.keyboard.AmstradKeyboardEvent;
import com.tsoft.jamstrad.pc.monitor.AmstradMonitor;

public class MonitorFullscreenAction extends AmstradPcAction {

	private static String NAME_FULLSCREEN = "Fullscreen";

	private static String NAME_WINDOWED = "Windowed";

	public MonitorFullscreenAction(AmstradPc amstradPc) {
		super(amstradPc, "");
		updateName();
		amstradPc.getMonitor().addMonitorListener(this);
		amstradPc.getKeyboard().addKeyboardListener(this);
		setEnabled(getAmstradContext().getMode().isFullscreenToggleEnabled());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		toggleFullscreen();
	}

	@Override
	public void amstradMonitorFullscreenChanged(AmstradMonitor monitor) {
		super.amstradMonitorFullscreenChanged(monitor);
		updateName();
	}

	private void updateName() {
		if (isFullscreen()) {
			changeName(NAME_WINDOWED);
		} else {
			changeName(NAME_FULLSCREEN);
		}
	}

	@Override
	public void amstradKeyboardEventDispatched(AmstradKeyboardEvent event) {
		super.amstradKeyboardEventDispatched(event);
		if (!isTriggeredByMenuKeyBindings()) {
			if (event.isKeyPressed() && event.getKeyCode() == KeyEvent.VK_F11) {
				toggleFullscreen();
			}
		}
	}

	private void toggleFullscreen() {
		if (isEnabled()) {
			runInSeparateThread(new Runnable() {
				@Override
				public void run() {
					getAmstradPc().getMonitor().toggleFullscreen();
				}
			});
		}
	}

	public boolean isFullscreen() {
		return getAmstradPc().getMonitor().isFullscreen();
	}

}