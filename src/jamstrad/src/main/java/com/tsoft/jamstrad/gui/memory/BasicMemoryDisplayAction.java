package com.tsoft.jamstrad.gui.memory;

import java.awt.event.ActionEvent;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.pc.action.AmstradPcAction;
import com.tsoft.jamstrad.pc.monitor.AmstradMonitor;
import com.tsoft.jamstrad.pc.monitor.display.source.AmstradAlternativeDisplaySource;

public class BasicMemoryDisplayAction extends AmstradPcAction {

	private BasicMemoryDisplaySource displaySource;

	private static String NAME_OPEN = "Show Basic memory";

	private static String NAME_CLOSE = "Hide Basic memory";

	public BasicMemoryDisplayAction(AmstradPc amstradPc) {
		super(amstradPc, "");
		updateName();
		amstradPc.getMonitor().addMonitorListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		toggleBasicMemory();
	}

	public void toggleBasicMemory() {
		if (NAME_OPEN.equals(getName())) {
			showBasicMemory();
		} else {
			hideBasicMemory();
		}
	}

	public void showBasicMemory() {
		if (isEnabled()) {
			getAmstradPc().getMonitor().swapDisplaySource(getDisplaySource());
		}
	}

	public void hideBasicMemory() {
		if (isEnabled()) {
			getAmstradPc().getMonitor().resetDisplaySource();
		}
	}

	@Override
	public void amstradDisplaySourceChanged(AmstradMonitor monitor) {
		super.amstradDisplaySourceChanged(monitor);
		updateName();
	}

	private void updateName() {
		if (isBasicMemoryShowing()) {
			changeName(NAME_CLOSE);
		} else {
			changeName(NAME_OPEN);
		}
	}

	public boolean isBasicMemoryShowing() {
		AmstradAlternativeDisplaySource altDisplaySource = getAmstradPc().getMonitor()
				.getCurrentAlternativeDisplaySource();
		return altDisplaySource != null && altDisplaySource instanceof BasicMemoryDisplaySource;
	}

	private BasicMemoryDisplaySource getDisplaySource() {
		if (displaySource == null) {
			displaySource = new BasicMemoryDisplaySource(getAmstradPc());
		}
		return displaySource;
	}

}