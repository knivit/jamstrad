package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;

import com.tsoft.jamstrad.pc.AmstradPc;

public class WindowAlwaysOnTopAction extends AmstradPcAction {

	public WindowAlwaysOnTopAction(AmstradPc amstradPc) {
		this(amstradPc, "Always on top");
	}

	public WindowAlwaysOnTopAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		boolean state = ((JCheckBoxMenuItem) event.getSource()).getState();
		getAmstradPc().getMonitor().setWindowAlwaysOnTop(state);
	}

}