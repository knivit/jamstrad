package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.pc.monitor.AmstradMonitorMode;

public class MonitorModeAction extends AmstradPcAction {

	private AmstradMonitorMode mode;

	public MonitorModeAction(AmstradMonitorMode mode, AmstradPc amstradPc, String name) {
		super(amstradPc, name);
		this.mode = mode;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		getAmstradPc().getMonitor().setMode(getMode());
	}

	public AmstradMonitorMode getMode() {
		return mode;
	}

}