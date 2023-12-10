package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;

import com.tsoft.jamstrad.pc.AmstradPc;

public class MonitorScanLinesEffectAction extends AmstradPcAction {

	public MonitorScanLinesEffectAction(AmstradPc amstradPc) {
		this(amstradPc, "Scan lines");
	}

	public MonitorScanLinesEffectAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		boolean state = ((JCheckBoxMenuItem) event.getSource()).getState();
		getAmstradPc().getMonitor().setScanLinesEffect(state);
	}

}